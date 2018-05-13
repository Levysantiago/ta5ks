package com.uesc.tac.ta5ks;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.uesc.tac.ta5ks.dao.TagDAO;
import com.uesc.tac.ta5ks.dao.TaskDAO;
import com.uesc.tac.ta5ks.model.Tag;
import com.uesc.tac.ta5ks.model.Task;
import com.uesc.tac.ta5ks.util.ChipControler;

import java.util.List;

public class TodayActivity extends AppCompatActivity {

    private static final int PICK_CONTACT_REQUEST = 1;
    private ListView lv_tasks;
    private Button btn_newTask;
    private TextView tv_backlog;
    private TextView tv_done;
    private List<Task> tasks;

    private SimpleAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);

        //Initializing the objects
        lv_tasks = findViewById(R.id.lv_tasks);
        btn_newTask = findViewById(R.id.btn_newTask);
        tv_backlog = findViewById(R.id.tv_backlog);
        tv_done = findViewById(R.id.tv_done);

        fillTaskList();

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodayActivity.this, DoneActivity.class);
                startActivity(intent);
            }
        });

        tv_backlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodayActivity.this, BacklogActivity.class);
                startActivity(intent);
            }
        });

        btn_newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TodayActivity.this, TagActivity.class);
                startActivityForResult(intent, PICK_CONTACT_REQUEST);
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

    private void fillTaskList(){
        //Getting all the tasks registered
        TaskDAO taskDAO = new TaskDAO(this);
        tasks = taskDAO.listTasks();

        //Filling the list view
        if(tasks.size() > 0){
            //Filling the list view
            CustomAdapter customAdapter = new CustomAdapter();
            lv_tasks.setAdapter(customAdapter);
        }
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.list_view_tv_style, null);

            TextView tv_tag = view.findViewById(R.id.tv_tag);
            TextView line_a = view.findViewById(R.id.line_a);
            TextView line_b = view.findViewById(R.id.line_b);

            tv_tag.getBackground().setTint(tasks.get(i).getTag().getColor());
            tv_tag.setText(tasks.get(i).getTag().getName());
            line_a.setText(tasks.get(i).getTitle());
            line_b.setText(tasks.get(i).getDescription());

            return view;
        }
    }
}
