package com.wsgmac1221.demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wsgmac1221.demo.view.MyDrawer;

public class MainActivity extends AppCompatActivity {


    private Context context;


    private Button btnBottom;
    private Button btnTop;


    private Button btnLeft;
    private Button btnRight;


    private Button btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initView();
    }

    private void initView() {

        btnBottom = (Button)findViewById(R.id.btn_bottom);
        btnTop = (Button)findViewById(R.id.btn_top);

        btnLeft = (Button)findViewById(R.id.btn_left);
        btnRight = (Button)findViewById(R.id.btn_right);


        btnSearch = (Button)findViewById(R.id.btn_search);


        btnBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MyDrawerBottomActivity.class));
            }
        });

        btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MyDrawerTopActivity.class));
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MyDrawerLeftActivity.class));
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MyDrawerRightActivity.class));
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MyDrawerRightSearchActivity.class));
            }
        });

    }
}
