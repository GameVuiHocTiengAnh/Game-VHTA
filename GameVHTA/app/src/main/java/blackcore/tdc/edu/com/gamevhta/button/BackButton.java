package blackcore.tdc.edu.com.gamevhta.button;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import blackcore.tdc.edu.com.gamevhta.R;

/**
 * Created by phong on 3/14/2017.
 */

public class BackButton extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener{

    private Intent intent;
    private Activity context;
    private MediaPlayer mClick;
    private Animation zoomIn;

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
                mClick.start();
                return true;
            case MotionEvent.ACTION_UP:
                BackButton.this.clearAnimation();
                mClick.start();
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


