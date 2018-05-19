package com.uesc.tac.ta5ks;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.uesc.tac.ta5ks.dao.TaskDAO;
import com.uesc.tac.ta5ks.model.Task;

import java.util.List;

/**
 * Created by levy on 25/04/18.
 */

public class TodayActivity extends GenericActivity {

    private Button btn_newTask;
    private TextView tv_today;
    private TextView tv_backlog;
    private TextView tv_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        //Inicializing the objects
        btn_newTask = findViewById(R.id.btn_newTask);
        tv_today = findViewById(R.id.tv_backlog);
        tv_backlog = findViewById(R.id.tv_backlog);
        tv_done = findViewById(R.id.tv_done);

        //Initializing some objects
        super.initializing();
        //Setting the status
        super.setSTATUS(2);
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
}
