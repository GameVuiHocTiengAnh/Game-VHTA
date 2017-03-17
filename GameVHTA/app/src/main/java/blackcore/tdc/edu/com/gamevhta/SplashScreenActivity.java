package blackcore.tdc.edu.com.gamevhta;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class SplashScreenActivity extends AppCompatActivity {

    private ImageView imvBus;
    private Animation trans;
    private MediaPlayer mpBus;
    private MusicService mService = new MusicService();
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder mBinder = (MusicService.MyBinder) iBinder;
            mService = mBinder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;

        }
    };

    protected void onPause(){
        mService.pauseMusic(mpBus);
        super.onPause();

    }
    protected  void onResume(){
        mService.playMusic(mpBus);
        super.onResume();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mpBus = MediaPlayer.create(getApplicationContext(),R.raw.bussound);
        imvBus = (ImageView) findViewById(R.id.iv1);
        trans = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate);

        mService.playMusic(mpBus);
        imvBus.startAnimation(trans);
        goNextActivity();
    }
    public void goNextActivity(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                mService.stopMusic(mpBus);
                finish();
            }
        }, 8000);
    }
}
