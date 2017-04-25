package blackcore.tdc.edu.com.gamevhta;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.PlayerOld;
import blackcore.tdc.edu.com.gamevhta.models.Score;
import blackcore.tdc.edu.com.gamevhta.my_adapter.AdapterAddName;


public class MainMenuActivity extends AppCompatActivity {

    AdapterAddName adapter;
    private ArrayList<PlayerOld> list  = null;
    private ListView listView;
    private boolean flagMain = true;
    private MediaPlayer mMain,mClick;
    private ImageView btnExit,btnPlay,btnGuide,btnScore,imgNameExit;
    private Animation scaleBtnPlay,scaleBtnScore,scaleBtnGuide,scaleBtnExit,zoomIn,rotate_crazy;
    private final int TIME_DELAY_SCALE_BTN = 500;
    private Dialog dialog,dialogAddName,dialogWarnAddName;
    private DbAccessHelper db;
    private String newPlayer ;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ConfigApplication.NEW_NAME = null;
        scaleBtnPlay = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        scaleBtnScore = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        scaleBtnGuide = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        scaleBtnExit = AnimationUtils.loadAnimation(this, R.anim.scale_anim);
        zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        rotate_crazy = AnimationUtils.loadAnimation(MainMenuActivity.this,R.anim.scale_anim_trieu);
        newPlayer = null;
        db = new DbAccessHelper(this);
        mMain = MediaPlayer.create(getApplicationContext(),R.raw.game_5_screen_background_sound);
        mClick = MediaPlayer.create(getApplicationContext(),R.raw.click);

        flagMain = true;
        playMusicMain();

        btnExit = (ImageView) findViewById(R.id.imageExit);
        btnPlay = (ImageView) findViewById(R.id.imagePlay);
        btnGuide = (ImageView) findViewById(R.id.imageGuide);
        btnScore = (ImageView) findViewById(R.id.imageScore);

        boolean delayBtn = new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnPlay.startAnimation(scaleBtnPlay);
                btnScore.startAnimation(scaleBtnScore);
                btnGuide.startAnimation(scaleBtnGuide);
                btnExit.startAnimation(scaleBtnExit);
            }
        },TIME_DELAY_SCALE_BTN);

        btnPlay.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnPlay.startAnimation(zoomIn);
                        mClick.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnPlay.clearAnimation();
                        mClick.start();
                        flagMain = true;
                        warnAddName();
                        return true;
                }
                return false;
            }
        });

        btnScore.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnScore.startAnimation(zoomIn);
                        mClick.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnScore.clearAnimation();
                        mClick.start();
                        flagMain = true;
                        playMusicMain();
                        Intent intent = new Intent(MainMenuActivity.this,ScoresActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });

        btnGuide.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnGuide.startAnimation(zoomIn);
                        mClick.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnGuide.clearAnimation();
                        mClick.start();
                        flagMain = true;
                        playMusicMain();
                        Intent intent = new Intent(getApplicationContext(),GuideActivity.class);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });

        btnExit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnExit.startAnimation(zoomIn);
                        mClick.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnExit.clearAnimation();
                        mClick.start();
                        dialogExit();

                }
                return false;
            }
        });
    }
    public void dialogExit(){
        dialog = new Dialog(MainMenuActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_dialog_exit);
        dialog.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        final ImageView btnYes = (ImageView) dialog.findViewById(R.id.btnYes);
        final ImageView btnNo = (ImageView) dialog.findViewById(R.id.btnNo);
        StartSmartAnimation.startAnimation( dialog.findViewById(R.id.btnYes) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        StartSmartAnimation.startAnimation( dialog.findViewById(R.id.btnNo) , AnimationType.DropOut , 2000 , 0 , true , 100 );
        imgNameExit = (ImageView) dialog.findViewById(R.id.imgNameExit);
        imgNameExit.startAnimation(rotate_crazy);

        btnYes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnYes.setSelected(!btnYes.isSelected());
                        btnYes.isSelected();
                        btnNo.setEnabled(false);
                        mClick.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        playMusicMain();
                        btnNo.setEnabled(true);
                        finish();
                        return true;
                }
                return false;
            }
        });

        btnNo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        btnNo.setSelected(!btnNo.isSelected());
                        btnNo.isSelected();
                        btnYes.setEnabled(false);
                        mClick.start();
                        return true;
                    case MotionEvent.ACTION_UP:
                        btnYes.setEnabled(true);
                        mClick.start();
                        dialog.dismiss();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMain.start();
        mMain.setLooping(true);
    }

    protected void onPause(){
        super.onPause();
        if (mMain != null) {
            mMain.pause();
        }

    }
    protected  void onResume(){
        mMain.start();
        mMain.setLooping(true);
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMain != null) {
            if (mMain.isPlaying()) {
                mMain.stop();
                mMain.release();
            } else
                mMain.release();
        }
    }

    private void playMusicMain() {
        if(flagMain == true){
            mMain.start();
            mMain.setLooping(true);
        }else if(flagMain == false){
            mClick.start();
            try {
                mMain.stop();
                mMain.release();
            }catch (Exception e)
            {
                Log.d("a","s");
            }
        }
    }
    public void addName(){
        dialogAddName = new Dialog(MainMenuActivity.this);
        dialogAddName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddName.setContentView(R.layout.activity_dialog_add_name);
        dialogAddName.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogAddName.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTwo;
        dialogAddName.show();

        final TextView txtOldName = (TextView) dialogAddName.findViewById(R.id.txtOldName);
        final TextView txtAddNewName = (TextView) dialogAddName.findViewById(R.id.txtAddnew);
        final ImageView imgAddName = (ImageView) dialogAddName.findViewById(R.id.btnAddName);
        final EditText edtAddName = (EditText) dialogAddName.findViewById(R.id.edtEnterName);

        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), getResources().getString(R.string.fontPath));
        txtAddNewName.setTypeface(custom_font);
        txtOldName.setTypeface(custom_font);

        imgAddName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.start();
                if (edtAddName.getText().toString().trim().length() <= 0) {
                    dialogAddName.dismiss();
                    warnAddName();
                }else {
                    dialogAddName.dismiss();
                    Intent intent = new Intent(MainMenuActivity.this, TopisChoosingActivity.class);
                    ConfigApplication.NEW_NAME = edtAddName.getText().toString();
                    startActivity(intent);
                    finish();
                }
            }
        });
        lvOldName();

    }

    public void warnAddName(){
        dialogWarnAddName = new Dialog(MainMenuActivity.this);
        dialogWarnAddName.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWarnAddName.setContentView(R.layout.activity_dialog_warning);
        dialogWarnAddName.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWarnAddName.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationTwo;
        dialogWarnAddName.show();

        final ImageView btnWarn = (ImageView) dialogWarnAddName.findViewById(R.id.btnWarn);

        btnWarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.start();
                dialogWarnAddName.dismiss();
                addName();
            }
        });
    }
    public void lvOldName(){

        listView = (ListView) dialogAddName.findViewById(R.id.lvOldName);
        list = db.getListPlayerOld();

        adapter = new AdapterAddName(this,R.layout.activity_lv_add_name_item,list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mClick.start();
                Intent intent = new Intent(MainMenuActivity.this,TopisChoosingActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onBackPressed(){
        dialogExit();
    }
    public void onSuperBackPressed(){
        super.onBackPressed();
    }
}
