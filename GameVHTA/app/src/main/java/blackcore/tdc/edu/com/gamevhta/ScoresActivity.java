package blackcore.tdc.edu.com.gamevhta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ListView;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.button.BackButton;
import blackcore.tdc.edu.com.gamevhta.models.Score;
import blackcore.tdc.edu.com.gamevhta.my_adapter.AdapterScore;

public class ScoresActivity extends AppCompatActivity {

    private BackButton imgBack;
    AdapterScore adapter;
    private ArrayList<Score> list = new ArrayList<Score>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores_layout);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imgBack = (BackButton) findViewById(R.id.btnBack);
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        imgBack.setMoveActivity(intent,ScoresActivity.this);

        ListView listView = (ListView) findViewById(R.id.lvScore);

        list.add(new Score("Người Chơi 1 ","500"));
        list.add(new Score("Người Chơi 2 ","1000"));
        list.add(new Score("Người Chơi 3 ","1500"));
        list.add(new Score("Người Chơi 4 ","2000"));
        list.add(new Score("Người Chơi 5 ","2500"));
        list.add(new Score("Người Chơi 6 ","3000"));
        list.add(new Score("Người Chơi 7 ","3500"));
        list.add(new Score("Người Chơi 8 ","4000"));
        list.add(new Score("Người Chơi 9 ","4500"));
        list.add(new Score("Người Chơi 10 ","5000"));

        adapter = new AdapterScore(this,R.layout.activity_lv_score_item,list);
        listView.setAdapter(adapter);


    }

    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
