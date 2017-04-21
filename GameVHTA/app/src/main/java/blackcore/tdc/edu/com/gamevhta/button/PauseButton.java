package blackcore.tdc.edu.com.gamevhta.button;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.CatchingWordsActivity;
import blackcore.tdc.edu.com.gamevhta.catching_words_game.my_models.BackgroudGameView;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.picture_puzzle_game.PicturePuzzleActivity;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;
import blackcore.tdc.edu.com.gamevhta.writing_words_game.WirtingNinjaActivity;

/**
 * Created by phong on 3/14/2017.
 * Modified by Shiro on 22/3/207
 */

public class PauseButton extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener,View.OnClickListener{

    private Dialog dialog;
    private ImageView imgList,imgReplay,imgResume,sleep;
    private Intent intent;
    private Activity context;
    private MediaPlayer mClick;
    private Animation zoomIn,rotate_crazy;
    private String returnIdBtn;
    private String screenUse = null;
    private BackgroudGameView game = null;

    public PauseButton(Context context) {
        super(context);
        init(context);
    }
    public PauseButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PauseButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                PauseButton.this.startAnimation(zoomIn);
                mClick.start();
                return true;
            case MotionEvent.ACTION_UP:
                PauseButton.this.clearAnimation();
                mClick.start();
                callOnClick();
                animationPause();
                dialog.show();
        }
        return true;
    }

    public void animationPause() {
        rotate_crazy = AnimationUtils.loadAnimation(context.getApplicationContext(),R.anim.scale_anim_trieu);
        sleep = (ImageView) dialog.findViewById(R.id.sleep);
        sleep.startAnimation(rotate_crazy);
    }
    public void setMoveActivity(Intent intent, Activity context){
        this.context =  context;
        this.intent = intent;
    }
    private void MoveActivity(){
        context.startActivity(intent);
        context.finish();
    }

    private void buildDialog(final Context context){
        imgList = (ImageView) dialog.findViewById(R.id.imgList);
        imgReplay = (ImageView) dialog.findViewById(R.id.imgReplay);
        imgResume = (ImageView) dialog.findViewById(R.id.imgResume);

        imgList.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mClick.start();
                        imgList.setSelected(!imgList.isSelected());
                        imgList.isSelected();
                        imgResume.setEnabled(false);
                        imgReplay.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mClick.start();
                        imgList.setSelected(false);
                        imgResume.setEnabled(true);
                        imgReplay.setEnabled(true);
                        MoveActivity();
                        return true;
                }
                return false;
            }
        });

        imgReplay.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                returnIdBtn = "replay";
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgReplay.setSelected(!imgReplay.isSelected());
                        imgReplay.isSelected();
                        mClick.start();
                        imgList.setEnabled(false);
                        imgResume.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mClick.start();
                        imgReplay.setSelected(false);
                        imgList.setEnabled(true);
                        imgResume.setEnabled(true);
                        intent = new Intent( PauseButton.this.context,PauseButton.this.context.getClass());
                        PauseButton.this.context.startActivity(intent);
                        PauseButton.this.context.finish();
                        return true;
                }
                return false;
            }
        });

        imgResume.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgResume.setSelected(!imgResume.isSelected());
                        imgResume.isSelected();
                        mClick.start();
                        imgList.setEnabled(false);
                        imgReplay.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        mClick.start();
                        imgResume.setSelected(false);
                        imgList.setEnabled(true);
                        imgReplay.setEnabled(true);
                        resumeGameMH6();
                        resumeTimePuzzle();
                        //resumeChoosingGame();
                        dialog.dismiss();
                        return true;
                }
                return false;
            }
        });
    }

    public void setScreenUse(String MH, @Nullable BackgroudGameView game, Activity activity){
        this.context = activity;
        if(MH.equals(ConfigApplication.SCREEN_USING_PAUSE_DIALOG_MH6)&& game != null){
            this.screenUse = ConfigApplication.SCREEN_USING_PAUSE_DIALOG_MH6;
            this.game = game;
        }else{
            if(MH.equals("WRITING_GAME_NINJA")){
                this.screenUse = "WRITING_GAME_NINJA";
            }else{
                if(MH.equals("PUZZLE_GAME")){
                    this.screenUse = "PUZZLE_GAME";
                }
            }
        }
    }

    public void stopGameMH6(){
        if(game != null) {
            game.stopMoveBG();
            if(screenUse.equals("WRITING_GAME_NINJA")){
                WirtingNinjaActivity activity = (WirtingNinjaActivity) context;
                activity.releaseTimer();
            }
        }
    }
    public void resumeGameMH6(){
        if(game != null){
            if(screenUse != null){
                if(screenUse.equals(ConfigApplication.SCREEN_USING_PAUSE_DIALOG_MH6)) {
                    CatchingWordsActivity activity = (CatchingWordsActivity) context;
                    activity.onResumeGame();
                }
            }
        }
    }

//    public  void resumeChoosingGame(){
//        if(screenUse != null){
//            if(screenUse.equals("")) {
//                ChoosingObjectActivity activity = (ChoosingObjectActivity) context;
//                activity.onResumeGame();
//            }
//        }
//    }


    public void init(Context context){
        this.setImageResource(R.drawable.pause);
        mClick = MediaPlayer.create(context, R.raw.click);
        zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_back_game);
        dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        this.buildDialog(context);
        this.setOnTouchListener(this);
        this.setOnClickListener(this);
    }

    private void resumeTimePuzzle(){
        if(screenUse.equals("PUZZLE_GAME")){
            PicturePuzzleActivity activity = (PicturePuzzleActivity) context;
            ((PicturePuzzleActivity) context).startCountingTime();
        }

    }

    @Override
    public void onClick(View v) {
        if(screenUse != null){
            if(screenUse.equals(ConfigApplication.SCREEN_USING_PAUSE_DIALOG_MH6)){
                stopGameMH6();
            }
        }
        if(!dialog.isShowing())
            dialog.show();
    }
}


