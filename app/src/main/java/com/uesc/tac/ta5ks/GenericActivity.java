package com.uesc.tac.ta5ks;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.uesc.tac.ta5ks.dao.TaskDAO;
import com.uesc.tac.ta5ks.model.Task;

import java.util.List;

/**
 * Created by levy on 19/05/18.
 */

public class GenericActivity extends AppCompatActivity {

    //Is necessary to set this attribute in son classes
    private static int STATUS;
    protected List<Task> tasks;
    protected ListView lv_tasks;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initializing(){
        lv_tasks = findViewById(R.id.lv_tasks);
        customAdapter = new CustomAdapter();
    }

    protected void fillTaskList(){
        //Getting all the tasks registered
        TaskDAO taskDAO = new TaskDAO(this);
        tasks = taskDAO.listTasks(STATUS);

        //Filling the list view
        if(tasks.size() > 0){
            //Filling the list view
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

        @SuppressLint("ResourceType")
        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.template_list_view, null);

            TextView tv_tag = view.findViewById(R.id.tv_tag);
            TextView line_a = view.findViewById(R.id.line_a);
            TextView line_b = view.findViewById(R.id.line_b);
            ImageView img_change_status = view.findViewById(R.id.img_change_status);
            img_change_status.setImageResource(R.drawable.ic_check);
            //The view id is the same item id in the list tasks
            img_change_status.setId(i);
            img_change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Taking the corresponding task in the list
                    TaskDAO taskDAO = new TaskDAO(GenericActivity.this);
                    Task task = tasks.get( view.getId() );
                    task.setStatus( DoneActivity.getSTATUS() );
                    taskDAO.updateTask(task);
                    fillTaskList();
                }
            });

            //The view id is the same item id in the list tasks
            view.setId(i);
            tv_tag.getBackground().setTint(tasks.get(i).getTag().getColor());
            tv_tag.setText(tasks.get(i).getTag().getName());
            line_a.setText(tasks.get(i).getTitle());
            line_b.setText(tasks.get(i).getDescription());

            return view;
        }
    }

    public static int getSTATUS() {
        return STATUS;
    }

    public static void setSTATUS(int STATUS) {
        GenericActivity.STATUS = STATUS;
    }
}
