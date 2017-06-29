package com.commonsware.cwac.cam2.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class SlackHelper {
  private final String deviceId;

  public SlackHelper(Context context) {
    deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
  }

  public void log(List<String> messages) {
    String message = String.format("DeviceID: %s", deviceId);
    for (String text: messages) {
      message += String.format("\n%s", text);
    }
    sendMessageToSlack(message);
    Log.e(SlackHelper.class.getSimpleName(), message);
  }

  public void sendMessageToSlack(String message) {
    String url = "https://hooks.slack.com/services/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";
    new AsyncTask<String, Void, Void>() {
      @Override
      protected Void doInBackground(String... params) {
        String url = params[0];
        String message = params[1];
        post(url, message);
        return null;
      }
    }.execute(url, message);
  }

  private void post(String urlString, String message){
    Log.e(SlackHelper.class.getSimpleName(), urlString);
    try {
      JSONObject jsonParam = new JSONObject();
      jsonParam.put("text", message);
      jsonParam.put("username", "Heallo Error alert");
      URL url;
      url = new URL (urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(5000);
      conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("POST");

      OutputStream os = conn.getOutputStream();
      os.write(jsonParam.toString().getBytes("UTF-8"));
      os.close();
      conn.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
      Log.e(SlackHelper.class.getSimpleName(), Log.getStackTraceString(e));
    }
  }
}
