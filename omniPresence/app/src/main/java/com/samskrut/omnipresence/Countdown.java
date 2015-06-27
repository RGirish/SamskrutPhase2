package com.samskrut.omnipresence;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;


public class Countdown extends AppCompatActivity {

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown);

        setFullscreen(true);

        /*Intent intent = new Intent(Countdown.this,ProjectList.class);
        startActivity(intent);
        finish();*/

        countDownTimer = new CountDownTimer(11000, 1000) {

            public void onTick(long millisUntilFinished) {
                ((TextView)findViewById(R.id.count)).setText(String.valueOf(millisUntilFinished/1000));
            }
            public void onFinish() {
                Intent intent = new Intent(Countdown.this,ProjectList.class);
                startActivity(intent);
                finish();
            }
        };

        countDownTimer.start();
    }

    public void onBackPressed(){
        countDownTimer.cancel();
        super.onBackPressed();
    }

    

    private void setFullscreen(boolean fullscreen) {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        if (fullscreen) {
            attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        else{
            attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        }
        getWindow().setAttributes(attrs);
    }

}