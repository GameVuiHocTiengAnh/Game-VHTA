package blackcore.tdc.edu.com.gamevhta.image_guessing_game;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.TextView;
import android.widget.Toast;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.LoadingGoInGameActivity;
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
    private Animation animation, animationTimer;
    private Timer timer = new Timer();
    private ImageButton btnImage1, btnImage2, btnImage3, btnImage4, btnImage5, btnImage6, btnPauseGame5;
    private TextView lblTimer, lblWord, lblScore, lblScoreGameOver, txtNameScoreWin, txtScoreWin;
    private MediaPlayer mpClicked, mpSoundBackground, mpWingame, mpGameOver, mpOK;
    private ImageView imgListOver, imgReplayOver, imgList, imgReplay, imgResume, imvNextGame,mh9_bgImage1;
    private EditText lblPlayerNameGameOver;

    private String OBJECT = "";
    private int TURN = 0;
    private int SCORE = 0;
    private int RESULT_FAILED = 0;
    private int RESULT_CHOSEN = -1;
    private ArrayList<WordObject> listImageFromDataO;
    private ArrayList<WordObject> listImageLevelO;

    private Dialog dialogBack;
    private Dialog dialogGameOver;
    private Dialog dialogWinGame;
    private DbAccessHelper dbAccessHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_screen_mh_9);
        initialize();
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
                    timer.cancel();
                    lblTimer.clearAnimation();
                    lblScoreGameOver.setText(String.valueOf(SCORE));
                    dialogGameOver.show();
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                }
            }
        };
        //get Object selected at screen topic
        if (getIntent().getExtras() != null) {
            OBJECT = getIntent().getStringExtra(ConfigApplication.OBJECT_SELECTED);
            addDataList();
        }
        Log.d("Guessing", SizeOfDevice.getScreenWidth() + "x" + SizeOfDevice.getScreenHeight());
    }

    @Override
    public void onBackPressed() {
        timer.cancel();
        dialogBack.show();
        return;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mpSoundBackground.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mpSoundBackground.pause();
        timer.cancel();
        dialogBack.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpSoundBackground.stop();
        mpWingame.stop();
        mpClicked.stop();
    }

    private void initialize() {
        //database
        dbAccessHelper = new DbAccessHelper(this);

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
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
        animationTimer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim_trieu);
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
        lblPlayerNameGameOver = (EditText) dialogGameOver.findViewById(R.id.lblPlayerNameGameOver);

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

        mpOK = MediaPlayer.create(getApplicationContext(), R.raw.dung);
        mpGameOver = MediaPlayer.create(getApplicationContext(), R.raw.sai);
        mpClicked = MediaPlayer.create(getApplicationContext(), R.raw.game_5_sound_clicked);
        mpWingame = MediaPlayer.create(getApplicationContext(), R.raw.wingame);
        mpSoundBackground = MediaPlayer.create(getApplicationContext(), R.raw.game_5_screen_background_sound);
        mpSoundBackground.setLooping(true);
        mpSoundBackground.start();

        //set time left default
        lblTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));

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
        //StartSmartAnimation.startAnimation(mh9_bgImage1,AnimationType.RotateInDownLeft,25000,0,true);
        lblTimer.clearAnimation();
        lblTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
        lblScore.setText(String.valueOf(SCORE));
        RESULT_FAILED = 0;
        RESULT_CHOSEN = -1;

        StartSmartAnimation.startAnimation(btnImage1, AnimationType.Pulse, 1500, 0, true);
        StartSmartAnimation.startAnimation(btnImage2, AnimationType.Pulse, 1500, 0, true);
        StartSmartAnimation.startAnimation(btnImage3, AnimationType.Pulse, 1500, 0, true);
        StartSmartAnimation.startAnimation(btnImage4, AnimationType.Pulse, 1500, 0, true);
        StartSmartAnimation.startAnimation(btnImage5, AnimationType.Pulse, 1500, 0, true);
        StartSmartAnimation.startAnimation(btnImage6, AnimationType.Pulse, 1500, 0, true);
        StartSmartAnimation.startAnimation(lblWord, AnimationType.BounceIn, 2000, 0, true);

        listImageLevelO = new ArrayList<>();
        Log.d("ImageSizeBefore", String.valueOf(listImageFromDataO.size()));
        Random rd = new Random();
        for (int j = 0; j < 6; j++) {
            int n = listImageFromDataO.size();
            int x = rd.nextInt(n);
            listImageLevelO.add(listImageFromDataO.get(x));
            listImageFromDataO.remove(x);
            //Log.d("ImageSizeSV","Index:"+j+"-X:"+ String.valueOf(x) + "-Object: " + listImageFromDataO.get(x).getwEng()+":"+listImageFromDataO.get(x).getwPathImage());
            //Log.d("ImageSizeLC","Index:"+j+"-X:"+ String.valueOf(x) + "-Object: " + listImageLevelO.get(j).getwEng()+":"+listImageLevelO.get(j).getwPathImage());
        }
        RESULT_CHOSEN = rd.nextInt(listImageLevelO.size());
        lblWord.setText(listImageLevelO.get(RESULT_CHOSEN).getwEng().toString());
        Log.d("ImageSizeResult", String.valueOf(RESULT_CHOSEN));

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
        listImageFromDataO = new ArrayList<>();
        listImageFromDataO = dbAccessHelper.getWordObject(ConfigApplication.OBJECT_ANIMALS);
        //Log.d("ImageSize", String.valueOf(listImageFromDataO.size()) + "-Object: " + listImageFromDataO.get(0).getwEng());
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

    //Add image from sdcard to ImageButton
    private Bitmap getImageBitmap(String imageName) {
        String path = Environment.getExternalStorageDirectory().toString() + "" + imageName + ".jpg";
        File fileImage = new File(path);
        Bitmap bmp = null;
        if (fileImage.exists()) {
            bmp = BitmapFactory.decodeFile(fileImage.getAbsolutePath());
        }
        return bmp;
    }

    //Font
    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPath));
        lblTimer.setTypeface(custom_font);
        lblWord.setTypeface(custom_font);
        lblScore.setTypeface(custom_font);
        lblScoreGameOver.setTypeface(custom_font);
        lblPlayerNameGameOver.setTypeface(custom_font);
    }

    //Event
    private void getEvents() {
        imgListOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageGuessingActivity.this, TopisChoosingActivity.class));
                finish();
            }
        });
        imgReplayOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataList();
                doSaveScore();
                TURN = 0;
                SCORE = 0;
                loadGame();
                dialogGameOver.dismiss();
            }
        });
        btnPauseGame5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPauseGame5.startAnimation(animation);
                timer.cancel();
                dialogBack.show();
            }
        });
        imgResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playTimer();
                dialogBack.dismiss();
            }
        });
        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageGuessingActivity.this, TopisChoosingActivity.class));
                finish();
            }
        });
        imgReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDataList();
                doSaveScore();
                TURN = 0;
                SCORE = 0;
                loadGame();
                dialogBack.dismiss();
            }
        });
        imvNextGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogWinGame.dismiss();
                Intent intent = new Intent(ImageGuessingActivity.this,RandomGamePracticeActivity.class);
                Bundle data = new Bundle();
                data.putSerializable(ConfigApplication.NAME_DATA_LIST,listImageLevelO);
                data.putInt(ConfigApplication.SCORES_BEFOR_GAME,SCORE);
                intent.putExtras(data);
                startActivity(intent);
                finish();


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
                        getResult(5);
                        mpClicked.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
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
                        getResult(4);
                        mpClicked.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
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
                        getResult(3);
                        mpClicked.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
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
                        getResult(2);
                        mpClicked.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
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
                        getResult(1);
                        mpClicked.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
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
                        getResult(0);
                        mpClicked.start();
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
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
            Log.d("ScoreSave", String.valueOf(SCORE));
            Log.d("ScoreSavePlayer", playerName);
            dbAccessHelper.doInsertScore(scoreObject);
            lblPlayerNameGameOver.setText("");
            Toast.makeText(getApplicationContext(), "Saving Score", Toast.LENGTH_SHORT).show();
        }
    }

    //Check Result
    private void getResult(int choose) {
        if (choose == RESULT_CHOSEN) {
            SCORE += lblWord.getText().length() * 10 * Integer.parseInt(lblTimer.getText().toString());
            lblScore.setText(String.valueOf(SCORE));
            StartSmartAnimation.startAnimation(lblScore,AnimationType.StandUp,1000,0,true);
            mpOK.start();
            Log.d("Size", String.valueOf(listImageFromDataO.size()));
            TURN++;
            Log.d("ImSizeTurn", String.valueOf(TURN));
            if (TURN >= 4) {
                timer.cancel();
                txtScoreWin.setText(String.valueOf(SCORE));
                dialogWinGame.show();
                mpWingame.start();
                Toast.makeText(getApplicationContext(), "You WIN", Toast.LENGTH_SHORT).show();
            } else {
                timer.cancel();
                loadGame();
            }
        } else {
            RESULT_FAILED++;
            if (RESULT_FAILED == 1) {
                new CustomToask(ImageGuessingActivity.this, R.drawable.screen_5_icon_ani, "Try one more time!");
            } else if (RESULT_FAILED == 2) {
                if (SCORE == 0)
                    lblPlayerNameGameOver.setVisibility(View.GONE);
                else
                    lblPlayerNameGameOver.setVisibility(View.VISIBLE);
                mpGameOver.start();
                lblScoreGameOver.setText(String.valueOf(SCORE));
                timer.cancel();
                dialogGameOver.show();
            }
        }
    }

}
