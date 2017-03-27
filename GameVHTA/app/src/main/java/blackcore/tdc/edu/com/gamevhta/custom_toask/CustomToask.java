package blackcore.tdc.edu.com.gamevhta.custom_toask;

import android.app.Activity;
import android.app.Application;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import blackcore.tdc.edu.com.gamevhta.R;

/**
 * Created by Hoang on 3/25/2017.
 */

public class CustomToask extends Toast {
    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context The context to use.  Usually your {@link Application}
     *                or {@link Activity} object.
     */
    private ImageView imageView;
    private TextView lblText;
    Toast toast;
    View layout;

    public CustomToask(Activity activity,int idImage, String text) {
        super(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        layout = inflater.inflate(R.layout.activity_custom_toask,
                (ViewGroup) activity.findViewById(R.id.toast_layout_root));

        imageView = (ImageView) layout.findViewById(R.id.image);
        if(idImage != 0)
            imageView.setImageDrawable(activity.getApplicationContext().getResources().getDrawable(idImage));
        else
            imageView.setVisibility(View.GONE);
        lblText = (TextView) layout.findViewById(R.id.text);
        lblText.setText(text);
        toast = new Toast(activity.getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 10, 10);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
