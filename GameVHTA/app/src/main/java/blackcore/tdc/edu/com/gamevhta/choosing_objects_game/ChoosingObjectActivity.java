package blackcore.tdc.edu.com.gamevhta.choosing_objects_game;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.WordObject;


/**
 * Created by Canh on 16/03/2017.
 */

public class ChoosingObjectActivity extends AppCompatActivity  {

    private Animation animation;

    private ImageView imgAnimalOne, imgAnimalTwo, imgAnimalThree, imgAnimalFour, imgAnimalFive, imgAnimalSix, imgAnimalDialog, imbNextGameWin;
    private TextView txtScore, txtWorddialog, txtNameScoreWin,txtScoreWin;
    private Dialog dialogGame, dialogWin;
    //private DbScoreHelper dbScoreHelper;

    private int SCORE_ONE = 0, SCORE_TWO = 0, SCORE_THREE = 0, SCORE_FOUR = 0, SCORE_FIVE = 0, SCORE_SIX = 0, SCORE_ALL = 0;
    private int check_finish = 0;
    private ArrayList<Bitmap> listBitMapAnswer = null;
    private ArrayList<WordObject> listImageFromDataO;
    private ArrayList<WordObject> listImageLevelO;
    private DbAccessHelper dbAccessHelper;


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

        imgAnimalOne = (ImageView) findViewById(R.id.imgAnimalOne);
        imgAnimalTwo = (ImageView) findViewById(R.id.imgAnimalTwo);
        imgAnimalThree = (ImageView) findViewById(R.id.imgAnimalThree);
        imgAnimalFour = (ImageView) findViewById(R.id.imgAnimalFour);
        imgAnimalFive = (ImageView) findViewById(R.id.imgAnimalFive);
        imgAnimalSix = (ImageView) findViewById(R.id.imgAnimalSix);
        txtScore = (TextView) findViewById(R.id.txtScore);
        txtWorddialog = (TextView) findViewById(R.id.txtWorddialog);

        //txtNameScoreWin = (TextView) dialogWin.findViewById(R.id.txtNameScoreWin);
       // txtScoreWin = (TextView) dialogWin.findViewById(R.id.txtScoreWin);
        //imbNextGameWin = (ImageView) dialogWin.findViewById(R.id.imvNextGame);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);

        //dialog game over
        dialogWin = new Dialog(ChoosingObjectActivity.this);
        dialogWin.setCancelable(false);
        dialogWin.setCanceledOnTouchOutside(false);
        dialogWin.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogWin.setContentView(R.layout.activity_dialog_win_game);
        dialogWin.getWindow().setBackgroundDrawableResource(R.color.tran);
        dialogWin.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;


        listBitMapAnswer = new ArrayList<>();
        Bitmap monkey = BitmapFactory.decodeResource(getResources(),R.drawable.screen_3_monkey);
        Bitmap caw = BitmapFactory.decodeResource(getResources(),R.drawable.screen_3_caw);
        Bitmap panda = BitmapFactory.decodeResource(getResources(),R.drawable.screen_3_panda);
        Bitmap pig = BitmapFactory.decodeResource(getResources(),R.drawable.screen_3_pig);
        Bitmap rabbit = BitmapFactory.decodeResource(getResources(),R.drawable.screen_3_rabbit);
        Bitmap tiger = BitmapFactory.decodeResource(getResources(),R.drawable.screen_3_tiger);

        listBitMapAnswer.add(monkey);
        listBitMapAnswer.add(caw);
        listBitMapAnswer.add(panda);
        listBitMapAnswer.add(pig);
        listBitMapAnswer.add(rabbit);
        listBitMapAnswer.add(tiger);
        Collections.shuffle(listBitMapAnswer);

        getAnimationImageButton();
        Answer();
    }
    private void Answer(){

        imgAnimalOne.setImageBitmap(listBitMapAnswer.get(0));
        imgAnimalTwo.setImageBitmap(listBitMapAnswer.get(1));
        imgAnimalThree.setImageBitmap(listBitMapAnswer.get(2));
        imgAnimalFour.setImageBitmap(listBitMapAnswer.get(3));
        imgAnimalFive.setImageBitmap(listBitMapAnswer.get(4));
        imgAnimalSix.setImageBitmap(listBitMapAnswer.get(5));
    }
    private Drawable getBitmapResource(String name) {
        int id = getResources().getIdentifier(name, "drawable", getApplicationContext().getPackageName());
        Log.d("ImageID", "dkm id dau"+ String.valueOf(id));
        Drawable dr;
        if(id != 0)
            dr =getApplicationContext().getResources().getDrawable(id);
        else
            dr =getApplicationContext().getResources().getDrawable(R.drawable.screen_3_caw);
        return  dr;
    }

    private Bitmap getImageBitmap(String imageName) {
        String path = Environment.getExternalStorageDirectory().toString() + "" + imageName + ".jpg";
        File fileImage = new File(path);
        Bitmap bitmap = null;
        if (fileImage.exists()) {
            bitmap = BitmapFactory.decodeFile(fileImage.getAbsolutePath());
        }
        return bitmap;
    }


    private void setFont() {
        Typeface custom_font = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.fontPath));
        txtScore.setTypeface(custom_font);
        txtWorddialog.setTypeface(custom_font);
        txtNameScoreWin.setTypeface(custom_font);
        txtScoreWin.setTypeface(custom_font);
    }

    public void getAnimationImageButton() {
        imgAnimalOne.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        imgAnimalOne.startAnimation(animation);
                        dialogGame = new Dialog(ChoosingObjectActivity.this);
                        dialogGame.setContentView(R.layout.activity_game_mh7_dialog);
                        dialogGame.getWindow().setBackgroundDrawableResource(R.color.tran);
                        dialogGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        imgAnimalDialog = (ImageView) dialogGame.findViewById(R.id.imgAnimalDialog);
                        imgAnimalDialog.setImageBitmap(listBitMapAnswer.get(0));

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish();
                                getResult(1);
                                if(check_finish == 6) {
                                    dialogWin.show();
                                   // EventWin();
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
                        // PRESSED
                        imgAnimalTwo.startAnimation(animation);
                        dialogGame = new Dialog(ChoosingObjectActivity.this);
                        dialogGame.setContentView(R.layout.activity_game_mh7_dialog);
                        dialogGame.getWindow().setBackgroundDrawableResource(R.color.tran);
                        dialogGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        imgAnimalDialog = (ImageView) dialogGame.findViewById(R.id.imgAnimalDialog);
                        imgAnimalDialog.setImageBitmap(listBitMapAnswer.get(1));


                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(2);
                                if(check_finish == 6) {
                                    dialogWin.show();
                                   // EventWin();
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
                        dialogGame = new Dialog(ChoosingObjectActivity.this);
                        dialogGame.setContentView(R.layout.activity_game_mh7_dialog);
                        dialogGame.getWindow().setBackgroundDrawableResource(R.color.tran);
                        dialogGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        imgAnimalDialog = (ImageView) dialogGame.findViewById(R.id.imgAnimalDialog);
                        imgAnimalDialog.setImageBitmap(listBitMapAnswer.get(2));

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(3);
                                if(check_finish == 6) {
                                    dialogWin.show();
                                   // EventWin();
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
                        dialogGame = new Dialog(ChoosingObjectActivity.this);
                        dialogGame.setContentView(R.layout.activity_game_mh7_dialog);
                        dialogGame.getWindow().setBackgroundDrawableResource(R.color.tran);
                        dialogGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        imgAnimalDialog = (ImageView) dialogGame.findViewById(R.id.imgAnimalDialog);
                        imgAnimalDialog.setImageBitmap(listBitMapAnswer.get(3));

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(4);
                                if(check_finish == 6) {
                                    dialogWin.show();
                                    //EventWin();
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
                        dialogGame = new Dialog(ChoosingObjectActivity.this);
                        dialogGame.setContentView(R.layout.activity_game_mh7_dialog);
                        dialogGame.getWindow().setBackgroundDrawableResource(R.color.tran);
                        dialogGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        imgAnimalDialog = (ImageView) dialogGame.findViewById(R.id.imgAnimalDialog);
                        imgAnimalDialog.setImageBitmap(listBitMapAnswer.get(4));

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
                                    dialogWin.show();
                                    //EventWin();
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
                        dialogGame = new Dialog(ChoosingObjectActivity.this);
                        dialogGame.setContentView(R.layout.activity_game_mh7_dialog);
                        dialogGame.getWindow().setBackgroundDrawableResource(R.color.tran);
                        dialogGame.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        imgAnimalDialog = (ImageView) dialogGame.findViewById(R.id.imgAnimalDialog);
                        imgAnimalDialog.setImageBitmap(listBitMapAnswer.get(5));

                        dialogGame.show();
                        final ImageView imgCal = (ImageView) dialogGame.findViewById(R.id.imgCal);
                        imgCal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogGame.dismiss();
                                ktcheck_finish ();
                                getResult(6);
                                if(check_finish == 6) {
                                    dialogWin.show();
                                    //EventWin();
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
    private void EventWin() {
        txtScoreWin.setText(String.valueOf(SCORE_ALL));
        imbNextGameWin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        imbNextGameWin.setSelected(!imbNextGameWin.isSelected());
                        imbNextGameWin.isSelected();
                        return true;
                    case MotionEvent.ACTION_UP:
                        imbNextGameWin.setSelected(false);
                        //Intent intent = new Intent(getApplicationContext(),LoadingGoInGameActivity.class);
                        Bundle sendScore = new Bundle();
                        sendScore.putInt("score",SCORE_ALL);
                        //intent.putExtra("pictutepuzzle",sendScore);
                        return true;
                }
                return false;
            }
        });
    }
    private void ktcheck_finish() {
        if (check_finish == 6){
            check_finish = 0;
            Toast.makeText(getApplicationContext(), "Dc 6 lan: " + check_finish, Toast.LENGTH_LONG).show();
        }
        else{
            check_finish++;
            Toast.makeText(getApplicationContext(), "so lan click" + check_finish, Toast.LENGTH_SHORT).show();
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
