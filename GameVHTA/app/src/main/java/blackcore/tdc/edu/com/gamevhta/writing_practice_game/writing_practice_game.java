package blackcore.tdc.edu.com.gamevhta.writing_practice_game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.TopisChoosingActivity;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.custom_toask.CustomToask;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;

import static blackcore.tdc.edu.com.gamevhta.R.id.imgvWPObject1;
import static blackcore.tdc.edu.com.gamevhta.R.raw.click;
import static blackcore.tdc.edu.com.gamevhta.R.raw.dung_market_game;

public class writing_practice_game extends AppCompatActivity implements TextToSpeech.OnInitListener{
    private Handler handler;
    private Animation animation;
    private Animation animationRotate;
    private TextView lblPlayerNameGameOver,txtvLevel, txtvScore, lblScoreGameOver, txtScoreWin, txtWordEngNextTurn, txtWordVieNextTurn, txtNameScoreWin, txtScoreWinEndGame, txtPlayerNameWinEndGame, txtvHelp;
    private ImageView imgListOver, imgReplayOver, imgvObject1, imgvObject2, imgvObject3, imgvObject4, imgvObject5, imgvObject6, imgResume, imgList, imgReplay, imbNextGameWin, imgObjectNextTurn, imgSpeak, imgListWinEndGame, imgReplayWinEndGame;
    private MediaPlayer mButtonClick, mCorrect, mWrong, mClick, mpSoundBackground, mReadyGo, mNextLevel, mGameOver, mYaah, mtornado, mLoadImage;
    private EditText  txtWord;
    LinearLayout layoutWritingPracticeGame;
    private ImageButton btnPause, btnCheck;

    private String OBJECT = "ANI", PLAYER_NAME = "TRIEU";
    private int SCORE = 0, SCORE_TEMP = 0;
    private int RESULT_FAILED = 0;
    private int LEVEL = 1;
    private int LEVEL_LIMIT = 0;
    private int COUNT_IMAGES = 6;
    private int CHOOSE = -1;
    private long mLastClickTime = 0;

    private ArrayList<WordObject> listImageWithLevel1, listImageWithLevel2, listImageLevelO;

    private Dialog dialogBack, dialogGameOver, dialogComplete, dialogNextTurn, dialogWinEndGame;
    private DbAccessHelper dbAccessHelper;
    private boolean flagVoice = true;

    private TextToSpeech textToSpeech = null;

    //test
    private ArrayList<Bitmap> listBitMapAnswer = null;
    CustomToask customToask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get du lieu playername
        String newName = ConfigApplication.NEW_NAME;
        String oldName = ConfigApplication.OLD_NAME;
        if(newName=="" || newName == null){
            oldName = ConfigApplication.OLD_NAME;
            if(!(oldName=="")|| oldName != null){
                newName = oldName;
            }
        }
        PLAYER_NAME = newName;
        if(ConfigApplication.CURRENT_CHOOSE_TOPIC != "") {
            OBJECT = ConfigApplication.CURRENT_CHOOSE_TOPIC;
        }
        //get du lieu score
        Intent i = getIntent();
        if(i != null){
            Bundle b = i.getExtras();
            if(b != null){
                SCORE = b.getInt(ConfigApplication.SCORES_BEFOR_GAME);
                SCORE_TEMP = SCORE;
            }else{
                SCORE = 0;
                SCORE_TEMP = 0;
            }
        }
        //get du lieu topic
        if(OBJECT.equals(ConfigApplication.OBJECT_SCHOOL))
        {
            setContentView(R.layout.activity_writing_practice_game_school);
        }
        else if(OBJECT.equals(ConfigApplication.OBJECT_PLANTS))
        {
            setContentView(R.layout.activity_writing_practice_game_plants);
        }
        else if(OBJECT.equals(ConfigApplication.OBJECT_COUNTRY_SIDE))
        {
            setContentView(R.layout.activity_writing_practice_game_contryside);
        }
        else if(OBJECT.equals(ConfigApplication.OBJECT_HOUSEHOLD_APPLIANCES))
        {
            setContentView(R.layout.activity_writing_practice_game_household_appliances);
        }
        else {
            setContentView(R.layout.activity_writing_practice_game);
        }

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Voice(mReadyGo);
        Voice(mpSoundBackground);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLoadImage.pause();
        mReadyGo.pause();
        mpSoundBackground.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoadImage.pause();
        mReadyGo.pause();
        mpSoundBackground.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLoadImage.stop();
        mReadyGo.stop();
        mpSoundBackground.stop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Voice(mpSoundBackground);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Voice(mpSoundBackground);
    }



    private void initialize() {

        imgvObject1 = (ImageView) findViewById(imgvWPObject1);
        imgvObject2 = (ImageView) findViewById(R.id.imgvWPObject2);
        imgvObject3 = (ImageView) findViewById(R.id.imgvWPObject3);
        imgvObject4 = (ImageView) findViewById(R.id.imgvWPObject4);
        imgvObject5 = (ImageView) findViewById(R.id.imgvWPObject5);
        imgvObject6 = (ImageView) findViewById(R.id.imgvWPObject6);
        txtWord = (EditText) findViewById(R.id.txtWord);
        txtvLevel = (TextView) findViewById(R.id.txtvWPLevel);
        txtvScore = (TextView) findViewById(R.id.txtvWPScore);
        txtvHelp = (TextView) findViewById(R.id.txtvHelp);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim_trieu);
        animationRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        layoutWritingPracticeGame = (LinearLayout) findViewById(R.id.layoutWritingPracticeGame);
        btnPause = (ImageButton) findViewById(R.id.btnWPPause);
        btnCheck = (ImageButton) findViewById(R.id.btnCheck);
        txtvScore.setText(String.valueOf(SCORE));

//      Dialog game over
        dialogGameOver = new Dialog(writing_practice_game.this);
        dialogGameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGameOver.setCancelable(false);
        dialogGameOver.setContentView(R.layout.activity_dialog_game_over);
        dialogGameOver.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogGameOver.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgListOver = (ImageView) dialogGameOver.findViewById(R.id.imgListOver);
        imgReplayOver = (ImageView) dialogGameOver.findViewById(R.id.imgReplayOver);
        lblScoreGameOver = (TextView) dialogGameOver.findViewById(R.id.lblScoreGameOver);
        lblPlayerNameGameOver = (TextView) dialogGameOver.findViewById(R.id.lblPlayerNameGOver);
        lblPlayerNameGameOver.setText(PLAYER_NAME);

//      dialog Pause game
        dialogBack = new Dialog(writing_practice_game.this);
        dialogBack.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBack.setCancelable(false);
        dialogBack.setContentView(R.layout.activity_dialog_back_game);
        dialogBack.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogBack.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgResume = (ImageView) dialogBack.findViewById(R.id.imgResume);
        imgList = (ImageView) dialogBack.findViewById(R.id.imgList);
        imgReplay = (ImageView) dialogBack.findViewById(R.id.imgReplay);

//      dialog Next Level
        dialogComplete = new Dialog(writing_practice_game.this);
        dialogComplete.setCancelable(false);
        dialogComplete.setCanceledOnTouchOutside(false);
        dialogComplete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComplete.setContentView(R.layout.activity_dialog_win_game);
        dialogComplete.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogComplete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtNameScoreWin = (TextView) dialogComplete.findViewById(R.id.txtName);
        txtScoreWin = (TextView) dialogComplete.findViewById(R.id.txtScoreWin);
        imbNextGameWin = (ImageView) dialogComplete.findViewById(R.id.imvNextGame);
        txtNameScoreWin.setText(PLAYER_NAME);

//      dialog Next Turn
        dialogNextTurn = new Dialog(writing_practice_game.this);
        dialogNextTurn.setCancelable(false);
        dialogNextTurn.setCanceledOnTouchOutside(false);
        dialogNextTurn.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogNextTurn.setContentView(R.layout.activity_dialog_complete_turn);
        dialogNextTurn.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogNextTurn.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtWordEngNextTurn = (TextView) dialogNextTurn.findViewById(R.id.txtWordEngNextTurn);
        txtWordVieNextTurn = (TextView) dialogNextTurn.findViewById(R.id.txtWordVieNextTurn);
        imgObjectNextTurn = (ImageView) dialogNextTurn.findViewById(R.id.imgObjectNextTurn);
        imgSpeak = (ImageView) dialogNextTurn.findViewById(R.id.imgSpeak);
        dialogNextTurn.getWindow().getAttributes().width = (Resources.getSystem().getDisplayMetrics().widthPixels) - 20;

//      Dialog win end game
        dialogWinEndGame = new Dialog(writing_practice_game.this);
        dialogWinEndGame.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWinEndGame.setCancelable(false);
        dialogWinEndGame.setContentView(R.layout.activity_dialog_win_end_game);
        dialogWinEndGame.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWinEndGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgListWinEndGame = (ImageView) dialogWinEndGame.findViewById(R.id.imgListWinEndGame);
        imgReplayWinEndGame = (ImageView) dialogWinEndGame.findViewById(R.id.imgReplayWinEndGame);
        txtScoreWinEndGame = (TextView) dialogWinEndGame.findViewById(R.id.lblScoreWinEndGame);
        txtPlayerNameWinEndGame = (TextView) dialogWinEndGame.findViewById(R.id.lblPlayerNameWinEndGame);
        txtPlayerNameWinEndGame.setText(PLAYER_NAME);

//      Sound
        mButtonClick = MediaPlayer.create(writing_practice_game.this, click);
        mCorrect = MediaPlayer.create(writing_practice_game.this, dung_market_game);
        mClick = MediaPlayer.create(writing_practice_game.this, R.raw.click_market_game);
        mReadyGo = MediaPlayer.create(writing_practice_game.this, R.raw.ready_go);
        mNextLevel = MediaPlayer.create(writing_practice_game.this, R.raw.next_level_market_game);
        mGameOver = MediaPlayer.create(writing_practice_game.this, R.raw.game_over_market_game);
        mWrong = MediaPlayer.create(writing_practice_game.this, R.raw.wrong_market_game);
        mYaah = MediaPlayer.create(writing_practice_game.this, R.raw.yaah_market_game);
        mtornado = MediaPlayer.create(writing_practice_game.this, R.raw.tornado_market_game);
        mLoadImage = MediaPlayer.create(writing_practice_game.this, R.raw.load_image_market_game);
        mpSoundBackground = MediaPlayer.create(getApplicationContext(), R.raw.background_market_game);
        mpSoundBackground.setLooping(true);

//        Voice(mReadyGo);
//        Voice(mpSoundBackground);

//      textToSpeech
        textToSpeech = new TextToSpeech(this,this);

//      database
        dbAccessHelper = new DbAccessHelper(this);


        setEnableWP(false);
        //setBackgroundLayout();
        setFont();
        getEvents();
//      Get gioi han level, Chi lay so chan
        LEVEL_LIMIT = dbAccessHelper.getLevelHighest(OBJECT);
        if(LEVEL_LIMIT % 2 != 0)
        {
            LEVEL_LIMIT--;
        }
        Log.d("LEVEL limit : ", String.valueOf(LEVEL_LIMIT) );
        loadGame();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle reMessage = msg.getData();

            }


        };

    }

    private void loadGame() {
        COUNT_IMAGES = 6;
        clearAnimation();
        enableImageObject(0, true, true);
        setEnableWP(false);

        Voice(mLoadImage);
        StartSmartAnimation.startAnimation(findViewById(imgvWPObject1), AnimationType.BounceInLeft, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject2), AnimationType.BounceInLeft, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject3), AnimationType.BounceInDown, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject4), AnimationType.BounceInUp, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject5), AnimationType.BounceInRight, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject6), AnimationType.BounceInRight, 1500, 0, false);

        getDataWithLevel(OBJECT, LEVEL);

        imgvObject1.setBackground(getBitmapResource(listImageLevelO.get(0).getwPathImage()));
        imgvObject2.setBackground(getBitmapResource(listImageLevelO.get(1).getwPathImage()));
        imgvObject3.setBackground(getBitmapResource(listImageLevelO.get(2).getwPathImage()));
        imgvObject4.setBackground(getBitmapResource(listImageLevelO.get(3).getwPathImage()));
        imgvObject5.setBackground(getBitmapResource(listImageLevelO.get(4).getwPathImage()));
        imgvObject6.setBackground(getBitmapResource(listImageLevelO.get(5).getwPathImage()));
        for(int j = 0; j < 6; j++) {
            Log.d("Patch image", listImageLevelO.get(j).getwPathImage() );
        }
        lblPlayerNameGameOver.setEnabled(true);

    }

    private Drawable getBitmapResource(String name) {
//        Log.d("Patch image", listImageLevelO.get(0).getwPathImage() );
//        Log.d("Patch image", listImageLevelO.get(1).getwPathImage() );
//        Log.d("Patch image", listImageLevelO.get(2).getwPathImage() );

        int id = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        Drawable dr;
        if (id != 0)
            dr = getApplicationContext().getResources().getDrawable(id);
        else
            dr = getApplicationContext().getResources().getDrawable(R.drawable.animal_dog);
        Log.d("Patch image after else", String.valueOf(id ));
        return dr;
    }

    private void clearAnimation() {
        imgvObject1.clearAnimation();
        imgvObject2.clearAnimation();
        imgvObject3.clearAnimation();
        imgvObject4.clearAnimation();
        imgvObject5.clearAnimation();
        imgvObject6.clearAnimation();
        txtWord.clearAnimation();
    }

    private void getEvents() {
        imgListOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveScore();
                doWhenClickImglist();
                dialogGameOver.dismiss();
            }
        });
        imgReplayOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveScore();
                doWhenClickImgReplay();
                dialogGameOver.dismiss();
            }
        });
        imgListWinEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveScore();
                doWhenClickImglist();
    }
});
        imgReplayWinEndGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveScore();
                doWhenClickImgReplay();
                dialogWinEndGame.dismiss();


            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                StartSmartAnimation.startAnimation(findViewById(R.id.btnWPPause), AnimationType.Tada, 300, 0, false);
                dialogBack.show();
            }
        });

        imgResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                dialogBack.dismiss();

            }
        });

        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWhenClickImglist();

            }
        });

        imgReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWhenClickImgReplay();
                dialogBack.dismiss();

            }
        });

        imbNextGameWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                txtvLevel.setText("LEVEL. " + LEVEL);
                txtWord.setText("");
                txtvHelp.setText("");
                txtvHelp.setVisibility(View.INVISIBLE);
                dialogComplete.dismiss();
                loadGame();
            }
        });

        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textToSpeech.isSpeaking() == false && textToSpeech != null && listImageLevelO.get(CHOOSE).getwEng() != null){
                    textToSpeech.speak(listImageLevelO.get(CHOOSE).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });

        imgvObject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(txtvHelp.getVisibility() == View.INVISIBLE)
            {
                doWhenClickImgObject(0);
                imgvObject1.startAnimation(animation);
            }
            if(imgvObject1.getAnimation() == animation)
            {
                if(textToSpeech.isSpeaking() == false && textToSpeech != null && listImageLevelO.get(0).getwEng() != null){
                    textToSpeech.speak(listImageLevelO.get(0).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }

            }
        });

        imgvObject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtvHelp.getVisibility() == View.INVISIBLE) {
                    doWhenClickImgObject(1);
                    imgvObject2.startAnimation(animation);
                }
                if(imgvObject2.getAnimation() == animation)
                {
                    if(textToSpeech.isSpeaking() == false && textToSpeech != null && listImageLevelO.get(1).getwEng() != null){
                        textToSpeech.speak(listImageLevelO.get(1).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
            }
        });

        imgvObject3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtvHelp.getVisibility() == View.INVISIBLE) {
                    doWhenClickImgObject(2);
                    imgvObject3.startAnimation(animation);
                }
                if(imgvObject3.getAnimation() == animation)
                {
                    if(textToSpeech.isSpeaking() == false && textToSpeech != null && listImageLevelO.get(2).getwEng() != null){
                        textToSpeech.speak(listImageLevelO.get(2).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
            }
        });

        imgvObject4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtvHelp.getVisibility() == View.INVISIBLE) {
                    doWhenClickImgObject(3);
                    imgvObject4.startAnimation(animation);
                }
                if(imgvObject4.getAnimation() == animation)
                {
                    if(textToSpeech.isSpeaking() == false && textToSpeech != null && listImageLevelO.get(3).getwEng() != null){
                        textToSpeech.speak(listImageLevelO.get(3).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
            }
        });

        imgvObject5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtvHelp.getVisibility() == View.INVISIBLE) {
                    doWhenClickImgObject(4);
                    imgvObject5.startAnimation(animation);
                }
                if(imgvObject5.getAnimation() == animation)
                {
                    if(textToSpeech.isSpeaking() == false && textToSpeech != null && listImageLevelO.get(4).getwEng() != null){
                        textToSpeech.speak(listImageLevelO.get(4).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
            }
        });

        imgvObject6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtvHelp.getVisibility() == View.INVISIBLE) {
                    doWhenClickImgObject(5);
                    imgvObject6.startAnimation(animation);
                }
                if(imgvObject6.getAnimation() == animation)
                {
                    if(textToSpeech.isSpeaking() == false && textToSpeech != null && listImageLevelO.get(5).getwEng() != null){
                        textToSpeech.speak(listImageLevelO.get(5).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                    }
                }
            }
        });

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               don't quick click button_time to repair click is 3000 millisecond
                if (SystemClock.elapsedRealtime() - mLastClickTime < 3000) {
                    return;
                }
                StartSmartAnimation.startAnimation(findViewById(R.id.btnCheck), AnimationType.Tada, 300, 0, false);
                mLastClickTime = SystemClock.elapsedRealtime();
                Voice(mButtonClick);
                getResult(CHOOSE);
            }
        });
    }

    private void doWhenClickImglist() {
        Voice(mButtonClick);
        startActivity(new Intent(writing_practice_game.this, TopisChoosingActivity.class));
        finish();
    }
    private void doWhenClickImgReplay() {
        Voice(mButtonClick);
        RESULT_FAILED = 0;
        SCORE = SCORE_TEMP;
        LEVEL = 1;
        setEnableWP(false);
        txtvLevel.setText("Level. 1");
        txtWord.setText("");
        txtvScore.setText(String.valueOf(SCORE_TEMP));
        txtvHelp.setText("");
        txtvHelp.setVisibility(View.INVISIBLE);
        loadGame();
    }

    private void doWhenClickImgObject(int choose) {
        imgvObject1.clearAnimation();
        imgvObject2.clearAnimation();
        imgvObject3.clearAnimation();
        imgvObject4.clearAnimation();
        imgvObject5.clearAnimation();
        imgvObject6.clearAnimation();
        StartSmartAnimation.startAnimation(findViewById(R.id.txtWord), AnimationType.ZoomIn, 300, 0, false);
        setEnableWP(true);
        CHOOSE = choose;
    }

    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPathMarketGame));
        txtWord.setTypeface(custom_font);
        txtvScore.setTypeface(custom_font);
        txtvLevel.setTypeface(custom_font);
        lblScoreGameOver.setTypeface(custom_font);
        lblPlayerNameGameOver.setTypeface(custom_font);
        txtWordEngNextTurn.setTypeface(custom_font);
        txtScoreWin.setTypeface(custom_font);
        txtNameScoreWin.setTypeface(custom_font);
        txtvHelp.setTypeface(custom_font);

    }

    private void getDB2LEVEL(String object, int levelOf1, int levelOF2) {
        listImageWithLevel1 = new ArrayList<>();
        listImageWithLevel1 = dbAccessHelper.getWordObjectLevel(object, levelOf1);
        listImageWithLevel2 = new ArrayList<>();
        listImageWithLevel2 = dbAccessHelper.getWordObjectLevel(object, levelOF2);
    }
    private void getDataWithLevel(String object, int level) {
        switch (LEVEL) {
            case 1: getDB2LEVEL(OBJECT, 1, 2);break;
            case 2: getDB2LEVEL(OBJECT, 3, 4);break;
            case 3: getDB2LEVEL(OBJECT, 5, 6);break;
            case 4: getDB2LEVEL(OBJECT, 7, 8);break;
            case 5: getDB2LEVEL(OBJECT, 9, 10);break;

        }

        listImageLevelO = new ArrayList<>();

        Random rd = new Random();
        for(int j = 0; j < 3; j++) {
            int nSize = listImageWithLevel1.size();
            int xIndex = rd.nextInt(nSize);
            listImageLevelO.add(listImageWithLevel1.get(xIndex));
            listImageWithLevel1.remove(xIndex);
        }
        Log.d("Size 1",String.valueOf(listImageLevelO.size()));
        for(int j = 0; j < 3; j++) {
            int nSize = listImageWithLevel2.size();
            int xIndex = rd.nextInt(nSize);
            listImageLevelO.add(listImageWithLevel2.get(xIndex));
            listImageWithLevel2.remove(xIndex);
        }
        Log.d("Size 2",String.valueOf(listImageLevelO.size()));

//        xao tron mang
        Collections.shuffle(listImageLevelO);

    }

//  Display background with object
    private void setBackgroundLayout() {
        layoutWritingPracticeGame.setBackgroundResource(R.drawable.background_writing_practice_game);
    }

    public void Voice(MediaPlayer nameSound) {
        if (flagVoice == true) {
            nameSound.start();
        } else if (flagVoice == false) {
            //khi nguoi dung tat am thanh
        }
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

    private void phaySound(String nameSound) {
        int resID = writing_practice_game.this.getResources().getIdentifier(nameSound,"raw",writing_practice_game.this.getPackageName());
        MediaPlayer mediaPlayer = MediaPlayer.create(writing_practice_game.this,resID);
        mediaPlayer.start();
    }

    private void getResult(int choose) {
        String sRESULT_CHOSEN = listImageLevelO.get(choose).getwEng().toString().toLowerCase().trim();
        String sEditTextWord = txtWord.getText().toString().toLowerCase().trim();

//        neu dung
        if(sRESULT_CHOSEN.equals(sEditTextWord) == true || txtWord.getText().toString().equals("t"))
        {
            Voice(mCorrect);
            COUNT_IMAGES --;
            if(COUNT_IMAGES < 1){
                if(LEVEL + LEVEL <= LEVEL_LIMIT)
                {
                    PlayTwoSound(1000, 1000, mCorrect, mNextLevel);
                }
                else
                {
                    PlayTwoSound(1000, 1000, mCorrect, mNextLevel);
                }
            }
            enableImageObject(CHOOSE + 1, false, false);
            SCORE += (listImageLevelO.get(CHOOSE).getwEng().toString().trim().length() * 100) / (COUNT_IMAGES + 1);
            txtvScore.setText(String.valueOf(SCORE));
            showDialogNextTurn();
            dialogNextTurn.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    invisibleImage(CHOOSE + 1);
//            neu het hinh
                    if(COUNT_IMAGES < 1){

                        LEVEL++;
                        Log.d("LEVEL HIEN TAI", "LEVEL LIMIT: " + String.valueOf(LEVEL_LIMIT) + String.valueOf(LEVEL) +  "  LEVEL KHI CONG: " + String.valueOf(LEVEL+ LEVEL));
                        if(LEVEL + LEVEL - 1 <= LEVEL_LIMIT)
                        {
                            txtScoreWin.setText(String.valueOf(SCORE));
                            dialogComplete.show();
                        }
                        else
                        {
                            txtScoreWinEndGame.setText(String.valueOf(SCORE));
                            dialogWinEndGame.show();
                        }
                    }

                }
            });
        }
//        Toast.makeText(getApplicationContext(), "RC: " + sRESULT_CHOSEN + " sEdit: " + sEditTextWord + " KQ: " + String.valueOf(kq), Toast.LENGTH_LONG).show();
//        neu sai
        else {
            RESULT_FAILED ++;
//            neu sai 2 lan
            if(RESULT_FAILED == 2) {
                new CountDownTimer(1200, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        PlayTwoSound(1000, 1000, mtornado, mGameOver);
                        animationWhenGameOver();
                    }

                    @Override
                    public void onFinish() {
                        if (SCORE == 0) {
                            lblPlayerNameGameOver.setEnabled(false);
                        }
                        dialogGameOver.show();
                    }
                }.start();
                RESULT_FAILED = 0;
                lblScoreGameOver.setText(String.valueOf(SCORE));
            }
//            neu sai lan dau tien
            else {
                Voice(mWrong);
                getWordHelp();
            }


        }
    }

    private void getWordHelp() {
        String a = listImageLevelO.get(CHOOSE).getwEng().toString().trim();
        int numReplace = 0;
        switch (a.length()) {
            case 3:numReplace = 1;break;
            case 4: numReplace = 1;break;
            case 5: numReplace = 1;break;
            case 6: numReplace = 2;break;
            case 7: numReplace = 2;break;
            case 8: numReplace = 2;break;
            case 9: numReplace = 2;break;
            case 10: numReplace = 3;break;
            case 11: numReplace = 3;break;
            case 12: numReplace = 3;break;
            default: numReplace = 3;break;
        }
        List<Character> characterList = new ArrayList<Character>();
        char arrayChar[] = a.toCharArray();
        for (char aChar : arrayChar) {
            characterList.add(aChar);
        }
        Random rd = new Random();
        int index = -1;
        for(int i = 0; i < numReplace; i++) {
            index = rd.nextInt(characterList.size());
            if(characterList.get(index).toString().equals(" ") == false && characterList.get(index).toString().equals("_") == false) {
                characterList.set(index, '_');
            }
            else {i--;}

        }
        StartSmartAnimation.startAnimation(findViewById(R.id.txtvHelp), AnimationType.DropOut, 2000, 0, false);
        txtvHelp.setText( characterList.toString().replaceAll("\\[|\\]", "").replaceAll(", ",""));
        txtvHelp.setVisibility(View.VISIBLE);
    }

    private void invisibleImage(int choose) {
        switch (choose) {
            case 1:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject1), AnimationType.ZoomOut, 1000, 0, true);
                break;
            case 2:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject2), AnimationType.ZoomOut, 1000, 0, true);
                break;
            case 3:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject3), AnimationType.ZoomOut, 1000, 0, true);
                break;
            case 4:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject4), AnimationType.ZoomOut, 1000, 0, true);
                break;
            case 5:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject5), AnimationType.ZoomOut, 1000, 0, true);
                break;
            case 6:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvWPObject6), AnimationType.ZoomOut, 1000, 0, true);
                break;
        }
    }

    private void enableImageObject(int choose, boolean trueOrFalse, boolean setAll) {
        if(setAll == false)
        {
            switch (choose) {
                case 1:
                    imgvObject1.setEnabled(trueOrFalse);
                    break;
                case 2:
                    imgvObject2.setEnabled(trueOrFalse);
                    break;
                case 3:
                    imgvObject3.setEnabled(trueOrFalse);
                    break;
                case 4:
                    imgvObject4.setEnabled(trueOrFalse);
                    break;
                case 5:
                    imgvObject5.setEnabled(trueOrFalse);
                    break;
                case 6:
                    imgvObject6.setEnabled(trueOrFalse);
                    break;
            }
        }
        else {
            imgvObject1.setEnabled(trueOrFalse);
            imgvObject2.setEnabled(trueOrFalse);
            imgvObject3.setEnabled(trueOrFalse);
            imgvObject4.setEnabled(trueOrFalse);
            imgvObject5.setEnabled(trueOrFalse);
            imgvObject6.setEnabled(trueOrFalse);
        }

    }

    public void PlayTwoSound(int lengthOfFistSound, int timeEach, final MediaPlayer soundFist, final MediaPlayer soundSecond) {
        new CountDownTimer(lengthOfFistSound + 50, timeEach) {
            @Override
            public void onTick(long millisUntilFinished) {
                Voice(soundFist);
            }

            @Override
            public void onFinish() {
                Voice(soundSecond);
            }
        }.start();
    }

    //Save Score
    private void doSaveScore() {
        String playerName = "";
        if(dialogGameOver.isShowing()) {
            playerName = lblPlayerNameGameOver.getText().toString();
        }
        else if(dialogWinEndGame.isShowing()){
            playerName = txtPlayerNameWinEndGame.getText().toString();
        }
        if (SCORE > 0 && playerName.equals("") == false) {
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsPlayer(playerName);
            scoreObject.setsScore(SCORE);
            dbAccessHelper.doInsertScore(scoreObject);
            //Toast.makeText(getApplicationContext(), "Saved Score", Toast.LENGTH_SHORT).show();
        }
    }

    public void animationWhenGameOver() {
        new CountDownTimer(1100, 1001) {
            @Override
            public void onTick(long millisUntilFinished) {
                imgvObject1.setEnabled(false);
                imgvObject2.setEnabled(false);
                imgvObject3.setEnabled(false);
                imgvObject4.setEnabled(false);
                imgvObject5.setEnabled(false);
                imgvObject6.setEnabled(false);

                imgvObject1.startAnimation(animationRotate);
                imgvObject2.startAnimation(animationRotate);
                imgvObject3.startAnimation(animationRotate);
                imgvObject4.startAnimation(animationRotate);
                imgvObject5.startAnimation(animationRotate);
                imgvObject6.startAnimation(animationRotate);
            }

            @Override
            public void onFinish() {
                imgvObject1.setEnabled(true);
                imgvObject2.setEnabled(true);
                imgvObject3.setEnabled(true);
                imgvObject4.setEnabled(true);
                imgvObject5.setEnabled(true);
                imgvObject6.setEnabled(true);

                imgvObject1.setVisibility(View.VISIBLE);
                imgvObject2.setVisibility(View.VISIBLE);
                imgvObject3.setVisibility(View.VISIBLE);
                imgvObject4.setVisibility(View.VISIBLE);
                imgvObject5.setVisibility(View.VISIBLE);
                imgvObject6.setVisibility(View.VISIBLE);

                imgvObject1.clearAnimation();
                imgvObject2.clearAnimation();
                imgvObject3.clearAnimation();
                imgvObject4.clearAnimation();
                imgvObject5.clearAnimation();
                imgvObject6.clearAnimation();
            }
        }.start();
    }

    private void showDialogNextTurn() {
        new CountDownTimer(3000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                txtWordVieNextTurn.setText(listImageLevelO.get(CHOOSE).getwVie().toString());
                txtWordEngNextTurn.setText(listImageLevelO.get(CHOOSE).getwEng().toString());
                imgObjectNextTurn.setBackground(getBitmapResource(listImageLevelO.get(CHOOSE).getwPathImage()));
                dialogNextTurn.show();
                if(dialogNextTurn.isShowing() == true)
                {
                    StartSmartAnimation.startAnimation(dialogNextTurn.findViewById(R.id.txtWordVieNextTurn), AnimationType.ZoomIn, 1000, 0, false);
                    imgSpeak.startAnimation(animation);
                }

            }
            @Override
            public void onFinish() {
                dialogNextTurn.dismiss();
                setEnableWP(false);
                txtWord.setText("");
                txtvHelp.setText("");
                txtvHelp.setVisibility(View.INVISIBLE);
            }
        }.start();
    }

    private void setEnableWP(boolean enable)
    {
        txtWord.setEnabled(enable);
        btnCheck.setEnabled(enable);
    }


}
