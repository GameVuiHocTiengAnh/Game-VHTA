package blackcore.tdc.edu.com.gamevhta.my_adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;

/**
 * Created by phong on 2/27/2017.
 */

public class AdapterScore extends ArrayAdapter<ScoreObject> {
    private Activity context;
    private int idLayout;

    public AdapterScore(Activity context, int idLayout) {
        super(context, idLayout);
        this.context = context;
        this.idLayout = idLayout;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = this.context.getLayoutInflater().inflate(idLayout, null);
        ImageView imvMedal = (ImageView) convertView.findViewById(R.id.imgMedal);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        TextView txtScore = (TextView) convertView.findViewById(R.id.txtScore);
        if(position<3)
            imvMedal.setImageResource(R.drawable.medal);
        else
            imvMedal = null;
        ScoreObject scoreObject = getItem(position);
        txtName.setText(scoreObject.getsPlayer());
        txtScore.setText(String.valueOf(scoreObject.getsScore()));
        Log.d("ScorePos", String.valueOf(position));
        Log.d("ScorePlay", scoreObject.getsPlayer());
        Log.d("ScoreInt", String.valueOf(scoreObject.getsScore()));
        Log.d("ScoreID", scoreObject.getsID());

        return convertView;
    }
}
