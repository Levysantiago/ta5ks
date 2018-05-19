package com.uesc.tac.ta5ks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by levy on 16/05/18.
 */

public class MainActivity extends AppCompatActivity {

    private EditText edt_email;
    private EditText edt_passwd;
    private TextView tv_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_email = findViewById(R.id.edit_email);
        edt_passwd = findViewById(R.id.edit_passwd);
        tv_login = findViewById(R.id.tv_login);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BacklogActivity.class);
                startActivity(intent);
            }
        });
    }
}
