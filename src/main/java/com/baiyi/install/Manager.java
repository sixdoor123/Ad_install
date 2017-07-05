package com.baiyi.install;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;

public class Manager {

    private static Manager manager;

    private Manager() {
    }

    public static Manager getInstance() {
        if (null == manager) {
            manager = new Manager();
        }
        return manager;
    }

    String facebookId;
    String googleId;

    public void uploadingInfo(final Context context) {

        if (!SPUtil.getInstance(context).isFirstInstall()) {
            return;
        }
        setHandler(context);
        HttpUtils utils = new HttpUtils(context);
        utils.setUrl("http://www.gotomob.com/v1.0/client/install");
        utils.setContentType(BaseHttp.Application_Json);
        utils.setMothed(BaseHttp.Methed_Post);
        utils.setCharset(BaseHttp.Charset);
        utils.setBodyData(Utils.getAppPostData(context));
        utils.setListener(new IHttpFinishedListener() {
            @Override
            public void onSuccess(boolean b, String result) {

                if (b && Utils.getState(result)) {
                    try {
                        SPUtil.getInstance(context).setIsFirstInstall(false);
                        ArrayList<RequestEntry> ids = Utils.getResultIds(result);
                        if (!Utils.isListEmpty(ids)) {

                            for (int i = 0; i < ids.size(); i++) {
                                if (ids.get(i).getAdtype().equals("facebook")) {
                                    facebookId = ids.get(i).getAppno();
                                }
                            }
                            if (!Utils.isStringEmpty(facebookId)) {
                                FacebookSdk.setApplicationId(facebookId);
                                FacebookSdk.sdkInitialize(context.getApplicationContext());
                                AppEventsLogger.activateApp(context);
                                handler.sendEmptyMessage(0);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        utils.start();
    }

    private Handler handler;

    private void setHandler(final Context context) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                try {
                    String oldFbId = Utils.getAppManifestXml(context);
                    if (!Utils.isStringEmpty(oldFbId)) {

                        FacebookSdk.setApplicationId(oldFbId);
                        FacebookSdk.sdkInitialize(context.getApplicationContext());
                        AppEventsLogger.activateApp(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
    }

}
