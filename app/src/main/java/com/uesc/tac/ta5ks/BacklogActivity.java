package com.uesc.tac.ta5ks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BacklogActivity extends GenericActivity{

    private static final int PICK_CONTACT_REQUEST = 1;
    private Button btn_newTask;
    private TextView tv_today;
    private TextView tv_backlog;
    private TextView tv_done;
    private ImageView img_settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);

        //Initializing the objects
        btn_newTask = findViewById(R.id.btn_newTask);
        img_settings = findViewById(R.id.img_settings);
        tv_backlog = findViewById(R.id.tv_today);
        tv_today = findViewById(R.id.tv_backlog);
        tv_done = findViewById(R.id.tv_done);

        //Changing menu items name
        tv_today.setText("today");
        tv_backlog.setText("backlog");

        //Initializing some objects
        super.initializing();
        //Setting the status
        super.setSTATUS(1);
        //Filling the list and updating it
        super.fillTaskList();

        super.lv_tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("SELECTED ITEM", ""+i);
            }
        });

        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BacklogActivity.this, TodayActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BacklogActivity.this, DoneActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        btn_newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BacklogActivity.this, TagActivity.class);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BacklogActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                fillTaskList();
            }
        }
    }
}