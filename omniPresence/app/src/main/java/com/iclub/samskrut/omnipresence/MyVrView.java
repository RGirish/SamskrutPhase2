
package com.iclub.samskrut.omnipresence;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;
import org.rajawali3d.cardboard.RajawaliCardboardRenderer;
import org.rajawali3d.cardboard.RajawaliCardboardView;
import javax.microedition.khronos.egl.EGLConfig;

public class MyVrView extends CardboardActivity implements CardboardView.StereoRenderer, SensorEventListener {

    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    private float X, Y, Z;
    int projectPos;
    int pos;
    String TAG="MYVRVIEW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        projectPos = getIntent().getIntExtra("projectPos",0);
        pos = getIntent().getIntExtra("pos",0);

        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        RajawaliCardboardView view = new RajawaliCardboardView(this);
        setContentView(view);
        setCardboardView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vv) {
                Log.e(TAG, "onCardboardTrigger TOUCH");
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(100);

                int Xint=(int)X;
                int Yint=(int)Y;
                int Zint=(int)Z;
                Log.e("VALUES" , (Xint)+" "+(Yint)+" "+(Zint));


                if((Xint==0 || Xint==1 || Xint==2) && (Yint==0 || Yint==1 || Yint==2) && (Zint==10 || Zint==9 || Zint==8)){
                    MyVrView.super.onBackPressed();
                    return;
                }

                //goes to the next image
                Cursor cursor2 = ProjectList.db.rawQuery("SELECT COUNT(pos) FROM subProjects WHERE projectPos=" + projectPos + ";", null);
                cursor2.moveToFirst();
                int COUNT_2 = cursor2.getInt(0);
                cursor2.close();
                if (pos + 1 < COUNT_2) {
                    Intent intent = new Intent(MyVrView.this, MyVrView.class);
                    intent.putExtra("projectPos", projectPos);
                    intent.putExtra("pos", pos + 1);
                    startActivity(intent);
                    finish();
                } else {
                    Cursor cursor1 = ProjectList.db.rawQuery("SELECT COUNT(pos) FROM projects;", null);
                    cursor1.moveToFirst();
                    int COUNT_1 = cursor1.getInt(0);
                    cursor1.close();
                    if (projectPos + 1 < COUNT_1) {
                        Intent intent = new Intent(MyVrView.this, MyVrView.class);
                        intent.putExtra("projectPos", projectPos + 1);
                        intent.putExtra("pos", 0);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(MyVrView.this, MyVrView.class);
                        intent.putExtra("projectPos", 0);
                        intent.putExtra("pos", 0);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });

        RajawaliCardboardRenderer renderer = new MyRenderer(this, projectPos,pos);
        view.setRenderer(renderer);
        view.setSurfaceRenderer(renderer);

        ProjectList.tts.stop();
        Cursor cursor = ProjectList.db.rawQuery("SELECT tts FROM subProjects WHERE projectPos=" + projectPos + " AND pos=" + pos + ";", null);
        cursor.moveToFirst();
        if(android.os.Build.VERSION.SDK_INT >= 21){
            ProjectList.tts.speak(cursor.getString(0), TextToSpeech.QUEUE_FLUSH, null, null);
        }else{
            ProjectList.tts.speak(cursor.getString(0), TextToSpeech.QUEUE_FLUSH, null);
        }
        cursor.close();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)){

                //goes to the next image
                Cursor cursor2 = ProjectList.db.rawQuery("SELECT COUNT(pos) FROM subProjects WHERE projectPos=" + projectPos + ";", null);
                cursor2.moveToFirst();
                int COUNT_2 = cursor2.getInt(0);
                cursor2.close();
                if (pos + 1 < COUNT_2) {
                    Intent intent = new Intent(this, MyVrView.class);
                    intent.putExtra("projectPos", projectPos);
                    intent.putExtra("pos", pos + 1);
                    startActivity(intent);
                    finish();
                } else {
                    Cursor cursor1 = ProjectList.db.rawQuery("SELECT COUNT(pos) FROM projects;", null);
                    cursor1.moveToFirst();
                    int COUNT_1 = cursor1.getInt(0);
                    cursor1.close();
                    if (projectPos + 1 < COUNT_1) {
                        Intent intent = new Intent(this, MyVrView.class);
                        intent.putExtra("projectPos", projectPos + 1);
                        intent.putExtra("pos", 0);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(this, MyVrView.class);
                        intent.putExtra("projectPos", 0);
                        intent.putExtra("pos", 0);
                        startActivity(intent);
                        finish();
                    }
                }
        } else{
            //goes back
            super.onBackPressed();
        }
        return true;
    }

    @Override
    public void onCardboardTrigger() {
        Log.e(TAG, "onCardboardTrigger");
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(100);

        int Xint=(int)X;
        int Yint=(int)Y;
        int Zint=(int)Z;
        Log.e("VALUES", (Xint) + " " + (Yint) + " " + (Zint));

        if((Xint==0 || Xint==1 || Xint==2) && (Yint==0 || Yint==1 || Yint==2) && (Zint==10 || Zint==9 || Zint==8)){
            MyVrView.super.onBackPressed();
            return;
        }

        //goes to the next image
        Cursor cursor2 = ProjectList.db.rawQuery("SELECT COUNT(pos) FROM subProjects WHERE projectPos=" + projectPos + ";", null);
        cursor2.moveToFirst();
        int COUNT_2 = cursor2.getInt(0);
        cursor2.close();
        if (pos + 1 < COUNT_2) {
            Intent intent = new Intent(MyVrView.this, MyVrView.class);
            intent.putExtra("projectPos", projectPos);
            intent.putExtra("pos", pos + 1);
            startActivity(intent);
            finish();
        } else {
            Cursor cursor1 = ProjectList.db.rawQuery("SELECT COUNT(pos) FROM projects;", null);
            cursor1.moveToFirst();
            int COUNT_1 = cursor1.getInt(0);
            cursor1.close();
            if (projectPos + 1 < COUNT_1) {
                Intent intent = new Intent(MyVrView.this, MyVrView.class);
                intent.putExtra("projectPos", projectPos + 1);
                intent.putExtra("pos", 0);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(MyVrView.this, MyVrView.class);
                intent.putExtra("projectPos", 0);
                intent.putExtra("pos", 0);
                startActivity(intent);
                finish();
            }
        }

    }

    @Override
    public void onRendererShutdown() {}

    @Override
    public void onSurfaceChanged(int width, int height) {}

    @Override
    public void onSurfaceCreated(EGLConfig config) {}

    @Override
    public void onNewFrame(HeadTransform headTransform) {}

    @Override
    public void onDrawEye(Eye eye) {}

    @Override
    public void onFinishFrame(Viewport viewport) {}




    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;
        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            X = sensorEvent.values[0];
            Y = sensorEvent.values[1];
            Z = sensorEvent.values[2];
            //Log.e("ACCELEROMETER",X+" "+Y+" "+Z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    protected void onPause() {
        super.onPause();
        senSensorManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
}