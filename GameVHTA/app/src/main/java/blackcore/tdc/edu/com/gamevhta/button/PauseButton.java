package blackcore.tdc.edu.com.gamevhta.button;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import blackcore.tdc.edu.com.gamevhta.LoadingGoInGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

/**
 * Created by phong on 3/14/2017.
 */

public class PauseButton extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener{

    private Dialog dialog;
    private ImageView imgList,imgReplay,imgResume;
    private Intent intent;
    private Activity context;
    private MediaPlayer mClick;
    private Animation zoomIn;
    private String returnIdBtn;
    private MusicService mService = new MusicService();

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

    public PauseButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setImageResource(R.drawable.pause);
        mClick = MediaPlayer.create(context, R.raw.click);
        zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                PauseButton.this.startAnimation(zoomIn);
                mService.playMusic(mClick);
                return true;
            case MotionEvent.ACTION_UP:
                PauseButton.this.clearAnimation();
                mService.playMusic(mClick);
                dialog = new Dialog(view.getContext());
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.activity_dialog_back_game);
                dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                dialog.show();

                Typeface custom_font = Typeface.createFromAsset(view.getContext().getAssets(), getResources().getString(R.string.fontPath));
                TextView txtpause = (TextView) dialog.findViewById(R.id.txtpause);
                txtpause.setTypeface(custom_font);

                imgList = (ImageView) dialog.findViewById(R.id.imgList);
                imgReplay = (ImageView) dialog.findViewById(R.id.imgReplay);
                imgResume = (ImageView) dialog.findViewById(R.id.imgResume);

                imgList.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                mService.playMusic(mClick);
                                imgList.setSelected(!imgList.isSelected());
                                imgList.isSelected();
                                return true;
                            case MotionEvent.ACTION_UP:
                                mService.playMusic(mClick);
                                MoveActivity();
                                return true;
                        }
                        return false;
                    }
                });

                imgReplay.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        switch (motionEvent.getAction()) {
                            case MotionEvent.ACTION_DOWN:
                                imgReplay.setSelected(!imgReplay.isSelected());
                                imgReplay.isSelected();
                                mService.playMusic(mClick);
                                return true;
                            case MotionEvent.ACTION_UP:
                                mService.playMusic(mClick);
                                intent = new Intent(context,LoadingGoInGameActivity.class);
                                context.startActivity(intent);
                                context.finish();
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
                                mService.playMusic(mClick);
                                return true;
                            case MotionEvent.ACTION_UP:
                                mService.playMusic(mClick);
                                dialog.dismiss();
                                return true;
                        }
                        return false;
                    }
                });
                return true;
        }
        return true;
    }

    public void setMoveActivity(Intent intent, Activity context){
        this.context =  context;
        this.intent = intent;
    }
    private void MoveActivity(){
        context.startActivity(intent);
        context.finish();
    }
}


