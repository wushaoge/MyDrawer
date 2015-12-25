package com.wsgmac1221.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wsgmac1221.demo.view.MyDrawer;

public class MyDrawerBottomActivity extends AppCompatActivity {


    private Context context;

    private MyDrawer myDrawer;

    private Button btnOpen;
    private Button btnClose;


    private Button btnWeibo;
    private Button btnQq;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_bottom);
        context = this;

        initView();
    }

    private void initView() {
        myDrawer = (MyDrawer)findViewById(R.id.md_mydrawer);

        myDrawer.setMyDrawerListener(new MyDrawer.MyDrawerListener() {
            @Override
            public void open() {
                Toast.makeText(context,"打开的回调函数",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void close() {
                Toast.makeText(context,"关闭的回调函数",Toast.LENGTH_SHORT).show();
            }
        });

        btnOpen = (Button)findViewById(R.id.btn_open);
        btnClose = (Button)findViewById(R.id.btn_close);

        btnWeibo = (Button)findViewById(R.id.btn_weibo);
        btnQq = (Button)findViewById(R.id.btn_qq);



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





        btnWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"微博登陆",Toast.LENGTH_SHORT).show();
            }
        });

        btnQq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"QQ登陆",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
