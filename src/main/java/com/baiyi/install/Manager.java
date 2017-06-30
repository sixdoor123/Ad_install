package com.baiyi.install;

import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

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

    public void uploadingInfo(final Context context) {

        if (!SPUtil.getInstance(context).isFirstInstall()) {
            return;
        }
        HttpUtils utils = new HttpUtils(context);
        utils.setUrl("https://117.34.95.103:443/web/v1.0/client/install");
        utils.setContentType(BaseHttp.Application_Json);
        utils.setMothed(BaseHttp.Methed_Post);
        utils.setCharset(BaseHttp.Charset);
        utils.setBodyData(Utils.getAppPostData(context));
        utils.setListener(new IHttpFinishedListener() {
            @Override
            public void onSuccess(boolean b, String result) {

                if (b) {
                    if (Utils.getState(result)) {
                        SPUtil.getInstance(context).setIsFirstInstall(false);

                        FacebookSdk.sdkInitialize(context.getApplicationContext());
                        AppEventsLogger.activateApp(context);
                    }
                }
            }
        });
        utils.start();
    }
}
