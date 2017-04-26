package blackcore.tdc.edu.com.gamevhta.writing_words_game;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.RandomGameMemoryChallengeActivity;
import blackcore.tdc.edu.com.gamevhta.RandomGamePracticeActivity;
import blackcore.tdc.edu.com.gamevhta.ScoresActivity;
import blackcore.tdc.edu.com.gamevhta.TopisChoosingActivity;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.CatchingWordsActivity;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.BackgroudGameView;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.custom_toask.CustomToask;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.image_guessing_game.ImageGuessingActivity;
import blackcore.tdc.edu.com.gamevhta.models.PlayerOld;
import blackcore.tdc.edu.com.gamevhta.models.Score;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;

/**
 * Created by Shiro on 4/4/2017.
 */

public class WirtingNinjaActivity extends CatchingWordsActivity{

    private Dialog dialogWriting;
    private Dialog dialogGameOver;
    private ImageView imvDialogWriting;
    private EditText txtDialogWriting;
    private Typeface gothic;
    private String textWord;
    private ArrayList<WordObject> listUsing;

    private ImageView btnCheck;
    private int scores;
    private TextView txtHintWord;
    private Bitmap bmUsing;
    private TextView txtTime;
    private int time;
    private Timer timer;
    private Handler timeHandler ;
    private Animation animationTimer;
    private boolean inDialogWriting;
    private ImageView imgListOver;
    private ImageView imgReplayOver;
    private TextView lblPlayerNameGameOver;
    private TextView lblScoreGameOver;
    private String newName = null;
    private int lvPass;
    private String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scores = 0;
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
                listUsing = (ArrayList<WordObject>) b.getSerializable(ConfigApplication.NAME_DATA_LIST);
                scores += 600;
                this.setScores(scores);
            }else{
                listUsing = this.getListUsing();
            }
        }

        if(listUsing.size() > 0 && scores == 0){
            scores += 600;
        }

        this.setListWordsUsing(listUsing);

        gothic = this.getTyeface();
        textWord = null;
        time = 50;
        bmUsing = null;
        inDialogWriting = false;
        this.setWritingMode(true);
        animationTimer = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_anim_trieu);
        initDialogOverGame();
        timeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bundle reMessage = msg.getData();
                String time = reMessage.getString("time");

                txtTime.setText(time);
                int t = Integer.parseInt(time);
                if (t > 0 && t <= 10) {
                } else if (t == 0) {
                    lblScoreGameOver.setText(scores+"");
                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.sai);
                    mediaPlayer.start();
                   dialogGameOver.show();
                    dialogWriting.dismiss();
                    timer.cancel();
                }
            }
        };
    }

    @Override
    protected void initDialogCatchingWords() {
        dialogWriting = new Dialog(this);
        dialogWriting.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWriting.setCanceledOnTouchOutside(false);
        dialogWriting.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWriting.setContentView(R.layout.dialog_writing_game);
        dialogWriting.getWindow().getAttributes().windowAnimations = R.style.CatchingWordsDialogAnimation;
        imvDialogWriting = (ImageView)dialogWriting.findViewById(R.id.imvWriting);
        txtDialogWriting = (EditText) dialogWriting.findViewById(R.id.edtEnterWord);
        btnCheck = (ImageView) dialogWriting.findViewById(R.id.btnCheckWriting);
        txtHintWord = (TextView) dialogWriting.findViewById(R.id.txtHintWord);
        txtTime = (TextView) dialogWriting.findViewById(R.id.txtTime);
        txtTime.setTypeface(gothic);
        txtDialogWriting.setTypeface(gothic);
        txtHintWord.setTypeface(gothic);

        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Tagtest",textWord);
                btnCheck.setSelected(true);
                btnCheck.isSelected();
                MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.click);
                mediaPlayer.start();
                if(textWord != null ){
                    String textWriting = txtDialogWriting.getText().toString();
//                    Log.d("Tagtest",textWriting);
                    if(textWriting.trim().compareToIgnoreCase(textWord.trim()) == 0){

//                        Log.d("Tagtest",getCountComplete()+"....."+listUsing.size());
                        if(WirtingNinjaActivity.this.getCountComplete() == listUsing.size()){
                            dialogWriting.dismiss();
                            WirtingNinjaActivity.this.setScores(scores);
                            Intent intent = new Intent(WirtingNinjaActivity.this,RandomGamePracticeActivity.class);
                            Bundle data = new Bundle();
                            data.putInt(ConfigApplication.SCORES_BEFOR_GAME,scores);
                            intent.putExtras(data);
                            WirtingNinjaActivity.this.levelComplete(intent);
                            savelevelPass();


                        }else{

                            timer.cancel();
                            dialogWriting.dismiss();
                            scores+= 2*Integer.parseInt(txtTime.getText().toString());
                            txtHintWord.setText("");
                            txtTime.setText("50");
                            if(bmUsing != null)
                                setWordObjectActionbar(bmUsing,textWord);
                            setScores(scores);
                            WirtingNinjaActivity.this.setNinjaDefaut();
                            inDialogWriting = false;
                            WirtingNinjaActivity.this.onResumeGame();
                        }
                    }else{
                        String hint;
                        char[] chars = textWord.toCharArray();
                        if(chars.length <= 4 && chars.length > 0){
                            hint = textWord.replace(chars[1],'_');
                        }else {
                            if(chars.length > 4 && chars.length <= 6){
                                hint = textWord.replace(chars[1],'_').replace(chars[3],'_');
                            }else{
                                if(chars.length > 6 && chars.length <= 10){
                                    hint = textWord.replace(chars[1],'_').replace(chars[3],'_').replace(chars[6],'_');
                                }else{
                                    hint = textWord.replace(chars[1],'_').replace(chars[3],'_').replace(chars[6],'_').replace(chars[8],'_');
                                }
                            }
                        }
                        Log.d("Tagtest",textWord);
                        Log.d("Tagtest",hint);
                        txtHintWord.setText(hint);
                    }
                }
                txtDialogWriting.setText("");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        btnCheck.setSelected(false);
                    }
                },200);
            }
        });



        imvDialogWriting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textToSpeech.speak(textWord, TextToSpeech.QUEUE_FLUSH,null);
                StartSmartAnimation.startAnimation(imvDialogWriting, AnimationType.StandUp,2000,0,true,2000);
            }
        });

        dialogWriting.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == event.KEYCODE_BACK){
//                    Log.d("Tagtest","back");
                    return true;
                }else{
                    return false;
                }

            }
        });
    }

    @Override
    protected void showDialogCatchingWords(Bitmap image, String text) {
        bmUsing = image;
        imvDialogWriting.setImageBitmap(image);
        textWord = text;
        dialogWriting.show();
        inDialogWriting = true;
        if(timer != null){
            timer.cancel();
        }
        txtTime.setText("50");
        playTimer();
    }
    private void playTimer() {
        timer = new Timer();
        txtTime.startAnimation(animationTimer);
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
                    timeHandler.sendMessage(message);
                }
            }
        },1000,1000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(timer != null)
            timer.cancel();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onResumeGame() {
//        setIsPauseGame(true);
        if(!inDialogWriting)
            super.onResumeGame();
        playTimer();
    }

    public void releaseTimer() {
        if (timer != null)
            timer.cancel();
    }

    @Override
    protected void setScreenUsingPauseBnt(String key, BackgroudGameView bgv, Activity activity) {
        super.setScreenUsingPauseBnt("WRITING_GAME_NINJA", bgv, this);
    }
    private void initDialogOverGame(){
        //dialog game over
        dialogGameOver = new Dialog(this);
        dialogGameOver.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogGameOver.setCancelable(false);
        dialogGameOver.setContentView(R.layout.activity_dialog_game_over);
        dialogGameOver.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogGameOver.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgListOver = (ImageView) dialogGameOver.findViewById(R.id.imgListOver);
        imgReplayOver = (ImageView) dialogGameOver.findViewById(R.id.imgReplayOver);
        lblScoreGameOver = (TextView) dialogGameOver.findViewById(R.id.lblScoreGameOver);
        lblPlayerNameGameOver = (TextView) dialogGameOver.findViewById(R.id.lblPlayerNameGOver);
        if(newName != null){
            lblPlayerNameGameOver.setText(newName);
        }

        imgListOver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgListOver.setSelected(!imgListOver.isSelected());
                        imgListOver.isSelected();
                        imgReplayOver.setEnabled(false);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASEDZ
                        doSaveScore();
                        imgListOver.setSelected(false);
                        startActivity(new Intent(WirtingNinjaActivity.this, ScoresActivity.class));
                        finish();
                        imgReplayOver.setEnabled(true);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        imgReplayOver.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgReplayOver.setSelected(!imgReplayOver.isSelected());
                        imgReplayOver.isSelected();
                        imgListOver.setEnabled(false);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        dialogGameOver.dismiss();
                        Intent intent = new Intent(WirtingNinjaActivity.this,WirtingNinjaActivity.class);
                        Bundle data = new Bundle();
                        data.putSerializable(ConfigApplication.NAME_DATA_LIST, listUsing);
                        intent.putExtras(data);
                        startActivity(intent);
                        finish();
                        imgListOver.setEnabled(true);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
    }
    private void doSaveScore() {
        if (scores > 0) {
            String playerName = lblPlayerNameGameOver.getText().toString();
            if (playerName.equals(""))
                playerName = "Unknown Player";
            ScoreObject scoreObject = new ScoreObject();
            scoreObject.setsPlayer(playerName);
            scoreObject.setsScore(scores);
            DbAccessHelper db = new DbAccessHelper(this);
            db.doInsertScore(scoreObject);
            lblPlayerNameGameOver.setText("");
        }
    }


    public ArrayList<WordObject> getListUsing() {
        DbAccessHelper db = new DbAccessHelper(this);
        ArrayList<WordObject> list = new ArrayList<>();
        if (db != null && newName != null) {
            ArrayList<WordObject> lv2 = null;
            try{
                listUsing = db.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC,lvPass+1 ); // when new player data is lv1 and lv2
                lv2 = db.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC,lvPass+2);
            }catch (NullPointerException e){
                int max = db.getLevelHighest(ConfigApplication.CURRENT_CHOOSE_TOPIC);
                listUsing = db.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC,max); // when new player data is lv1 and lv2
               lv2 = db.getWordObjectLevel(ConfigApplication.CURRENT_CHOOSE_TOPIC,max -1);
            }finally {
                if(lv2 != null)
                    listUsing.addAll(lv2);
            }
            Log.d("Tagtest", ConfigApplication.CURRENT_CHOOSE_TOPIC);

            for(int i = 0; i < 6; i++){
                Random ran = new Random();
                int s = listUsing.size();
                int dom = ran.nextInt(s);
                list.add(listUsing.get(dom));
                listUsing.remove(dom);
            }
        }
        return list;
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
