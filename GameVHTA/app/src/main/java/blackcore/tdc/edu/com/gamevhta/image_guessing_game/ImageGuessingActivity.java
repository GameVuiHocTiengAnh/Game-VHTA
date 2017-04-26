package blackcore.tdc.edu.com.gamevhta.image_guessing_game;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
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
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGamePracticeActivity;
import blackcore.tdc.edu.com.gamevhta.TopisChoosingActivity;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.custom_toask.CustomToask;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.SizeOfDevice;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;


/**
 * Created by Hoang on 3/16/2017.
 */

public class ImageGuessingActivity extends AppCompatActivity {
    private Handler handler;
    private Animation animation, animationTimer, animationOver;
    private Timer timer = new Timer();
    private ImageButton btnImage1, btnImage2, btnImage3, btnImage4, btnImage5, btnImage6, btnPauseGame5;
    private TextView lblTimer, lblWord, lblScore, lblScoreGameOver, txtNameScoreWin, txtScoreWin;
    private MediaPlayer mpChoose, mpSoundBackground, mpWingame, mpGameOver, mpOK, mpClicked, mpLoadImage, mpWrong;
    private ImageView imgListOver, imgReplayOver, imgList, imgReplay, imgResume, imvNextGame, imgSleep;
    private TextView lblPlayerNameGameOver;
    private LinearLayout lnBackGroundWord;
    private String OBJECT = "";
    private int TURN = 0;
    private int SCORE = 0;
    private int RESULT_FAILED = 0;
    private int RESULT_CHOSEN = -1;
    private ArrayList<WordObject> listImageFromDataO;
    private ArrayList<WordObject> listImageLevelO = null;
    private ArrayList<WordObject> listImageLevelGame = null;

    private Dialog dialogBack;
    private Dialog dialogGameOver;
    private Dialog dialogWinGame;
    private DbAccessHelper dbAccessHelper;

    private String newName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_screen_mh_9);
        initialize();
        dbAccessHelper = new DbAccessHelper(this);
        newName = ConfigApplication.NEW_NAME;
        Intent i = getIntent();
        if(i != null){
            Bundle b = i.getExtras();
            if(b != null){
                listImageLevelGame = (ArrayList<WordObject>) b.getSerializable(ConfigApplication.NAME_DATA_LIST);
                SCORE += 600;
                addDataList();
            }else{
                Log.d("Tagtest","vao data khong hi");
                //addDataList();
                listImageFromDataO = dbAccessHelper.getWordObject(ConfigApplication.CURRENT_CHOOSE_TOPIC);
                listImageLevelGame = dbAccessHelper.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC,1); // when new player data is lv1 and lv2
                ArrayList<WordObject> lv2 = dbAccessHelper.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC, 2);
                listImageLevelGame.addAll(lv2);
                Log.d("Tagtest", listImageLevelGame.size()+"");
            }
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle reMessage = msg.getData();
                String time = reMessage.getString("time");

                lblTimer.setText(time);
                int t = Integer.parseInt(time);
                if (t > 0 && t <= 10) {
                    lblTimer.startAnimation(animationTimer);
                } else if (t == 0) {
                    if (SCORE == 0)
                        lblPlayerNameGameOver.setVisibility(View.GONE);
                    else
                        lblPlayerNameGameOver.setVisibility(View.VISIBLE);
                    mpGameOver.start();
                    timer.cancel();
                    executeAnimation();
                    lblTimer.clearAnimation();
                    lblScoreGameOver.setText(String.valueOf(SCORE));
                    if (!dialogGameOver.isShowing())
                        dialogGameOver.show();
                }
            }
        };
        //get Object selected at screen topic
//        if (getIntent().getExtras() != null) {
//            OBJECT = getIntent().getStringExtra(ConfigApplication.OBJECT_SELECTED);
//            addDataList();
//        }
        new CustomToask(ImageGuessingActivity.this, R.drawable.animal_bat, "Chọn 1 tấm hình đúng với từ vựng nhé...");
        Log.d("Guessing", SizeOfDevice.getScreenWidth() + "x" + SizeOfDevice.getScreenHeight());
//        for (String arr : ConfigApplication.getListImageFromSDCard("/Download/")) {
//            Log.d("Guessing", "File Name: " + arr);
//        }

    }

    @Override
    public void onBackPressed() {
        mpClicked.start();
        imgSleep.startAnimation(animationTimer);
        timer.cancel();
        if (!dialogBack.isShowing())
            dialogBack.show();
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mpSoundBackground.start();
        if (dialogWinGame.isShowing() || dialogGameOver.isShowing())
            dialogBack.dismiss();
        else {
            if (dialogGameOver.isShowing())
                dialogGameOver.dismiss();
            if (dialogWinGame.isShowing())
                dialogWinGame.dismiss();
            if (dialogBack.isShowing())
                imgSleep.startAnimation(animationTimer);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mpSoundBackground.pause();
        timer.cancel();
        if (!dialogGameOver.isShowing() && !dialogWinGame.isShowing() && !dialogBack.isShowing()) {
            dialogBack.show();
            imgSleep.clearAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clearAnimation();
        mpSoundBackground.stop();
        mpWingame.stop();
        mpChoose.stop();
        mpClicked.stop();
        mpLoadImage.stop();
    }

    private void initialize() {

        lnBackGroundWord = (LinearLayout) findViewById(R.id.lnBackGroundWord);
        btnImage1 = (ImageButton) findViewById(R.id.btnImage1);
        btnImage2 = (ImageButton) findViewById(R.id.btnImage2);
        btnImage3 = (ImageButton) findViewById(R.id.btnImage3);
        btnImage4 = (ImageButton) findViewById(R.id.btnImage4);
        btnImage5 = (ImageButton) findViewById(R.id.btnImage5);
        btnImage6 = (ImageButton) findViewById(R.id.btnImage6);
        btnPauseGame5 = (ImageButton) findViewById(R.id.btnPauseGame5);
        btnPauseGame5.setVisibility(View.GONE);
        lblTimer = (TextView) findViewById(R.id.lblTimer);
        lblWord = (TextView) findViewById(R.id.lblWord);
        lblScore = (TextView) findViewById(R.id.lblScore);
        //animation
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        animationTimer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim_trieu);
        animationOver = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim_screen_5);
        //dialog game over
        dialogGameOver = new Dialog(ImageGuessingActivity.this);
        dialogGameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGameOver.setCancelable(false);
        dialogGameOver.setContentView(R.layout.activity_dialog_game_over);
        dialogGameOver.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogGameOver.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgListOver = (ImageView) dialogGameOver.findViewById(R.id.imgListOver);
        imgReplayOver = (ImageView) dialogGameOver.findViewById(R.id.imgReplayOver);
        lblScoreGameOver = (TextView) dialogGameOver.findViewById(R.id.lblScoreGameOver);
        lblPlayerNameGameOver = (TextView) dialogGameOver.findViewById(R.id.lblPlayerNameGOver);

        //dialog back game
        dialogBack = new Dialog(ImageGuessingActivity.this);
        dialogBack.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBack.setCancelable(false);
        dialogBack.setContentView(R.layout.activity_dialog_back_game);
        dialogBack.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogBack.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgResume = (ImageView) dialogBack.findViewById(R.id.imgResume);
        imgList = (ImageView) dialogBack.findViewById(R.id.imgList);
        imgReplay = (ImageView) dialogBack.findViewById(R.id.imgReplay);
        imgSleep = (ImageView) dialogBack.findViewById(R.id.sleep);

        //dialog win game
        dialogWinGame = new Dialog(ImageGuessingActivity.this);
        dialogWinGame.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWinGame.setCancelable(false);
        dialogWinGame.setContentView(R.layout.activity_dialog_win_game);
        dialogWinGame.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWinGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtNameScoreWin = (TextView) dialogWinGame.findViewById(R.id.txtNameScoreWin);
        txtScoreWin = (TextView) dialogWinGame.findViewById(R.id.txtScoreWin);
        imvNextGame = (ImageView) dialogWinGame.findViewById(R.id.imvNextGame);

        mpWrong = MediaPlayer.create(getApplicationContext(), R.raw.game_5_ohoh);
        mpLoadImage = MediaPlayer.create(getApplicationContext(), R.raw.game_5_load_image);
        mpClicked = MediaPlayer.create(getApplicationContext(), R.raw.click);
        mpOK = MediaPlayer.create(getApplicationContext(), R.raw.dung_market_game);
        mpGameOver = MediaPlayer.create(getApplicationContext(), R.raw.sai);
        mpChoose = MediaPlayer.create(getApplicationContext(), R.raw.game_5_sound_clicked);
        mpWingame = MediaPlayer.create(getApplicationContext(), R.raw.wingame);
        mpSoundBackground = MediaPlayer.create(getApplicationContext(), R.raw.game_5_screen_background_sound);
        mpSoundBackground.setLooping(true);
        mpSoundBackground.start();

        //set time left default
        lblTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
        listImageLevelGame = new ArrayList<>();
        listImageFromDataO = new ArrayList<>();
        listImageLevelO = new ArrayList<>();
        getEvents();
        setFont();
        getAnimationImageButton();

        addDataList();
        loadGame();
    }

    //play timer
    private void playTimer() {
        timer = new Timer();
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
    }

    //play game
    private void loadGame() {
        clearAnimation();
        lblTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
        lblScore.setText(String.valueOf(SCORE));
        RESULT_FAILED = 0;
        RESULT_CHOSEN = -1;
        mpLoadImage.start();
        StartSmartAnimation.startAnimation(btnImage1, AnimationType.FlipInX, 2500, 0, true);
        StartSmartAnimation.startAnimation(btnImage2, AnimationType.FlipInX, 2000, 0, true);
        StartSmartAnimation.startAnimation(btnImage3, AnimationType.FlipInX, 2300, 0, true);
        StartSmartAnimation.startAnimation(btnImage4, AnimationType.FlipInX, 2200, 0, true);
        StartSmartAnimation.startAnimation(btnImage5, AnimationType.FlipInX, 2100, 0, true);
        StartSmartAnimation.startAnimation(btnImage6, AnimationType.FlipInX, 2400, 0, true);
        StartSmartAnimation.startAnimation(lblWord, AnimationType.ZoomInRubberBand, 1500, 0, true);

        listImageLevelO = new ArrayList<>();
//        ArrayList<WordObject> cloneData0 = (ArrayList<WordObject>) listImageFromDataO.clone();
        Random rd = new Random();
        Log.d("RESULT", "Size: "+ String.valueOf(listImageFromDataO.size()));
        int t = listImageLevelGame.size();
        if(t > 0) {
            int xt = rd.nextInt(t);
            listImageLevelO.add(listImageLevelGame.get(xt));
            listImageLevelGame.remove(xt);
            for (int j = 0; j < 5; j++) {
                rd = new Random();
                int n = listImageFromDataO.size();
                int x = rd.nextInt(n);
                listImageLevelO.add(listImageFromDataO.get(x));
                listImageFromDataO.remove(x);
            }
        }else {
            for (int j = 0; j < 6; j++) {
                rd = new Random();
                int n = listImageFromDataO.size();
                int x = rd.nextInt(n);
                listImageLevelO.add(listImageFromDataO.get(x));
                listImageFromDataO.remove(x);
            }
        }
        RESULT_CHOSEN = rd.nextInt(listImageLevelO.size());
        Log.d("RESULT", String.valueOf(RESULT_CHOSEN));
        String word = listImageLevelO.get(RESULT_CHOSEN).getwEng();
//        if(word.length()>5) {
//            lnBackGroundWord.getLayoutParams().width = 300;
//        }
//        else
//            lnBackGroundWord.getLayoutParams().width = 200;
        lblWord.setText(word);

        btnImage1.setBackground(getBitmapResource(listImageLevelO.get(0).getwPathImage()));
        btnImage2.setBackground(getBitmapResource(listImageLevelO.get(1).getwPathImage()));
        btnImage3.setBackground(getBitmapResource(listImageLevelO.get(2).getwPathImage()));
        btnImage4.setBackground(getBitmapResource(listImageLevelO.get(3).getwPathImage()));
        btnImage5.setBackground(getBitmapResource(listImageLevelO.get(4).getwPathImage()));
        btnImage6.setBackground(getBitmapResource(listImageLevelO.get(5).getwPathImage()));
        timer = new Timer();
        playTimer();
    }

    //List Image was loaded from database
    private void addDataList() {
        //database
        Log.d("TagtestSize", ConfigApplication.CURRENT_CHOOSE_TOPIC + " Objhect");
        dbAccessHelper = new DbAccessHelper(this);
        listImageFromDataO = dbAccessHelper.getWordObject(ConfigApplication.CURRENT_CHOOSE_TOPIC);
        Log.d("TagtestSize", listImageFromDataO.size()+"");

    }

    //Add image from resource to ImageButton
    private Drawable getBitmapResource(String name) {
        int id = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        Log.d("ImageID", String.valueOf(id));
        Drawable dr;
        if (id != 0)
            dr = getApplicationContext().getResources().getDrawable(id);
        else
            dr = getApplicationContext().getResources().getDrawable(R.drawable.animal_bee);
        return dr;
    }

    //Font
    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPath));
        lblTimer.setTypeface(custom_font);
        lblWord.setTypeface(custom_font);
        lblScore.setTypeface(custom_font);
        lblScoreGameOver.setTypeface(custom_font);
        lblPlayerNameGameOver.setTypeface(custom_font);
        txtNameScoreWin.setTypeface(custom_font);
        txtScoreWin.setTypeface(custom_font);
    }

    //Event
    private void getEvents() {

        imgListOver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgListOver.setSelected(!imgListOver.isSelected());
                        imgListOver.isSelected();
                        mpClicked.start();
                        imgReplayOver.setEnabled(false);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        doSaveScore();
                        imgListOver.setSelected(false);
                        startActivity(new Intent(ImageGuessingActivity.this, TopisChoosingActivity.class));
                        finish();
                        imgReplayOver.setEnabled(true);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        imgReplayOver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgReplayOver.setSelected(!imgReplayOver.isSelected());
                        imgReplayOver.isSelected();
                        mpClicked.start();
                        imgListOver.setEnabled(false);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        addDataList();
                        imgReplayOver.setSelected(false);
                        doSaveScore();
                        TURN = 0;
                        SCORE = 0;
                        loadGame();
                        dialogGameOver.dismiss();
                        imgListOver.setEnabled(true);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        btnPauseGame5.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnPauseGame5.setSelected(!btnPauseGame5.isSelected());
                        btnPauseGame5.isSelected();
                        mpClicked.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        btnPauseGame5.setSelected(false);
                        btnPauseGame5.startAnimation(animation);
                        timer.cancel();
                        dialogBack.show();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        imgResume.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgResume.setSelected(!imgResume.isSelected());
                        imgResume.isSelected();
                        mpClicked.start();
                        imgList.setEnabled(false);
                        imgReplay.setEnabled(false);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        imgResume.setSelected(false);
                        playTimer();
                        dialogBack.dismiss();
                        imgSleep.clearAnimation();
                        imgList.setEnabled(true);
                        imgReplay.setEnabled(true);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        imgList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgList.setSelected(!imgList.isSelected());
                        imgList.isSelected();
                        mpClicked.start();
                        imgResume.setEnabled(false);
                        imgReplay.setEnabled(false);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        imgList.setSelected(false);
                        startActivity(new Intent(ImageGuessingActivity.this, TopisChoosingActivity.class));
                        finish();
                        imgResume.setEnabled(true);
                        imgReplay.setEnabled(true);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        imgReplay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgReplay.setSelected(!imgReplay.isSelected());
                        imgReplay.isSelected();
                        mpClicked.start();
                        imgList.setEnabled(false);
                        imgResume.setEnabled(false);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        imgReplay.setSelected(false);
                        addDataList();
                        TURN = 0;
                        SCORE = 0;
                        loadGame();
                        dialogBack.dismiss();
                        imgSleep.clearAnimation();
                        imgList.setEnabled(true);
                        imgResume.setEnabled(true);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        imvNextGame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imvNextGame.setSelected(!imvNextGame.isSelected());
                        imvNextGame.isSelected();
                        mpClicked.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        imvNextGame.setSelected(false);
                        dialogWinGame.dismiss();
                        Intent intent = new Intent(ImageGuessingActivity.this, RandomGamePracticeActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable(ConfigApplication.NAME_DATA_LIST, listImageLevelO);
                        data.putInt(ConfigApplication.SCORES_BEFOR_GAME, SCORE);
                        intent.putExtras(data);
                        startActivity(intent);
                        finish();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    private void getAnimationImageButton() {
        btnImage6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnImage6.startAnimation(animation);
                        mpChoose.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        getResult(5);
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
                        mpChoose.start();

                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        getResult(4);
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
                        mpChoose.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        getResult(3);
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
                        mpChoose.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        getResult(2);
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
                        mpChoose.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        getResult(1);
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
                        mpChoose.start();
                        return true;
                    // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        getResult(0);
                        btnImage1.clearAnimation();
                        return true; // if you want to handle the touch event
                }
                return false;

            }
        });
    }

    //Save Score
    private void doSaveScore() {
        if (SCORE > 0) {
            String playerName = lblPlayerNameGameOver.getText().toString();
            if (playerName.equals(""))
                playerName = "Unknown Player";
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsPlayer(playerName);
            scoreObject.setsScore(SCORE);
            dbAccessHelper.doInsertScore(scoreObject);
            lblPlayerNameGameOver.setText("");
        }
    }

    //Check Result
    private void getResult(int choose) {
        if (choose == RESULT_CHOSEN) {
            //Word's length * 5 * time left
            SCORE += lblWord.getText().length() * 5 * Integer.parseInt(lblTimer.getText().toString());
            lblScore.setText(String.valueOf(SCORE));
            StartSmartAnimation.startAnimation(lblScore, AnimationType.StandUp, 1500, 0, true);
            mpOK.start();
            TURN++;
            if (TURN >= 5) {
                timer.cancel();
                executeAnimation();
                txtScoreWin.setText(String.valueOf(SCORE));
                dialogWinGame.show();
                mpWingame.start();
            } else {
                timer.cancel();
                loadGame();
            }
        } else {
            RESULT_FAILED++;
            if (RESULT_FAILED == 1) {
                mpWrong.start();
                new CustomToask(ImageGuessingActivity.this, R.drawable.animal_bat, "SAI rồi! Chọn thêm 1 lần nữa nhé...");
            } else if (RESULT_FAILED == 2) {
                executeAnimation();
                mpGameOver.start();
                if (SCORE == 0)
                    lblPlayerNameGameOver.setVisibility(View.GONE);
                else
                    lblPlayerNameGameOver.setVisibility(View.VISIBLE);
                lblScoreGameOver.setText(String.valueOf(SCORE));
                timer.cancel();
                dialogGameOver.show();
            }
        }
    }

    private void clearAnimation() {
        lblTimer.clearAnimation();
        lblScore.clearAnimation();
        lblWord.clearAnimation();
        btnImage1.clearAnimation();
        btnImage2.clearAnimation();
        btnImage3.clearAnimation();
        btnImage4.clearAnimation();
        btnImage5.clearAnimation();
        btnImage6.clearAnimation();

    }

    //Animation Wingame for Image
    private void executeAnimation() {
        clearAnimation();
        btnImage1.startAnimation(animationOver);
        btnImage2.startAnimation(animationOver);
        btnImage3.startAnimation(animationOver);
        btnImage4.startAnimation(animationOver);
        btnImage5.startAnimation(animationOver);
        btnImage6.startAnimation(animationOver);
    }
}
