package com.example.david.header_animation_demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.david.header_animation_demo.indicator.AbsTransformationBarIndicator;
import com.example.david.header_animation_demo.indicator.TransformationBarIndicator;
import com.example.david.header_animation_demo.widget.ObservableListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private ObservableListView listView;
    private List<String> dataArray;
    private final static int DATA_COUNT = 50;
    private View headView;
    private View animatorView;
    private View headViewTopLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        initData();
        init();
    }

    private void init() {
        listView = (ObservableListView) findViewById(R.id.listview);
        animatorView = findViewById(R.id.animator_view);
        headView = LayoutInflater.from(this).inflate(R.layout.tj_refresh_header, null);
        headViewTopLayout = headView.findViewById(R.id.refresh_head_top_layout);
        listView.addHeaderView(headView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, dataArray);
        listView.setAdapter(adapter);

        AbsTransformationBarIndicator indicator = new TransformationBarIndicator(this, listView, headView, animatorView);

        animatorView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        if(dataArray == null) {
            dataArray = new ArrayList<>();
        }
        for(int i = 0; i < DATA_COUNT; i++) {
            dataArray.add(String.valueOf(i + 1));
        }
    }
}
