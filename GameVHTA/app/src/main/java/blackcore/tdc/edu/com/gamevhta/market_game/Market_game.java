package blackcore.tdc.edu.com.gamevhta.market_game;

import android.app.Dialog;
import android.content.ClipData;
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
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;

import static blackcore.tdc.edu.com.gamevhta.R.layout.activity_market_game;

public class Market_game extends AppCompatActivity {

    private Handler handler;
    private Animation animation;
    private Animation animationRotate;
    private Timer timer = new Timer();
    private TextView txtvWord, txtvTimer, txtvLevel, txtvScore, lblScoreGameOver, txtvTurnNumber;
    private ImageView imgListOver, imgReplayOver, imgvObject1, imgvObject2, imgvObject3, imgvObject4, imgvObject5, imgvObject6, imgResume, imgList, imgReplay, imgBag;
    private MediaPlayer mpClicked, mpSoundBackground;
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
    private int INDEX_SELECTED = -1;

    private ArrayList<WordObject> listImageFromDataO;
    private ArrayList<WordObject> listImageLevelO;

    private Dialog dialogBack;
    private Dialog dialogGameOver;
    private DbAccessHelper DbAccessHelper;
    private boolean flagVoice = true;

    //test
    private ArrayList<Bitmap> listBitMapAnswer = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_market_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initialize();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle reMessage = msg.getData();
                String time = reMessage.getString("time");

                txtvTimer.setText(time);
                int t = Integer.parseInt(time);
                if (t > 0 && t <= 10) {
                    txtvTimer.startAnimation(animation);
                } else if (t == 0) {
                    txtvTimer.clearAnimation();
                    timer.cancel();
                    lblScoreGameOver.setText(String.valueOf(SCORE));
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

//        dialog Backgame
        dialogBack = new Dialog(Market_game.this);
        dialogBack.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogBack.setCancelable(false);
        dialogBack.setContentView(R.layout.activity_dialog_back_game);
        dialogBack.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogBack.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgResume = (ImageView) dialogBack.findViewById(R.id.imgResume);
        imgList = (ImageView) dialogBack.findViewById(R.id.imgList);
        imgReplay = (ImageView) dialogBack.findViewById(R.id.imgReplay);

        mpClicked = MediaPlayer.create(getApplicationContext(), R.raw.game_5_sound_clicked);
        mpSoundBackground = MediaPlayer.create(getApplicationContext(), R.raw.game_9_screen_background_sound);
        mpSoundBackground.setLooping(true);
        mpSoundBackground.start();

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

    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                    imgBag.startAnimation(animation);
                    txtvWord.startAnimation(animation);
                    v.startAnimation(animationRotate);
                    v.startDrag(data, myShadowBuilder, v, 0);

                    return true;
                case MotionEvent.ACTION_UP:
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
                    flagVoice = false;
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
        txtvTimer.setText(String.valueOf(ConfigApplication.TIME_LEFT_GAME_MARKET));
        txtvScore.setText(String.valueOf(SCORE));
        RESULT_CHOSEN = -1;
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
                doWhenClickImglist();
            }
        });
        imgReplayOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWhenClickImgReplay();
                dialogGameOver.dismiss();
            }
        });
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                dialogBack.show();
            }
        });

        imgResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        //doSaveScore();
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
            Toast.makeText(getApplicationContext(), "ban chon dung", Toast.LENGTH_SHORT).show();
            TURN++;
            txtvTurnNumber.setText(String.valueOf(TURN));

//            khi turn > 3
            if (TURN > 3) {
                Toast.makeText(getApplicationContext(), "Qua level", Toast.LENGTH_SHORT).show();

                timer.cancel();
                LEVEL++;
                txtvLevel.setText("Level. " + String.valueOf(LEVEL));
                loadGame();
                TURN = 1;
                txtvTurnNumber.setText(String.valueOf(TURN));
            } else {
                timer.cancel();
                SCORE += txtvWord.getText().length() * 10 * Integer.parseInt(txtvTimer.getText().toString());
                txtvScore.setText(String.valueOf(SCORE));
                loadGame();

            }
        } else {

            RESULT_FAILED++;
            if (RESULT_FAILED == 2) {
                Toast.makeText(getApplicationContext(), "ban chon sai lan " + RESULT_FAILED, Toast.LENGTH_SHORT).show();
                timer.cancel();
                dialogGameOver.show();
                RESULT_FAILED = 0;
                lblScoreGameOver.setText(String.valueOf(SCORE));
                txtvLevel.setText("Level. 1");
                txtvTurnNumber.setText("1");
            } else {
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

}
