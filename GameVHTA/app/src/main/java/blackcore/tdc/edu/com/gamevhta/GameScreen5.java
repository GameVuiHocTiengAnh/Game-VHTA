package blackcore.tdc.edu.com.gamevhta;

import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hoang on 3/16/2017.
 */

public class GameScreen5 extends AppCompatActivity {
    private Handler handler;
    private Animation animation;
    private Timer timer = new Timer();
    private ImageButton btnImage1, btnImage2, btnImage3, btnImage4, btnImage5, btnImage6;
    private TextView lblTimer, lblWord, lblScore;
    private MediaPlayer mp, mpSoundBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_screen_5);
        initialize();
        setFont();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle reMessage = msg.getData();
                String time = reMessage.getString("time");

                lblTimer.setText(time);
                int t = Integer.parseInt(time);
                if(t>0 && t<=10) {
                    lblTimer.startAnimation(animation);
                }
                else if (t == 0) {
                    timer.cancel();
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                }
            }
        };
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int t = Integer.parseInt(lblTimer.getText().toString());
                if (t > 0) {
                    t--;
                    Message message = new Message();
                    Bundle sendMsg = new Bundle();
                    sendMsg.putString("time", String.valueOf(t));
                    message.setData(sendMsg);
                    handler.sendMessage(message);
                }
            }
        }, 1000, 1000);

        getAnimationImageButton();
        mp = MediaPlayer.create(getApplicationContext(), R.raw.game_5_sound_clicked);
        mpSoundBackground= MediaPlayer.create(getApplicationContext(), R.raw.game_9_screen_background_sound);
        mpSoundBackground.setLooping(true);
        mpSoundBackground.start();
    }

    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPath));
        lblTimer.setTypeface(custom_font);
        lblWord.setTypeface(custom_font);
        lblScore.setTypeface(custom_font);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mpSoundBackground.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mpSoundBackground.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpSoundBackground.stop();
    }

    private void initialize() {
        btnImage1 = (ImageButton) findViewById(R.id.btnImage1);
        btnImage2 = (ImageButton) findViewById(R.id.btnImage2);
        btnImage3 = (ImageButton) findViewById(R.id.btnImage3);
        btnImage4 = (ImageButton) findViewById(R.id.btnImage4);
        btnImage5 = (ImageButton) findViewById(R.id.btnImage5);
        btnImage6 = (ImageButton) findViewById(R.id.btnImage6);
        lblTimer = (TextView) findViewById(R.id.lblTimer);
        lblWord = (TextView) findViewById(R.id.lblWord);
        lblScore = (TextView) findViewById(R.id.lblScore);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
    }

    private void getAnimationImageButton() {
        btnImage6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnImage6.startAnimation(animation);
                        mp.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        btnImage6.clearAnimation();
                        return true; // if you want to handle the touch event
                }
                return false;

            }
        });
        btnImage5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnImage5.startAnimation(animation);
                        mp.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        btnImage5.clearAnimation();
                        return true; // if you want to handle the touch event
                }
                return false;

            }
        });
        btnImage4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnImage4.startAnimation(animation);
                        mp.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        btnImage4.clearAnimation();
                        return true; // if you want to handle the touch event
                }
                return false;

            }
        });
        btnImage3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnImage3.startAnimation(animation);
                        mp.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        btnImage3.clearAnimation();
                        return true; // if you want to handle the touch event
                }
                return false;

            }
        });
        btnImage2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnImage2.startAnimation(animation);
                        mp.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        btnImage2.clearAnimation();
                        return true; // if you want to handle the touch event
                }
                return false;

            }
        });

        btnImage1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnImage1.startAnimation(animation);
                        mp.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        btnImage1.clearAnimation();
                        return true; // if you want to handle the touch event
                }
                return false;

            }
        });
    }
}
