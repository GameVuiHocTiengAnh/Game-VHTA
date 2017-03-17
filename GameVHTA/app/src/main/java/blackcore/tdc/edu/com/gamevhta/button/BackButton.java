package blackcore.tdc.edu.com.gamevhta.button;

import android.app.Activity;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

/**
 * Created by phong on 3/14/2017.
 */

public class BackButton extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener{

    private Intent intent;
    private Activity context;
    private MediaPlayer mClick;
    private Animation zoomIn;
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

    public BackButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setImageResource(R.drawable.back1);
        mClick = MediaPlayer.create(context, R.raw.click);
        zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        this.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                BackButton.this.startAnimation(zoomIn);
                mService.playMusic(mClick);
                return true;
            case MotionEvent.ACTION_UP:
                BackButton.this.clearAnimation();
                mService.playMusic(mClick);
                MoveActivity();
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


