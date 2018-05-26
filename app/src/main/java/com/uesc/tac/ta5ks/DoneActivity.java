package com.uesc.tac.ta5ks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DoneActivity extends GenericActivity {

    private Button btn_newTask;
    private TextView tv_today;
    private TextView tv_backlog;
    private TextView tv_done;
    private static final int STATUS = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        //Inicializing the objects
        btn_newTask = findViewById(R.id.btn_newTask);
        tv_done = findViewById(R.id.tv_today);
        tv_today = findViewById(R.id.tv_backlog);
        tv_backlog = findViewById(R.id.tv_done);

        //Changing menu items names
        tv_done.setText("done");
        tv_today.setText("today");
        tv_backlog.setText("backlog");

        //Setting the status
        this.setSTATUS(STATUS);
        //Initializing some objects
        super.initializing();
        //Filling the list and updating it
        super.fillTaskList();

        tv_backlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoneActivity.this, BacklogActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DoneActivity.this, TodayActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

    }

    public static int getSTATUS(){
        return STATUS;
    }
}
