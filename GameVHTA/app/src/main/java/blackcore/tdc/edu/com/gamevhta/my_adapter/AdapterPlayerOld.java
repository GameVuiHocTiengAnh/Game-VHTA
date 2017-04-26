package blackcore.tdc.edu.com.gamevhta.my_adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import blackcore.tdc.edu.com.gamevhta.R;
import blackcore.tdc.edu.com.gamevhta.models.PlayerOld;

/**
 * Created by Shiro on 4/26/2017.
 */

public class AdapterPlayerOld extends ArrayAdapter<PlayerOld> {
    private Activity contex;
    private ArrayList<PlayerOld> arr = null;
    private int idLayout ;
    public AdapterPlayerOld(@NonNull Activity context, @LayoutRes int resource, ArrayList<PlayerOld> arr) {
        super(context, resource, arr);
        this.contex = context;
        this.idLayout = resource;
        this.arr = arr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = contex.getLayoutInflater().inflate(idLayout,null);
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
