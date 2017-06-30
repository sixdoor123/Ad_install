package com.baiyi.install;

import android.content.Context;
import android.content.SharedPreferences;


public class SPUtil {

    private static SPUtil spUtil;
    private SharedPreferences sharedPreferences;
    private static final String SP_NAME = "Install_Config";
    private static final String Is_First_Install = "Is_First_Install";

    private SPUtil(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static SPUtil getInstance(Context context) {
        if (null == spUtil) {
            spUtil = new SPUtil(context);
        }
        return spUtil;
    }

    public boolean isFirstInstall() {
        return sharedPreferences.getBoolean(Is_First_Install, true);
    }

    public void setIsFirstInstall(boolean isFirst) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Is_First_Install, isFirst);
        editor.commit();
    }
}
