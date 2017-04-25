package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreenActivity extends AppCompatActivity {

    Handler handler =  new Handler();
    private ImageView imvBus;
    private Animation trans;
    private MediaPlayer mpBus = null;
    @Override
    protected void onPause(){

        if (mpBus != null) {
            if(mpBus.isPlaying())
                 mpBus.pause();
            Log.d("Tagtest","pausesound");
        }
        super.onPause();
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
        if (mpBus != null) {
            if (mpBus.isPlaying()) {
                mpBus.stop();
                mpBus.release();
            } else
                mpBus.release();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imvBus = (ImageView) findViewById(R.id.iv1);
        trans = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);
        mpBus = MediaPlayer.create(getApplicationContext(),R.raw.bussound);
        mpBus.start();
        Log.d("Tagtest","starsound");
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
                mpBus = null;
                finish();
            }
        }, 7000);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
