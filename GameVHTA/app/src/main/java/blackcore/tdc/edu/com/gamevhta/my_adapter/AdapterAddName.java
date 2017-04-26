package blackcore.tdc.edu.com.gamevhta.my_adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.models.PlayerOld;
import blackcore.tdc.edu.com.gamevhta.models.Score;
import blackcore.tdc.edu.com.gamevhta.models.ScoreObject;

/**
 * Created by phong on 2/27/2017.
 */

public class AdapterAddName extends ArrayAdapter<Score> {
    private Activity context;
    private int idLayout;
    private ArrayList<PlayerOld> arr;

    public AdapterAddName(Activity context, int idLayout, ArrayList<PlayerOld> arr) {
        super(context, idLayout);
        this.context = context;
        this.idLayout = idLayout;
        this.arr = arr;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = this.context.getLayoutInflater().inflate(idLayout, null);
        TextView txtName = (TextView) convertView.findViewById(R.id.txtName);
        TextView txtLevel = (TextView) convertView.findViewById(R.id.txtLevel);
        if(arr.size() > 0 && position >= 0) {
            txtName.setText(arr.get(position).getName());
            String text = arr.get(position).getLvPass();
            String[] textLv = text.split("_");
            txtLevel.setText(textLv[0] +" : "+textLv[1]);
        }
        return convertView;
    }
}
