package blackcore.tdc.edu.com.gamevhta.market_game;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
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
import android.speech.tts.TextToSpeech;
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

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
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

public class Market_game extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private Handler handler;
    private Animation animation;
    private Animation animationRotate;
    private Timer timer = new Timer();
    private TextView txtvWord, txtvTimer, txtvLevel, txtvScore, lblScoreGameOver, txtvTurnNumber, txtScoreWin, txtWordEngNextTurn, txtWordVieNextTurn, txtNameScoreWin;
    private ImageView imgListOver, imgReplayOver, imgvObject1, imgvObject2, imgvObject3, imgvObject4, imgvObject5, imgvObject6, imgResume, imgList, imgReplay, imgBag, imbNextGameWin, imgObjectNextTurn, imgSpeak;
    private MediaPlayer mButtonClick, mCorrect, mWrong, mClick, mTickTac, mpSoundBackground, mReadyGo, mNextLevel, mGameOver, mYaah, mtornado, mLoadImage;
    private MusicService mService = new MusicService();
    private EditText lblPlayerNameGameOver;
    private Layout layoutBag;
    LinearLayout layoutMarketgame;
    private ImageButton btnPause;

    private String OBJECT = "";
    private int TURN = 0;
    private int SCORE = 0;
    private int RESULT_FAILED = 0;
    private int RESULT_CHOSEN = -1;
    private int LEVEL = 1;
    private int TIMES_PAUSE = 0;
    private boolean TIMER_IS_RUN = false;
    private boolean IS_RESUM = false;
    private int score_temp = 0;

    private ArrayList<WordObject> listImageFromDataO;
    private ArrayList<WordObject> listImageLevelO;

    private Dialog dialogBack, dialogGameOver, dialogComplete, dialogNextTurn;
    private DbAccessHelper DbAccessHelper;
    private boolean flagVoice = true;

    private TextToSpeech textToSpeech = null;

    //test
    private ArrayList<Bitmap> listBitMapAnswer = null;
    CustomToask customToask;

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
        if (dialogBack.isShowing() == true || dialogComplete.isShowing() == true || dialogGameOver.isShowing() == true) {
            if(dialogBack.isShowing() == true)
            {
                TIMES_PAUSE++;
                flagVoice = true;
                //timer.cancel();
                Toast.makeText(getApplicationContext(),"da vao duoc if trong resum" + String.valueOf(TIMES_PAUSE), Toast.LENGTH_SHORT).show();
                setWhenPause();
                IS_RESUM = true;
                Voice(mpSoundBackground);
            }
            else {
                flagVoice = true;
                timer.cancel();
                IS_RESUM = true;
                Voice(mpSoundBackground);
            }
        } else {
            flagVoice = true;
            timer.cancel();
            IS_RESUM = true;
            playTimer();
            Voice(mpSoundBackground);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dialogBack.isShowing() == true || dialogComplete.isShowing() == true || dialogGameOver.isShowing() == true) {
            setWhenPause();
            flagVoice = false;
            if(dialogBack.isShowing() == true)
            {
                TIMES_PAUSE++;
            }
        } else {
            Log.d("TIME PAUSE ------", String.valueOf(TIMES_PAUSE));
            TIMES_PAUSE++;
            setWhenPause();
            flagVoice = false;

        }
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

//      dialog Pause game
        dialogBack = new Dialog(Market_game.this);
        dialogBack.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBack.setCancelable(false);
        dialogBack.setContentView(R.layout.activity_dialog_back_game);
        dialogBack.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogBack.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgResume = (ImageView) dialogBack.findViewById(R.id.imgResume);
        imgList = (ImageView) dialogBack.findViewById(R.id.imgList);
        imgReplay = (ImageView) dialogBack.findViewById(R.id.imgReplay);

//      dialog Next Level
        dialogComplete = new Dialog(Market_game.this);
        dialogComplete.setCancelable(false);
        dialogComplete.setCanceledOnTouchOutside(false);
        dialogComplete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComplete.setContentView(R.layout.activity_dialog_win_game);
        dialogComplete.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogComplete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtNameScoreWin = (TextView) dialogComplete.findViewById(R.id.txtNameScoreWin);
        txtScoreWin = (TextView) dialogComplete.findViewById(R.id.txtScoreWin);
        imbNextGameWin = (ImageView) dialogComplete.findViewById(R.id.imvNextGame);

//      dialog Next Turn
        dialogNextTurn = new Dialog(Market_game.this);
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

//        Sound
        mButtonClick = MediaPlayer.create(Market_game.this, R.raw.click);
        mCorrect = MediaPlayer.create(Market_game.this, R.raw.dung_market_game);
        mClick = MediaPlayer.create(Market_game.this, R.raw.click_market_game);
        mTickTac = MediaPlayer.create(Market_game.this, R.raw.time);
        mReadyGo = MediaPlayer.create(Market_game.this, R.raw.ready_go);
        mNextLevel = MediaPlayer.create(Market_game.this, R.raw.next_level_market_game);
        mGameOver = MediaPlayer.create(Market_game.this, R.raw.game_over_market_game);
        mWrong = MediaPlayer.create(Market_game.this, R.raw.wrong_market_game);
        mYaah = MediaPlayer.create(Market_game.this, R.raw.yaah_market_game);
        mtornado = MediaPlayer.create(Market_game.this, R.raw.tornado_market_game);
        mLoadImage = MediaPlayer.create(Market_game.this, R.raw.load_image_market_game);
        mpSoundBackground = MediaPlayer.create(getApplicationContext(), R.raw.background_market_game);
        mpSoundBackground.setLooping(true);

        Voice(mReadyGo);
        Voice(mpSoundBackground);

//      textToSpeech
        textToSpeech = new TextToSpeech(this,this);


        ///database
        DbAccessHelper = new DbAccessHelper(this);
        //set time left default
        txtvTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME_MARKET));
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
                    new CountDownTimer(1200, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            PlayTwoSound(1000, 1000, mtornado, mGameOver);
                            animationWhenGameOver();
                        }

                        @Override
                        public void onFinish() {
                            lblScoreGameOver.setText(String.valueOf(SCORE));
                            dialogBack.dismiss();
                            dialogComplete.dismiss();
                            if (SCORE == 0) {
                                lblPlayerNameGameOver.setEnabled(false);
                                lblPlayerNameGameOver.setHint("");
                            }
                            dialogGameOver.show();
                            //Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_SHORT).show();
                        }
                    }.start();

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
                        //Toast.makeText(getApplicationContext(), "sai roi", Toast.LENGTH_SHORT).show();
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
//      if animation still run on image object == > clear animation
        clearAnimation();

        Voice(mLoadImage);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject1), AnimationType.BounceInLeft, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject2), AnimationType.BounceInLeft, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject3), AnimationType.BounceInDown, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject4), AnimationType.BounceInUp, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject5), AnimationType.BounceInRight, 1500, 0, false);
        StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject6), AnimationType.BounceInRight, 1500, 0, false);


        txtvTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME_MARKET));
        txtvTimer.clearAnimation();
        txtvScore.setText(String.valueOf(SCORE));
        RESULT_CHOSEN = -1;
        TIMES_PAUSE = 0;
        if (IS_RESUM == true) {
            timer.cancel();
            playTimer();
        }
        addDataList();
        listImageLevelO = new ArrayList<>();
        Random rd = new Random();
        for (int j = 0; j < 6; j++) {
            int n = listImageFromDataO.size();
            int x = rd.nextInt(n);
            listImageLevelO.add(listImageFromDataO.get(x));
            listImageFromDataO.remove(x);
            Log.d("Danh sach image", String.valueOf(listImageLevelO.size()) + "-Object: " + listImageFromDataO.get(j).getwEng() + ":" + listImageFromDataO.get(j).getwPathImage());
        }
        Log.d("Tong size", String.valueOf(listImageFromDataO.size()));
        RESULT_CHOSEN = rd.nextInt(listImageLevelO.size());
        //listImageFromDataO.remove(RESULT_CHOSEN);
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
        lblPlayerNameGameOver.setEnabled(true);
        lblPlayerNameGameOver.setHint("Enter your name!");
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
            dr = getApplicationContext().getResources().getDrawable(R.drawable.animal_bee);
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
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPathMarketGame));
        txtvTimer.setTypeface(custom_font);
        txtvWord.setTypeface(custom_font);
        txtvScore.setTypeface(custom_font);
        txtvLevel.setTypeface(custom_font);
        lblScoreGameOver.setTypeface(custom_font);
        lblPlayerNameGameOver.setTypeface(custom_font);
        txtWordEngNextTurn.setTypeface(custom_font);
        txtScoreWin.setTypeface(custom_font);
        txtNameScoreWin.setTypeface(custom_font);
    }

    private void getEvents() {


        imgListOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                doSaveScore();
                doWhenClickImglist();
            }
        });
        imgReplayOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                doSaveScore();
                timer.cancel();
                doWhenClickImgReplay();
                dialogGameOver.dismiss();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                TIMES_PAUSE++;
                if (TIMES_PAUSE > 1) {
                    TIMER_IS_RUN = true;
                    new CustomToask(Market_game.this, R.drawable.smile_cry_market_game, "Thời gian vẫn đang chạy đó nha !!!");
                } else {
                    TIMER_IS_RUN = false;
                    new CustomToask(Market_game.this, R.drawable.smile_market_game, "Thời gian được dừng khi Pause lần đầu !!!");
                }

                //Toast.makeText(getApplicationContext(), String.valueOf(TIMES_PAUSE), Toast.LENGTH_SHORT).show();
                setWhenPause();
                dialogBack.show();
            }
        });

        imgResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                //Toast.makeText(getApplicationContext(), String.valueOf(TIMER_IS_RUN), Toast.LENGTH_SHORT).show();
                if (TIMER_IS_RUN == false) {
                    playTimer();
                }
                dialogBack.dismiss();
            }
        });

        imgList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                doWhenClickImglist();
            }
        });

        imgReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                timer.cancel();
                doWhenClickImgReplay();
                dialogBack.dismiss();
            }
        });

        imbNextGameWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Voice(mButtonClick);
                dialogComplete.dismiss();
                LEVEL++;
                txtvLevel.setText("Level. " + String.valueOf(LEVEL));
                int tempTIMES_PAUSE = TIMES_PAUSE;
                //Toast.makeText(getApplicationContext(), "temp= " + tempTIMES_PAUSE, Toast.LENGTH_SHORT).show();
                loadGame();
                //van giu nguyen TimePause khi qua level
                TIMES_PAUSE = tempTIMES_PAUSE;
                TURN = 0;
                txtvTurnNumber.setText(String.valueOf(TURN + 1));
                //Toast.makeText(getApplicationContext(), "turn khi click next level:" + TURN, Toast.LENGTH_SHORT).show();
            }
        });

        imgSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textToSpeech != null && listImageLevelO.get(RESULT_CHOSEN -1).getwEng() != null){
                    textToSpeech.speak(listImageLevelO.get(RESULT_CHOSEN -1).getwEng(),TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
    }

    private void doWhenClickImglist() {
        timer.cancel();
//        txtvLevel.setText("Level. 1"); dung xoa
//        txtvTurnNumber.setText("1");
//        lblPlayerNameGameOver.setText("");
        startActivity(new Intent(Market_game.this, TopisChoosingActivity.class));
        finish();
    }

    private void doWhenClickImgReplay() {
        addDataList();
        RESULT_FAILED = 0;
        TURN = 0;//truong hop truoc thi bang 1
        SCORE = 0;
        LEVEL = 1;
        txtvLevel.setText("Level. 1");
        txtvTurnNumber.setText("1");
        lblPlayerNameGameOver.setText("");
        loadGame();
    }

    private void getResult(int choose) {
//        neu chon dung
        if (choose == RESULT_CHOSEN) {
            TURN++;
//            khi turn = 3 => qua level
            if (TURN == 3) {
                showDialogNextTurn();
                Voice(mCorrect);
                PlayTwoSound(1000, 1000, mCorrect, mNextLevel);
                timer.cancel();
                SCORE += txtvWord.getText().length() * 2 * Integer.parseInt(txtvTimer.getText().toString());
                txtvScore.setText(String.valueOf(SCORE));
                dialogNextTurn.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Voice(mNextLevel);
                        txtvTurnNumber.setText(String.valueOf(TURN));
                        txtScoreWin.setText(String.valueOf(SCORE));
                        dialogComplete.show();
                    }
                });

            }
//            turn < 3 tiep tuc load
            else if (TURN < 3) {
                showDialogNextTurn();
                Voice(mCorrect);
                timer.cancel();
                SCORE += txtvWord.getText().length() * 2 * Integer.parseInt(txtvTimer.getText().toString());
                txtvScore.setText(String.valueOf(SCORE));
                dialogNextTurn.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        txtvTurnNumber.setText(String.valueOf(TURN + 1));
                        int tempTIMES_PAUSE = TIMES_PAUSE;
                        loadGame();
                        TIMES_PAUSE = tempTIMES_PAUSE;
                    }
                });

            }
        }
//        neu sai
        else {
            RESULT_FAILED++;
            if (RESULT_FAILED == 2) {
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
                            lblPlayerNameGameOver.setHint("");
                        }
                        dialogGameOver.show();
                    }
                }.start();

                //Toast.makeText(getApplicationContext(), "ban chon sai lan " + RESULT_FAILED, Toast.LENGTH_SHORT).show();
                timer.cancel();
                RESULT_FAILED = 0;
                lblScoreGameOver.setText(String.valueOf(SCORE));
                txtvLevel.setText("Level. 1");
                txtvTurnNumber.setText("1");
            }
//            chon sai 1 lan thi nhan duoc tro giup
            else {
                //Voice(mWrong);
                PlayTwoSound(600, 600, mWrong, mLoadImage);
                new CustomToask(Market_game.this, R.drawable.smile_market_game, "Sai lần đầu --- Bỏ 3 Tấm Sai !!!");
                //Toast.makeText(getApplicationContext(), "chon sai lan 1 duoc tro giup", Toast.LENGTH_SHORT).show();
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
                    if (n != 0 && n != RESULT_CHOSEN && n != choose && n != temp) {
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
            if (lblPlayerNameGameOver.getText().toString().equals("") == false) {
                ScoreObject scoreObject = new ScoreObject();
                scoreObject.setsPlayer(playerName);
                scoreObject.setsScore(Integer.valueOf(lblScoreGameOver.getText().toString()));
                DbAccessHelper.doInsertScore(scoreObject);
                lblPlayerNameGameOver.setText("");
                //Toast.makeText(getApplicationContext(), "Saved Score", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //    set visible image
    private void invisibleImage(int choose) {
        switch (choose) {
            case 1:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject1), AnimationType.ZoomOutLeft, 2000, 0, true);
                break;
            case 2:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject2), AnimationType.ZoomOutLeft, 2000, 0, true);
                break;
            case 3:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject3), AnimationType.ZoomOutUp, 2000, 0, true);
                break;
            case 4:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject4), AnimationType.ZoomOutDown, 2000, 0, true);
                break;
            case 5:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject5), AnimationType.ZoomOutRight, 2000, 0, true);
                break;
            case 6:
                StartSmartAnimation.startAnimation(findViewById(R.id.imgvObject6), AnimationType.ZoomOutRight, 2000, 0, true);
                break;
        }
    }

    private void setWhenPause() {
        if (TIMES_PAUSE == 1) {
            timer.cancel();
        }
        else if(TIMES_PAUSE > 3) {
            playTimer();
        }

    }

    private void playTimer() {
        timer.cancel();
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
    public void Voice(MediaPlayer nameSound) {
        if (flagVoice == true) {
            mService.playMusic(nameSound);
        } else if (flagVoice == false) {
            //khi nguoi dung tat am thanh
        }
    }

    //    Play sound sequence
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

                imgvObject1.setVisibility(View.INVISIBLE);
                imgvObject2.setVisibility(View.INVISIBLE);
                imgvObject3.setVisibility(View.INVISIBLE);
                imgvObject4.setVisibility(View.INVISIBLE);
                imgvObject5.setVisibility(View.INVISIBLE);
                imgvObject6.setVisibility(View.INVISIBLE);

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
                txtWordVieNextTurn.setText(listImageLevelO.get(RESULT_CHOSEN  - 1).getwVie().toString());
                txtWordEngNextTurn.setText(listImageLevelO.get(RESULT_CHOSEN  - 1).getwEng().toString());
                imgObjectNextTurn.setBackground(getBitmapResource(listImageLevelO.get(RESULT_CHOSEN - 1).getwPathImage()));
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
            }
        }.start();
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

    private void addScore() {
        new CountDownTimer(txtvWord.getText().length() * (Integer.parseInt(txtvTimer.getText().toString()) * 5), 1) {
            @Override
            public void onTick(long millisUntilFinished) {
                Toast.makeText(getApplicationContext(), String.valueOf(txtvWord.getText().length() * (Integer.parseInt(txtvTimer.getText().toString()) * 5)), Toast.LENGTH_SHORT).show();
                SCORE++;
                txtvScore.setText(String.valueOf(SCORE));
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "da chay  xong", Toast.LENGTH_SHORT).show();
            }
        }.start();
    }

    private void clearAnimation() {
        imgvObject1.clearAnimation();
        imgvObject2.clearAnimation();
        imgvObject3.clearAnimation();
        imgvObject4.clearAnimation();
        imgvObject5.clearAnimation();
        imgvObject6.clearAnimation();
    }
//    private void animationWhenCompleteLevel(){
//        imgvObject1.startAnimation();
//        imgvObject2.startAnimation(animationRotate);
//        imgvObject3.startAnimation(animationRotate);
//        imgvObject4.startAnimation(animationRotate);
//        imgvObject5.startAnimation(animationRotate);
//        imgvObject6.startAnimation(animationRotate);
//    }

}
