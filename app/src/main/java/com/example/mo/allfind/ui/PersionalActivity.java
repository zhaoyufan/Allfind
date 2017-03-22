package com.example.mo.allfind.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mo.allfind.R;

public class PersionalActivity extends AppCompatActivity {
    private LinearLayout myyouhui;
    private TextView common_title_bar_left,tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persional);
        myyouhui= (LinearLayout) findViewById(R.id.second_myyouhui);
        myyouhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(PersionalActivity.this,YouhuiActivity.class);
                startActivity(it);
            }
        });

        common_title_bar_left = (TextView) findViewById(R.id.common_title_bar_left);
        common_title_bar_left.setVisibility(View.VISIBLE);
        common_title_bar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title= (TextView) findViewById(R.id.tv_title);
        tv_title.setText("个人中心");
    }
}
