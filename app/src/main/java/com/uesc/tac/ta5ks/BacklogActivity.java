package com.uesc.tac.ta5ks;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

public class BacklogActivity extends GenericActivity{

    private TextView tv_today;
    private TextView tv_backlog;
    private TextView tv_done;
    private static final int STATUS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pages);

        //Initializing the objects
        tv_backlog = findViewById(R.id.tv_menu_1);
        tv_today = findViewById(R.id.tv_menu_2);
        tv_done = findViewById(R.id.tv_menu_3);

        //Setting menu items names
        tv_today.setText("today");
        tv_backlog.setText("backlog");
        tv_done.setText("done");

        //Setting the status
        this.setSTATUS(STATUS);
        //Setting the next status when the img status is clicked
        this.setNextStatus(TodayActivity.getSTATUS());
        //Setting the next and previous page
        this.setNextPage(TodayActivity.getSTATUS());
        this.setPrevPage(DoneActivity.getSTATUS());
        //Initializing some objects
        super.initializing();
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
    }

    public static int getSTATUS(){
        return STATUS;
    }
}