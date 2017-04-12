package blackcore.tdc.edu.com.gamevhta.bubble_hitting_game;

import android.app.Dialog;
import android.content.Intent;
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
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.LoadingGoOutGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGamePracticeActivity;
import blackcore.tdc.edu.com.gamevhta.button.PauseButton;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

/**
 * Created by Canh on 30/03/2017.
 */

public class BubbleHittingActivity extends AppCompatActivity {

    private Animation mAnimation;
    private ImageView imgQuestionsOne, imgQuestionsTwo, imgQuestionsThree, imgQuestionsFour, imgQuestionsFive, imgQuestionsSix, imgAnimalDialog;
    private ImageView imgBubbleOne, imgBubbleTwo, imgBubbleThree, imgBubbleFour, imgBubbleFive, imgBubbleSix, imgDialogBubbleOne, imgDialogBubbleTwo, imgDialogBubbleThree;
    private ImageView imgListOver, imgReplayOver, imgNextGameWin;

    private TextView txtWordOne, txtWordTwo, txtWordThree, txtTime, txtWord, txtScore, txtScoreGameOver, txtScoreWin;
    private Dialog bubbledialog, dialogGameOver, dialogWinGame;
    private EditText edtPlayerNameGameOver;
    private MediaPlayer mMusicMainGame = null, mCorrect = null, mWrong=null, mWin=null;
    private MusicService mService = new MusicService();
    private Handler handler;
    private Timer timer;
    private int check_finish = 0;
    private int SCORE_ONE = 0, SCORE_TWO = 0, SCORE_THREE = 0, SCORE_FOUR = 0, SCORE_FIVE = 0, SCORE_SIX = 0, SCORE_ALL = 0;
    private DbAccessHelper dbAccessHelper;
    private ArrayList<WordObject> listImageGame = null;
    private ArrayList<WordObject> listImageData = null;
    PauseButton imgBackGame;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bubble_hitting_game);

        dbAccessHelper = new DbAccessHelper(this);
        listImageData= new ArrayList<>();
        listImageGame= new ArrayList<>();
        imgQuestionsOne = (ImageView) findViewById(R.id.imgQuestionsOne);
        imgQuestionsTwo = (ImageView) findViewById(R.id.imgQuestionsTwo);
        imgQuestionsThree = (ImageView) findViewById(R.id.imgQuestionsThree);
        imgQuestionsFour = (ImageView) findViewById(R.id.imgQuestionsFour);
        imgQuestionsFive = (ImageView) findViewById(R.id.imgQuestionsFive);
        imgQuestionsSix = (ImageView) findViewById(R.id.imgQuestionsSix);

        imgBubbleOne = (ImageView) findViewById(R.id.imgBubbleOne);
        imgBubbleTwo = (ImageView) findViewById(R.id.imgBubbleTwo);
        imgBubbleThree = (ImageView) findViewById(R.id.imgBubbleThree);
        imgBubbleFour = (ImageView) findViewById(R.id.imgBubbleFour);
        imgBubbleFive = (ImageView) findViewById(R.id.imgBubbleFive);
        imgBubbleSix = (ImageView) findViewById(R.id.imgBubbleSix);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtWord = (TextView) findViewById(R.id.txtWordOne);
        txtWord = (TextView) findViewById(R.id.txtWordTwo);
        txtWord = (TextView) findViewById(R.id.txtWordThree);

        //dialog overgame
        dialogGameOver = new Dialog(BubbleHittingActivity.this);
        dialogGameOver.setCancelable(false);
        dialogGameOver.setCanceledOnTouchOutside(false);
        dialogGameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGameOver.setContentView(R.layout.activity_dialog_game_over);
        dialogGameOver.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogGameOver.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        imgListOver = (ImageView) dialogGameOver.findViewById(R.id.imgListOver);
        imgReplayOver = (ImageView) dialogGameOver.findViewById(R.id.imgReplayOver);
        edtPlayerNameGameOver = (EditText) dialogGameOver.findViewById(R.id.lblPlayerNameGameOver);
        txtScoreGameOver = (TextView) dialogGameOver.findViewById(R.id.lblScoreGameOver);

        //dialog win
        dialogWinGame = new Dialog(BubbleHittingActivity.this);
        dialogWinGame.setCancelable(false);
        dialogWinGame.setCanceledOnTouchOutside(false);
        dialogWinGame.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWinGame.setContentView(R.layout.activity_dialog_win_game);
        dialogWinGame.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWinGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        imgNextGameWin = (ImageView) dialogWinGame.findViewById(R.id.imvNextGame);
        txtScoreWin = (TextView) dialogWinGame.findViewById(R.id.txtScoreWin);

        mMusicMainGame = MediaPlayer.create(BubbleHittingActivity.this, R.raw.starters);
        mCorrect = MediaPlayer.create(BubbleHittingActivity.this,R.raw.dung);
        mWrong = MediaPlayer.create(BubbleHittingActivity.this,R.raw.sai);
        mWin = MediaPlayer.create(BubbleHittingActivity.this, R.raw.wingame);


        txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));

        mMusicMainGame.start();
        mMusicMainGame.setLooping(true);
        mMusicMainGame.setVolume(0.5f,0.5f);

        addDataList();
        createDataGame();
        moveActivity();
        ImageQuestion();
        effectImageQuestion();
        getImageButton();
        EventOver();
    }
    private void ImageQuestion(){
        imgQuestionsOne.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
        imgQuestionsTwo.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
        imgQuestionsThree.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
        imgQuestionsFour.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
        imgQuestionsFive.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
        imgQuestionsSix.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
    }

    private void effectImageQuestion(){
        mAnimation = new TranslateAnimation(0, 0, 0,40);
        mAnimation.setDuration(1500);
        mAnimation.setFillAfter(true);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);

        imgQuestionsOne.setAnimation(mAnimation);
        imgBubbleOne.setAnimation(mAnimation);
        imgQuestionsTwo.setAnimation(mAnimation);
        imgBubbleTwo.setAnimation(mAnimation);
        imgQuestionsThree.setAnimation(mAnimation);
        imgBubbleThree.setAnimation(mAnimation);
        imgQuestionsFour.setAnimation(mAnimation);
        imgBubbleFour.setAnimation(mAnimation);
        imgQuestionsFive.setAnimation(mAnimation);
        imgBubbleFive.setAnimation(mAnimation);
        imgQuestionsSix.setAnimation(mAnimation);
        imgBubbleSix.setAnimation(mAnimation);
    }
    private void effectGameOver(){
        StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );

        StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
    }

    //Add image from resource to ImageButton
    private Drawable getBitmapResource(String name) {
        int id = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        Log.d("ImageID", String.valueOf(id));
        Drawable dr;
        if (id != 0)
            dr = getApplicationContext().getResources().getDrawable(id);
        else
            dr = getApplicationContext().getResources().getDrawable(R.drawable.animal_bat);
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
        mMusicMainGame.pause();
        timer.cancel();
        super.onPause();
    }
    public void onResume() {
        mMusicMainGame.start();
        Timer();
        timer.cancel();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMusicMainGame.stop();
        mMusicMainGame.release();
        mCorrect.stop();
        mCorrect.release();
        mWin.stop();
        mWin.release();
    }
    public void moveActivity() {
        imgBackGame = (PauseButton) findViewById(R.id.btnPausegame);
        Intent intent = new Intent(getApplicationContext(),LoadingGoOutGameActivity.class);
        imgBackGame.setMoveActivity(intent,this);
    }

    private void Timer() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle reMessage = msg.getData();
                String time = reMessage.getString("time");
                txtTime.setText(time);
                int t = Integer.parseInt(time);
                if (t > 0 && t <= 10) {
                } else if (t == 0) {
                    timer.cancel();
                    EventOver();
                    effectGameOver();
                    bubbledialog.dismiss();
                    dialogGameOver.show();
                }
            }
        };
        playTimer();
    }
    private void playTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int t = Integer.parseInt(txtTime.getText().toString());
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

    private void BubbleDialog(){
        bubbledialog = new Dialog(BubbleHittingActivity.this);
        bubbledialog.setCancelable(false);
        bubbledialog.setCanceledOnTouchOutside(false);
        bubbledialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        bubbledialog.setContentView(R.layout.activity_bubble_hitting_dialog);
        bubbledialog.getWindow().setBackgroundDrawableResource(R.color.tran);
        bubbledialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgAnimalDialog = (ImageView) bubbledialog.findViewById(R.id.imgAnimalDialog);
        txtWordOne = (TextView) bubbledialog.findViewById(R.id.txtWordOne);
        txtWordTwo = (TextView) bubbledialog.findViewById(R.id.txtWordTwo);
        txtWordThree = (TextView) bubbledialog.findViewById(R.id.txtWordThree);
    }
    private void setTextDialog(ArrayList<String> strr){
        txtWordOne.setText(strr.get(0));
        txtWordTwo.setText(strr.get(1));
        txtWordThree.setText(strr.get(2));
    }
    private WordObject getIntRandomInList(){
        Random ran = new Random();
        int dom = ran.nextInt(listImageData.size());
        WordObject object = listImageData.get(dom);
        listImageData.remove(dom);
        return object ;
    }

    public void getImageButton() {
        imgQuestionsOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgBubbleOne.setSelected(!imgQuestionsOne.isSelected());
                        imgQuestionsOne.isSelected();
                        imgQuestionsOne.setEnabled(false);
                        BubbleDialog();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(listImageGame.get(0).getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        Collections.shuffle(arr);
                        setTextDialog(arr);
                        bubbledialog.show();
                        //txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                        Timer();
                        txtWordOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(0).getwEng().equals(txtWordOne.getText().toString())){
                                    getResult(1);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(0).getwEng().equals(txtWordTwo.getText().toString())){
                                    getResult(1);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(0).getwEng().equals(txtWordThree.getText().toString())){
                                    getResult(1);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        imgQuestionsOne.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        timer.cancel();
                        return true;
                }
                return false;
            }
        });
        imgQuestionsTwo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgBubbleTwo.setSelected(!imgQuestionsTwo.isSelected());
                        imgQuestionsTwo.isSelected();
                        imgQuestionsTwo.setEnabled(false);
                        BubbleDialog();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(listImageGame.get(1).getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        Collections.shuffle(arr);
                        setTextDialog(arr);
                        bubbledialog.show();
                        Timer();
                        txtWordOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //Log.d("wordone", listImageGame.get(0).getwEng());
                                if(listImageGame.get(1).getwEng().equals(txtWordOne.getText().toString())){
                                    getResult(2);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(1).getwEng().equals(txtWordTwo.getText().toString())){
                                    getResult(2);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(1).getwEng().equals(txtWordThree.getText().toString())){
                                    getResult(2);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        imgQuestionsTwo.setEnabled(false);
                        imgBubbleTwo.setVisibility(View.INVISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP:

                        return true;
                }
                return false;
            }
        });
        imgQuestionsThree.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgBubbleThree.setSelected(!imgQuestionsThree.isSelected());
                        imgQuestionsThree.isSelected();
                        imgQuestionsThree.setEnabled(false);
                        BubbleDialog();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(listImageGame.get(2).getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        Collections.shuffle(arr);
                        setTextDialog(arr);
                        bubbledialog.show();
                        Timer();
                        txtWordOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(2).getwEng().equals(txtWordOne.getText().toString())){
                                    getResult(3);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(2).getwEng().equals(txtWordTwo.getText().toString())){
                                    getResult(3);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(2).getwEng().equals(txtWordThree.getText().toString())){
                                    getResult(3);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        imgQuestionsThree.setEnabled(false);
                        imgBubbleThree.setVisibility(View.INVISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                }
                return false;
            }
        });
        imgQuestionsFour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgBubbleFour.setSelected(!imgQuestionsFour.isSelected());
                        imgQuestionsFour.isSelected();
                        imgQuestionsFour.setEnabled(false);
                        BubbleDialog();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(listImageGame.get(3).getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        Collections.shuffle(arr);
                        setTextDialog(arr);
                        bubbledialog.show();
                        Timer();
                        txtWordOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(3).getwEng().equals(txtWordOne.getText().toString())){
                                    getResult(4);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(3).getwEng().equals(txtWordTwo.getText().toString())){
                                    getResult(4);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(3).getwEng().equals(txtWordThree.getText().toString())){
                                    getResult(4);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        imgQuestionsFour.setEnabled(false);
                        imgBubbleFour.setVisibility(View.INVISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                }
                return false;
            }
        });
        imgQuestionsFive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgBubbleFive.setSelected(!imgQuestionsFive.isSelected());
                        imgQuestionsFive.isSelected();
                        imgQuestionsFive.setEnabled(false);
                        imgQuestionsFive.setEnabled(false);
                        BubbleDialog();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(listImageGame.get(4).getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        Collections.shuffle(arr);
                        setTextDialog(arr);
                        bubbledialog.show();
                        Timer();
                        txtWordOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(4).getwEng().equals(txtWordOne.getText().toString())){
                                    getResult(5);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(4).getwEng().equals(txtWordTwo.getText().toString())){
                                    getResult(5);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(4).getwEng().equals(txtWordThree.getText().toString())){
                                    getResult(5);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        imgQuestionsFive.setEnabled(false);
                        imgBubbleFive.setVisibility(View.INVISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                }
                return false;
            }
        });
        imgQuestionsSix.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgBubbleSix.setSelected(!imgQuestionsSix.isSelected());
                        imgQuestionsSix.isSelected();
                        imgQuestionsSix.setEnabled(false);
                        BubbleDialog();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
                        ArrayList<String> arr = new ArrayList<String>();
                        arr.add(listImageGame.get(5).getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        arr.add(getIntRandomInList().getwEng());
                        Collections.shuffle(arr);
                        setTextDialog(arr);
                        bubbledialog.show();
                        Timer();
                        txtWordOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(5).getwEng().equals(txtWordOne.getText().toString())){
                                    getResult(6);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordTwo.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(5).getwEng().equals(txtWordTwo.getText().toString())){
                                    getResult(6);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        txtWordThree.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(5).getwEng().equals(txtWordThree.getText().toString())){
                                    getResult(6);
                                    mCorrect.start();
                                    timer.cancel();
                                    txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
                                    Timer();
                                    timer.cancel();
                                    bubbledialog.dismiss();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    ktcheck_finish ();
                                    if(check_finish == 6) {
                                        EventWin();
                                        mWin.start();
                                        dialogWinGame.show();
                                    }
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
                                    edtPlayerNameGameOver.setVisibility(View.VISIBLE);
                                    txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
                                    timer.cancel();
                                    effectGameOver();
                                    dialogGameOver.show();
                                }
                            }
                        });
                        imgQuestionsSix.setEnabled(false);
                        imgBubbleSix.setVisibility(View.INVISIBLE);
                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                }
                return false;
            }
        });
    }

    private void EventOver() {
        txtScoreGameOver.setText(String.valueOf(SCORE_ALL));
        imgListOver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgListOver.setSelected(!imgListOver.isSelected());
                        imgListOver.isSelected();
                        imgReplayOver.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgListOver.setSelected(false);
                        imgReplayOver.setEnabled(true);
                        doSaveScore();
                        Intent intent = new Intent(BubbleHittingActivity.this, LoadingGoOutGameActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
        imgReplayOver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgReplayOver.setSelected(!imgReplayOver.isSelected());
                        imgReplayOver.isSelected();
                        //mService.playMusic(mClick);
                        imgListOver.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        //mService.playMusic(mClick);
                        imgReplayOver.setSelected(false);
                        imgListOver.setEnabled(true);
                        doSaveScore();
                        SCORE_ALL = 0;
                        Intent intent = new Intent(BubbleHittingActivity.this,BubbleHittingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
    private void EventWin() {
        txtScoreWin.setText(String.valueOf(SCORE_ALL));
        imgNextGameWin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgNextGameWin.setSelected(!imgNextGameWin.isSelected());
                        imgNextGameWin.isSelected();
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgNextGameWin.setSelected(false);
                        Intent intent = new Intent(BubbleHittingActivity.this,RandomGamePracticeActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable(ConfigApplication.NAME_DATA_LIST,listImageGame);
                        intent.putExtras(data);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
    private void doSaveScore() {
        if (SCORE_ALL > 0) {
            String playerName = edtPlayerNameGameOver.getText().toString();
            if (playerName.equals(""))
                playerName = "Unknown Player";
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsPlayer(playerName);
            scoreObject.setsScore(SCORE_ALL);
            dbAccessHelper.doInsertScore(scoreObject);
            edtPlayerNameGameOver.setText("");
        }
    }
    private void ktcheck_finish() {
        if (check_finish == 6){
            check_finish = 0;
        }
        else{
            check_finish++;
        }
    }
    private  void getResult(int choose){
        if (choose == 1) {
            SCORE_ONE += 10 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 2) {
            SCORE_TWO += 10 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 3) {
            SCORE_THREE +=  10 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 4) {
            SCORE_FOUR +=  10 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 5) {
            SCORE_FIVE +=  10 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 6) {
            SCORE_SIX +=  10 * Integer.parseInt(txtTime.getText().toString());
        }
        SCORE_ALL = SCORE_ONE + SCORE_TWO + SCORE_THREE + SCORE_FOUR + SCORE_FIVE + SCORE_SIX;
        txtScore.setText(String.valueOf(SCORE_ALL));

    }
}
