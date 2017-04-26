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
import blackcore.tdc.edu.com.gamevhta.models.PlayerOld;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;

/**
 * Created by Canh on 30/03/2017.
 */

public class BubbleHittingActivity extends AppCompatActivity {

    private Animation mAnimation;
    private ImageView imgQuestionsOne, imgQuestionsTwo, imgQuestionsThree, imgQuestionsFour, imgQuestionsFive, imgQuestionsSix, imgAnimalDialog;
    private ImageView imgBubbleOne, imgBubbleTwo, imgBubbleThree, imgBubbleFour, imgBubbleFive, imgBubbleSix, imgDialogBubbleOne, imgDialogBubbleTwo, imgDialogBubbleThree;
    private ImageView imgListOver, imgReplayOver, imgNextGameWin;

    private TextView txtWordOne, txtWordTwo, txtWordThree, txtTime, txtScore, lblScoreGameOver, txtScoreWin, lblPlayerNameGameOver, txtNamePlayerOver, txtNameOver, txtNameWin;
    private Dialog bubbledialog, dialogGameOver, dialogWinGame;
    private MediaPlayer mMusicMainGame = null, mCorrect = null, mWrong=null, mWin=null;
    private Handler handler;
    private Timer timer;
    private int check_finish = 0;
    private int SCORE_ONE = 0, SCORE_TWO = 0, SCORE_THREE = 0, SCORE_FOUR = 0, SCORE_FIVE = 0, SCORE_SIX = 0, SCORE_ALL = 0;
    private DbAccessHelper dbAccessHelper;
    private ArrayList<WordObject> listImageGame = null;
    private ArrayList<WordObject> listImageData = null;
    private String newName = null;
    PauseButton imgBackGame;
    private int lvPass;
    private String oldName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bubble_hitting_game);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtScore = (TextView) findViewById(R.id.txtScore);

        listImageGame= new ArrayList<>();
        imgQuestionsOne = (ImageView) findViewById(R.id.imgQuestionsOne);
        imgQuestionsTwo = (ImageView) findViewById(R.id.imgQuestionsTwo);
        imgQuestionsThree = (ImageView) findViewById(R.id.imgQuestionsThree);
        imgQuestionsFour = (ImageView) findViewById(R.id.imgQuestionsFour);
        imgQuestionsFive = (ImageView) findViewById(R.id.imgQuestionsFive);
        imgQuestionsSix = (ImageView) findViewById(R.id.imgQuestionsSix);

        newName = ConfigApplication.NEW_NAME;
        lvPass = ConfigApplication.LV_PASS;
        oldName = ConfigApplication.OLD_NAME;
        if(newName=="" || newName == null){
            oldName = ConfigApplication.OLD_NAME;
            if(!(oldName=="")|| oldName != null){
                newName = oldName;
            }
        }
        Intent i = getIntent();
        if(i != null){
            Bundle b = i.getExtras();
            if(b != null){
                listImageGame = (ArrayList<WordObject>) b.getSerializable(ConfigApplication.NAME_DATA_LIST);
                SCORE_ALL += 600;
                txtScore.setText(SCORE_ALL+"");
            }else{
                listImageGame = this.getListUsing();
            }
        }

        dbAccessHelper = new DbAccessHelper(this);
        listImageData= new ArrayList<>();

        imgBubbleOne = (ImageView) findViewById(R.id.imgBubbleOne);
        imgBubbleTwo = (ImageView) findViewById(R.id.imgBubbleTwo);
        imgBubbleThree = (ImageView) findViewById(R.id.imgBubbleThree);
        imgBubbleFour = (ImageView) findViewById(R.id.imgBubbleFour);
        imgBubbleFive = (ImageView) findViewById(R.id.imgBubbleFive);
        imgBubbleSix = (ImageView) findViewById(R.id.imgBubbleSix);

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
        lblScoreGameOver = (TextView) dialogGameOver.findViewById(R.id.lblScoreGameOver);
        txtNameOver = (TextView) dialogGameOver.findViewById(R.id.lblPlayerNameGOver);

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
        txtNameWin = (TextView) dialogWinGame.findViewById(R.id.txtName);

        mMusicMainGame = MediaPlayer.create(BubbleHittingActivity.this, R.raw.picturepuzzle);
        mCorrect = MediaPlayer.create(BubbleHittingActivity.this,R.raw.dung_market_game);
        mWrong = MediaPlayer.create(BubbleHittingActivity.this,R.raw.wrong_market_game);
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
        listImageData = dbAccessHelper.getWordObject(ConfigApplication.CURRENT_CHOOSE_TOPIC);
    }
    private void createDataGame(){
//        listImageGame = new ArrayList<>();
//        Random random = new Random();
//        for (int i = 0; i < 6; i++){
//            int x = listImageData.size();
//            int n = random.nextInt(x);
//            listImageGame.add(listImageData.get(n));
//            listImageData.remove(n);
//        }
    }
    @Override
    public void onBackPressed() {
        imgBackGame.callOnClick();
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
    public void getAnswerWord(){
        mCorrect.start();
        timer.cancel();
        txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
        Timer();
        timer.cancel();
        bubbledialog.dismiss();

        ktcheck_finish ();
        if(check_finish == 6) {
            EventWin();
            mWin.start();
            dialogWinGame.show();
            savelevelPass();
        }
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
                        Timer();
                        txtWordOne.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(listImageGame.get(0).getwEng().equals(txtWordOne.getText().toString())){
                                    getResult(1);
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else{
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                if(listImageGame.get(1).getwEng().equals(txtWordOne.getText().toString())){
                                    getResult(2);
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                  getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
                                    getAnswerWord();
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgQuestionsSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                    StartSmartAnimation.startAnimation( findViewById(R.id.imgBubbleSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
                                } else {
                                    mWrong.start();
                                    bubbledialog.dismiss();
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
        lblScoreGameOver.setText(SCORE_ALL+"");
        txtNameOver.setText(newName);
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
//                        doSaveScore();
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
                        imgListOver.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgReplayOver.setSelected(false);
                        imgListOver.setEnabled(true);
//                        doSaveScore();
//                        SCORE_ALL = 0;
                        txtScore.setText(SCORE_ALL+"");
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
        txtScoreWin.setText(SCORE_ALL+"");
        txtNameWin.setText(newName);
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
                        data.putInt(ConfigApplication.SCORES_BEFOR_GAME,SCORE_ALL);
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
            String playerName = lblPlayerNameGameOver.getText().toString();
            if (playerName.equals(""))
                playerName = "Unknown Player";
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsPlayer(playerName);
            scoreObject.setsScore(SCORE_ALL);
            dbAccessHelper.doInsertScore(scoreObject);
            lblPlayerNameGameOver.setText("");
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
            SCORE_ONE +=  2 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 2) {
            SCORE_TWO +=  2 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 3) {
            SCORE_THREE += 2 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 4) {
            SCORE_FOUR += 2 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 5) {
            SCORE_FIVE += 2 * Integer.parseInt(txtTime.getText().toString());
        }else if(choose == 6) {
            SCORE_SIX +=  2 * Integer.parseInt(txtTime.getText().toString());
        }
        SCORE_ALL += SCORE_ONE + SCORE_TWO + SCORE_THREE + SCORE_FOUR + SCORE_FIVE + SCORE_SIX;
        txtScore.setText(SCORE_ALL+"");

    }
    public ArrayList<WordObject> getListUsing() {
        DbAccessHelper db = new DbAccessHelper(this);
        if (db != null && newName != null) {
            listImageData = db.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC, 1); // when new player data is lv1 and lv2
            ArrayList<WordObject> lv2 = db.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC, 2);
            listImageData.addAll(lv2);
            Log.d("Tagtest", ConfigApplication.CURRENT_CHOOSE_TOPIC);
        }
        return listImageData;
    }
    public void savelevelPass(){
        DbAccessHelper db = new DbAccessHelper(this);
        PlayerOld playerOld = new PlayerOld();
        String tokenLVPass = (lvPass+1)+"_"+ConfigApplication.CURRENT_CHOOSE_TOPIC;
        playerOld.setName(newName);
        playerOld.setLvPass(tokenLVPass);
        db.doInsertPlayerOlde(playerOld);
        db.close();
        ConfigApplication.LV_PASS = lvPass+1;
    }

}
