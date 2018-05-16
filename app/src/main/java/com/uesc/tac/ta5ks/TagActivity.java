package com.uesc.tac.ta5ks;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class TagActivity extends AppCompatActivity {
    private float scale;
    private FlexboxLayout flexbox;
    private EditText edit_title;
    private EditText edit_description;
    private ImageView img_NewProject;
    private ImageView img_back;
    private Button btn_finalizeTask;
    private ConstraintLayout lt_constraint;
    private ViewGroup layout;

    private int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag);

        scale = this.getResources().getDisplayMetrics().density;

        //Getting the components
        flexbox = findViewById(R.id.lt_flexbox);
        lt_constraint = findViewById(R.id.lt_constraint);
        edit_title = findViewById(R.id.edit_title);
        edit_description = findViewById(R.id.edit_description);
        img_NewProject = findViewById(R.id.img_newProject);
        img_back = findViewById(R.id.img_back);
        btn_finalizeTask = findViewById(R.id.btn_finalizeTask);
        defaultColor = ContextCompat.getColor(this, R.color.colorPrimary);

        //Updating list of tags
        updateFlexbox(flexbox);

        //Back button
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Register task button
        btn_finalizeTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout = flexbox;
                TagDAO tagDAO = new TagDAO(TagActivity.this);
                TaskDAO taskDAO = new TaskDAO(TagActivity.this);
                Integer selectedProject = getSelectedTagIndex();
                if(selectedProject != null){
                    //Conseguimos obter o nome
                    final ToggleChip chip = (ToggleChip) layout.getChildAt(selectedProject);

                    Tag tag = tagDAO.searchTag(chip.getText().toString());
                    Task task = new Task(edit_title.getText().toString(),
                            edit_description.getText().toString(),1, tag);
                    taskDAO.addTask(task);
                    Toast.makeText(TagActivity.this, "Task Registered!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
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
            img_NewProject.setImageDrawable(ContextCompat.getDrawable(TagActivity.this, R.drawable.remove));

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
                    img_NewProject.setImageDrawable(ContextCompat.getDrawable(TagActivity.this, R.drawable.remove));

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
                    img_NewProject.setImageDrawable(ContextCompat.getDrawable(TagActivity.this, R.drawable.add));
                    img_NewProject.getLayoutParams().width = (int) (50 * scale + 0.5f);
                    img_NewProject.getLayoutParams().height = (int) (50 * scale + 0.5f);
                    img_NewProject.requestLayout();
                    break;
                }
                case DragEvent.ACTION_DROP : {
                    TagDAO tagDAO = new TagDAO(TagActivity.this);
                    Tag tag = new Tag();
                    tag.setName((String) chip.getText());
                    //Removing tag from DB
                    tagDAO.removeTag(tag);
                    img_NewProject.setImageDrawable(ContextCompat.getDrawable(TagActivity.this, R.drawable.add));
                    Toast.makeText(TagActivity.this, "Tag "+tag.getName()+" removed.", Toast.LENGTH_SHORT).show();

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

    public Integer getSelectedTagIndex() {
        Integer index = null;
        for (int i = 0; i < layout.getChildCount(); i++) {
            ToggleChip chip = (ToggleChip) this.layout.getChildAt(i);
            if (chip.isChecked()) {
                index = i;
            }
        }
        return index;
    }

    public void callProjectNameDialog(final int color){
        // Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) TagActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        final View customView = inflater.inflate(R.layout.template_new_project,null);

        int width = (int) (300 * scale + 0.5f);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TagActivity.this);

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
                                TagDAO tagDAO = new TagDAO(TagActivity.this);
                                String name = userInput.getText().toString();
                                tagDAO.addTag(new Tag(name, color));

                                Toast.makeText(TagActivity.this, "Tag Registered! ", Toast.LENGTH_LONG).show();

                                updateFlexbox(flexbox);
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
        TagDAO tagDAO = new TagDAO(TagActivity.this);
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
                Toast.makeText(TagActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                defaultColor = color;
                //addNewProject(color, flexbox);
                callProjectNameDialog(color);
            }
        });

        ambilWarnaDialog.show();
    }
}
