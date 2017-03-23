package blackcore.tdc.edu.com.gamevhta.catching_words_game;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.TopisChoosingActivity;
import blackcore.tdc.edu.com.gamevhta.button.PauseButton;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.BackgroudGameView;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.CharacterGameView;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;
import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class CatchingWordsActivity extends AppCompatActivity {

    private BackgroudGameView backgroudGameView;
    private CharacterGameView ninja;
    private FrameLayout frameGame;
    private Typeface gothic;    //fontface
    private ImageView imvVocalubary;
    private TextView txtVocalubary;
    private TextView tvScore;
    private ProgressBar helth;
    private MusicService themeMusic = new MusicService();
    private MediaPlayer songThemeMusic;
    private PauseButton btnPause;
    private MediaPlayer soundThrowShuriken;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder mBinder = (MusicService.MyBinder) iBinder;
            themeMusic = mBinder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            themeMusic = null;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //WindowManager.LayoutParams attribute = getWindow().getAttributes();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        frameGame = new FrameLayout(this);
        FrameLayout.LayoutParams lpframe = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        frameGame.setLayoutParams(lpframe);
        setContentView(frameGame);

        helth = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        // add charracter for game
        ninja = new CharacterGameView(this);
        ninja.setZOrderOnTop(true);
        ninja.getHolder().setFormat(PixelFormat.TRANSPARENT);

        //add backgroud for frame game
        backgroudGameView = new BackgroudGameView(this,0,0,ninja);
        backgroudGameView.startMoveBG(this);

        frameGame.addView(backgroudGameView);
        frameGame.addView(ninja);
        this.addTopBarView(frameGame);
        this.addProgressbarHelth(frameGame);
        this.playThemeMusic();
        //soundThrowShuriken = MediaPlayer.create(this,R.raw.shuriken_throw);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btnPause.callOnClick();
        themeMusic.playMusic(songThemeMusic);
    }

    @Override
    protected void onStop() {
        backgroudGameView.stopMoveBG();
        themeMusic.pauseMusic(songThemeMusic);
        super.onStop();
    }

    private void addTopBarView(FrameLayout rootLayout){

        LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams lpLinear = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lpLinear.setMargins(0,10,0,0);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(lpLinear);

        gothic = Typeface.createFromAsset(getAssets(),"fonts/gothic.ttf");

        LinearLayout.LayoutParams lpHorizaltal = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT);
        lpHorizaltal.weight = 1;
        lpHorizaltal.gravity = Gravity.CENTER;
        LinearLayout.LayoutParams lpWrap = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        int marginLeftRight = ConfigCWGame.getMarginVocalubary();
        lpHorizaltal.setMargins(marginLeftRight,10,marginLeftRight,10);
        lpWrap.setMargins(10,10,10,10);
        LinearLayout lnBack = new LinearLayout(this);
        LinearLayout lnVocalubary = new LinearLayout(this);
        LinearLayout lnScore = new LinearLayout(this);

        lnBack.setLayoutParams(lpWrap);
        lnScore.setLayoutParams(lpHorizaltal);

        LinearLayout.LayoutParams lpSubElement = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        lpSubElement.gravity = Gravity.CENTER;
        // add btn back
        btnPause = new PauseButton(this,null);
        Intent intent = new Intent(this,TopisChoosingActivity.class);
        btnPause.setMoveActivity(intent,this);
        btnPause.setScreenUse(ConfigApplication.SCREEN_USING_PAUSE_DIALOG_MH6,backgroudGameView);
        btnPause.setLayoutParams(lpSubElement);
        lnBack.addView(btnPause);

        // show vocalubary
        lnVocalubary.setLayoutParams(lpHorizaltal);
        lnVocalubary.setGravity(Gravity.CENTER);
        imvVocalubary = new ImageView(this);
        imvVocalubary.setLayoutParams(lpWrap);
        txtVocalubary = new TextView(this);
        txtVocalubary.setTypeface(gothic);
        txtVocalubary.setTextColor(Color.RED);
        txtVocalubary.setLayoutParams(lpWrap);
        txtVocalubary.setText("");
        txtVocalubary.setTextSize(ConfigCWGame.getSizeTextScores());
        lnVocalubary.addView(imvVocalubary);
        lnVocalubary.addView(txtVocalubary);

        // show score

        tvScore = new TextView(this);
        lnScore.setLayoutParams(lpWrap);
        lnScore.setGravity(Gravity.CENTER_HORIZONTAL);
        lnScore.setBackgroundResource(R.drawable.backgroud_word);
        LinearLayout.LayoutParams lpWrapScores = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lpWrapScores.setMargins(0, ConfigCWGame.getMarginScoresView(),0,0);
        tvScore.setLayoutParams(lpWrapScores);
        tvScore.setText("1000");
        tvScore.setTextColor(Color.RED);
        tvScore.setTextSize(ConfigCWGame.getSizeTextScores());
        tvScore.setTypeface(gothic);
        lnScore.addView(tvScore);

        linearLayout.addView(lnBack);
        linearLayout.addView(lnVocalubary);
        linearLayout.addView(lnScore);

        rootLayout.addView(linearLayout);
    }

    private void addProgressbarHelth(FrameLayout root){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(300, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(SizeOfDevice.getScreenWidth() - 1100,SizeOfDevice.getScreenHeight() - 350,0,0);
        helth.setLayoutParams(lp);
        helth.setMax(100);
        helth.setProgress(100);
        helth.setVisibility(View.INVISIBLE);
        root.addView(helth);
    }

    public void updateProgressHeth(final int progress){
        boolean handler = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                helth.setProgress(progress);
            }
        },1300);

//        Log.d("Tagtest",helth.getProgress()+"");
    }

    public void showHelthbar(){
        helth.setVisibility(View.VISIBLE);
    }

    public void hideHelthbar(){
        helth.setVisibility(View.INVISIBLE);
    }

    public void onResumeGame(){
        if(!backgroudGameView.getPausegame()) {
            backgroudGameView.startMoveBG(this);
        }else {
            ninja.setRunning(false);
            ninja.onResumeMySurfaceView();
        }
    }

    private void playThemeMusic(){
        songThemeMusic = MediaPlayer.create(getApplicationContext(),R.raw.blizzards);
        songThemeMusic.setVolume(0.25f,0.25f);
        songThemeMusic.setLooping(true);
        themeMusic.playMusic(this.songThemeMusic);
    }

    public void throwShuriken(){
        Animation throwShurikenAnim = AnimationUtils.loadAnimation(this,R.anim.throw_shuriken);
        final ImageView shuriken = new ImageView(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(50, 50, Gravity.BOTTOM);
        lp.setMargins(440,0,0,175);
        shuriken.setBackgroundResource(R.drawable.mh6_shuriken);
        shuriken.setLayoutParams(lp);
        shuriken.bringToFront();
        shuriken.startAnimation(throwShurikenAnim);
        soundThrowShuriken.setVolume(0.9f,0.9f);
        soundThrowShuriken.seekTo(0);
        soundThrowShuriken.start();
        frameGame.addView(shuriken);

        throwShurikenAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                shuriken.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void setImvVocalubary(Bitmap images , String text){
        this.imvVocalubary.setImageBitmap(images);
        this.txtVocalubary.setText(text);
    }

}
