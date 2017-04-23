package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.button.BackButton;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.Score;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.my_adapter.AdapterScore;

public class ScoresActivity extends AppCompatActivity {

    private BackButton imgBack;
    AdapterScore adapter;
    private ArrayList<Score> list = new ArrayList<Score>();

    private MediaPlayer mScore;
    private DbAccessHelper dbAccessHelper;

    protected void onPause(){
        super.onPause();
        try{
            mScore.pause();
        }catch (Exception e){
            Log.d("ss","dd");
        }

    }
    protected  void onResume(){
        super.onResume();
        mScore.start();
        mScore.setLooping(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            mScore.stop();
            mScore.release();
        }catch (Exception e){
            Log.d("a","s");
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mScore.start();
        mScore.setLooping(true);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imgBack = (BackButton) findViewById(R.id.btnBack);
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        imgBack.setMoveActivity(intent,ScoresActivity.this);

        ListView listView = (ListView) findViewById(R.id.lvScore);
        //get Score
        dbAccessHelper = new DbAccessHelper(this);
        ArrayList<ScoreObject> listScore =dbAccessHelper.getScoreObject(10);

        adapter = new AdapterScore(this,R.layout.activity_lv_score_item);
        listView.setAdapter(adapter);
        for (ScoreObject so : listScore){
            adapter.add(so);
        }
        adapter.notifyDataSetChanged();

        mScore = MediaPlayer.create(getApplicationContext(),R.raw.picturepuzzle);

        musicScore();


    }

    public void musicScore(){
        mScore.start();
        mScore.setLooping(true);
    }

    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
