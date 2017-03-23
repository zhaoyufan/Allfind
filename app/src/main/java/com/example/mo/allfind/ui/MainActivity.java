package com.example.mo.allfind.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.mo.allfind.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button login,regist;
    ImageView qq,wx,wb;
    SHARE_MEDIA[] platforms = {SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.SINA};
    UMShareAPI umShareAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setOnclick();
    }

    private void setOnclick() {
        login.setOnClickListener(this);
        regist.setOnClickListener(this);
        qq.setOnClickListener(this);
        wx.setOnClickListener(this);
        wb.setOnClickListener(this);

    }


    private void initView() {
        umShareAPI = UMShareAPI.get(this);
        login = (Button) findViewById(R.id.login);
        regist = (Button) findViewById(R.id.regist);
        qq = (ImageView) findViewById(R.id.qq_login);
        wx = (ImageView) findViewById(R.id.wx_login);
        wb = (ImageView) findViewById(R.id.wb_login);
    }
    /**
     * 授权登录回调接口
     */
    UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            //授权开始的回调
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            //获取授权成功后的用户信息，回调接口里获取数据
            Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            String temp = "";
            for (String key : data.keySet()) {
                temp = temp + key + " : " + data.get(key) + "\n";
            }
            umShareAPI.getPlatformInfo(MainActivity.this, platforms[0], new UMAuthListener() {
                @Override
                public void onStart(SHARE_MEDIA share_media) {

                }

                @Override
                public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                    Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
                    String temp = "";
                    for (String key : map.keySet()) {
                        temp = temp + key + " : " + map.get(key) + "\n";
                    }
                    Log.i("aaa",temp);
                    Intent intent = new Intent(MainActivity.this,MapActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

                }

                @Override
                public void onCancel(SHARE_MEDIA share_media, int i) {

                }
            });

        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            Toast.makeText( MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText( MainActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login:
                Intent intent = new Intent(MainActivity.this,MapActivity.class);
                startActivity(intent);
                break;
            case R.id.regist:
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
                break;
            case R.id.qq_login:
                umShareAPI.doOauthVerify(MainActivity.this, platforms[0], umAuthListener);
                break;
            case R.id.wx_login:
                Toast.makeText(MainActivity.this,"微信登录还在建设中...",Toast.LENGTH_SHORT).show();
                break;
            case R.id.wb_login:
                Toast.makeText(MainActivity.this,"微博登录还在建设中...",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
