package com.uesc.tac.ta5ks;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.uesc.tac.ta5ks.dao.TagDAO;
import com.uesc.tac.ta5ks.dao.TaskDAO;
import com.uesc.tac.ta5ks.model.Tag;
import com.uesc.tac.ta5ks.model.Task;
import com.uesc.tac.ta5ks.util.ChipControler;

import java.util.List;

import fisk.chipcloud.ToggleChip;
import yuku.ambilwarna.AmbilWarnaDialog;

/**
 * Created by levy on 02/05/18.
 */

public class TaskActivity extends AppCompatActivity {
    private float scale;
    private FlexboxLayout flexbox;
    private EditText edit_title;
    private EditText edit_description;
    private ImageView img_NewProject;
    private ImageView img_back;
    private Button btn_createTask;
    private ConstraintLayout lt_constraint;
    private ViewGroup layout;
    private TextView tv_title;

    private int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        scale = this.getResources().getDisplayMetrics().density;

        //Getting the components
        flexbox = findViewById(R.id.lt_flexbox);
        lt_constraint = findViewById(R.id.lt_constraint);
        edit_title = findViewById(R.id.edit_title);
        edit_description = findViewById(R.id.edit_description);
        img_NewProject = findViewById(R.id.img_newProject);
        img_back = findViewById(R.id.img_back);
        btn_createTask = findViewById(R.id.btn_createTask);
        tv_title = findViewById(R.id.tv_title);
        defaultColor = ContextCompat.getColor(this, R.color.colorPrimary);

        //Updating list of tags
        updateFlexbox(flexbox);

        //Getting the attributes from another activity
        String text = getIntent().getStringExtra("pageTitle");
        if(text != null){
            tv_title.setText(text);
        }else{
            tv_title.setText("add new task");
        }
        text = getIntent().getStringExtra("btn_createTask");
        if(text != null){
            btn_createTask.setText( getIntent().getStringExtra("btn_createTask") );
        }else{
            btn_createTask.setText( "create task" );
        }
        //The default status is 1 because the first registration is in backlog
        final int STATUS = getIntent().getIntExtra("status", 1);
        edit_title.setText( getIntent().getStringExtra("title") );
        edit_description.setText( getIntent().getStringExtra("description") );
        final int task_id = getIntent().getIntExtra("task_id", -1);
        setSelectedTagByName( getIntent().getStringExtra("selectedTag") );

        //Back button
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Register task button
        btn_createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout = flexbox;
                TagDAO tagDAO = new TagDAO(TaskActivity.this);
                TaskDAO taskDAO = new TaskDAO(TaskActivity.this);
                Integer selectedProject = getSelectedTagIndex();

                //Validating
                if(edit_title.getText().toString().isEmpty()){
                    Toast.makeText(TaskActivity.this, "Please, insert a valid title",
                            Toast.LENGTH_SHORT).show();
                    edit_title.requestFocus();
                }else if(selectedProject == null){
                    Toast.makeText(TaskActivity.this, "Please, select a project",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(selectedProject != null){
                        //Getting the corresponding chip
                        final ToggleChip chip = (ToggleChip) layout.getChildAt(selectedProject);

                        Tag tag = tagDAO.searchTag(chip.getText().toString());

                        Task task = new Task(edit_title.getText().toString(),
                                edit_description.getText().toString(), STATUS, tag);


                        //Inserting informations on database
                        if(btn_createTask.getText().equals("create task")){
                            //create a new task
                            taskDAO.addTask(task);
                            Toast.makeText(TaskActivity.this, "Task Registered!", Toast.LENGTH_SHORT).show();
                        }else{
                            //update task
                            task.setId(task_id);
                            taskDAO.updateTask(task);
                            Toast.makeText(TaskActivity.this, "Task Updated!", Toast.LENGTH_SHORT).show();
                        }

                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }

            }
        });

        //New Project/Tag button
        img_NewProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenColorPicker(false);
            }
        });
    }

    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDrag(data, shadowBuilder, view, 0);
            view.setVisibility(View.INVISIBLE);
            img_NewProject.setImageDrawable(ContextCompat.getDrawable(TaskActivity.this, R.drawable.remove));

            return true;
        }
    };

    View.OnDragListener onDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent dragEvent) {
            int dragEvet = dragEvent.getAction();
            final ToggleChip chip = (ToggleChip) dragEvent.getLocalState();

            switch (dragEvet){
                case DragEvent.ACTION_DRAG_ENTERED : {
                    img_NewProject.setImageDrawable(ContextCompat.getDrawable(TaskActivity.this, R.drawable.remove));

                    //Changing width and height
                    img_NewProject.getLayoutParams().width = (int) (70 * scale + 0.5f);
                    img_NewProject.getLayoutParams().height = (int) (70 * scale + 0.5f);
                    img_NewProject.requestLayout();
                    break;
                }
                case DragEvent.ACTION_DRAG_EXITED : {
                    //Updating the width and height to old size
                    img_NewProject.getLayoutParams().width = (int) (50 * scale + 0.5f);
                    img_NewProject.getLayoutParams().height = (int) (50 * scale + 0.5f);
                    img_NewProject.requestLayout();
                    break;
                }
                case DragEvent.ACTION_DRAG_ENDED : {
                    chip.setVisibility(View.VISIBLE);

                    //Updating the width and height to old size
                    img_NewProject.setImageDrawable(ContextCompat.getDrawable(TaskActivity.this, R.drawable.add));
                    img_NewProject.getLayoutParams().width = (int) (50 * scale + 0.5f);
                    img_NewProject.getLayoutParams().height = (int) (50 * scale + 0.5f);
                    img_NewProject.requestLayout();
                    break;
                }
                case DragEvent.ACTION_DROP : {
                    TagDAO tagDAO = new TagDAO(TaskActivity.this);
                    Tag tag = tagDAO.searchTag((String) chip.getText());

                    //Removing tag from DB
                    callDeleteTagDialog(tag);
                    img_NewProject.setImageDrawable(ContextCompat.getDrawable(TaskActivity.this, R.drawable.add));

                    //Updating the width and height to old size
                    img_NewProject.getLayoutParams().width = (int) (50 * scale + 0.5f);
                    img_NewProject.getLayoutParams().height = (int) (50 * scale + 0.5f);
                    img_NewProject.requestLayout();

                    updateFlexbox(flexbox);
                    break;
                }
            }

            return true;
        }
    };

    public void setSelectedTagByName(String tagName) {
        for (int i = 0; i < flexbox.getChildCount(); i++) {
            ToggleChip chip = (ToggleChip) this.flexbox.getChildAt(i);
            if(chip.getText().equals(tagName)){
                chip.callOnClick();
                break;
            }
        }
    }

    public Integer getSelectedTagIndex() {
        Integer index = null;
        for (int i = 0; i < layout.getChildCount(); i++) {
            ToggleChip chip = (ToggleChip) this.layout.getChildAt(i);
            if (chip.isChecked()) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void callDeleteTagDialog(Tag tag){
        TagDAO tagDAO = new TagDAO(this);

        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) TaskActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view (Using the project name template to doesn't need to create another one)
        final View customView = inflater.inflate(R.layout.template_new_project,null);

        //Updating the dialog to this aim
        TextView tv_content = customView.findViewById(R.id.tv_content);
        EditText editText = customView.findViewById(R.id.edt_projectName);

        //Disabling the editText
        editText.setEnabled(false);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TaskActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(customView);

        //Verifying if the tag is used by a task
        boolean used = tagDAO.isTagUsed(tag);

        if(used){
            alertDialogBuilder.setTitle("Warning!");
            tv_content.setText("This tag is been used by a task. It can't be deleted.");

            // set dialog message
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                }
                            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }else{
            //Removing tag from DB
            tagDAO.removeTag(tag);
            Toast.makeText(TaskActivity.this, "Tag "+tag.getName()+" removed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void callProjectNameDialog(final int color){
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) TaskActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        final View customView = inflater.inflate(R.layout.template_new_project,null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TaskActivity.this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(customView);
        alertDialogBuilder.setTitle("New Project");

        final EditText userInput = customView.findViewById(R.id.edt_projectName);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // Inserting a new tag in database
                                TagDAO tagDAO = new TagDAO(TaskActivity.this);
                                String name = userInput.getText().toString();
                                Tag tag = tagDAO.searchTag(name);
                                if(tag != null){
                                    Toast.makeText(TaskActivity.this, "Tag \""+name+"\"" +
                                            " already exists! ", Toast.LENGTH_LONG).show();
                                }else{
                                    tagDAO.addTag(new Tag(name, color));
                                    Toast.makeText(TaskActivity.this, "Tag Registered! "
                                            , Toast.LENGTH_LONG).show();

                                    updateFlexbox(flexbox);
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void updateFlexbox(FlexboxLayout flexbox){
        TagDAO tagDAO = new TagDAO(TaskActivity.this);
        List<Tag> tags = tagDAO.listTags();
        flexbox.removeAllViews();
        int chipCount = 0;

        if(tags.size() > 0){
            for(Tag tag : tags){
                ChipControler.createChips(this, tag.getName(), tag.getColor(), flexbox);
                //Setting the long click event to each chip
                final ToggleChip chip = (ToggleChip) flexbox.getChildAt(chipCount++);
                chip.setOnLongClickListener(onLongClickListener);

                img_NewProject.setOnDragListener(onDragListener);
            }
        }
    }

    private void OpenColorPicker (boolean AlphaSupport){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(TaskActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                callProjectNameDialog(color);
            }
        });

        ambilWarnaDialog.show();
    }
}
