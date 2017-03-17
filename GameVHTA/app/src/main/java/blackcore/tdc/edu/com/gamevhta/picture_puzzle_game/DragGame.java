package blackcore.tdc.edu.com.gamevhta.picture_puzzle_game;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import blackcore.tdc.edu.com.gamevhta.LoadingGoInGameActivity;
import blackcore.tdc.edu.com.gamevhta.LoadingGoOutGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class DragGame extends AppCompatActivity {
    final int color = Color.parseColor("#FFFFFF");
    private boolean flagVoice = true;

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

    ImageView imgBackGame,imgList,imgReplay,imgResume;
    ImageView imbAnimalOne,imbAnimalTwo,imbAnimalThree,imbAnimalFour,imbAnimalFive,imbAnimalSix;
    ImageView imbAnimalQuestionOne,imbAnimalQuestionTwo,imbAnimalQuestionThree,imbAnimalQuestionFour,imbAnimalQuestionFive,imbAnimalQuestionSix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mClick = MediaPlayer.create(getApplicationContext(), R.raw.click);
        zoomIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

        imbAnimalOne = (ImageView) findViewById(R.id.imbAnimalone);
        imbAnimalTwo = (ImageView) findViewById(R.id.imbAnimaltwo);
        imbAnimalThree = (ImageView) findViewById(R.id.imbAnimalthree);
        imbAnimalFour = (ImageView) findViewById(R.id.imbAnimlafour);
        imbAnimalFive = (ImageView) findViewById(R.id.imbAnimalfive);
        imbAnimalSix = (ImageView) findViewById(R.id.imbAnimalsix);

        imbAnimalQuestionOne = (ImageView) findViewById(R.id.imbAnimalquestionone);
        imbAnimalQuestionTwo = (ImageView) findViewById(R.id.imbAnimalquestiontwo);
        imbAnimalQuestionThree = (ImageView) findViewById(R.id.imbAnimalquestionthree);
        imbAnimalQuestionFour = (ImageView) findViewById(R.id.imbAnimalquestionfour);
        imbAnimalQuestionFive = (ImageView) findViewById(R.id.imbAnimalquestionfive);
        imbAnimalQuestionSix = (ImageView) findViewById(R.id.imbAnimalquestionsix);

        imbAnimalOne.setOnTouchListener(touchListener);
        imbAnimalTwo.setOnTouchListener(touchListener);
        imbAnimalThree.setOnTouchListener(touchListener);
        imbAnimalFour.setOnTouchListener(touchListener);
        imbAnimalFive.setOnTouchListener(touchListener);
        imbAnimalSix.setOnTouchListener(touchListener);

        imbAnimalQuestionOne.setOnDragListener(dragListener);
        imbAnimalQuestionTwo.setOnDragListener(dragListener);
        imbAnimalQuestionThree.setOnDragListener(dragListener);
        imbAnimalQuestionFour.setOnDragListener(dragListener);
        imbAnimalQuestionFive.setOnDragListener(dragListener);
        imbAnimalQuestionSix.setOnDragListener(dragListener);

        imbAnimalQuestionOne.setColorFilter(color);
        imbAnimalQuestionTwo.setColorFilter(color);
        imbAnimalQuestionThree.setColorFilter(color);
        imbAnimalQuestionFour.setColorFilter(color);
        imbAnimalQuestionFive.setColorFilter(color);
        imbAnimalQuestionSix.setColorFilter(color);

        moveActivity();

    }

    public void moveActivity() {
        imgBackGame = (ImageView) findViewById(R.id.btnBackGame);
        imgBackGame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgBackGame.startAnimation(zoomIn);
                        mService.playMusic(mClick);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgBackGame.clearAnimation();
                        mService.pauseMusic(mClick);
                        onPause();
                        final Dialog dialog = new Dialog(view.getContext());
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.activity_dialog_back_game);
                        dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
                        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog.show();

                        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), getResources().getString(R.string.fontPath));
                        TextView txtpause = (TextView) dialog.findViewById(R.id.txtpause);
                        txtpause.setTypeface(custom_font);

                        imgList = (ImageView) dialog.findViewById(R.id.imgList);
                        imgReplay = (ImageView) dialog.findViewById(R.id.imgReplay);
                        imgResume = (ImageView) dialog.findViewById(R.id.imgResume);

                        imgList.setOnTouchListener(new View.OnTouchListener() {
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
                                        Intent intent = new Intent(getApplicationContext(),LoadingGoOutGameActivity.class);
                                        startActivity(intent);
                                        finish();
                                        return true;
                                }
                                return false;
                            }
                         });

                        imgReplay.setOnTouchListener(new View.OnTouchListener() {
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
                                        Intent intent = new Intent(getApplicationContext(),LoadingGoInGameActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
                                        startActivity(intent);
                                        finish();
                                        return true;
                                }
                                return false;
                            }
                        });

                        imgResume.setOnTouchListener(new View.OnTouchListener() {
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
                                        onResume();
                                        return true;
                                }
                                return false;
                            }
                        });
                        return true;
                }
                return false;
            }
        });
    }

    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onPause();

    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    protected void onResume(){
        // TODO Auto-generated method stub
        super.onResume();
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent motionEvent) {
            switch (motionEvent.getAction()){
                case MotionEvent.ACTION_DOWN:
                    ClipData data = ClipData.newPlainText("", "");
                    View.DragShadowBuilder myShadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, myShadowBuilder, v, 0);
                    return true;
                case MotionEvent.ACTION_UP:
                    return true;
            }
            return false;
        }
    };

    public void Voice(){
        if(flagVoice == true) {
            MediaPlayer mPlayer = MediaPlayer.create(DragGame.this,R.raw.dung);
            mPlayer.start();
        }else if(flagVoice == false){
            MediaPlayer mPlayer = MediaPlayer.create(DragGame.this,R.raw.sai);
            mPlayer.start();
        }
    }

    View.OnDragListener dragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent event) {
            int dragEvent = event.getAction();
            final View v = (View) event.getLocalState();
            switch (dragEvent) {
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    if(v.getId() == R.id.imbAnimalone && view.getId() == R.id.imbAnimalquestionone){
                        imbAnimalQuestionOne.clearColorFilter();
                        imbAnimalQuestionOne.setImageResource(R.drawable.buffalo_answer);
                        flagVoice = true;
                        Voice();
                        imbAnimalOne.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimaltwo && view.getId() == R.id.imbAnimalquestiontwo){
                        imbAnimalQuestionTwo.clearColorFilter();
                        imbAnimalQuestionTwo.setImageResource(R.drawable.bunny_answer);
                        flagVoice = true;
                        Voice();
                        imbAnimalTwo.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimalthree && view.getId() == R.id.imbAnimalquestionthree){
                        imbAnimalQuestionThree.clearColorFilter();
                        imbAnimalQuestionThree.setImageResource(R.drawable.cat_answer);
                        flagVoice = true;
                        Voice();
                        imbAnimalThree.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimlafour && view.getId() == R.id.imbAnimalquestionfour){
                        imbAnimalQuestionFour.clearColorFilter();
                        imbAnimalQuestionFour.setImageResource(R.drawable.cow_answer);
                        flagVoice = true;
                        Voice();
                        imbAnimalFour.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimalfive && view.getId() == R.id.imbAnimalquestionfive){
                        imbAnimalQuestionFive.clearColorFilter();
                        imbAnimalQuestionFive.setImageResource(R.drawable.goat_answer);
                        flagVoice = true;
                        Voice();
                        imbAnimalFive.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimalsix && view.getId() == R.id.imbAnimalquestionsix){
                        imbAnimalQuestionSix.clearColorFilter();
                        imbAnimalQuestionSix.setImageResource(R.drawable.dog_answer);
                        flagVoice = true;
                        Voice();
                        imbAnimalSix.setVisibility(View.INVISIBLE);
                    }else
                        flagVoice = false;
                        Voice();
                    break;
            }
            return true;
        }
    };
}
