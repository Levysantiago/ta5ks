package com.uesc.tac.ta5ks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Created by levy on 25/04/18.
 */

public class BacklogActivity extends AppCompatActivity {

    private ListView lv_tasks;
    private Button btn_newTask;
    private TextView tv_today;
    private TextView tv_backlog;
    private TextView tv_done;

    private String[] titulo = {"Fazer atv de teoria", "Pegar comprovante de matrícula teste teste"};
    private String[] descricao = {"Fazer atividade e entregar até segunda. É pra pesquisar sobre...",
            "Pegar comprovante de matrícula com TPDB no colegiado."};
    private int[] statusColors = {Color.CYAN, Color.BLUE};

    private SimpleAdapter sAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backlog);

        //Inicializing the objects
        lv_tasks = findViewById(R.id.lv_tasks);
        btn_newTask = findViewById(R.id.btn_newTask);
        tv_today = findViewById(R.id.tv_today);
        tv_backlog = findViewById(R.id.tv_backlog);
        tv_done = findViewById(R.id.tv_done);

        //Filling the list view
        BacklogActivity.CustomAdapter customAdapter = new BacklogActivity.CustomAdapter();
        lv_tasks.setAdapter(customAdapter);

        tv_today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BacklogActivity.this, TodayActivity.class);
                startActivity(intent);
            }
        });

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BacklogActivity.this, DoneActivity.class);
                startActivity(intent);
            }
        });
    }

    class CustomAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return statusColors.length;
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

            View view_status= view.findViewById(R.id.view_status);
            TextView line_a = view.findViewById(R.id.line_a);
            TextView line_b = view.findViewById(R.id.line_b);

            view_status.getBackground().setTint(statusColors[i]);
            line_a.setText(titulo[i]);
            line_b.setText(descricao[i]);

            return view;
        }
    }
}
