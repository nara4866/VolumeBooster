package com.example.volumebooster;

import android.app.Activity;
import android.media.audiofx.LoudnessEnhancer;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private LoudnessEnhancer enhancer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(60, 60, 60, 60);

        TextView gainText = new TextView(this);
        gainText.setText("増幅量: +0.0 dB");
        gainText.setTextSize(24f);
        layout.addView(gainText);

        SeekBar seekBar = new SeekBar(this);
        seekBar.setMax(1500); // 最大 +15.0 dB
        layout.addView(seekBar);

        Switch enableSwitch = new Switch(this);
        enableSwitch.setText("ブースターを有効化");
        enableSwitch.setPadding(0, 60, 0, 0);
        layout.addView(enableSwitch);

        setContentView(layout);

        try {
            enhancer = new LoudnessEnhancer(0);
        } catch (Exception e) {
            Toast.makeText(this, "エラー: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        enableSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (enhancer != null) {
                enhancer.setEnabled(isChecked);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar s, int progress, boolean fromUser) {
                if (enhancer != null) {
                    enhancer.setTargetGain(progress);
                    gainText.setText(String.format("増幅量: +%.1f dB", progress / 100.0));
                }
            }
            @Override public void onStartTrackingTouch(SeekBar s) {}
            @Override public void onStopTrackingTouch(SeekBar s) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (enhancer != null) {
            enhancer.release();
        }
    }
}
