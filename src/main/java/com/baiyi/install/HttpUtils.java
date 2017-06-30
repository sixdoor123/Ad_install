package com.baiyi.install;


import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class HttpUtils extends BaseHttp implements Runnable {
    private IHttpFinishedListener listener = null;
    private Context mContext = null;

    public HttpUtils(Context context) {
        this.mContext = context;
    }

    public void start() {
        if (!NetWorkUtils.isNetworkAvailable(mContext)) {
            if (listener != null) {
                listener.onSuccess(false, null);
            }
        } else {
            executorService.submit(this);
        }
    }


    public void setListener(IHttpFinishedListener listener) {
        this.listener = listener;
    }

    @Override
    public void run() {
        OutputStream out = null;
        BufferedReader in = null;
        HttpURLConnection connection = null;
        try {
            if (Utils.isStringEmpty(getUrl())) {
                if (listener != null) {
                    listener.onSuccess(false, null);
                }
                return;
            }
            URL url = new URL(getUrl());
            if (url.getProtocol().toUpperCase().equals("HTTPS")) {
                SSLFactory.sslInit();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                connection = https;
            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            connection.setConnectTimeout(300000);
            connection.setReadTimeout(300000);
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(getMothed() == null ? BaseHttp.Methed_Post : getMothed());
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Charset", getCharset());
            connection.setRequestProperty("Content-DataType", getContentType() == null ? BaseHttp.Application_Json : getContentType());
            connection.addRequestProperty("Content-type", getContentType() == null ? BaseHttp.Application_Json : getContentType());
            connection.setRequestProperty("Accept", BaseHttp.Application_Json);
            connection.connect();
            if (getBodyData() != null) {
                out = connection.getOutputStream();
                byte[] bytes = getBodyData().getBytes(getCharset() == null ? BaseHttp.Charset : getCharset());
                out.write(bytes, 0, bytes.length);
                out.flush();
            }
            int code = connection.getResponseCode();
            if (code == 200) {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = in.readLine()) != null) {
                    sb.append(line.trim());
                }
                if (listener != null) {
                    listener.onSuccess(true, sb.toString());
                }
            } else {
                if (listener != null) {
                    listener.onSuccess(false, null);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onSuccess(false, null);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
            }

        }
    }

}
