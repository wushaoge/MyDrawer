package com.wsgmac1221.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.wsgmac1221.demo.view.MyDrawer;

public class MyDrawerRightSearchActivity extends AppCompatActivity {


    private Context context;

    private MyDrawer myDrawer;

    private TextView tv_search;

    private Button btnOpen;
    private Button btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_right_search);
        context = this;

        initView();
    }

    private void initView() {
        myDrawer = (MyDrawer)findViewById(R.id.md_mydrawer);


        btnOpen = (Button)findViewById(R.id.btn_open);
        btnClose = (Button)findViewById(R.id.btn_close);

        tv_search = (TextView)findViewById(R.id.tv_search);

        myDrawer.setMyDrawerListener(new MyDrawer.MyDrawerListener() {
            @Override
            public void open() {
                tv_search.setText("确定");
            }

            @Override
            public void close() {
                tv_search.setText("搜索");

            }
        });

        btnOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawer.open();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDrawer.close();
            }
        });





    }
}
