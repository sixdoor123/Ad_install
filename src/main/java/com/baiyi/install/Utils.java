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

    public static boolean isListEmpty(List<?> list) {
        if (list == null || list.size() == 0)
            return true;
        return false;
    }

    public static String getAppPostData(Context context) {
        JSONObject object = new JSONObject();
        try {
            JSONObject infoObject = new JSONObject();
            infoObject.put("app", getInstalledApp(context));
            object.put("infos", infoObject);
            object.put("appid", "727d83e329684a1c8820ac262d4c800b");
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

    public static ArrayList<RequestEntry> getResultIds(String jsonObject) {

        ArrayList<RequestEntry> ids = new ArrayList<>();
        try {
            JSONObject o = new JSONObject(jsonObject);
            JSONArray dataArray = o.getJSONArray("data");
            for (int i = 0; i < dataArray.length(); i++) {
                RequestEntry requestEntry = new RequestEntry();
                JSONObject dataObject = dataArray.getJSONObject(i);
                requestEntry.setAdtype(dataObject.getString("adtype"));
                requestEntry.setAppno(dataObject.getString("appno"));
                ids.add(requestEntry);
            }
            return ids;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static String getAppManifestXml(Context context) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            String oldFbId = info.metaData.getString("com.facebook.sdk.ApplicationId");
            return oldFbId;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
