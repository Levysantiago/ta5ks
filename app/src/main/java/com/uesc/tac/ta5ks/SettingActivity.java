package com.uesc.tac.ta5ks;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by levy on 16/05/18.
 */

public class SettingActivity extends AppCompatActivity {

    private LinearLayout layout_settings;
    private TextView tv_title;
    private Button btn_theme;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        layout_settings = findViewById(R.id.layout_settings);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("settings");
        btn_theme = findViewById(R.id.btn_theme);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
