package com.example.fruits2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    SensorManager manager;
    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView enemy;
    private ImageView fruit1;
    private ImageView fruit2;
    private ImageView fruit3;
    private ImageView fruit4;
    private ImageView fruit5;

    private int frameHeight;
    private int frameWidth;
    private int boxSize;

    private int screenWidth;
    private int screenHeight;

    private Handler handler = new Handler();
    private Timer timer = new Timer();

    private boolean action_right = false;
    private boolean action_left = false;
    private boolean start_flg = false;

    // 位置
    private float boxX, enemyX, enemyY, fruit1X, fruit1Y,fruit2X, fruit2Y,fruit3X, fruit3Y,fruit4X, fruit4Y,fruit5X, fruit5Y;

    private int score = 0;

    SensorEventListener listener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if(sensorEvent.values.length >= 1) {
                if(sensorEvent.values[1] > 1.0 ){
                    action_right = true;
                } else if (sensorEvent.values[1] < -1.0) {
                    action_left = true;
                } else {
                    action_right = false;
                    action_left = false;
                }
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoreLabel = findViewById(R.id.scoreLabel);
        startLabel = findViewById(R.id.startLabel);
        box = findViewById(R.id.box);
        fruit1 = findViewById(R.id.fruit1);
        fruit2 = findViewById(R.id.fruit2);
        fruit3 = findViewById(R.id.fruit3);
        fruit4 = findViewById(R.id.fruit4);
        fruit5 = findViewById(R.id.fruit5);
        enemy = findViewById(R.id.enemy);

        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        screenWidth = size.x;
        screenHeight = size.y;

        fruit1.setX(-80.0f);
        fruit1.setY(screenHeight+80.0f);
        fruit2.setX(-80.0f);
        fruit2.setY(screenHeight+80.0f);
        fruit3.setX(-80.0f);
        fruit3.setY(screenHeight+80.0f);
        fruit4.setX(-80.0f);
        fruit4.setY(screenHeight+80.0f);
        fruit5.setX(-80.0f);
        fruit5.setY(screenHeight+80.0f);
        enemy.setX(-80.0f);
        enemy.setY(screenHeight+80.0f);

        scoreLabel.setText("Score：0");


    }

    public void changePos() {
        float enemyCenterX = enemyX + enemy.getWidth() / 2;
        float enemyCenterY = enemyY + enemy.getHeight() / 2;
        hitCheck(enemyCenterX, enemyCenterY);
        float fruit1CenterX = fruit1X + fruit1.getWidth() / 2;
        float fruit1CenterY = fruit1Y + fruit1.getHeight() / 2;
        hitCheck(fruit1CenterX, fruit1CenterY);
        float fruit2CenterX = fruit2X + fruit2.getWidth() / 2;
        float fruit2CenterY = fruit2Y + fruit2.getHeight() / 2;
        hitCheck(fruit2CenterX, fruit2CenterY);
        float fruit3CenterX = fruit3X + fruit3.getWidth() / 2;
        float fruit3CenterY = fruit3Y + fruit3.getHeight() / 2;
        hitCheck(fruit3CenterX, fruit3CenterY);
        float fruit4CenterX = fruit4X + fruit4.getWidth() / 2;
        float fruit4CenterY = fruit4Y + fruit4.getHeight() / 2;
        hitCheck(fruit4CenterX, fruit4CenterY);
        float fruit5CenterX = fruit5X + fruit5.getWidth() / 2;
        float fruit5CenterY = fruit5Y + fruit5.getHeight() / 2;
        hitCheck(fruit5CenterX, fruit5CenterY);




        enemyY += 30;
        if (enemyY >screenHeight) {
            enemyY =  - 500;
            enemyX = (float)Math.floor(Math.random() * (frameWidth - enemy.getWidth()));
        }
        enemy.setX(enemyX);
        enemy.setY(enemyY);

        fruit1Y += 28;
        if (fruit1Y >screenHeight) {
            fruit1Y =  - 30;
            fruit1X = (float)Math.floor(Math.random() * (frameWidth - fruit1.getWidth()));
        }
        fruit1.setX(fruit1X);
        fruit1.setY(fruit1Y);

        fruit2Y += 16;
        if (fruit2Y >screenHeight) {
            fruit2Y =  - 100;
            fruit2X = (float)Math.floor(Math.random() * (frameWidth - fruit2.getWidth()));
        }
        fruit2.setX(fruit2X);
        fruit2.setY(fruit2Y);

        fruit3Y += 20;
        if (fruit3Y >screenHeight) {
            fruit3Y =  - 250;
            fruit3X = (float)Math.floor(Math.random() * (frameWidth - fruit3.getWidth()));
        }
        fruit3.setX(fruit3X);
        fruit3.setY(fruit3Y);

        fruit4Y += 24;
        if (fruit4Y >screenHeight) {
            fruit4Y =  - 200;
            fruit4X = (float)Math.floor(Math.random() * (frameWidth - fruit4.getWidth()));
        }
        fruit4.setX(fruit4X);
        fruit4.setY(fruit4Y);

        fruit5Y += 26;
        if (fruit5Y >screenHeight) {
            fruit5Y =  - 150;
            fruit5X = (float)Math.floor(Math.random() * (frameWidth - fruit5.getWidth()));
        }
        fruit5.setX(fruit5X);
        fruit5.setY(fruit5Y);

        if (action_right) {
            boxX += 20;

        } else if(action_left) {
            boxX -= 20;
        }
        if (boxX < 0) boxX = 0;

        if (boxX > frameWidth - boxSize) boxX = frameWidth - boxSize;
        box.setX(boxX);

        scoreLabel.setText("Score : " + score);
    }

    public void hitCheck(float x,float y){
        if (hitStatus(x,y)){
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
            intent.putExtra("SCORE", score);
            startActivity(intent);
        }
    }


    public boolean hitStatus(float centerX, float centerY) {
        return (frameHeight - boxSize <= centerY && centerY <= frameHeight &&
                boxX <= centerX && centerX <= boxX + boxSize) ? true : false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg == false) {

            start_flg = true;

            FrameLayout frame = findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            frameWidth = frame.getWidth();
            boxSize = box.getHeight();

            box.setY(frameHeight - boxSize);
            boxX = box.getX();
            enemyY = enemy.getY();
            fruit1Y= fruit1.getY();
            fruit2Y= fruit2.getY();
            fruit3Y= fruit3.getY();
            fruit4Y= fruit4.getY();
            fruit5Y= fruit5.getY();

            startLabel.setVisibility(View.GONE);



            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                            score += 10;
                        }
                    });
                }
            }, 0, 20);

        }
        return true;
    }

    @Override
    public void onBackPressed() { }

    @Override
    protected void onResume() {
        super.onResume();
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Sensor s = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        int sdelay = SensorManager.SENSOR_DELAY_NORMAL;
        if(s!=null){
            manager.registerListener(listener, s,sdelay);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        manager.unregisterListener(listener);
    }
}