package blackcore.tdc.edu.com.gamevhta.picture_puzzle_game;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.LoadingGoInGameActivity;
import blackcore.tdc.edu.com.gamevhta.LoadingGoOutGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGameMemoryChallengeActivity;
import blackcore.tdc.edu.com.gamevhta.button.PauseButton;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.CatchingWordsActivity;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class PicturePuzzleActivity extends AppCompatActivity {

    TextView txtTime,txtScore,txtAnswerOne,txtAnswerTwo,txtAnswerThree,txtAnswerFour,txtAnswerFive,txtAnswerSix,lblScoreGameOver,txtNameScoreWin,txtScoreWin;
    EditText lblPlayerNameGameOver;

    private Dialog dialogOver,dialogWin;
    private ArrayList<WordObject> listImageGame = null;
    private ArrayList<WordObject> listImageData = null;
    private ArrayList<Bitmap> listImage;
    private ArrayList<String> listText;

    final int color = Color.parseColor("#FFFFFF");
    private boolean flagVoice = true, flagWin = true;
    private MediaPlayer mCorrect,mWrong,mClick,mTickTac,mWin,mMusicMainGame,mOver,mClickGame,mCartoonImage;
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
    private Animation scale,zoom_in;
    private ImageView imgListOver, imgReplayOver;
    private Handler handler;
    private Timer timer;
    private int SCORE_ONE = 0, SCORE_TWO = 0, SCORE_THREE = 0, SCORE_FOUR = 0, SCORE_FIVE = 0, SCORE_SIX = 0, SCORE_ALL = 0;
    private int timesDrop = 0;
    private DbAccessHelper dbAccessHelper;
    private String OBJECT = "";

    ImageView imbAnimalOne,imbAnimalTwo,imbAnimalThree,imbAnimalFour,imbAnimalFive,imbAnimalSix,imbNextGameWin;
    ImageView imbAnimalQuestionOne,imbAnimalQuestionTwo,imbAnimalQuestionThree,imbAnimalQuestionFour,imbAnimalQuestionFive,imbAnimalQuestionSix;
    private LinearLayout background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        listImageGame = new ArrayList<>();
        listImageData = new ArrayList<>();

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

        zoom_in = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
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
        lblPlayerNameGameOver = (EditText) dialogOver.findViewById(R.id.lblPlayerNameGameOver);
        lblScoreGameOver = (TextView) dialogOver.findViewById(R.id.lblScoreGameOver);

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

        //get Object selected at screen topic
        if (getIntent().getExtras() != null) {
            OBJECT = getIntent().getStringExtra(ConfigApplication.OBJECT_SELECTED);
            //listImageFromData = dbWordHelper.getWordObject(OBJECT, 30);
        }

        mService.playMusic(mMusicMainGame);
        mMusicMainGame.setLooping(true);
        mMusicMainGame.setVolume(0.5f,0.5f);
        mService.playMusic(mCartoonImage);
        mCartoonImage.setVolume(1f,1f);
        randomBackgroundGame();
        addDataList();
        randomImage();
        moveActivity();
        setFont();
        Text();
        effectText();
        effectImageAnswer();
        effectImageQuestion();
        Answer();
        Question();
        Timer();
    }
    public void randomBackgroundGame(){
        background = (LinearLayout) findViewById(R.id.scene);
        Resources res = getResources();
        TypedArray myImages = res.obtainTypedArray(R.array.myImages);
        Random random = new Random();
        int r = random.nextInt(myImages.length());
        int i = myImages.getResourceId(r, -1);
        background.setBackgroundResource(i);
    }

    //List Image was loaded from database
    private void addDataList() {
        listImageData = new ArrayList<>();
        listImageData = dbAccessHelper.getWordObject(ConfigApplication.OBJECT_ANIMALS);
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
            dr =getApplicationContext().getResources().getDrawable(R.drawable.screen_5_dv);
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
                    mService.playMusic(mTickTac);
                    mTickTac.setLooping(true);

                } else if (t == 0) {
                    timer.cancel();
                    mService.stopMusic(mTickTac);
                    mService.playMusic(mOver);
                    effectWin();
                    EventOver();
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
        mService.pauseMusic(mTickTac);
        mService.pauseMusic(mMusicMainGame);
        super.onPause();
    }

    protected void onResume(){
        // TODO Auto-generated method stub
        mService.playMusic(mMusicMainGame);
        super.onResume();
    }

    public void effectText(){
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerOne) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerTwo) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerThree) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFour) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFive) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerSix) , AnimationType.DropOut , 2000 , 0 , true , 300 );
    }

    public void effectImageAnswer(){
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalone) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimaltwo) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalthree) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimlafour) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalfive) , AnimationType.DropOut , 2000 , 0 , true , 300 );
        StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalsix) , AnimationType.DropOut , 2000 , 0 , true , 300 );
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
                    mService.playMusic(mClickGame);
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                    v.startAnimation(scale);
                    v.startDrag(data, myShadowBuilder, v, 0);
                    return true;
                case MotionEvent.ACTION_UP:
                    v.clearAnimation();
                    mService.stopMusic(mClickGame);
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
                        if (v != null) {
                            v.clearAnimation();
                        }
                        if(v.getId() == R.id.imbAnimalone && view.getId() == R.id.imbAnimalquestionone){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerOne) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionone) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionOne.clearColorFilter();
                            imbAnimalQuestionOne.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
                            getResult(1);
                            flagVoice = true;
                            Voice();
                            imbAnimalOne.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimaltwo && view.getId() == R.id.imbAnimalquestiontwo){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerTwo) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestiontwo) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionTwo.clearColorFilter();
                            imbAnimalQuestionTwo.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
                            getResult(2);
                            flagVoice = true;
                            Voice();
                            imbAnimalTwo.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalthree && view.getId() == R.id.imbAnimalquestionthree){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerThree) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionthree) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionThree.clearColorFilter();
                            imbAnimalQuestionThree.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
                            getResult(3);
                            flagVoice = true;
                            Voice();
                            imbAnimalThree.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimlafour && view.getId() == R.id.imbAnimalquestionfour){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFour) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionfour) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionFour.clearColorFilter();
                            imbAnimalQuestionFour.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
                            getResult(4);
                            flagVoice = true;
                            Voice();
                            imbAnimalFour.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalfive && view.getId() == R.id.imbAnimalquestionfive){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerFive) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionfive) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionFive.clearColorFilter();
                            imbAnimalQuestionFive.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
                            getResult(5);
                            flagVoice = true;
                            Voice();
                            imbAnimalFive.setVisibility(View.INVISIBLE);
                        }else if(v.getId() == R.id.imbAnimalsix && view.getId() == R.id.imbAnimalquestionsix){
                            timesDrop();
                            StartSmartAnimation.startAnimation( findViewById(R.id.txtAnswerSix) , AnimationType.RubberBand , 2000 , 0 , true , 300 );
                            StartSmartAnimation.startAnimation( findViewById(R.id.imbAnimalquestionsix) , AnimationType.Tada , 2000 , 0 , true , 300 );
                            imbAnimalQuestionSix.clearColorFilter();
                            imbAnimalQuestionSix.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
                            getResult(6);
                            flagVoice = true;
                            Voice();
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
                            mService.playMusic(mWin);
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
                dbAccessHelper.doInsertScore(scoreObject);
                lblPlayerNameGameOver.setText("");
                Toast.makeText(getApplicationContext(), "Saving Score", Toast.LENGTH_SHORT).show();
            }
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
            doSaveScore();
        }
}
