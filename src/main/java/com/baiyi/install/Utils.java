package com.baiyi.install;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static boolean isStringEmpty(String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        } else if ("".equals(str)) {
            return true;
        } else if (str == null) {
            return true;
        }
        return false;
    }

    public static String getAppPostData(Context context) {
        JSONObject object;
        try {
            object = new JSONObject();
            JSONObject infoObject = new JSONObject();
            infoObject.put("app", getInstalledApp(context));
            object.put("infos", infoObject);
            ApplicationInfo applicationInfo = context.getApplicationInfo();
            object.put("appid", applicationInfo.processName);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return object.toString();

    }


    private static String getInstalledApp(Context context) {

        JSONObject object = new JSONObject();

        PackageManager manager = context.getPackageManager();
        List<PackageInfo> packageInfos = manager.getInstalledPackages(0);
        for (int i = 0; i < packageInfos.size(); i++) {
            CharSequence charSequence = packageInfos.get(i).applicationInfo.loadLabel(manager);
            try {
                object.put("a" + (i + 1), charSequence);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object.toString();
    }

    public static int getIntNodeValue(String jsonObject, String name) {

        try {
            JSONObject o = new JSONObject(jsonObject);
            boolean isHas = o.has(name) && (!o.isNull(name));
            return isHas ? o.getInt(name) : 0;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean getState(String jsonObject) {
        int result = getIntNodeValue(jsonObject, "status");
        return result == 1 ? true : false;
    }

    /**
     * {"status":1,"msg":"操作成功","data":[{"adtype":"facebook","appno":"1026188327518665"},{"adtype":"google","appno":"1026188327518665"}]}
     *
     * @param jsonObject
     * @return
     */
    public static ArrayList<String> getResultIds(String jsonObject) {

        ArrayList<String> ids = new ArrayList<>();
        try {
            JSONObject o = new JSONObject(jsonObject);
            JSONArray dataArray = o.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {

                JSONObject dataObject = dataArray.getJSONObject(i);
                ids.add(dataObject.getString("appno"));
            }
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
//gradlew makejar
