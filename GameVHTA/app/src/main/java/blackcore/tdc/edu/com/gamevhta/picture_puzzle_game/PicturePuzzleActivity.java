package blackcore.tdc.edu.com.gamevhta.picture_puzzle_game;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.LoadingGoInGameActivity;
import blackcore.tdc.edu.com.gamevhta.LoadingGoOutGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.button.PauseButton;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.data_models.DbScoreHelper;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class PicturePuzzleActivity extends AppCompatActivity {

    TextView txtTime,txtScore,txtAnswerOne,txtAnswerTwo,txtAnswerThree,txtAnswerFour,txtAnswerFive,txtAnswerSix,lblScoreGameOver;
    EditText lblPlayerNameGameOver;

    private Dialog dialogOver;
    private ArrayList<Bitmap> listBitMapAnswer = null;

    final int color = Color.parseColor("#FFFFFF");
    private boolean flagVoice = true, flagWin = true;

    private MediaPlayer mCorrect,mWrong,mClick,mTickTac;
    private MusicService mService = new MusicService();
    PauseButton imgBackGame;
    private int timeCount;

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
    private Animation animation;
    private ImageView imgListOver, imgReplayOver;
    private Handler handler;
    private Timer timer;
    private int SCORE_ONE = 0, SCORE_TWO = 0, SCORE_THREE = 0, SCORE_FOUR = 0, SCORE_FIVE = 0, SCORE_SIX = 0, SCORE_ALL = 0;
    private DbAccessHelper dbWordHelper;
    private DbScoreHelper dbScoreHelper;
    private String OBJECT = "";

    ImageView imbAnimalOne,imbAnimalTwo,imbAnimalThree,imbAnimalFour,imbAnimalFive,imbAnimalSix;
    ImageView imbAnimalQuestionOne,imbAnimalQuestionTwo,imbAnimalQuestionThree,imbAnimalQuestionFour,imbAnimalQuestionFive,imbAnimalQuestionSix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mCorrect = MediaPlayer.create(PicturePuzzleActivity.this,R.raw.dung);
        mWrong = MediaPlayer.create(PicturePuzzleActivity.this,R.raw.sai);
        mClick = MediaPlayer.create(PicturePuzzleActivity.this, R.raw.click);
        mTickTac = MediaPlayer.create(PicturePuzzleActivity.this, R.raw.time);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtAnswerOne = (TextView) findViewById(R.id.txtAnswerOne);
        txtAnswerTwo = (TextView) findViewById(R.id.txtAnswerTwo);
        txtAnswerThree = (TextView) findViewById(R.id.txtAnswerThree);
        txtAnswerFour = (TextView) findViewById(R.id.txtAnswerFour);
        txtAnswerFive = (TextView) findViewById(R.id.txtAnswerFive);
        txtAnswerSix = (TextView) findViewById(R.id.txtAnswerSix);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

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

        listBitMapAnswer = new ArrayList<>();
        Bitmap cat = BitmapFactory.decodeResource(getResources(),R.drawable.cat_answer);
        Bitmap cow = BitmapFactory.decodeResource(getResources(),R.drawable.cow_answer);
        Bitmap dog = BitmapFactory.decodeResource(getResources(),R.drawable.dog_answer);
        Bitmap buffalo = BitmapFactory.decodeResource(getResources(),R.drawable.buffalo_answer);
        Bitmap rabbit = BitmapFactory.decodeResource(getResources(),R.drawable.bunny_answer);
        Bitmap goat = BitmapFactory.decodeResource(getResources(),R.drawable.goat_answer);
        listBitMapAnswer.add(cat);
        listBitMapAnswer.add(cow);
        listBitMapAnswer.add(dog);
        listBitMapAnswer.add(buffalo);
        listBitMapAnswer.add(rabbit);
        listBitMapAnswer.add(goat);
        Collections.shuffle(listBitMapAnswer);

        //database
        dbWordHelper = new DbAccessHelper(this);
        dbScoreHelper = new DbScoreHelper(this);

        dialogOver = new Dialog(PicturePuzzleActivity.this);
        dialogOver.setCancelable(false);
        dialogOver.setCanceledOnTouchOutside(false);
        dialogOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOver.setContentView(R.layout.activity_dialog_game_over);
        dialogOver.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogOver.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        imgListOver = (ImageView) dialogOver.findViewById(R.id.imgListOver);
        imgReplayOver = (ImageView) dialogOver.findViewById(R.id.imgReplayOver);
        lblPlayerNameGameOver = (EditText) dialogOver.findViewById(R.id.lblPlayerNameGameOver);
        lblScoreGameOver = (TextView) dialogOver.findViewById(R.id.lblScoreGameOver);

        //get Object selected at screen topic
        if (getIntent().getExtras() != null) {
            OBJECT = getIntent().getStringExtra(ConfigApplication.OBJECT_SELECTED);
            //listImageFromData = dbWordHelper.getWordObject(OBJECT, 30);
        }

        moveActivity();
        setFont();
        Answer();
        Question();
        Timer();
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
                    txtTime.startAnimation(animation);
                    mService.playMusic(mTickTac);
                    mTickTac.setLooping(true);

                } else if (t == 0) {
                    timer.cancel();
                    mService.stopMusic(mTickTac);
                    mService.playMusic(mWrong);
                    Event();
                    dialogOver.show();
                }
            }
        };
        txtTime.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
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

    private void Event(){

        lblScoreGameOver.setText(String.valueOf(SCORE_ALL));

        imgListOver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mService.playMusic(mClick);
                        imgListOver.setSelected(!imgListOver.isSelected());
                        imgListOver.isSelected();
                        return true;
                    case MotionEvent.ACTION_UP:
                        mService.playMusic(mClick);
                        imgListOver.setSelected(false);
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
                        mService.playMusic(mClick);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mService.playMusic(mClick);
                        imgReplayOver.setSelected(false);
                        doSaveScore();
                        SCORE_ALL = 0;
                        Intent intent = new Intent(PicturePuzzleActivity.this, LoadingGoInGameActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    private void Answer(){
        imbAnimalOne.setImageBitmap(listBitMapAnswer.get(0));
        imbAnimalTwo.setImageBitmap(listBitMapAnswer.get(1));
        imbAnimalThree.setImageBitmap(listBitMapAnswer.get(2));
        imbAnimalFour.setImageBitmap(listBitMapAnswer.get(3));
        imbAnimalFive.setImageBitmap(listBitMapAnswer.get(4));
        imbAnimalSix.setImageBitmap(listBitMapAnswer.get(5));
    }
    private void Question(){
        imbAnimalQuestionOne.setImageBitmap(listBitMapAnswer.get(0));
        imbAnimalQuestionTwo.setImageBitmap(listBitMapAnswer.get(1));
        imbAnimalQuestionThree.setImageBitmap(listBitMapAnswer.get(2));
        imbAnimalQuestionFour.setImageBitmap(listBitMapAnswer.get(3));
        imbAnimalQuestionFive.setImageBitmap(listBitMapAnswer.get(4));
        imbAnimalQuestionSix.setImageBitmap(listBitMapAnswer.get(5));
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
    }

    public void moveActivity() {
        imgBackGame = (PauseButton) findViewById(R.id.btnBackGame);
        Intent intent = new Intent(getApplicationContext(),LoadingGoOutGameActivity.class);
        imgBackGame.setMoveActivity(intent,this);

    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
    }
    public void onSuperBackPressed(){
        super.onBackPressed();
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        timer.cancel();
        super.onPause();
    }

    protected void onResume(){
        // TODO Auto-generated method stub
        super.onResume();
    }



    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, myShadowBuilder, v, 0);
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
            }
            return false;
        }
    };

    public void Voice(){
        if(flagVoice == true) {
            mService.playMusic(mCorrect);
        }else if(flagVoice == false){
            mService.playMusic(mWrong);
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
                        if(v.getId() == R.id.imbAnimalone && view.getId() == R.id.imbAnimalquestionone){
                            imbAnimalQuestionOne.clearColorFilter();
                            imbAnimalQuestionOne.setImageBitmap(listBitMapAnswer.get(0));
                            getResult(1);
                            flagVoice = true;
                            Voice();
                            imbAnimalOne.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimaltwo && view.getId() == R.id.imbAnimalquestiontwo){
                            imbAnimalQuestionTwo.clearColorFilter();
                            imbAnimalQuestionTwo.setImageBitmap(listBitMapAnswer.get(1));
                            getResult(2);
                            flagVoice = true;
                            Voice();
                            imbAnimalTwo.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalthree && view.getId() == R.id.imbAnimalquestionthree){
                            imbAnimalQuestionThree.clearColorFilter();
                            imbAnimalQuestionThree.setImageBitmap(listBitMapAnswer.get(2));
                            getResult(3);
                            flagVoice = true;
                            Voice();
                            imbAnimalThree.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimlafour && view.getId() == R.id.imbAnimalquestionfour){
                            imbAnimalQuestionFour.clearColorFilter();
                            imbAnimalQuestionFour.setImageBitmap(listBitMapAnswer.get(3));
                            getResult(4);
                            flagVoice = true;
                            Voice();
                            imbAnimalFour.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalfive && view.getId() == R.id.imbAnimalquestionfive){
                            imbAnimalQuestionFive.clearColorFilter();
                            imbAnimalQuestionFive.setImageBitmap(listBitMapAnswer.get(4));
                            getResult(5);
                            flagVoice = true;
                            Voice();
                            imbAnimalFive.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalsix && view.getId() == R.id.imbAnimalquestionsix){
                            imbAnimalQuestionSix.clearColorFilter();

                            imbAnimalQuestionSix.setImageBitmap(listBitMapAnswer.get(5));
                            getResult(6);
                            flagVoice = true;
                            Voice();
                            imbAnimalSix.setVisibility(View.INVISIBLE);
                        }else
                            flagVoice = false;
                            Voice();
                            break;
                }
                return true;
            }
        };

    //Save Score
    private void doSaveScore() {
        if(SCORE_ALL>0) {
            String playerName = lblPlayerNameGameOver.getText().toString();
            if (playerName.equals(""))
                playerName = "Unknown Player";
            else {
                ScoreObject scoreObject = new ScoreObject();
                scoreObject.setsPlayer(playerName);
                scoreObject.setsScore(SCORE_ALL);
                Log.d("ScoreSave", String.valueOf(SCORE_ALL));
                Log.d("ScoreSavePlayer", playerName);
                dbScoreHelper.doInsertScore(scoreObject);
                lblPlayerNameGameOver.setText("");
                Toast.makeText(getApplicationContext(), "Saving Score", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void winGame(int win) {

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
            doSaveScore();
        }
}
