package blackcore.tdc.edu.com.gamevhta;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class SplashScreenActivity extends AppCompatActivity {

    Handler handler =  new Handler();
    private ImageView imvBus;
    private Animation trans;
    private MediaPlayer mpBus;
    @Override
    protected void onPause(){
        super.onPause();
        try {
            mpBus.pause();
        }catch (Exception e)
        {
            Log.d("a","s");
        }

    }
    @Override
    protected  void onResume(){
        super.onResume();
        mpBus.start();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mpBus.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        try {
            mpBus.stop();
            mpBus.release();
        }catch (Exception e)
        {
            Log.d("a","s");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mpBus = MediaPlayer.create(getApplicationContext(),R.raw.bussound);
        imvBus = (ImageView) findViewById(R.id.iv1);
        trans = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);

        mpBus.start();
        imvBus.startAnimation(trans);
        goNextActivity();
    }
    public void goNextActivity(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                mpBus.stop();
                mpBus.release();
                finish();
            }
        }, 7000);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
