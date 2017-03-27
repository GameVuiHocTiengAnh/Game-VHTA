package blackcore.tdc.edu.com.gamevhta.catching_words_game;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.LoadingGoInGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGameMemoryChallengeActivity;
import blackcore.tdc.edu.com.gamevhta.TopisChoosingActivity;
import blackcore.tdc.edu.com.gamevhta.button.PauseButton;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.BackgroudGameView;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.CharacterGameView;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.ShurikenView;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ConfigCWGame;
import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class CatchingWordsActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

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
    private ShurikenView throwShuriken;

    private DbAccessHelper db;
    private ArrayList<WordObject> listWords = null;
    private ArrayList<WordObject> listWordsUsing = null;

    private int countComplete;
    private int scores;
    private int characterHeight;

    private Bitmap imgUsing = null;
    private String wordUsing = null;

    private Dialog dialogCatchingWords = null;
    private ImageView imvDialogCatching;
    private TextView txtDialogCatching;

    private MediaPlayer catchingSound;
    private TextToSpeech textToSpeech = null;
    private MediaPlayer dialogWinSound = null;
    private MediaPlayer mplClicked = null;

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

        textToSpeech = new TextToSpeech(this,this);
        countComplete = 0;
        scores = 0;
        //get objects from db
        try {
            db = new DbAccessHelper(this);
            listWordsUsing = new ArrayList<WordObject>();
              if(db != null)
                listWords = db.getWordObject(ConfigApplication.OBJECT_ANIMALS);
                initObjectUSing();
            Log.d("Tagtest",listWords.get(0).getwPathImage());
        }catch (NullPointerException e){
            Log.d("Tagtest",e.toString());
        }

        frameGame = new FrameLayout(this);
        FrameLayout.LayoutParams lpframe = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        frameGame.setLayoutParams(lpframe);
        setContentView(frameGame);


        helth = new ProgressBar(this,null,android.R.attr.progressBarStyleHorizontal);
        // add charracter for game
        ninja = new CharacterGameView(this);
        ninja.getHolder().setFormat(PixelFormat.TRANSPARENT);
        ninja.setZOrderOnTop(true);
        characterHeight = ninja.getCharacHeight();

        //add backgroud for frame game
        backgroudGameView = new BackgroudGameView(this,0,0,ninja);
        backgroudGameView.startMoveBG(this);

        //add view throw shuriken
        throwShuriken = new ShurikenView(this);
        throwShuriken.setRunning(true);
        throwShuriken.getHolder().setFormat(PixelFormat.TRANSPARENT);
        throwShuriken.setZOrderOnTop(true);

        frameGame.addView(backgroudGameView);
        frameGame.addView(ninja);
        frameGame.addView(throwShuriken);

        //add TopActionBar
        this.addTopBarView(frameGame);
        this.addProgressbarHelth(frameGame);

        this.playThemeMusic();
        catchingSound = MediaPlayer.create(getApplicationContext(),R.raw.catching_words);
        imvVocalubary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textToSpeech != null && wordUsing != null){
                    startAnimClickedOnImvVoca();
                    textToSpeech.speak(wordUsing,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
        this.initDialogCatchingWords();
        this.dialogWinSound = MediaPlayer.create(getApplicationContext(),R.raw.wingame);
        this.mplClicked = MediaPlayer.create(getApplicationContext(),R.raw.click);
//        Log.d("Tagtest",SizeOfDevice.getScreenWidth() +"=============="+ SizeOfDevice.getScreenHeight());


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        btnPause.callOnClick();
        themeMusic.playMusic(songThemeMusic);
    }

    @Override
    public void onBackPressed() {
        btnPause.callOnClick();
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
        tvScore.setText("0");
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
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ConfigCWGame.withProgressHelth(), FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(SizeOfDevice.getScreenWidth()/2,ConfigCWGame.marginBotOfHelth(),0,0);
        helth.setLayoutParams(lp);
        helth.setMax(100);
        helth.setProgress(100);
        helth.setVisibility(View.INVISIBLE);
        root.addView(helth);
    }

    public void updateProgressHeth(final int progress ){
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
        if(!backgroudGameView.isPauseGame()) {
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
        final MediaPlayer  soundThrowShuriken = MediaPlayer.create(getApplicationContext(),R.raw.shuriken_throw);
        soundThrowShuriken.setVolume(0.9f,0.9f);
        throwShuriken.setComplete(false);
        throwShuriken.onResumeMySurfaceView();
        soundThrowShuriken.start();
        soundThrowShuriken.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                soundThrowShuriken.release();
            }
        });
    }
    public void setImvVocalubary(){
        scores += 100;
        tvScore.setText(scores+"");
        String imvName = listWordsUsing.get(countComplete).getwPathImage();
        String imvText = listWordsUsing.get(countComplete).getwEng();
        int idImgeDr = getResources().getIdentifier(imvName, "drawable", getApplicationContext().getPackageName());
        Bitmap imgVoca = BitmapFactory.decodeResource(getResources(),idImgeDr);
        if(imgVoca != null){
            this.imgUsing = imgVoca;
            this.wordUsing = imvText;
            showDialogCatchingWords(imgVoca,imvText);
        }
        countComplete++;
        if(countComplete == listWordsUsing.size()){
            boolean handlerDayley = new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    levelComplete();
                    countComplete = 0;
                }
            },2000);

        }
    }

    public int leghtWordOfOjectUsing(){
        return listWordsUsing.get(countComplete).getwEng().length();
    };

    private void levelComplete() {
       Dialog dialogWin = new Dialog(this);
        dialogWin.setCancelable(false);
        dialogWin.setCanceledOnTouchOutside(false);
        dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWin.setContentView(R.layout.activity_dialog_win_game);
        dialogWin.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        final ImageView imbNextGameWin = (ImageView) dialogWin.findViewById(R.id.imvNextGame);
        TextView txtNameScoreWin = (TextView) dialogWin.findViewById(R.id.txtNameScoreWin);
        txtNameScoreWin.setTypeface(gothic);
        TextView txtScoreWin = (TextView) dialogWin.findViewById(R.id.txtScoreWin);
        txtScoreWin.setText(this.scores+"");
        txtScoreWin.setTypeface(gothic);
        dialogWinSound.start();
        dialogWin.show();
        backgroudGameView.stopMoveBG();

        imbNextGameWin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mplClicked.start();
                        imbNextGameWin.setSelected(!imbNextGameWin.isSelected());
                        imbNextGameWin.isSelected();
                        return true;
                    case MotionEvent.ACTION_UP:
                        imbNextGameWin.setSelected(false);
                        Intent intent = new Intent(CatchingWordsActivity.this,RandomGameMemoryChallengeActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable(ConfigApplication.NAME_DATA_LIST,listWordsUsing);
                        intent.putExtras(data);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    public void initObjectUSing() {
        Random ran = new Random();

        for (int i = 0; i < 6; i++) {
            int listSize = listWords.size();
            int dom = ran.nextInt(listSize);
            listWordsUsing.add(listWords.get(dom));
            listWords.remove(dom);
        }
    }

    public Bitmap getBmUsing(){
        return this.imgUsing;
    }
    public String getWordUsing(){
        return this.wordUsing;
    }
    public int getCharacHeight(){
        return characterHeight;
    }

    private void initDialogCatchingWords(){
        dialogCatchingWords = new Dialog(this);
        dialogCatchingWords.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogCatchingWords.setCanceledOnTouchOutside(false);
        dialogCatchingWords.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogCatchingWords.setContentView(R.layout.dialog_catching_words_layout);
        dialogCatchingWords.getWindow().getAttributes().windowAnimations = R.style.CatchingWordsDialogAnimation;
        imvDialogCatching = (ImageView)dialogCatchingWords.findViewById(R.id.mh6_imge_voca);
        txtDialogCatching = (TextView)dialogCatchingWords.findViewById(R.id.mh6_txt_voce);
        txtDialogCatching.setTextColor(Color.RED);
        txtDialogCatching.setTypeface(gothic);
    }

    private void showDialogCatchingWords(final Bitmap image, final String text){
        imvDialogCatching.setImageBitmap(image);
        txtDialogCatching.setText(text);
        dialogCatchingWords.show();
        this.starAnimationImageDialogCatching();
        catchingSound.seekTo(0);
        catchingSound.start();
        if(textToSpeech != null)
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        boolean hanlerDaylay = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogCatchingWords.dismiss();
                imvVocalubary.setImageBitmap(image);
                txtVocalubary.setText(text);
                starAnimGroupVoca();
            }
        },2000);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void starAnimationImageDialogCatching(){
        StartSmartAnimation.startAnimation(imvDialogCatching, AnimationType.StandUp,2000,0,true,600);
        StartSmartAnimation.startAnimation(txtDialogCatching, AnimationType.Shake,2000,0,true,600);
    }
    private void starAnimGroupVoca(){
        StartSmartAnimation.startAnimation(imvVocalubary,AnimationType.StandUp,2000,0,true,2000);
        StartSmartAnimation.startAnimation(txtVocalubary,AnimationType.ShakeBand,2000,0,true,2000);
    }

    private void startAnimClickedOnImvVoca(){
        StartSmartAnimation.startAnimation(imvVocalubary,AnimationType.Swing,2000,0,true,300);
    }
}
