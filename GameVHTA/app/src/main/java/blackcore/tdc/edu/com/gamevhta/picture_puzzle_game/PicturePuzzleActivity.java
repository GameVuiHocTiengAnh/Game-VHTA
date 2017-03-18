package blackcore.tdc.edu.com.gamevhta.picture_puzzle_game;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import blackcore.tdc.edu.com.gamevhta.LoadingGoOutGameActivity;
import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.button.PauseButton;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class PicturePuzzleActivity extends AppCompatActivity {

    TextView txtTime,txtScore,txtAnswerOne,txtAnswerTwo,txtAnswerThree,txtAnswerFour,txtAnswerFive,txtAnswerSix;

    private Dialog dialog;
    private ArrayList<Bitmap> listBitMapAnswer = null;

    final int color = Color.parseColor("#FFFFFF");
    private boolean flagVoice = true;

    private MediaPlayer mCorrect,mWrong;
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

    ImageView imbAnimalOne,imbAnimalTwo,imbAnimalThree,imbAnimalFour,imbAnimalFive,imbAnimalSix;
    ImageView imbAnimalQuestionOne,imbAnimalQuestionTwo,imbAnimalQuestionThree,imbAnimalQuestionFour,imbAnimalQuestionFive,imbAnimalQuestionSix;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_drop_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mCorrect = MediaPlayer.create(PicturePuzzleActivity.this,R.raw.dung);
        mWrong = MediaPlayer.create(PicturePuzzleActivity.this,R.raw.sai);

        txtTime = (TextView) findViewById(R.id.txtTime);
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtAnswerOne = (TextView) findViewById(R.id.txtAnswerOne);
        txtAnswerTwo = (TextView) findViewById(R.id.txtAnswerTwo);
        txtAnswerThree = (TextView) findViewById(R.id.txtAnswerThree);
        txtAnswerFour = (TextView) findViewById(R.id.txtAnswerFour);
        txtAnswerFive = (TextView) findViewById(R.id.txtAnswerFive);
        txtAnswerSix = (TextView) findViewById(R.id.txtAnswerSix);

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

        listBitMapAnswer = new ArrayList<>();
        Bitmap cat = BitmapFactory.decodeResource(getResources(),R.drawable.cat_answer);
        Bitmap cow = BitmapFactory.decodeResource(getResources(),R.drawable.cow_answer);
        Bitmap dog = BitmapFactory.decodeResource(getResources(),R.drawable.dog_answer);
        Bitmap buffalo = BitmapFactory.decodeResource(getResources(),R.drawable.buffalo_answer);
        Bitmap rabbit = BitmapFactory.decodeResource(getResources(),R.drawable.bunny_answer);
        Bitmap goat = BitmapFactory.decodeResource(getResources(),R.drawable.goat_answer);
        listBitMapAnswer.add(cat);
        listBitMapAnswer.add(cow);
        listBitMapAnswer.add(dog);
        listBitMapAnswer.add(buffalo);
        listBitMapAnswer.add(rabbit);
        listBitMapAnswer.add(goat);
        moveActivity();
        setFont();
        Answer();
        Question();
    }

    private void Answer(){
        imbAnimalOne.setImageBitmap(listBitMapAnswer.get(0));
        imbAnimalTwo.setImageBitmap(listBitMapAnswer.get(1));
        imbAnimalThree.setImageBitmap(listBitMapAnswer.get(2));
        imbAnimalFour.setImageBitmap(listBitMapAnswer.get(3));
        imbAnimalFive.setImageBitmap(listBitMapAnswer.get(4));
        imbAnimalSix.setImageBitmap(listBitMapAnswer.get(5));
    }
    private void Question(){
        imbAnimalQuestionOne.setImageBitmap(listBitMapAnswer.get(0));
        imbAnimalQuestionTwo.setImageBitmap(listBitMapAnswer.get(1));
        imbAnimalQuestionThree.setImageBitmap(listBitMapAnswer.get(2));
        imbAnimalQuestionFour.setImageBitmap(listBitMapAnswer.get(3));
        imbAnimalQuestionFive.setImageBitmap(listBitMapAnswer.get(4));
        imbAnimalQuestionSix.setImageBitmap(listBitMapAnswer.get(5));
    }
    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getApplicationContext().getAssets(), getResources().getString(R.string.fontPath));
        txtTime.setTypeface(custom_font);
        txtScore.setTypeface(custom_font);
        txtAnswerOne.setTypeface(custom_font);
        txtAnswerTwo.setTypeface(custom_font);
        txtAnswerThree.setTypeface(custom_font);
        txtAnswerFour.setTypeface(custom_font);
        txtAnswerFive.setTypeface(custom_font);
        txtAnswerSix.setTypeface(custom_font);
    }

    public void moveActivity() {
        PauseButton imgBackGame = (PauseButton) findViewById(R.id.btnBackGame);
        Intent intent = new Intent(getApplicationContext(),LoadingGoOutGameActivity.class);
        imgBackGame.setMoveActivity(intent,PicturePuzzleActivity.this);


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
            mService.playMusic(mCorrect);
        }else if(flagVoice == false){
            mService.playMusic(mWrong);
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
                        imbAnimalQuestionOne.setImageBitmap(listBitMapAnswer.get(0));
                        flagVoice = true;
                        Voice();
                        imbAnimalOne.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimaltwo && view.getId() == R.id.imbAnimalquestiontwo){
                        imbAnimalQuestionTwo.clearColorFilter();
                        imbAnimalQuestionTwo.setImageBitmap(listBitMapAnswer.get(1));
                        flagVoice = true;
                        Voice();
                        imbAnimalTwo.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimalthree && view.getId() == R.id.imbAnimalquestionthree){
                        imbAnimalQuestionThree.clearColorFilter();
                        imbAnimalQuestionThree.setImageBitmap(listBitMapAnswer.get(2));
                        flagVoice = true;
                        Voice();
                        imbAnimalThree.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimlafour && view.getId() == R.id.imbAnimalquestionfour){
                        imbAnimalQuestionFour.clearColorFilter();
                        imbAnimalQuestionFour.setImageBitmap(listBitMapAnswer.get(3));
                        flagVoice = true;
                        Voice();
                        imbAnimalFour.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimalfive && view.getId() == R.id.imbAnimalquestionfive){
                        imbAnimalQuestionFive.clearColorFilter();
                        imbAnimalQuestionFive.setImageBitmap(listBitMapAnswer.get(4));
                        flagVoice = true;
                        Voice();
                        imbAnimalFive.setVisibility(View.INVISIBLE);
                    }else if(v.getId() == R.id.imbAnimalsix && view.getId() == R.id.imbAnimalquestionsix){
                        imbAnimalQuestionSix.clearColorFilter();

                        imbAnimalQuestionSix.setImageBitmap(listBitMapAnswer.get(5));
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
