package com.awen.gesture;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;


import com.awen.gesturelib.callback.GesturePasswordCallback;
import com.awen.gesturelib.view.GesturePasswordView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.gLoginErroTv)
    TextView mErroTv;
    @BindView(R.id.mAccountNameTv)
    TextView mAccountNameTv;
    @BindView(R.id.gestureContainer)
    GesturePasswordView gestureContainer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mErroTv.setVisibility(View.INVISIBLE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mAccountNameTv.setText("欢迎您 " + "186****9331");
        gestureContainer.setGesturePasswordCallback(new GesturePasswordCallback() {
            @Override
            public void onGesturePassword(String password) {
                if(password.length()<6){
                    mErroTv.setText("请输入6位以上的手势密码");
                    mErroTv.setVisibility(View.VISIBLE);
                    Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
                    mErroTv.startAnimation(shake);
                    mHandler.sendEmptyMessageDelayed(0, 1500);
                    gestureContainer.clearDrawStatus(1500);
                }else{
                    Toast toast = Toast.makeText(MainActivity.this,String.format("恭喜，密码%s输入正确！！！",password),Toast.LENGTH_SHORT);
                    toast.show();
                    gestureContainer.clearDrawStatus(0);
                }
            }
        });
    }
}