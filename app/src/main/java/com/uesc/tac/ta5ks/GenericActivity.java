package com.uesc.tac.ta5ks;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.uesc.tac.ta5ks.dao.TagDAO;
import com.uesc.tac.ta5ks.dao.TaskDAO;
import com.uesc.tac.ta5ks.model.Tag;
import com.uesc.tac.ta5ks.model.Task;

import java.util.List;

/**
 * Created by levy on 19/05/18.
 */

public class GenericActivity extends AppCompatActivity {

    //Is necessary to set this attribute in son classes
    private static final int PICK_CONTACT_REQUEST = 1;
    protected static int STATUS;
    protected static int NEXT_STATUS;
    private ImageView img_settings;
    private Button btn_newTask;
    protected List<Task> tasks;
    protected ListView lv_tasks;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initializing(){
        lv_tasks = findViewById(R.id.lv_tasks);
        img_settings = findViewById(R.id.img_settings);
        btn_newTask = findViewById(R.id.btn_newTask);

        img_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GenericActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        btn_newTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GenericActivity.this, TaskActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

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

    protected void updateTaskList(){
        //Getting all the tasks registered
        TaskDAO taskDAO = new TaskDAO(this);
        tasks = taskDAO.listTasks(STATUS);

        customAdapter.notifyDataSetChanged();
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
            CharSequence message = "";

            final TextView tv_tag = view.findViewById(R.id.tv_tag);
            final TextView line_a = view.findViewById(R.id.line_a);
            final TextView line_b = view.findViewById(R.id.line_b);
            ImageView img_change_status = view.findViewById(R.id.img_change_status);

            switch (STATUS){
                case 1:{
                    img_change_status.setImageResource(R.drawable.ic_play);
                    message = "Status changed to today";
                    break;
                }
                case 2:{
                    img_change_status.setImageResource(R.drawable.ic_check);
                    message = "Status changed to done";
                    break;
                }
                case 3:{
                    img_change_status.setImageResource(R.drawable.ic_remove);
                    message = "Tag removed";
                    break;
                }
                default:{
                    img_change_status.setImageResource(R.drawable.ic_check);
                    break;
                }
            }
            //The view id is the same item id in the list tasks
            img_change_status.setId(i);
            final CharSequence finalMessage = message;
            img_change_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Taking the corresponding task in the list
                    TaskDAO taskDAO = new TaskDAO(GenericActivity.this);
                    Task task = tasks.get( view.getId() );

                    //If the status isn't Done
                    if(STATUS != 3){
                        task.setStatus(NEXT_STATUS);
                    }else{
                        //Add a question before removing the task ---------------- THIS IS IMPORTANT
                        //Removing a task
                        taskDAO.removeTask(task);
                    }

                    taskDAO.updateTask(task);
                    updateTaskList();
                    Toast.makeText(GenericActivity.this, finalMessage, Toast.LENGTH_SHORT).show();
                }
            });

            //The view id is the same item id in the list tasks
            view.setId(i);
            view.setBackground(ContextCompat.getDrawable(GenericActivity.this, R.drawable.ripple_effect));
            tv_tag.getBackground().setTint(tasks.get(i).getTag().getColor());
            tv_tag.setText(tasks.get(i).getTag().getName());
            line_a.setText(tasks.get(i).getTitle());
            line_b.setText(tasks.get(i).getDescription());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(GenericActivity.this, TaskActivity.class);

                    intent.putExtra("pageTitle", "edit task");
                    intent.putExtra("title", line_a.getText());
                    intent.putExtra("description", line_b.getText());
                    intent.putExtra("selectedTag", tv_tag.getText());
                    intent.putExtra("btn_createTask", "update task");
                    intent.putExtra("task_id", tasks.get(view.getId()).getId());

                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    callChangeStatusDialog(tasks.get(view.getId()));

                    return false;
                }
            });

            //Changing the text style to strike, only to done activity
            if(STATUS == 3){
                line_a.setPaintFlags(line_a.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                line_a.setPaintFlags(line_a.getPaintFlags() | Paint.HINTING_OFF);
            }

            return view;
        }
    }

    public void callChangeStatusDialog(final Task task){
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) GenericActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        final View customView = inflater.inflate(R.layout.template_change_status,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(GenericActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(customView);
        //Setting the dialog title
        alertDialogBuilder.setTitle("Change status to");

        //Setting the objects
        final RadioButton radio_button_1 = customView.findViewById(R.id.radio_1);
        final RadioButton radio_button_2 = customView.findViewById(R.id.radio_2);

        switch (STATUS){
            case 1:{
                radio_button_1.setText("Today");
                radio_button_2.setText("Done");
                break;
            }
            case 2:{
                radio_button_1.setText("Backlog");
                radio_button_2.setText("Done");
                break;
            }
            case 3:{
                radio_button_1.setText("Today");
                radio_button_2.setText("Backlog");
                break;
            }
        }

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                TaskDAO taskDAO = new TaskDAO(GenericActivity.this);
                                String selected = "";
                                int changedStatus;

                                if(radio_button_1.isChecked()){
                                    selected = radio_button_1.getText().toString();
                                }else if(radio_button_2.isChecked()){
                                    selected = radio_button_2.getText().toString();
                                }else{
                                    //Validating
                                }

                                switch (selected){
                                    case "Today":{
                                        changedStatus = TodayActivity.getSTATUS();
                                        break;
                                    }
                                    case "Backlog":{
                                        changedStatus = BacklogActivity.getSTATUS();
                                        break;
                                    }
                                    case "Done":{
                                        changedStatus = DoneActivity.getSTATUS();
                                        break;
                                    }
                                    default:{
                                        changedStatus = STATUS;
                                        break;
                                    }
                                }

                                task.setStatus( changedStatus );
                                taskDAO.updateTask(task);
                                Toast.makeText(GenericActivity.this, "Task changed to "+selected+" status.",
                                        Toast.LENGTH_SHORT).show();

                                updateTaskList();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Cancel button
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                updateTaskList();
            }
        }
    }

    public static void setSTATUS(int STATUS) {
        GenericActivity.STATUS = STATUS;
    }

    public static void setNextStatus(int nextStatus) {
        NEXT_STATUS = nextStatus;
    }
}
