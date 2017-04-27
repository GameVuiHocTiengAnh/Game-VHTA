package blackcore.tdc.edu.com.gamevhta.picture_puzzle_game;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.LoadingGoOutGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGameMemoryChallengeActivity;
import blackcore.tdc.edu.com.gamevhta.button.PauseButton;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.BackgroudGameView;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;

public class PicturePuzzleActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    TextView txtTime,txtScore,txtAnswerOne,txtAnswerTwo,txtAnswerThree,txtAnswerFour,txtAnswerFive,txtAnswerSix,lblScoreGameOver,txtNameScoreWin,txtScoreWin,txtNamePlayerOver,
            txtNamePlayerWin,txtNameWin,lblPlayerNameGameOver,lblPlayerNameScoreOver;

    private Dialog dialogOver,dialogWin;
    private ArrayList<WordObject> listImageGame = null;
    private ArrayList<WordObject> listImageData = null;
    private ArrayList<Bitmap> listImage;
    private ArrayList<String> listText;

    final int color = Color.parseColor("#FFFFFF");
    private boolean flagVoice = true, flagWin = true;
    private MediaPlayer mCorrect = null,mWrong=null,mClick=null,mTickTac=null,mWin=null,mMusicMainGame=null,mOver=null,mClickGame=null,mCartoonImage=null;
    PauseButton imgBackGame;
    private Animation scale,zoom_in;
    private ImageView imgListOver, imgReplayOver;
    private Handler handler;
    private Timer timer;
    private TextToSpeech textToSpeech = null;
    private int SCORE_ONE = 0, SCORE_TWO = 0, SCORE_THREE = 0, SCORE_FOUR = 0, SCORE_FIVE = 0, SCORE_SIX = 0, SCORE_ALL = 0;
    private int timesDrop = 0;
    private DbAccessHelper dbAccessHelper;
    private String OBJECT = "";
    private String topicBackground;
    ImageView imbAnimalOne,imbAnimalTwo,imbAnimalThree,imbAnimalFour,imbAnimalFive,imbAnimalSix,imbNextGameWin;
    ImageView imbAnimalQuestionOne,imbAnimalQuestionTwo,imbAnimalQuestionThree,imbAnimalQuestionFour,imbAnimalQuestionFive,imbAnimalQuestionSix;
    private LinearLayout background;

    private String newName = null;
    private int lvPass;
    private String oldName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        lvPass = ConfigApplication.LV_PASS;
        newName = ConfigApplication.NEW_NAME;
        oldName = ConfigApplication.OLD_NAME;
        if(newName=="" || newName == null){
            oldName = ConfigApplication.OLD_NAME;
            if(!(oldName=="")|| oldName != null){
                newName = oldName;
            }
        }
       DbAccessHelper db = new DbAccessHelper(this);
        if (db != null && newName != null) {
            listImageGame = db.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC, lvPass+1); // when new player data is lv1 and lv2
            ArrayList<WordObject> lv2 = db.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC, lvPass+2);
            listImageGame.addAll(lv2);
            Log.d("Tagtest", ConfigApplication.CURRENT_CHOOSE_TOPIC);
        }
//        listImageGame = new ArrayList<>();
        listImageData = new ArrayList<>();

        textToSpeech = new TextToSpeech(this,this);
        imgBackGame = (PauseButton) findViewById(R.id.btnBackGame);
        imgBackGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
            }
        });
        imgBackGame.setScreenUse("PUZZLE_GAME",null,this);

        mCorrect = MediaPlayer.create(PicturePuzzleActivity.this,R.raw.dung_market_game);
        mWrong = MediaPlayer.create(PicturePuzzleActivity.this,R.raw.sai);
        mOver = MediaPlayer.create(PicturePuzzleActivity.this,R.raw.wrong_market_game);
        mClickGame = MediaPlayer.create(PicturePuzzleActivity.this, R.raw.click_market_game);
        mClick = MediaPlayer.create(PicturePuzzleActivity.this, R.raw.click);
        mTickTac = MediaPlayer.create(PicturePuzzleActivity.this, R.raw.time);
        mWin = MediaPlayer.create(PicturePuzzleActivity.this, R.raw.wingame);
        mMusicMainGame = MediaPlayer.create(PicturePuzzleActivity.this, R.raw.picturepuzzle);
        mCartoonImage = MediaPlayer.create(PicturePuzzleActivity.this, R.raw.cartoondown);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtAnswerOne = (TextView) findViewById(R.id.txtAnswerOne);
        txtAnswerTwo = (TextView) findViewById(R.id.txtAnswerTwo);
        txtAnswerThree = (TextView) findViewById(R.id.txtAnswerThree);
        txtAnswerFour = (TextView) findViewById(R.id.txtAnswerFour);
        txtAnswerFive = (TextView) findViewById(R.id.txtAnswerFive);
        txtAnswerSix = (TextView) findViewById(R.id.txtAnswerSix);

        zoom_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.takingoffanimator);
        scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim_trieu);

        imbAnimalOne = (ImageView) findViewById(R.id.imbAnimalone);
        imbAnimalTwo = (ImageView) findViewById(R.id.imbAnimaltwo);
        imbAnimalThree = (ImageView) findViewById(R.id.imbAnimalthree);
        imbAnimalFour = (ImageView) findViewById(R.id.imbAnimlafour);
        imbAnimalFive = (ImageView) findViewById(R.id.imbAnimalfive);
        imbAnimalSix = (ImageView) findViewById(R.id.imbAnimalsix);

        imbAnimalQuestionOne = (ImageView) findViewById(R.id.imbAnimalquestionone);
        imbAnimalQuestionTwo = (ImageView) findViewById(R.id.imbAnimalquestiontwo);
        imbAnimalQuestionThree = (ImageView) findViewById(R.id.imbAnimalquestionthree);
        imbAnimalQuestionFour = (ImageView) findViewById(R.id.imbAnimalquestionfour);
        imbAnimalQuestionFive = (ImageView) findViewById(R.id.imbAnimalquestionfive);
        imbAnimalQuestionSix = (ImageView) findViewById(R.id.imbAnimalquestionsix);

        imbAnimalOne.setOnTouchListener(touchListener);
        imbAnimalTwo.setOnTouchListener(touchListener);
        imbAnimalThree.setOnTouchListener(touchListener);
        imbAnimalFour.setOnTouchListener(touchListener);
        imbAnimalFive.setOnTouchListener(touchListener);
        imbAnimalSix.setOnTouchListener(touchListener);

        imbAnimalQuestionOne.setColorFilter(color);
        imbAnimalQuestionTwo.setColorFilter(color);
        imbAnimalQuestionThree.setColorFilter(color);
        imbAnimalQuestionFour.setColorFilter(color);
        imbAnimalQuestionFive.setColorFilter(color);
        imbAnimalQuestionSix.setColorFilter(color);

        imbAnimalQuestionOne.setOnDragListener(dragListener);
        imbAnimalQuestionTwo.setOnDragListener(dragListener);
        imbAnimalQuestionThree.setOnDragListener(dragListener);
        imbAnimalQuestionFour.setOnDragListener(dragListener);
        imbAnimalQuestionFive.setOnDragListener(dragListener);
        imbAnimalQuestionSix.setOnDragListener(dragListener);


        //database
        dbAccessHelper = new DbAccessHelper(this);

        //dialog overgame
        dialogOver = new Dialog(PicturePuzzleActivity.this);
        dialogOver.setCancelable(false);
        dialogOver.setCanceledOnTouchOutside(false);
        dialogOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOver.setContentView(R.layout.activity_dialog_game_over);
        dialogOver.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogOver.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        imgListOver = (ImageView) dialogOver.findViewById(R.id.imgListOver);
        imgReplayOver = (ImageView) dialogOver.findViewById(R.id.imgReplayOver);
        lblPlayerNameScoreOver = (TextView) dialogOver.findViewById(R.id.lblPlayerNameGameOver);
        lblPlayerNameGameOver = (TextView) dialogOver.findViewById(R.id.lblPlayerNameGOver);
        lblScoreGameOver = (TextView) dialogOver.findViewById(R.id.lblScoreGameOver);
        txtNamePlayerOver = (TextView) dialogOver.findViewById(R.id.txtNamePlayer);


        //dialog win
        dialogWin = new Dialog(PicturePuzzleActivity.this);
        dialogWin.setCancelable(false);
        dialogWin.setCanceledOnTouchOutside(false);
        dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWin.setContentView(R.layout.activity_dialog_win_game);
        dialogWin.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        imbNextGameWin = (ImageView) dialogWin.findViewById(R.id.imvNextGame);
        txtNameScoreWin = (TextView) dialogWin.findViewById(R.id.txtNameScoreWin);
        txtScoreWin = (TextView) dialogWin.findViewById(R.id.txtScoreWin);
        txtNamePlayerWin = (TextView) dialogWin.findViewById(R.id.txtNamePlayer);
        txtNameWin = (TextView) dialogWin.findViewById(R.id.txtName);

        if(newName != null){
            lblPlayerNameGameOver.setText(newName);
            txtNameWin.setText(newName);
        }

        mMusicMainGame.start();
        mMusicMainGame.setLooping(true);
        mMusicMainGame.setVolume(0.5f,0.5f);
        mCartoonImage.start();
        mCartoonImage.setVolume(1f,1f);
        addDataList();
        randomBackground();
        randomImage();
        moveActivity();
        setFont();
        Text();
        effectText();
        effectImageAnswer();
        effectImageQuestion();
        Answer();
        Question();

        txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
        Timer();

    }

    public void randomBackground(){
        topicBackground = ConfigApplication.CURRENT_CHOOSE_TOPIC;
        if (topicBackground.equals(ConfigApplication.OBJECT_ANIMALS))
        {
            background = (LinearLayout) findViewById(R.id.scene);
            Resources res = getResources();
            TypedArray topicSchool = res.obtainTypedArray(R.array.topicAnimal);
            Random random = new Random();
            int r = random.nextInt(topicSchool.length());
            int i = topicSchool.getResourceId(r, -1);
            background.setBackgroundResource(i);

        }else if (topicBackground.equals(ConfigApplication.OBJECT_SCHOOL))
        {
            background = (LinearLayout) findViewById(R.id.scene);
            Resources res = getResources();
            TypedArray topicAnimal = res.obtainTypedArray(R.array.topicSchool);
            Random random = new Random();
            int r = random.nextInt(topicAnimal.length());
            int i = topicAnimal.getResourceId(r, -1);
            background.setBackgroundResource(i);

        }else if (topicBackground.equals(ConfigApplication.OBJECT_COUNTRY_SIDE))
        {
            background = (LinearLayout) findViewById(R.id.scene);
            Resources res = getResources();
            TypedArray topicFarm = res.obtainTypedArray(R.array.topicFarm);
            Random random = new Random();
            int r = random.nextInt(topicFarm.length());
            int i = topicFarm.getResourceId(r, -1);
            background.setBackgroundResource(i);

        }else if (topicBackground.equals(ConfigApplication.OBJECT_HOUSEHOLD_APPLIANCES))
        {
            background = (LinearLayout) findViewById(R.id.scene);
            Resources res = getResources();
            TypedArray topicHome = res.obtainTypedArray(R.array.topicHome);
            Random random = new Random();
            int r = random.nextInt(topicHome.length());
            int i = topicHome.getResourceId(r, -1);
            background.setBackgroundResource(i);

        }else if (topicBackground.equals(ConfigApplication.OBJECT_PLANTS))
        {
            background = (LinearLayout) findViewById(R.id.scene);
            Resources res = getResources();
            TypedArray topicPlant = res.obtainTypedArray(R.array.topicPlant);
            Random random = new Random();
            int r = random.nextInt(topicPlant.length());
            int i = topicPlant.getResourceId(r, -1);
            background.setBackgroundResource(i);
        }
    }

    private void addDataList() {
        listImageData = new ArrayList<>();
        if(newName != null){
            listImageData  = dbAccessHelper.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC, lvPass+1); // when new player data is lv1 and lv2
            ArrayList<WordObject> lv2 = dbAccessHelper.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC, lvPass+2);
            listImageData.addAll(lv2);
        }

    }

    public void randomImage(){
        listImageGame = new ArrayList<>();
        Random random = new Random();
        for(int i = 0 ; i < 6 ; i++){
            int x = listImageData.size()-1;
            int n = random.nextInt(x);
            listImageGame.add(listImageData.get(n));
            listImageData.remove(n);
        }


    }

    //Add image from resource to ImageButton
    private Drawable getBitmapResource(String name) {
        int id = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        Log.d("ImageID", String.valueOf(id));
        Drawable dr;
        if(id != 0)
            dr =getApplicationContext().getResources().getDrawable(id);
        else
            dr =getApplicationContext().getResources().getDrawable(R.drawable.animal_hamster);
        return  dr;
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
                    txtTime.startAnimation(zoom_in);
                } else if (t == 0) {
                    timer.cancel();
                    try {
                        mOver.start();
                        effectWin();
                        EventOver();
                        dialogOver.show();
                    }catch (Exception e){
                        Log.d("s","s");
                    }
                }
            }
        };

        timer = new Timer();

        TimerTask timerTask = new TimerTask() {
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
        };
        timer.schedule(timerTask,1000, 1000);
    }

    private void Text(){
        txtAnswerOne.setText(listImageGame.get(0).getwEng());
        txtAnswerTwo.setText(listImageGame.get(1).getwEng());
        txtAnswerThree.setText(listImageGame.get(2).getwEng());
        txtAnswerFour.setText(listImageGame.get(3).getwEng());
        txtAnswerFive.setText(listImageGame.get(4).getwEng());
        txtAnswerSix.setText(listImageGame.get(5).getwEng());
    }

    private void Answer(){
        imbAnimalOne.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
        imbAnimalTwo.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
        imbAnimalThree.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
        imbAnimalFour.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
        imbAnimalFive.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
        imbAnimalSix.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
    }
    private void Question(){
        imbAnimalQuestionOne.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
        imbAnimalQuestionTwo.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
        imbAnimalQuestionThree.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
        imbAnimalQuestionFour.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
        imbAnimalQuestionFive.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
        imbAnimalQuestionSix.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
    }
    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), getResources().getString(R.string.fontPath));
        txtTime.setTypeface(custom_font);
        txtScore.setTypeface(custom_font);
        txtAnswerOne.setTypeface(custom_font);
        txtAnswerTwo.setTypeface(custom_font);
        txtAnswerThree.setTypeface(custom_font);
        txtAnswerFour.setTypeface(custom_font);
        txtAnswerFive.setTypeface(custom_font);
        txtAnswerSix.setTypeface(custom_font);
        lblScoreGameOver.setTypeface(custom_font);
        lblPlayerNameGameOver.setTypeface(custom_font);
        txtNameScoreWin.setTypeface(custom_font);
        txtScoreWin.setTypeface(custom_font);
        txtNamePlayerWin.setTypeface(custom_font);
        txtNameWin.setTypeface(custom_font);
        txtNamePlayerOver.setTypeface(custom_font);
        lblPlayerNameScoreOver.setTypeface(custom_font);

    }

    public void moveActivity() {
        Intent intent = new Intent(getApplicationContext(),LoadingGoOutGameActivity.class);
        imgBackGame.setMoveActivity(intent,this);

    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        return;
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        timer.cancel();
        try {
            mTickTac.pause();
            mMusicMainGame.pause();
        }catch (Exception e) {
            Log.d("a","a");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        imgBackGame.callOnClick();
        mMusicMainGame.start();
        mMusicMainGame.setLooping(true);
        timer.cancel();
        Timer();
        mTickTac.start();
    }

    protected void onResume(){
        // TODO Auto-generated method stub
        super.onResume();
        mMusicMainGame.start();
        mMusicMainGame.setLooping(true);
        timer.cancel();
        Timer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();

        }
        if (mMusicMainGame != null) {
            if (mMusicMainGame.isPlaying()) {
                mMusicMainGame.stop();
                mMusicMainGame.release();
            } else
                mMusicMainGame.release();
        }

        try {
            mCartoonImage.stop();
            mCartoonImage.release();
            mWin.stop();
            mWin.release();
            mCorrect.stop();
            mCorrect.release();
            mClickGame.stop();
            mClickGame.release();
            mTickTac.stop();
            mTickTac.release();
            mOver.stop();
            mOver.release();
        }catch (Exception e) {
            Log.d("a","a");
        }

    }

    public void effectText(){
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerOne) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerTwo) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerThree) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFour) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFive) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerSix) , AnimationType.DropOut , 2000 , 0 , true , 100 );
    }

    public void effectImageAnswer(){
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalone) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimaltwo) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalthree) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimlafour) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalfive) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalsix) , AnimationType.DropOut , 2000 , 0 , true , 100 );
    }

    public void effectImageQuestion(){
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionone) , AnimationType.StandUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestiontwo) , AnimationType.StandUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionthree) , AnimationType.StandUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionfour) , AnimationType.StandUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionfive) , AnimationType.StandUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionsix) , AnimationType.StandUp , 2000 , 0 , true , 300 );
    }

    public void effectWin(){

        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalone) , AnimationType.SlideOutDown , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimaltwo) , AnimationType.SlideOutDown , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalthree) , AnimationType.SlideOutDown , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimlafour) , AnimationType.SlideOutDown , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalfive) , AnimationType.SlideOutDown , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalsix) , AnimationType.SlideOutDown , 2000 , 0 , true , 300 );

        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionone) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestiontwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionthree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionfour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionfive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionsix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );

        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerOne) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerTwo) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerThree) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFour) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFive) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerSix) , AnimationType.ZoomOutUp , 2000 , 0 , true , 300 );
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mClickGame.start();
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                    v.startAnimation(scale);
                    v.startDrag(data, myShadowBuilder, v, 0);
                    return true;
                case MotionEvent.ACTION_UP:
                    v.clearAnimation();
                    mClickGame.stop();
                    mClickGame.release();
                    return true;
            }
            return false;
        }
    };

    public void Voice(){
        if(flagVoice == true) {
            mCorrect.start();
        }else if(flagVoice == false){
            mWrong.start();
        }
    }

        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                int dragEvent = event.getAction();
                final View v = (View) event.getLocalState();
                switch (dragEvent) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        break;
                    case DragEvent.ACTION_DROP:
                        if (v != null) {
                            v.clearAnimation();
                        }
                        if(v.getId() == R.id.imbAnimalone && view.getId() == R.id.imbAnimalquestionone){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionone) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionOne.clearColorFilter();
                            imbAnimalQuestionOne.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
                            getResult(1);
                            flagVoice = true;
                            Voice();
                            mCorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerOne) , AnimationType.Tada , 2000 , 0 , true , 300 );
                                    textToSpeech.speak(listImageGame.get(0).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                            imbAnimalOne.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimaltwo && view.getId() == R.id.imbAnimalquestiontwo){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestiontwo) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionTwo.clearColorFilter();
                            imbAnimalQuestionTwo.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
                            getResult(2);
                            flagVoice = true;
                            Voice();
                            mCorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerTwo) , AnimationType.Tada , 2000 , 0 , true , 300 );
                                    textToSpeech.speak(listImageGame.get(1).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                            imbAnimalTwo.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalthree && view.getId() == R.id.imbAnimalquestionthree){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionthree) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionThree.clearColorFilter();
                            imbAnimalQuestionThree.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
                            getResult(3);
                            flagVoice = true;
                            Voice();
                            mCorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerThree) , AnimationType.Tada , 2000 , 0 , true , 300 );
                                    textToSpeech.speak(listImageGame.get(2).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                            imbAnimalThree.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimlafour && view.getId() == R.id.imbAnimalquestionfour){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionfour) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionFour.clearColorFilter();
                            imbAnimalQuestionFour.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
                            getResult(4);
                            flagVoice = true;
                            Voice();
                            mCorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFour) , AnimationType.Tada , 2000 , 0 , true , 300 );
                                    textToSpeech.speak(listImageGame.get(3).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                            imbAnimalFour.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalfive && view.getId() == R.id.imbAnimalquestionfive){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionfive) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionFive.clearColorFilter();
                            imbAnimalQuestionFive.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
                            getResult(5);
                            flagVoice = true;
                            Voice();
                            mCorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFive) , AnimationType.Tada , 2000 , 0 , true , 300 );
                                    textToSpeech.speak(listImageGame.get(4).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                            imbAnimalFive.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalsix && view.getId() == R.id.imbAnimalquestionsix){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionsix) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionSix.clearColorFilter();
                            imbAnimalQuestionSix.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
                            getResult(6);
                            flagVoice = true;
                            Voice();
                            mCorrect.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mediaPlayer) {
                                    StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerSix) , AnimationType.Tada , 2000 , 0 , true , 300 );
                                    textToSpeech.speak(listImageGame.get(5).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                                }
                            });
                            imbAnimalSix.setVisibility(View.INVISIBLE);
                        }else
                            flagVoice = false;
                            Voice();
                            break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        if (v != null) {
                            v.clearAnimation();
                        }
                        if(timesDrop == 6)
                        {
                            effectWin();
                            timer.cancel();
                            mWin.start();
                            EventWin();
                            dialogWin.show();
                        }
                }
                return true;
            }
        };

        private void EventOver(){

            lblScoreGameOver.setText(String.valueOf(SCORE_ALL));

            imgListOver.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mClick.start();
                            imgListOver.setSelected(!imgListOver.isSelected());
                            imgListOver.isSelected();
                            imgReplayOver.setEnabled(false);
                            return true;
                        case MotionEvent.ACTION_UP:
                            mClick.start();
                            imgListOver.setSelected(false);
                            imgReplayOver.setEnabled(true);
                            doSaveScore();
                            SCORE_ALL = 0;
                            Intent intent = new Intent(PicturePuzzleActivity.this, LoadingGoOutGameActivity.class);
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
                            mClick.start();
                            imgListOver.setEnabled(false);
                            return true;
                        case MotionEvent.ACTION_UP:
                            mClick.start();
                            imgReplayOver.setSelected(false);
                            imgListOver.setEnabled(true);
                            SCORE_ALL = 0;
                            Intent intent = new Intent(PicturePuzzleActivity.this,PicturePuzzleActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                            startActivity(intent);
                            finish();
                            return true;
                    }
                    return false;
                }
            });
        }
        private void EventWin()
        {
            txtScoreWin.setText(String.valueOf(SCORE_ALL));

            imbNextGameWin.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {

                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mClick.start();
                            imbNextGameWin.setSelected(!imbNextGameWin.isSelected());
                            imbNextGameWin.isSelected();
                            return true;
                        case MotionEvent.ACTION_UP:
                            mClick.start();
                            imbNextGameWin.setSelected(false);
                            Intent intent = new Intent(PicturePuzzleActivity.this,RandomGameMemoryChallengeActivity.class);
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

    private void timesDrop() {
        if(timesDrop == 6)
        {
            timesDrop = 0;
        }
        else
        {
            timesDrop++;
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

        public void startCountingTime(){
            if(timer != null)
                timer.cancel();
            Timer();
        }
    private void doSaveScore() {
        if (SCORE_ALL > 0) {
            String playerName = lblPlayerNameGameOver.getText().toString();
            if (playerName.equals(""))
                playerName = "Unknown Player";
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsPlayer(playerName);
            scoreObject.setsScore(SCORE_ALL);
            DbAccessHelper db = new DbAccessHelper(this);
            db.doInsertScore(scoreObject);
            lblPlayerNameGameOver.setText("");
        }
    }
}
