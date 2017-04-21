package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.button.BackButton;
import blackcore.tdc.edu.com.gamevhta.models.BitmapTopic;
import blackcore.tdc.edu.com.gamevhta.topic.FragmentTopics;

public class TopisChoosingActivity extends AppCompatActivity {

    private BackButton btnBack;
    private ImageView btnPrev,btnNext;
    public ArrayList<BitmapTopic> ImageTopics = null;
    public static Integer position = 0;
    private Animation alpha1,alpha3;
    private MediaPlayer mClick,mTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topis_choosing_layout);

        //create image of topics
        ImageTopics = new ArrayList<BitmapTopic>();
        Bitmap animal = BitmapFactory.decodeResource(getResources(),R.drawable.animal);
        Bitmap school = BitmapFactory.decodeResource(getResources(),R.drawable.school);
        Bitmap home = BitmapFactory.decodeResource(getResources(),R.drawable.home);
        Bitmap country = BitmapFactory.decodeResource(getResources(),R.drawable.country);
        Bitmap plants = BitmapFactory.decodeResource(getResources(),R.drawable.plants);

        alpha1 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        alpha3 = AnimationUtils.loadAnimation(this, R.anim.alpha);

        ImageTopics.add(new BitmapTopic("animal",animal));
        ImageTopics.add(new BitmapTopic("school",school));
        ImageTopics.add(new BitmapTopic("home",home));
        ImageTopics.add(new BitmapTopic("country",country));
        ImageTopics.add(new BitmapTopic("plants",plants));


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnPrev = (ImageView) findViewById(R.id.btnPrev);
        btnNext = (ImageView) findViewById(R.id.btnNext);

        btnBack = (BackButton) findViewById(R.id.btnBack);
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        btnBack.setMoveActivity(intent,TopisChoosingActivity.this);

        mClick = MediaPlayer.create(getApplicationContext(),R.raw.click);
        mTopic = MediaPlayer.create(getApplicationContext(),R.raw.picturepuzzle);
        mTopic.start();
        mTopic.setLooping(true);

        //call frist Topic
        this.callFragmentTopics(position);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnNext.startAnimation(alpha3);
                mClick.start();
                if(position >= ImageTopics.size() -1){
                    position = 0;
                    callFragmentTopics(position);

                }else{
                    position++;
                    callFragmentTopics(position);
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPrev.startAnimation(alpha1);
                mClick.start();
                if(position <= 0){
                    position=ImageTopics.size() -1;
                    callFragmentTopics(position);
                }else {
                    position--;
                    callFragmentTopics(position);
                }
            }
        });
    }

    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause(){
        super.onPause();
        try {
            mTopic.pause();
        }catch (Exception e) {
            Log.d("a","a");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mTopic.start();
        mTopic.setLooping(true);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mTopic.start();
        mTopic.setLooping(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mTopic.stop();
            mTopic.release();
        }catch (Exception e) {
            Log.d("a","a");
        }
    }

    private void callFragmentTopics(int position){
        FragmentTopics fragment = new FragmentTopics(ImageTopics.get(position));
        android.support.v4.app.FragmentManager fragManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragTrans = fragManager.beginTransaction();
        fragManager.popBackStack();
        fragTrans.setCustomAnimations(R.anim.slide_up, R.anim.slide_down, R.anim.slide_up, R.anim.slide_down);
        fragTrans.replace(R.id.fragment_map, fragment);
        fragTrans.commit();

    }
}
