package blackcore.tdc.edu.com.gamevhta;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import blackcore.tdc.edu.com.gamevhta.service.MusicService;

//import blackcore.tdc.edu.com.gamevhta.data_models.DbScoreHelper;

public class MainMenuActivity extends AppCompatActivity {

    private boolean flagMain = true;
    private MediaPlayer mMain,mClick;
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
    private ImageView btnExit,btnPlay,btnGuide,btnScore,imgNameExit;
    private Animation scaleBtnPlay,scaleBtnScore,scaleBtnGuide,scaleBtnExit,zoomIn,rotate_crazy;
    private final int TIME_DELAY_SCALE_BTN = 500;
    private Dialog dialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        scaleBtnPlay = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        scaleBtnScore = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        scaleBtnGuide = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        scaleBtnExit = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        rotate_crazy = AnimationUtils.loadAnimation(MainMenuActivity.this,R.anim.scale_anim_trieu);

        mMain = MediaPlayer.create(getApplicationContext(),R.raw.blizzards);
        mClick = MediaPlayer.create(getApplicationContext(),R.raw.click);

        flagMain = true;
        playMusicMain();

        btnExit = (ImageView) findViewById(R.id.imageExit);
        btnPlay = (ImageView) findViewById(R.id.imagePlay);
        btnGuide = (ImageView) findViewById(R.id.imageGuide);
        btnScore = (ImageView) findViewById(R.id.imageScore);

        boolean delayBtn = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnPlay.startAnimation(scaleBtnPlay);
                btnScore.startAnimation(scaleBtnScore);
                btnGuide.startAnimation(scaleBtnGuide);
                btnExit.startAnimation(scaleBtnExit);
            }
        },TIME_DELAY_SCALE_BTN);

        btnPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnPlay.startAnimation(zoomIn);
                        mService.playMusic(mClick);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnPlay.clearAnimation();
                        mService.playMusic(mClick);
                        flagMain = true;
                        playMusicMain();
                        Intent intent = new Intent(getApplicationContext(),TopisChoosingActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });

        btnScore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnScore.startAnimation(zoomIn);
                        mService.playMusic(mClick);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnScore.clearAnimation();
                        mService.playMusic(mClick);
                        flagMain = true;
                        playMusicMain();
                        Intent intent = new Intent(MainMenuActivity.this,ScoresActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });

        btnGuide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnGuide.startAnimation(zoomIn);
                        mService.playMusic(mClick);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnGuide.clearAnimation();
                        mService.playMusic(mClick);
                        flagMain = true;
                        playMusicMain();
                        Intent intent = new Intent(getApplicationContext(),GuideActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });

        btnExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnExit.startAnimation(zoomIn);
                        mService.playMusic(mClick);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnExit.clearAnimation();
                        mService.playMusic(mClick);
                        dialogExit();

                }
                return false;
            }
        });
    }
    public void dialogExit(){
        dialog = new Dialog(MainMenuActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_exit);
        dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        final ImageView btnYes = (ImageView) dialog.findViewById(R.id.btnYes);
        final ImageView btnNo = (ImageView) dialog.findViewById(R.id.btnNo);
        StartSmartAnimation.startAnimation( dialog.findViewById(R.id.btnYes) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( dialog.findViewById(R.id.btnNo) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        imgNameExit = (ImageView) dialog.findViewById(R.id.imgNameExit);
        imgNameExit.startAnimation(rotate_crazy);

        btnYes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnYes.setSelected(!btnYes.isSelected());
                        btnYes.isSelected();
                        btnNo.setEnabled(false);
                        mService.playMusic(mClick);
                        return true;
                    case MotionEvent.ACTION_UP:
                        playMusicMain();
                        btnNo.setEnabled(true);
                        finish();
                        return true;
                }
                return false;
            }
        });

        btnNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnNo.setSelected(!btnNo.isSelected());
                        btnNo.isSelected();
                        btnYes.setEnabled(false);
                        mService.playMusic(mClick);
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnYes.setEnabled(true);
                        mService.playMusic(mClick);
                        dialog.dismiss();
                        return true;
                }
                return false;
            }
        });
    }

    protected void onPause(){
        mService.pauseMusic(mMain);
        super.onPause();

    }
    protected  void onResume(){
        mService.playMusic(mMain);
        super.onResume();
    }

    private void playMusicMain() {
        if(flagMain == true){
            mService.playMusic(mMain);
            mMain.setLooping(true);
        }else if(flagMain == false){
            mService.playMusic(mClick);
            mService.stopMusic(mMain);
        }
    }

    public void onBackPressed(){
        dialogExit();
    }
    public void onSuperBackPressed(){
        super.onBackPressed();
    }
}
