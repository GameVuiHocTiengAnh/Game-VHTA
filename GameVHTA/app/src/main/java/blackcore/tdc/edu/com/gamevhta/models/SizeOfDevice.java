package blackcore.tdc.edu.com.gamevhta.models;

import android.content.res.Resources;

/**
 * Created by Shiro on 24/02/2017.
 */

public  class SizeOfDevice {

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

}
