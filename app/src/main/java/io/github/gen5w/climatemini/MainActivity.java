
package io.github.gen5w.climatemini;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.graphics.Color;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public final class MainActivity extends Activity {
    private TextView status;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 28, 28, 28);
        root.setBackgroundColor(Color.rgb(21, 28, 38));

        root.addView(label("Gen5W Climate Mini 0.1",
                23, Color.WHITE));
        root.addView(label(
                "진단용 미니창 · 실제 차량 제어 비활성화",
                16, 0xffffcd70));
        root.addView(label(
                "DRIVER ONLY / A/C / HEAT / SYNC 상태는 " +
                "차량 API 검증 전까지 확인할 수 없습니다.",
                15, Color.LTGRAY));

        status = label("상태: 대기", 15, Color.WHITE);
        root.addView(status);

        Button open = new Button(this);
        open.setText("미니창 열기");
        root.addView(open);

        open.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 23 &&
                    !Settings.canDrawOverlays(this)) {
                status.setText("오버레이 권한 필요");
                Intent i = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
                startActivity(i);
                return;
            }
            startService(new Intent(
                    this, OverlayService.class));
            status.setText("미니창 표시 요청 완료");
        });

        Button close = new Button(this);
        close.setText("미니창 닫기");
        root.addView(close);

        close.setOnClickListener(v -> {
            stopService(new Intent(
                    this, OverlayService.class));
            status.setText("미니창 닫힘");
        });

        root.addView(label(
            "안전: 주행 중 조작하지 마세요.",
            14, 0xffffcd70));

        setContentView(root);
    }

    private TextView label(String text, int size, int color) {
        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(size);
        v.setTextColor(color);
        v.setPadding(0, 12, 0, 12);
        return v;
    }
}
