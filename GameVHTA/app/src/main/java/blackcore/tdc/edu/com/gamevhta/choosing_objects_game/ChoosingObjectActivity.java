package blackcore.tdc.edu.com.gamevhta.choosing_objects_game;

import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.config_app.ConfigApplication;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;


/**
 * Created by Canh on 16/03/2017.
 */

public class ChoosingObjectActivity extends AppCompatActivity  {

    private Animation animation;
    private ImageView imgAnimalOne, imgAnimalTwo, imgAnimalThree, imgAnimalFour, imgAnimalFive, imgAnimalSix, imgAnimalDialog, imbNextGameWin;
    private TextView txtScore, txtWorddialog, txtScoreWin;
    private Dialog dialogGame, dialogWin;
    private DbAccessHelper dbAccessHelper;
    TextToSpeech txtWord;

    private int SCORE_ONE = 0, SCORE_TWO = 0, SCORE_THREE = 0, SCORE_FOUR = 0, SCORE_FIVE = 0, SCORE_SIX = 0, SCORE_ALL = 0;
    private int check_finish = 0;
    private ArrayList<WordObject> listImageGame = null;
    private ArrayList<WordObject> listImageData = null;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_creen_mh7);
        initialize();
    }

    private void initialize() {
        dbAccessHelper = new DbAccessHelper(this);
        listImageData= new ArrayList<>();
        listImageGame= new ArrayList<>();
        imgAnimalOne = (ImageView) findViewById(R.id.imgAnimalOne);
        imgAnimalTwo = (ImageView) findViewById(R.id.imgAnimalTwo);
        imgAnimalThree = (ImageView) findViewById(R.id.imgAnimalThree);
        imgAnimalFour = (ImageView) findViewById(R.id.imgAnimalFour);
        imgAnimalFive = (ImageView) findViewById(R.id.imgAnimalFive);
        imgAnimalSix = (ImageView) findViewById(R.id.imgAnimalSix);
        txtScore = (TextView) findViewById(R.id.txtScore);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

        //imbNextGameWin = (ImageView) dialogWin.findViewById(R.id.imvNextGame);

        //dialog win game
        dialogWin = new Dialog(ChoosingObjectActivity.this);
        dialogWin.setCancelable(false);
        dialogWin.setCanceledOnTouchOutside(false);
        dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWin.setContentView(R.layout.activity_dialog_win_game);
        dialogWin.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtScoreWin = (TextView) dialogWin.findViewById(R.id.txtScoreWin);

        addDataList();
        createDataGame();
        Answer();
        getAnimationImageButton();
    }
    private void Answer(){
        imgAnimalOne.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
        imgAnimalTwo.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
        imgAnimalThree.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
        imgAnimalFour.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
        imgAnimalFive.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
        imgAnimalSix.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
    }
    //Add image from resource to ImageButton
    private Drawable getBitmapResource(String name) {
        int id = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        Log.d("ImageID", String.valueOf(id));
        Drawable dr;
        if (id != 0)
            dr = getApplicationContext().getResources().getDrawable(id);
        else
            dr = getApplicationContext().getResources().getDrawable(R.drawable.screen_5_dv);
        return dr;
    }
    private void addDataList(){
        listImageData = new ArrayList<>();
        listImageData = dbAccessHelper.getWordObject(ConfigApplication.OBJECT_ANIMALS);
    }
    private void createDataGame(){
        listImageGame = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 6; i++){
            int x = listImageData.size();
            int n = random.nextInt(x);
            listImageGame.add(listImageData.get(n));
            listImageData.remove(n);
        }
    }
//    private void setImageVocalubary(){
//        for (int j = 0; j < 6; j++){
//            String txtText = listImageGame.get(j).getwEng();
//            String pathName = listImageGame.get(j).getwPathImage();
//            int idImg = getResources().getIdentifier(pathName, "drawble", getApplication().getPackageName());
//            Bitmap imvVorca = BitmapFactory.decodeResource(getResources(), idImg);
//            lisText.add(txtText);
//            listImage.add(imvVorca);
//        }
//    }

    public void onResumeGame() {
        super.onResume();
    }

    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPath));
        txtScore.setTypeface(custom_font);
        txtWorddialog.setTypeface(custom_font);
        //txtNameScoreWin.setTypeface(custom_font);
        txtScoreWin.setTypeface(custom_font);
    }
    public void getAnimationImageButton() {
        imgAnimalOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgAnimalOne.startAnimation(animation);
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(0).getwPathImage()));
                        txtWorddialog.setText(listImageGame.get(0).getwEng());

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish();
                                getResult(1);
                                if(check_finish == 6) {
                                    EventWin();
                                    dialogWin.show();
                                }
                            }
                        });

                        final ImageView img_markOne = (ImageView) findViewById(R.id.img_markOne);
                        img_markOne.setVisibility(View.INVISIBLE);
                        imgAnimalOne.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgAnimalOne.clearAnimation();
                        return true;

                }
                return false;
            }
        });

        imgAnimalTwo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgAnimalTwo.startAnimation(animation);
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(1).getwPathImage()));
                        txtWorddialog.setText(listImageGame.get(1).getwEng());

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(2);
                                if(check_finish == 6) {
                                    EventWin();
                                    dialogWin.show();
                                }
                            }
                        });

                        final ImageView img_markTwo = (ImageView) findViewById(R.id.img_markTwo);
                        img_markTwo.setVisibility(View.INVISIBLE);
                        imgAnimalTwo.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgAnimalTwo.clearAnimation();
                        return true;
                }
                return false;
            }
        });
        imgAnimalThree.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgAnimalThree.startAnimation(animation);
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(2).getwPathImage()));
                        txtWorddialog.setText(listImageGame.get(2).getwEng());

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(3);
                                if(check_finish == 6) {
                                    EventWin();
                                    dialogWin.show();
                                }
                            }
                        });

                        final ImageView img_markThree = (ImageView) findViewById(R.id.img_markThree);
                        img_markThree.setVisibility(View.INVISIBLE);
                        imgAnimalThree.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgAnimalThree.clearAnimation();
                        return true;
                }
                return false;
            }
        });

        imgAnimalFour.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imgAnimalFour.startAnimation(animation);
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(3).getwPathImage()));
                        txtWorddialog.setText(listImageGame.get(3).getwEng());

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(4);
                                if(check_finish == 6) {
                                    EventWin();
                                    dialogWin.show();
                                }
                            }
                        });

                        final ImageView img_markFour = (ImageView) findViewById(R.id.img_markFour);
                        img_markFour.setVisibility(View.INVISIBLE);
                        imgAnimalFour.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        imgAnimalFour.clearAnimation();
                        return true;

                }
                return false;
            }
        });
        imgAnimalFive.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgAnimalFive.startAnimation(animation);
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(4).getwPathImage()));
                        txtWorddialog.setText(listImageGame.get(4).getwEng());

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(5);
                                if(check_finish == 6)
                                {
                                    EventWin();
                                    dialogWin.show();
                                }
                            }
                        });

                        final ImageView img_markFive = (ImageView) findViewById(R.id.img_markFive);
                        img_markFive.setVisibility(View.INVISIBLE);
                        imgAnimalFive.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        imgAnimalFive.clearAnimation();
                        return true;
                }
                return false;
            }
        });

        imgAnimalSix.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgAnimalSix.startAnimation(animation);
                        dialogGame();
                        imgAnimalDialog.setImageDrawable(getBitmapResource(listImageGame.get(5).getwPathImage()));
                        txtWorddialog.setText(listImageGame.get(5).getwEng());

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(6);
                                if(check_finish == 6) {
                                    EventWin();
                                    dialogWin.show();
                                }
                            }
                        });

                        final ImageView img_markSix = (ImageView) findViewById(R.id.img_markSix);
                        img_markSix.setVisibility(View.INVISIBLE);
                        imgAnimalSix.setEnabled(false);
                        return true;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        imgAnimalSix.clearAnimation();
                        return true;

                }
                return false;
            }
        });
    }
    private void dialogGame(){
        dialogGame = new Dialog(ChoosingObjectActivity.this);
        dialogGame.setContentView(R.layout.activity_game_mh7_dialog);
        dialogGame.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        imgAnimalDialog = (ImageView) dialogGame.findViewById(R.id.imgAnimalDialog);
        txtWorddialog = (TextView) dialogGame.findViewById(R.id.txtWorddialog);
    }
    private  void Speech(){
        txtWord = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        txtWord.setLanguage(Locale.UK);
                    }
            }
        });
    }
    private void EventWin() {
        txtScoreWin.setText(String.valueOf(SCORE_ALL));
//        imbNextGameWin.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        imbNextGameWin.setSelected(!imbNextGameWin.isSelected());
//                        imbNextGameWin.isSelected();
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        imbNextGameWin.setSelected(false);
//                        Intent intent = new Intent(getApplicationContext(),LoadingGoInGameActivity.class);
//                        Bundle sendScore = new Bundle();
//                        sendScore.putInt("score",SCORE_ALL);
//                        intent.putExtra("pictutepuzzle",sendScore);
//                        return true;
//                }
//                return false;
//            }
//        });
    }

    private void ktcheck_finish() {
        if (check_finish == 6){
            check_finish = 0;
            //Toast.makeText(getApplicationContext(), "Dc 6 lan: " + check_finish, Toast.LENGTH_LONG).show();
        }
        else{
            check_finish++;
            //Toast.makeText(getApplicationContext(), "so lan click" + check_finish, Toast.LENGTH_SHORT).show();
        }
    }
    private void doSaveScore() {
    }

    //Check Result
    private void getResult(int choose) {
        if (choose == 1) {
            SCORE_ONE += 100;
        }else if(choose == 2) {
            SCORE_TWO += 100;
        }else if(choose == 3) {
            SCORE_THREE += 100;
        }else if(choose == 4) {
            SCORE_FOUR += 100;
        }else if(choose == 5) {
            SCORE_FIVE += 100;
        }else if(choose == 6) {
            SCORE_SIX += 100;
        }
        SCORE_ALL = SCORE_ONE + SCORE_TWO + SCORE_THREE + SCORE_FOUR + SCORE_FIVE + SCORE_SIX;
        txtScore.setText(String.valueOf(SCORE_ALL));
        doSaveScore();
    }

}
