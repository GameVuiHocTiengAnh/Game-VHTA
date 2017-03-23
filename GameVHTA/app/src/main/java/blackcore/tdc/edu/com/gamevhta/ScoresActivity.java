package blackcore.tdc.edu.com.gamevhta;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.button.BackButton;
import blackcore.tdc.edu.com.gamevhta.data_models.DbAccessHelper;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;
import blackcore.tdc.edu.com.gamevhta.models.Score;
import blackcore.tdc.edu.com.gamevhta.my_adapter.AdapterScore;
import blackcore.tdc.edu.com.gamevhta.service.MusicService;

public class ScoresActivity extends AppCompatActivity {

    private BackButton imgBack;
    AdapterScore adapter;
    private ArrayList<Score> list = new ArrayList<Score>();

    private MediaPlayer mScore;
    private MusicService mService = new MusicService();
    private DbAccessHelper dbAccessHelper;
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

    protected void onPause(){
        mService.pauseMusic(mScore);
        super.onPause();

    }
    protected  void onResume(){
        mService.playMusic(mScore);
        super.onResume();
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

        mScore = MediaPlayer.create(getApplicationContext(),R.raw.score);

        musicScore();


    }

    public void musicScore(){
        mService.playMusic(mScore);
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
