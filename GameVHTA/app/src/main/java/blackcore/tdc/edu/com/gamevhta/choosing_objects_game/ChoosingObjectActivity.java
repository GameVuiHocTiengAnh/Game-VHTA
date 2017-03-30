package blackcore.tdc.edu.com.gamevhta.choosing_objects_game;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.podcopic.animationlib.library.AnimationType;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.LoadingGoOutGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGameMemoryChallengeActivity;
import blackcore.tdc.edu.com.gamevhta.button.PauseButton;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

import static com.podcopic.animationlib.library.StartSmartAnimation.startAnimation;


/**
 * Created by Canh on 16/03/2017.
 */

public class ChoosingObjectActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private Animation animation, animationsacle;
    private ImageView imgAnimalOne, imgAnimalTwo, imgAnimalThree, imgAnimalFour, imgAnimalFive, imgAnimalSix, imgAnimalDialog, imbNextGameWin;
    private TextView txtScore, txtWordEng, txtWordVie, txtScoreWin;
    private MediaPlayer mClick, mMusicMainGame, mGameWin;
    private MusicService mService = new MusicService();
    private Dialog dialogGame, dialogWin;
    private DbAccessHelper dbAccessHelper;
    private TextToSpeech textToSpeech = null;
    private TextView txtWordOne, txtWordTwo, txtWordThree, txtWordFour, txtWordFive, txtWordSix;
    PauseButton imgBackGame;

    private int SCORE_ONE = 0, SCORE_TWO = 0, SCORE_THREE = 0, SCORE_FOUR = 0, SCORE_FIVE = 0, SCORE_SIX = 0, SCORE_ALL = 0;
    private int check_finish = 0;
    private ArrayList<WordObject> listImageGame = null;
    private ArrayList<WordObject> listImageData = null;


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


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_creen_mh7);

        dbAccessHelper = new DbAccessHelper(this);
        listImageData= new ArrayList<>();
        listImageGame= new ArrayList<>();
        imgAnimalOne = (ImageView) findViewById(R.id.imgAnimalOne);
        imgAnimalTwo = (ImageView) findViewById(R.id.imgAnimalTwo);
        imgAnimalThree = (ImageView) findViewById(R.id.imgAnimalThree);
        imgAnimalFour = (ImageView) findViewById(R.id.imgAnimalFour);
        imgAnimalFive = (ImageView) findViewById(R.id.imgAnimalFive);
        imgAnimalSix = (ImageView) findViewById(R.id.imgAnimalSix);
        txtScore = (TextView) findViewById(R.id.txtScore);

        txtWordOne = (TextView) findViewById(R.id.txtWordOne);
        txtWordTwo = (TextView) findViewById(R.id.txtWordTwo);
        txtWordThree = (TextView) findViewById(R.id.txtWordThree);
        txtWordFour = (TextView) findViewById(R.id.txtWordFour);
        txtWordFive = (TextView) findViewById(R.id.txtWordFive);
        txtWordSix = (TextView) findViewById(R.id.txtWordSix);

        mClick = MediaPlayer.create(ChoosingObjectActivity.this, R.raw.chat_sound);
        mMusicMainGame = MediaPlayer.create(ChoosingObjectActivity.this, R.raw.log_cabin);
        mGameWin = MediaPlayer.create(ChoosingObjectActivity.this, R.raw.wingame);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        animationsacle = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim_trieu);


        //dialog win game
        dialogWin = new Dialog(ChoosingObjectActivity.this);
        dialogWin.setCancelable(false);
        dialogWin.setCanceledOnTouchOutside(false);
        dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWin.setContentView(R.layout.activity_dialog_win_game);
        dialogWin.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtScoreWin = (TextView) dialogWin.findViewById(R.id.txtScoreWin);
        imbNextGameWin = (ImageView) dialogWin.findViewById(R.id.imvNextGame);

        //Text to Speech
        textToSpeech = new TextToSpeech(this,this);
        mService.playMusic(mMusicMainGame);
        mMusicMainGame.setLooping(true);
        mMusicMainGame.setVolume(0.5f,0.5f);
        mClick.setVolume(0.4f,0.4f);

        addDataList();
        createDataGame();
        moveActivity();
        Answer();
        setFont();
        getAnimationImageButton();
    }

    private void Answer(){
        imgAnimalOne.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
        imgAnimalTwo.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
        imgAnimalThree.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
        imgAnimalFour.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
        imgAnimalFive.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
        imgAnimalSix.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
    }
    //Add image from resource to ImageButton
    private Drawable getBitmapResource(String name) {
        int id = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        Log.d("ImageID", String.valueOf(id));
        Drawable dr;
        if (id != 0)
            dr = getApplicationContext().getResources().getDrawable(id);
        else
            dr = getApplicationContext().getResources().getDrawable(R.drawable.screen_5_dv);
        return dr;
    }
    private void addDataList(){
        listImageData = new ArrayList<>();
        listImageData = dbAccessHelper.getWordObject(ConfigApplication.OBJECT_ANIMALS);
    }
    private void createDataGame(){
        listImageGame = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 6; i++){
            int x = listImageData.size();
            int n = random.nextInt(x);
            listImageGame.add(listImageData.get(n));
            listImageData.remove(n);
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        mService.pauseMusic(mMusicMainGame);
        super.onPause();
    }
    public void onResume() {
        mService.playMusic(mMusicMainGame);
        super.onResume();
    }
    private void dialogGame(){
        dialogGame = new Dialog(ChoosingObjectActivity.this);
        dialogGame.setContentView(R.layout.activity_game_mh7_dialog);
        dialogGame.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgAnimalDialog = (ImageView) dialogGame.findViewById(R.id.imgAnimalDialog);
        txtWordEng = (TextView) dialogGame.findViewById(R.id.txtWordEng);
        txtWordVie = (TextView) dialogGame.findViewById(R.id.txtWordVie);
        dialogGame.getWindow().getAttributes().width = (Resources.getSystem().getDisplayMetrics().widthPixels) - 80;
        startAnimation( dialogGame.findViewById(R.id.imgAnimalDialog) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        startAnimation( dialogGame.findViewById(R.id.txtWordEng) , AnimationType.BounceIn , 2000 , 0 , true , 300 );
        startAnimation( dialogGame.findViewById(R.id.txtWordVie) , AnimationType.BounceInLeft , 2000 , 0 , true , 300 );
    }
    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPath));
        txtScore.setTypeface(custom_font);
        txtWordOne.setTypeface(custom_font);
        txtWordTwo.setTypeface(custom_font);
        txtWordThree.setTypeface(custom_font);
        txtWordFour.setTypeface(custom_font);
        txtWordFive.setTypeface(custom_font);
        txtWordSix.setTypeface(custom_font);
        txtScoreWin.setTypeface(custom_font);
    }
    public void moveActivity() {
        imgBackGame = (PauseButton) findViewById(R.id.btnPausegame);
        Intent intent = new Intent(getApplicationContext(),LoadingGoOutGameActivity.class);
        imgBackGame.setMoveActivity(intent,this);
    }
    public void getAnimationImageButton() {
        imgAnimalOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mService.playMusic(mClick);
                        imgAnimalOne.startAnimation(animation);
                        if(textToSpeech != null && listImageGame.get(0).getwEng() != null){
                            textToSpeech.speak(listImageGame.get(0).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
                        txtWordEng.setText(listImageGame.get(0).getwEng());
                        txtWordVie.setText(listImageGame.get(0).getwVie());
                        imgAnimalDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgAnimalDialog.startAnimation(animationsacle);
                                if(textToSpeech != null && listImageGame.get(0).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(0).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mService.playMusic(mClick);
                                dialogGame.dismiss();
                                ktcheck_finish();
                                getResult(1);
                                if(check_finish == 6) {
                                    EventWin();
                                    mService.playMusic(mGameWin);
                                    dialogWin.show();
                                }

                                startAnimation( findViewById(R.id.txtWordOne) , AnimationType.ZoomInDown , 2000 , 0 , true , 300 );
                                startAnimation( findViewById(R.id.imgAnimalOne) , AnimationType.Tada , 2000 , 0 , true , 300 );
                                txtWordOne.setText(listImageGame.get(0).getwEng());
                                if(textToSpeech != null && listImageGame.get(0).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(0).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        final ImageView img_markOne = (ImageView) findViewById(R.id.img_markOne);
                        img_markOne.setVisibility(View.INVISIBLE);
                        imgAnimalOne.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgAnimalOne.clearAnimation();
                        return true;

                }
                return false;
            }
        });

        imgAnimalTwo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mService.playMusic(mClick);
                        imgAnimalTwo.startAnimation(animation);
                        if(textToSpeech != null && listImageGame.get(1).getwEng() != null){
                            textToSpeech.speak(listImageGame.get(1).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
                        txtWordEng.setText(listImageGame.get(1).getwEng());
                        txtWordVie.setText(listImageGame.get(1).getwVie());
                        imgAnimalDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgAnimalDialog.startAnimation(animationsacle);
                                if(textToSpeech != null && listImageGame.get(1).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(1).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mService.playMusic(mClick);
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(2);
                                if(check_finish == 6) {
                                    EventWin();
                                    mService.playMusic(mGameWin);
                                    dialogWin.show();
                                }
                                startAnimation( findViewById(R.id.txtWordTwo) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                                startAnimation( findViewById(R.id.imgAnimalTwo) , AnimationType.Tada , 2000 , 0 , true , 300 );
                                txtWordTwo.setText(listImageGame.get(1).getwEng());
                                if(textToSpeech != null && listImageGame.get(1).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(1).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        final ImageView img_markTwo = (ImageView) findViewById(R.id.img_markTwo);
                        img_markTwo.setVisibility(View.INVISIBLE);
                        imgAnimalTwo.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgAnimalTwo.clearAnimation();
                        return true;
                }
                return false;
            }
        });
        imgAnimalThree.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mService.playMusic(mClick);
                        imgAnimalThree.startAnimation(animation);
                        if(textToSpeech != null && listImageGame.get(2).getwEng() != null){
                            textToSpeech.speak(listImageGame.get(2).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
                        txtWordEng.setText(listImageGame.get(2).getwEng());
                        txtWordVie.setText(listImageGame.get(2).getwVie());
                        imgAnimalDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgAnimalDialog.startAnimation(animationsacle);
                                if(textToSpeech != null && listImageGame.get(2).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(2).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });
                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mService.playMusic(mClick);
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(3);
                                if(check_finish == 6) {
                                    EventWin();
                                    mService.playMusic(mGameWin);
                                    dialogWin.show();
                                }
                                startAnimation( findViewById(R.id.txtWordThree) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                                startAnimation( findViewById(R.id.imgAnimalThree) , AnimationType.Tada , 1000 , 0 , true , 300 );
                                txtWordThree.setText(listImageGame.get(2).getwEng());
                                if(textToSpeech != null && listImageGame.get(2).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(2).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });
                        final ImageView img_markThree = (ImageView) findViewById(R.id.img_markThree);
                        img_markThree.setVisibility(View.INVISIBLE);
                        imgAnimalThree.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgAnimalThree.clearAnimation();
                        return true;
                }
                return false;
            }
        });

        imgAnimalFour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mService.playMusic(mClick);
                        imgAnimalFour.startAnimation(animation);
                        if(textToSpeech != null && listImageGame.get(3).getwEng() != null){
                            textToSpeech.speak(listImageGame.get(3).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
                        txtWordEng.setText(listImageGame.get(3).getwEng());
                        txtWordVie.setText(listImageGame.get(3).getwVie());
                        imgAnimalDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgAnimalDialog.startAnimation(animationsacle);
                                if(textToSpeech != null && listImageGame.get(3).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(3).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mService.playMusic(mClick);
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(4);
                                if(check_finish == 6) {
                                    EventWin();
                                    mService.playMusic(mGameWin);
                                    dialogWin.show();
                                }
                                startAnimation( findViewById(R.id.txtWordFour) , AnimationType.ZoomInRight , 2000 , 0 , true , 300 );
                                startAnimation( findViewById(R.id.imgAnimalFour) , AnimationType.Tada , 1000 , 0 , true , 300 );
                                txtWordFour.setText(listImageGame.get(3).getwEng());
                                if(textToSpeech != null && listImageGame.get(3).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(3).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        final ImageView img_markFour = (ImageView) findViewById(R.id.img_markFour);
                        img_markFour.setVisibility(View.INVISIBLE);
                        imgAnimalFour.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgAnimalFour.clearAnimation();
                        return true;

                }
                return false;
            }
        });
        imgAnimalFive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mService.playMusic(mClick);
                        imgAnimalFive.startAnimation(animation);
                        if(textToSpeech != null && listImageGame.get(4).getwEng() != null){
                            textToSpeech.speak(listImageGame.get(4).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                        dialogGame();

                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
                        txtWordEng.setText(listImageGame.get(4).getwEng());
                        txtWordVie.setText(listImageGame.get(4).getwVie());
                        imgAnimalDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgAnimalDialog.startAnimation(animationsacle);
                                if(textToSpeech != null && listImageGame.get(4).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(4).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });
                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mService.playMusic(mClick);
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(5);
                                if(check_finish == 6) {
                                    EventWin();
                                    mService.playMusic(mGameWin);
                                    dialogWin.show();
                                }
                                startAnimation( findViewById(R.id.txtWordFive) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                                startAnimation( findViewById(R.id.imgAnimalFive) , AnimationType.BounceIn , 1000 , 0 , true , 300 );
                                txtWordFive.setText(listImageGame.get(4).getwEng());
                                if(textToSpeech != null && listImageGame.get(4).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(4).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        final ImageView img_markFive = (ImageView) findViewById(R.id.img_markFive);
                        img_markFive.setVisibility(View.INVISIBLE);
                        imgAnimalFive.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        imgAnimalFive.clearAnimation();
                        return true;
                }
                return false;
            }
        });

        imgAnimalSix.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mService.playMusic(mClick);
                        imgAnimalSix.startAnimation(animation);
                        if(textToSpeech != null && listImageGame.get(5).getwEng() != null){
                            textToSpeech.speak(listImageGame.get(5).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                        }
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
                        txtWordEng.setText(listImageGame.get(5).getwEng());
                        txtWordVie.setText(listImageGame.get(5).getwVie());
                        imgAnimalDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imgAnimalDialog.startAnimation(animationsacle);

                                if(textToSpeech != null && listImageGame.get(5).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(5).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                mService.playMusic(mClick);
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(6);
                                if(check_finish == 6) {
                                    EventWin();
                                    mService.playMusic(mGameWin);
                                    dialogWin.show();
                                }
                                startAnimation( findViewById(R.id.txtWordSix) , AnimationType.ZoomInLeft , 2000 , 0 , true , 300 );
                                startAnimation( findViewById(R.id.imgAnimalSix) , AnimationType.Tada , 1000 , 0 , true , 300 );
                                txtWordSix.setText(listImageGame.get(5).getwEng());
                                if(textToSpeech != null && listImageGame.get(5).getwEng() != null){
                                    textToSpeech.speak(listImageGame.get(5).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            }
                        });

                        final ImageView img_markSix = (ImageView) findViewById(R.id.img_markSix);
                        img_markSix.setVisibility(View.INVISIBLE);
                        imgAnimalSix.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        imgAnimalSix.clearAnimation();
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void EventWin()
    {
        txtScoreWin.setText(String.valueOf(SCORE_ALL));
        imbNextGameWin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mService.playMusic(mClick);
                        imbNextGameWin.setSelected(!imbNextGameWin.isSelected());
                        imbNextGameWin.isSelected();
                        return true;
                    case MotionEvent.ACTION_UP:
                        mService.playMusic(mClick);
                        imbNextGameWin.setSelected(false);
                        Intent intent = new Intent(getApplicationContext(),RandomGameMemoryChallengeActivity.class);
                        Bundle sendScore = new Bundle();
                        sendScore.putInt("score",SCORE_ALL);
                        intent.putExtra("pictutepuzzle",sendScore);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private void ktcheck_finish() {
        if (check_finish == 6){
            check_finish = 0;
        }
        else{
            check_finish++;
        }
    }

    //Check Result
    private void getResult(int choose) {
        if (choose == 1) {
            SCORE_ONE += 100;
        }else if(choose == 2) {
            SCORE_TWO += 100;
        }else if(choose == 3) {
            SCORE_THREE += 100;
        }else if(choose == 4) {
            SCORE_FOUR += 100;
        }else if(choose == 5) {
            SCORE_FIVE += 100;
        }else if(choose == 6) {
            SCORE_SIX += 100;
        }
        SCORE_ALL = SCORE_ONE + SCORE_TWO + SCORE_THREE + SCORE_FOUR + SCORE_FIVE + SCORE_SIX;
        txtScore.setText(String.valueOf(SCORE_ALL));
    }
}
