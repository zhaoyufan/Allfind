package com.example.mo.allfind.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.mo.allfind.R;

public class YouhuiActivity extends AppCompatActivity {
    private TextView common_title_bar_left,tv_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youhui);
        common_title_bar_left= (TextView) findViewById(R.id.common_title_bar_left);
        common_title_bar_left.setVisibility(View.VISIBLE);
        common_title_bar_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("优惠券");
    }
}
