package blackcore.tdc.edu.com.gamevhta.market_game;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.DragEvent;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.TopisChoosingActivity;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.custom_toask.CustomToask;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

import static blackcore.tdc.edu.com.gamevhta.R.layout.activity_market_game;

public class Market_game extends AppCompatActivity {

    private Handler handler;
    private Animation animation;
    private Animation animationRotate;
    private Timer timer = new Timer();
    private TextView txtvWord, txtvTimer, txtvLevel, txtvScore, lblScoreGameOver, txtvTurnNumber;
    private ImageView imgListOver, imgReplayOver, imgvObject1, imgvObject2, imgvObject3, imgvObject4, imgvObject5, imgvObject6, imgResume, imgList, imgReplay, imgBag;
    private MediaPlayer mCorrect, mWrong, mClick, mTickTac,  mpSoundBackground, mReadyGo, mNextLevel, mGameOver, mYaah;
    private MusicService mService = new MusicService();
    private EditText lblPlayerNameGameOver;
    private Layout layoutBag;
    LinearLayout layoutMarketgame;
    private ImageButton btnPause;

    private String OBJECT = "";
    private int TURN = 1;
    private int SCORE = 0;
    private int RESULT_FAILED = 0;
    private int RESULT_CHOSEN = -1;
    private int LEVEL = 1;
    private int TIMES_PAUSE = 0;
    private boolean TIMER_IS_RUN = false;
    private boolean IS_RESUM = false;

    private ArrayList<WordObject> listImageFromDataO;
    private ArrayList<WordObject> listImageLevelO;

    private Dialog dialogBack;
    private Dialog dialogGameOver;
    private DbAccessHelper DbAccessHelper;
    private boolean flagVoice = true;

    //test
    private ArrayList<Bitmap> listBitMapAnswer = null;
    CustomToask customToask ;

    //service
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_market_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();



    }

    @Override
    protected void onResume() {
        super.onResume();
        flagVoice = true;
        timer.cancel();
        IS_RESUM = true;
        playTimer();
        Voice(mpSoundBackground);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("TIME PAUSE ------", String.valueOf(TIMES_PAUSE));
        TIMES_PAUSE++;
        setWhenPause();
        flagVoice = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mTickTac.pause();
        mGameOver.pause();
        mpSoundBackground.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTickTac.stop();
        mGameOver.stop();
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
        imgvObject1 = (ImageView) findViewById(R.id.imgvObject1);
        imgvObject2 = (ImageView) findViewById(R.id.imgvObject2);
        imgvObject3 = (ImageView) findViewById(R.id.imgvObject3);
        imgvObject4 = (ImageView) findViewById(R.id.imgvObject4);
        imgvObject5 = (ImageView) findViewById(R.id.imgvObject5);
        imgvObject6 = (ImageView) findViewById(R.id.imgvObject6);
        txtvWord = (TextView) findViewById(R.id.txtvWord);
        txtvTimer = (TextView) findViewById(R.id.txtvTimer);
        txtvLevel = (TextView) findViewById(R.id.txtvLevel);
        txtvScore = (TextView) findViewById(R.id.txtvScore);
        txtvTurnNumber = (TextView) findViewById(R.id.txtvTurnNumber);
        imgBag = (ImageView) findViewById(R.id.imgBag);
        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim_trieu);
        animationRotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        layoutMarketgame = (LinearLayout) findViewById(R.id.layoutMarketgame);
        btnPause = (ImageButton) findViewById(R.id.btnPause);

//      Dialog game over
        dialogGameOver = new Dialog(Market_game.this);
        dialogGameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGameOver.setCancelable(false);
        dialogGameOver.setContentView(R.layout.activity_dialog_game_over);
        dialogGameOver.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogGameOver.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgListOver = (ImageView) dialogGameOver.findViewById(R.id.imgListOver);
        imgReplayOver = (ImageView) dialogGameOver.findViewById(R.id.imgReplayOver);
        lblScoreGameOver = (TextView) dialogGameOver.findViewById(R.id.lblScoreGameOver);
        lblPlayerNameGameOver = (EditText) dialogGameOver.findViewById(R.id.lblPlayerNameGameOver);

//      dialog Backgame
        dialogBack = new Dialog(Market_game.this);
        dialogBack.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBack.setCancelable(false);
        dialogBack.setContentView(R.layout.activity_dialog_back_game);
        dialogBack.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogBack.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgResume = (ImageView) dialogBack.findViewById(R.id.imgResume);
        imgList = (ImageView) dialogBack.findViewById(R.id.imgList);
        imgReplay = (ImageView) dialogBack.findViewById(R.id.imgReplay);

//        Sound
        mCorrect = MediaPlayer.create(Market_game.this,R.raw.dung_market_game);
        mClick = MediaPlayer.create(Market_game.this, R.raw.click_market_game);
        mTickTac = MediaPlayer.create(Market_game.this, R.raw.time);
        mReadyGo = MediaPlayer.create(Market_game.this, R.raw.ready_go);
        mNextLevel = MediaPlayer.create(Market_game.this, R.raw.next_level_market_game);
        mGameOver = MediaPlayer.create(Market_game.this, R.raw.game_over_market_game);
        mWrong = MediaPlayer.create(Market_game.this, R.raw.wrong_market_game);
        mYaah = MediaPlayer.create(Market_game.this, R.raw.yaah_market_game);
        mpSoundBackground = MediaPlayer.create(getApplicationContext(), R.raw.background_market_game);
        mpSoundBackground.setLooping(true);
        Voice(mReadyGo);
        Voice(mpSoundBackground);


        ///database
        DbAccessHelper = new DbAccessHelper(this);
        //set time left default
        //txtvTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME_MARKET));
        txtvTimer.setText(String.valueOf(15));
        imgvObject1.setOnTouchListener(touchListener);
        imgvObject2.setOnTouchListener(touchListener);
        imgvObject3.setOnTouchListener(touchListener);
        imgvObject4.setOnTouchListener(touchListener);
        imgvObject5.setOnTouchListener(touchListener);
        imgvObject6.setOnTouchListener(touchListener);

        imgBag.setOnDragListener(dragListener);

        setBackgroundLayout();
        addDataList();
        setFont();
        getEvents();
        loadGame();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle reMessage = msg.getData();
                String time = reMessage.getString("time");

                txtvTimer.setText(time);
                int t = Integer.parseInt(time);
                if (t > 0 && t <= 10) {
                    txtvTimer.startAnimation(animation);
                    Voice(mTickTac);
                } else if (t == 0) {
                    txtvTimer.clearAnimation();
                    timer.cancel();
                    lblScoreGameOver.setText(String.valueOf(SCORE));
                    Voice(mGameOver);
                    dialogGameOver.show();
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                }
            }


        };

        if (getIntent().getExtras() != null) {
            OBJECT = getIntent().getStringExtra(ConfigApplication.OBJECT_SELECTED);
            addDataList();
        }

    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Voice(mClick);
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                    imgBag.startAnimation(animation);
                    txtvWord.startAnimation(animation);
                    v.startAnimation(animationRotate);
                    v.startDrag(data, myShadowBuilder, v, 0);
                    return true;
                case MotionEvent.ACTION_UP:
                    v.clearAnimation();
                    return true;
            }
            return false;
        }
    };

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {

            int dragEvent = event.getAction();
            final View v = (View) event.getLocalState();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (v != null) {
                        v.clearAnimation();
                        imgBag.clearAnimation();
                        txtvWord.clearAnimation();
                    }
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    if (v != null) {
                        v.clearAnimation();
                        imgBag.clearAnimation();
                        txtvWord.clearAnimation();
                    }
                    break;
                case DragEvent.ACTION_DROP:
                    if (v != null) {
                        v.clearAnimation();
                        imgBag.clearAnimation();
                        txtvWord.clearAnimation();
                    }
                    if (v.getId() == R.id.imgvObject1 && view.getId() == R.id.imgBag) {
                        getResult(1);

                    } else if (v.getId() == R.id.imgvObject2 && view.getId() == R.id.imgBag) {
                        getResult(2);
                    } else if (v.getId() == R.id.imgvObject3 && view.getId() == R.id.imgBag) {
                        getResult(3);
                    } else if (v.getId() == R.id.imgvObject4 && view.getId() == R.id.imgBag) {
                        getResult(4);
                    } else if (v.getId() == R.id.imgvObject5 && view.getId() == R.id.imgBag) {
                        getResult(5);
                    } else if (v.getId() == R.id.imgvObject6 && view.getId() == R.id.imgBag) {
                        getResult(6);
                    } else
                        Toast.makeText(getApplicationContext(), "sai roi", Toast.LENGTH_SHORT).show();
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    if (v != null) {
                        v.clearAnimation();
                        imgBag.clearAnimation();
                        txtvWord.clearAnimation();
                    }

            }
            return true;
        }
    };

    private void loadGame() {
        new CustomToask(Market_game.this,R.drawable.tiger, "TestCToast");
        //txtvTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME_MARKET));
        txtvTimer.setText(String.valueOf(15));
        txtvScore.setText(String.valueOf(SCORE));
        RESULT_CHOSEN = -1;
        TIMES_PAUSE = 0;
        if (IS_RESUM == true) {
            //them vo de test loi
            timer.cancel();
            playTimer();
        }

        listImageLevelO = new ArrayList<>();
        Random rd = new Random();
        for (int j = 0; j < 6; j++) {
            int x = rd.nextInt(listImageFromDataO.size());
            listImageLevelO.add(j, listImageFromDataO.get(x));
            Log.d("Danh sach image", String.valueOf(listImageLevelO.size()) + "-Object: " + listImageFromDataO.get(j).getwEng() + ":" + listImageFromDataO.get(j).getwPathImage());
        }
        Log.d("Tong size", String.valueOf(listImageFromDataO.size()));
        RESULT_CHOSEN = rd.nextInt(listImageLevelO.size());
        listImageFromDataO.remove(RESULT_CHOSEN);
        Log.d("size image", String.valueOf(listImageLevelO.size()));
        txtvWord.setText(listImageLevelO.get(RESULT_CHOSEN).getwEng().toString());
        Log.d("gia tri result chossen", String.valueOf(RESULT_CHOSEN));
        RESULT_CHOSEN += 1;
        Log.d("RC sau khi cong", String.valueOf(RESULT_CHOSEN));

        imgvObject1.setBackground(getBitmapResource(listImageLevelO.get(0).getwPathImage()));
        imgvObject2.setBackground(getBitmapResource(listImageLevelO.get(1).getwPathImage()));
        imgvObject3.setBackground(getBitmapResource(listImageLevelO.get(2).getwPathImage()));
        imgvObject4.setBackground(getBitmapResource(listImageLevelO.get(3).getwPathImage()));
        imgvObject5.setBackground(getBitmapResource(listImageLevelO.get(4).getwPathImage()));
        imgvObject6.setBackground(getBitmapResource(listImageLevelO.get(5).getwPathImage()));

        imgvObject1.setVisibility(View.VISIBLE);
        imgvObject2.setVisibility(View.VISIBLE);
        imgvObject3.setVisibility(View.VISIBLE);
        imgvObject4.setVisibility(View.VISIBLE);
        imgvObject5.setVisibility(View.VISIBLE);
        imgvObject6.setVisibility(View.VISIBLE);
    }

    //List Image was loaded from database
    private void addDataList() {
        listImageFromDataO = new ArrayList<>();
        listImageFromDataO = DbAccessHelper.getWordObject(ConfigApplication.OBJECT_ANIMALS);
    }

    private Drawable getBitmapResource(String name) {
        int id = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
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

    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPath));
        txtvTimer.setTypeface(custom_font);
        txtvWord.setTypeface(custom_font);
        txtvScore.setTypeface(custom_font);
        txtvLevel.setTypeface(custom_font);
        lblScoreGameOver.setTypeface(custom_font);
        lblPlayerNameGameOver.setTypeface(custom_font);
    }

    private void getEvents() {
        imgListOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveScore();
                doWhenClickImglist();
            }
        });
        imgReplayOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSaveScore();
                timer.cancel();
                doWhenClickImgReplay();
                dialogGameOver.dismiss();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TIMES_PAUSE++;
                if(TIMES_PAUSE > 1) {
                    TIMER_IS_RUN = true;
                }
                else {
                    TIMER_IS_RUN = false; 
                }

                Toast.makeText(getApplicationContext(), String.valueOf(TIMES_PAUSE), Toast.LENGTH_SHORT).show();
                setWhenPause();
                dialogBack.show();
            }
        });

        imgResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), String.valueOf(TIMER_IS_RUN), Toast.LENGTH_SHORT).show();
                if(TIMER_IS_RUN == false) {
                    playTimer();
                }
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
                timer.cancel();
                doWhenClickImgReplay();
                dialogBack.dismiss();
            }
        });
    }

    private void doWhenClickImglist() {
        timer.cancel();
        txtvLevel.setText("Level. 1");
        txtvTurnNumber.setText("1");
        lblPlayerNameGameOver.setText("");
        startActivity(new Intent(Market_game.this, TopisChoosingActivity.class));
        finish();
    }

    private void doWhenClickImgReplay() {
        addDataList();
        TURN = 1;
        SCORE = 0;
        txtvLevel.setText("Level. 1");
        txtvTurnNumber.setText("1");
        lblPlayerNameGameOver.setText("");
        loadGame();
    }

    private void getResult(int choose) {
//        neu chon dung
        if (choose == RESULT_CHOSEN) {
            if(TURN <= 3) {
                if (TURN < 3)
                    Voice(mCorrect);
                if (TURN == 3)
                    PlayTwoSound(1000, 1000, mCorrect, mNextLevel);
            }

            Voice(mCorrect);
            Toast.makeText(getApplicationContext(), "ban chon dung", Toast.LENGTH_SHORT).show();
            TURN++;
            txtvTurnNumber.setText(String.valueOf(TURN));

//            khi turn > 3 => qua level
            if (TURN > 3) {
                Toast.makeText(getApplicationContext(), "Qua level", Toast.LENGTH_SHORT).show();

                timer.cancel();
                LEVEL++;
                txtvLevel.setText("Level. " + String.valueOf(LEVEL));
                loadGame();
                //van giu nguyen TimePause khi qua level
                TIMES_PAUSE = 1;
                TURN = 1;
                txtvTurnNumber.setText(String.valueOf(TURN));
            }
//            turn < 3 tiep tuc load
            else {
                timer.cancel();
                SCORE += txtvWord.getText().length() * 10 * Integer.parseInt(txtvTimer.getText().toString());
                txtvScore.setText(String.valueOf(SCORE));
                loadGame();

            }
        }
//        neu sai
        else {
            RESULT_FAILED++;
            if(RESULT_FAILED < 2) {
                Voice(mWrong);;
            }
            else
               Voice(mGameOver);


//            kiem tra so lan chon sai
            if (RESULT_FAILED == 2) {
                Toast.makeText(getApplicationContext(), "ban chon sai lan " + RESULT_FAILED, Toast.LENGTH_SHORT).show();
                timer.cancel();
                //Voice(mGameOver);
                dialogGameOver.show();
                RESULT_FAILED = 0;
                lblScoreGameOver.setText(String.valueOf(SCORE));
                txtvLevel.setText("Level. 1");
                txtvTurnNumber.setText("1");
            }
//            chon sai 1 lan thi nhan duoc tro giup
            else {
                Toast.makeText(getApplicationContext(), "chon sai lan 1 duoc tro giup", Toast.LENGTH_SHORT).show();
                invisibleImage(choose);
                // set time repeat
                timer.cancel();
                txtvTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME_MARKET));
                txtvTimer.clearAnimation();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int t = Integer.parseInt(txtvTimer.getText().toString());
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

                //delete 3 picture
                int timesDelete = 0;
                invisibleImage(choose);
                Random rdNumberIsDelete = new Random();
                int n;
                int temp = -1;
                while (timesDelete != 2) {
                    n = rdNumberIsDelete.nextInt(6);
                    Log.d("chosse ------- ", "Choose: " + choose + "   n: " + n + "  RS: " + RESULT_CHOSEN);
                    if (n != 0 && n != RESULT_CHOSEN  && n != choose && n != temp) {
                        invisibleImage(n);
                        temp = n;
                        timesDelete++;
                    }
                }
            }
        }
    }

    private void setBackgroundLayout() {
        layoutMarketgame.setBackgroundResource(R.drawable.background_market_game);
    }

    //Save Score
    private void doSaveScore() {
        if (Integer.parseInt(lblScoreGameOver.getText().toString()) > 0) {
            String playerName = lblPlayerNameGameOver.getText().toString();
            if ( lblPlayerNameGameOver.getText().toString().equals("") == false) {
                ScoreObject scoreObject = new ScoreObject();
                scoreObject.setsPlayer(playerName);
                scoreObject.setsScore(Integer.valueOf(lblScoreGameOver.getText().toString()));
                DbAccessHelper.doInsertScore(scoreObject);
                lblPlayerNameGameOver.setText("");
                Toast.makeText(getApplicationContext(), "Saved Score", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    set visible image
    private void invisibleImage(int choose) {
        switch (choose) {
            case 1:
                imgvObject1.setVisibility(View.INVISIBLE);
                break;
            case 2:
                imgvObject2.setVisibility(View.INVISIBLE);
                break;
            case 3:
                imgvObject3.setVisibility(View.INVISIBLE);
                break;
            case 4:
                imgvObject4.setVisibility(View.INVISIBLE);
                break;
            case 5:
                imgvObject5.setVisibility(View.INVISIBLE);
                break;
            case 6:
                imgvObject6.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void setWhenPause()
    {
        if(TIMES_PAUSE == 1) {
            timer.cancel();
        }
    }
    private void playTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int t = Integer.parseInt(txtvTimer.getText().toString());
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

//  Play Sound
    public void Voice(MediaPlayer nameSound){
        if(flagVoice == true) {
            mService.playMusic(nameSound);
        }else if(flagVoice == false){
            //khi nguoi dung tat am thanh
        }
    }

//    Play sound sequence
    public void PlayTwoSound(int lengthOfFistSound, int timeEach,final MediaPlayer soundFist,final MediaPlayer soundSecond) {
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

}
