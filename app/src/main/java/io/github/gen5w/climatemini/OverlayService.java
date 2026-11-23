
package io.github.gen5w.climatemini;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class OverlayService extends Service {
    private WindowManager manager;
    private LinearLayout panel;
    private WindowManager.LayoutParams params;
    private int startX, startY;
    private float touchX, touchY;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (panel != null) return START_NOT_STICKY;

        manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(12, 8, 12, 9);

        GradientDrawable bg = new GradientDrawable();
        bg.setColor(0xee182532);
        bg.setCornerRadius(16);
        bg.setStroke(2, 0xff60758a);
        panel.setBackground(bg);

        TextView header = text(
            "☰   CLIMATE MINI  ·  진단 모드     ✕",
            15, Color.WHITE
        );
        panel.addView(header);

        header.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchX = e.getRawX();
                        touchY = e.getRawY();
                        startX = params.x;
                        startY = params.y;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        params.x = startX +
                            Math.round(e.getRawX() - touchX);
                        params.y = startY +
                            Math.round(e.getRawY() - touchY);

                        if (manager != null)
                            manager.updateViewLayout(panel, params);
                        return true;

                    case MotionEvent.ACTION_UP:
                        if (Math.abs(e.getRawX() - touchX) < 12 &&
                            Math.abs(e.getRawY() - touchY) < 12)
                            stopSelf();
                        return true;
                }
                return false;
            }
        });

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);

        for (String name : new String[]{
                "DRIVER", "A/C", "HEAT", "SYNC"}) {

            TextView button = text(
                name + "\n?", 12, 0xffd7e5f0
            );

            button.setGravity(Gravity.CENTER);
            button.setBackgroundColor(0xff3e4c59);

            LinearLayout.LayoutParams lp =
                new LinearLayout.LayoutParams(0, 76, 1);

            lp.setMargins(3, 4, 3, 4);
            row.addView(button, lp);

            button.setOnClickListener(v ->
                android.widget.Toast.makeText(
                    this,
                    name + ": 차량 제어 미연결",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            );
        }

        panel.addView(row);

        panel.addView(text(
            "? = 상태 미확인  |  차량 명령 전송 안 함",
            11, 0xffffcc75
        ));

        int type = Build.VERSION.SDK_INT >= 26
            ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            : WindowManager.LayoutParams.TYPE_PHONE;

        params = new WindowManager.LayoutParams(
            430,
            WindowManager.LayoutParams.WRAP_CONTENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 220;
        params.y = 130;

        try {
            manager.addView(panel, params);
        } catch (Exception ex) {
            panel = null;
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    private TextView text(String s, int sp, int color) {
        TextView v = new TextView(this);
        v.setText(s);
        v.setTextSize(sp);
        v.setTextColor(color);
        v.setPadding(8, 7, 8, 7);
        return v;
    }

    @Override
    public void onDestroy() {
        if (manager != null && panel != null) {
            try {
                manager.removeView(panel);
            } catch (Exception ignored) {}
        }
        panel = null;
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent i) {
        return null;
    }
}
