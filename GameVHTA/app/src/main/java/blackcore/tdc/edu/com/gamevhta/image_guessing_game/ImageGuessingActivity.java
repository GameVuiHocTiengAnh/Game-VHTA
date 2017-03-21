package blackcore.tdc.edu.com.gamevhta.image_guessing_game;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.TopisChoosingActivity;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbScoreHelper;
import blackcore.tdc.edu.com.gamevhta.data_models.DbWordHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;


/**
 * Created by Hoang on 3/16/2017.
 */

public class ImageGuessingActivity extends AppCompatActivity {
    private Handler handler;
    private Animation animation;
    private Timer timer = new Timer();
    private ImageButton btnImage1, btnImage2, btnImage3, btnImage4, btnImage5, btnImage6;
    private TextView lblTimer, lblWord, lblScore, lblScoreGameOver;
    private MediaPlayer mpClicked, mpSoundBackground;
    private ImageView imgListOver, imgReplayOver;
    private EditText lblPlayerNameGameOver;

    private String OBJECT = "";
    private int TURN = 0;
    private int SCORE = 0;
    private int RESULT_FAILED = 0;
    private int RESULT_CHOSEN = -1;
    //    private ArrayList<WordObject> listImageFromData;
//    private ArrayList<WordObject> listImageLevel;
    private ArrayList<String> listImageFromData;
    private ArrayList<String> listImageLevel;

    private Dialog dialogGameOver;
    private DbWordHelper dbWordHelper;
    private DbScoreHelper dbScoreHelper;

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
                    lblTimer.startAnimation(animation);
                } else if (t == 0) {
                    timer.cancel();
                    doSaveScore();
                    dialogGameOver.show();
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                }
            }
        };

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mpSoundBackground.stop();
    }

    private void initialize() {
        btnImage1 = (ImageButton) findViewById(R.id.btnImage1);
        btnImage2 = (ImageButton) findViewById(R.id.btnImage2);
        btnImage3 = (ImageButton) findViewById(R.id.btnImage3);
        btnImage4 = (ImageButton) findViewById(R.id.btnImage4);
        btnImage5 = (ImageButton) findViewById(R.id.btnImage5);
        btnImage6 = (ImageButton) findViewById(R.id.btnImage6);
        lblTimer = (TextView) findViewById(R.id.lblTimer);
        lblWord = (TextView) findViewById(R.id.lblWord);
        lblScore = (TextView) findViewById(R.id.lblScore);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

        listImageFromData = new ArrayList<>();

        dialogGameOver = new Dialog(ImageGuessingActivity.this);
        dialogGameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGameOver.setCancelable(false);
        dialogGameOver.setContentView(R.layout.activity_dialog_game_over);
        dialogGameOver.getWindow().setBackgroundDrawableResource(R.color.tran);
        imgListOver = (ImageView) dialogGameOver.findViewById(R.id.imgListOver);
        imgReplayOver = (ImageView) dialogGameOver.findViewById(R.id.imgReplayOver);
        lblScoreGameOver = (TextView) dialogGameOver.findViewById(R.id.lblScoreGameOver);
        lblPlayerNameGameOver = (EditText) dialogGameOver.findViewById(R.id.lblPlayerNameGameOver);

        mpClicked = MediaPlayer.create(getApplicationContext(), R.raw.game_5_sound_clicked);
        mpSoundBackground = MediaPlayer.create(getApplicationContext(), R.raw.game_9_screen_background_sound);
        mpSoundBackground.setLooping(true);
        mpSoundBackground.start();

        //database
        dbWordHelper = new DbWordHelper(this);
        dbScoreHelper = new DbScoreHelper(this);
        //set time left default
        lblTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));

        //get Object selected at screen topic
        if (getIntent().getExtras() != null) {
            OBJECT = getIntent().getStringExtra(ConfigApplication.OBJECT_SELECTED);
            //listImageFromData = dbWordHelper.getWordObject(OBJECT, 30);
        }
        //example
        addDataList();
        getEvents();
        setFont();
        getAnimationImageButton();
        loadGame();
    }

    //play game
    private void loadGame() {
        lblTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME));
        lblScore.setText(String.valueOf(SCORE));
        RESULT_FAILED = 0;
        RESULT_CHOSEN = -1;
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

        listImageLevel = new ArrayList<>();
        Random rd = new Random();
        for (int j = 0; j < 6; j++) {
            int x = rd.nextInt(listImageFromData.size());
            listImageLevel.add(j, "Word" + x);
            listImageFromData.remove(x);
        }
        RESULT_CHOSEN = rd.nextInt(listImageLevel.size()) + 1;
        lblWord.setText(listImageLevel.get(RESULT_CHOSEN - 1).toString());
        Toast.makeText(getApplicationContext(), "RS: " + RESULT_CHOSEN, Toast.LENGTH_SHORT).show();
        switch (RESULT_CHOSEN) {
            case 1:
                //btnImage1.setImageBitmap(getImageBitmap(listImageLevel.get(RESULT_CHOSEN)));
                break;
            case 2:
                //btnImage2.setImageBitmap(getImageBitmap(listImageLevel.get(RESULT_CHOSEN)));
                break;
            case 3:
                //btnImage3.setImageBitmap(getImageBitmap(listImageLevel.get(RESULT_CHOSEN)));
                break;
            case 4:
                //btnImage4.setImageBitmap(getImageBitmap(listImageLevel.get(RESULT_CHOSEN)));
                break;
            case 5:
                //btnImage5.setImageBitmap(getImageBitmap(listImageLevel.get(RESULT_CHOSEN)));
                break;
            case 6:
                //btnImage6.setImageBitmap(getImageBitmap(listImageLevel.get(RESULT_CHOSEN)));
                break;
        }
    }

    //List Image was loaded from database
    private void addDataList() {
        for (int i = 0; i < 30; i++) {
            listImageFromData.add("Word" + (i + 1));
        }
    }

    //Add image to ImageButton
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
    }

    private void getAnimationImageButton() {
        btnImage6.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        btnImage6.startAnimation(animation);
                        getResult(6);
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
                        getResult(5);
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
                        getResult(4);
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
                        getResult(3);
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
                        getResult(2);
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
                        getResult(1);
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
        if(SCORE>0) {
            String playerName = lblPlayerNameGameOver.getText().toString();
            if (playerName.equals(""))
                playerName = "Unknown Player";
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsPlayer(playerName);
            scoreObject.setsScore(SCORE);
            Log.d("ScoreSave", String.valueOf(SCORE));
            Log.d("ScoreSavePlayer", playerName);
            dbScoreHelper.doInsertScore(scoreObject);
            lblPlayerNameGameOver.setText("");
            Toast.makeText(getApplicationContext(), "Saving Score", Toast.LENGTH_SHORT).show();
        }
    }

    //Check Result
    private void getResult(int choose) {
        if (choose == RESULT_CHOSEN) {
            Log.d("Size", String.valueOf(listImageFromData.size()));
            TURN++;
            Log.d("SizeTurn", String.valueOf(TURN));
            if(TURN >= 5){
                Toast.makeText(getApplicationContext(), "You WIN", Toast.LENGTH_SHORT).show();
            }else {
                timer.cancel();
                SCORE += lblWord.getText().length() * 10 * Integer.parseInt(lblTimer.getText().toString());
                lblScore.setText(String.valueOf(SCORE));
                loadGame();

            }
        } else {
            RESULT_FAILED++;
            if (RESULT_FAILED == 2) {
                lblScoreGameOver.setText(String.valueOf(SCORE));
                timer.cancel();
                dialogGameOver.show();
            }
        }
    }

}
