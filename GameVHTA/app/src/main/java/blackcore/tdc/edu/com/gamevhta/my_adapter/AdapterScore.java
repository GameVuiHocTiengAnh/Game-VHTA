package blackcore.tdc.edu.com.gamevhta.my_adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.models.Score;

/**
 * Created by phong on 2/27/2017.
 */

public class AdapterScore extends ArrayAdapter<Score>{
    private Activity context;
    private int idLayout;
    private ArrayList<Score> arr;
    public AdapterScore(Activity context, int idLayout, ArrayList<Score> arr) {
        super(context, idLayout, arr);
        this.context = context;
        this.idLayout = idLayout;
        this.arr = arr;
    }
    public View getView(final int position, View convertView, ViewGroup parent){
            convertView = this.context.getLayoutInflater().inflate(idLayout,null);
        ImageView imvMedal = (ImageView) convertView.findViewById(R.id.imgMedal);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        TextView txtScore = (TextView) convertView.findViewById(R.id.txtScore);
        imvMedal.setImageResource(R.drawable.medal);
        txtName.setText(arr.get(position).getName());
        txtScore.setText(arr.get(position).getScore());
        return convertView;
    }
}
