package com.uesc.tac.ta5ks;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by levy on 25/04/18.
 */

public class TodayActivity extends GenericActivity {

    private Button btn_newTask;
    private TextView tv_today;
    private TextView tv_backlog;
    private TextView tv_done;
    private static final int STATUS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        //Inicializing the objects
        btn_newTask = findViewById(R.id.btn_newTask);
        tv_today = findViewById(R.id.tv_backlog);
        tv_backlog = findViewById(R.id.tv_backlog);
        tv_done = findViewById(R.id.tv_done);

        //Setting the status
        this.setSTATUS(STATUS);
        //Setting the next status when the img status is clicked
        this.setNextStatus(DoneActivity.getSTATUS());
        //Initializing some objects
        super.initializing();
        //Filling the list and updating it
        super.fillTaskList();

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodayActivity.this, DoneActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        tv_backlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodayActivity.this, BacklogActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }

    public static int getSTATUS(){
        return STATUS;
    }
}
