package com.qylk.music.update;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.google.gson.*;
import com.qylk.music.core.bus.UiBus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

@Singleton
public class UpdateChecker {
    private static final String UPDATE_CHECK_URL = "http://www.qylk.com/v1/update.json";
    private static final String UPDATE_CHECK_TIME = "updateCheckTime";
    private static final String UPDATE_RESULT = "updateCheckResult";
    private final String mVersionName;
    private final int mVersionCode;

    private final SharedPreferences mPreferences;
    private final UiBus uiBus;
    private final HttpClient client;
    private AsyncTask<Void, Void, UpdateCheckBean> mTask;
    private UpdateCheckBean mCheckBean;
    private Gson gson = new Gson();

    @Inject
    public UpdateChecker(Context context, UiBus uiBus, HttpClient client) {
        this.uiBus = uiBus;
        this.client = client;
        mPreferences = context.getSharedPreferences("setting", Context.MODE_PRIVATE);
        PackageInfo pInfo = null;
        try {
            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
        } catch (PackageManager.NameNotFoundException e) {
        }
        mVersionCode = pInfo == null ? 1 : pInfo.versionCode;
        mVersionName = pInfo == null ? "" : pInfo.versionName;
    }

    public void check(boolean manual) {
        UpdateCheckBean checkBean = getCheckBean();
        if (checkBean != null && checkBean.forceUpdate == 1 && checkBean.versionCode > mVersionCode) {
            UpdateEvent updateEvent = new UpdateEvent(TextUtils.equals(checkBean.versionName, mVersionName), checkBean.forceUpdate > 0, manual, getCheckBean().changeLog, getCheckBean().appUrl);
            uiBus.post(updateEvent);
        } else {
            long today = getToday().getTimeInMillis();
            long lastCheckTime = mPreferences.getLong(UPDATE_CHECK_TIME, -1);
            if (manual || today > lastCheckTime) {
                mTask = new CheckUpdateTask(manual);
                mTask.execute();
            }
        }
    }

    public boolean hasNewVersion() {
        UpdateCheckBean checkBean = getCheckBean();
        boolean ret = false;
        if (checkBean != null) {
            ret = checkBean.versionCode > mVersionCode && !TextUtils.equals(checkBean.versionName, mVersionName);
        }
        return ret;
    }

    private Calendar getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear(Calendar.HOUR_OF_DAY);
        calendar.clear(Calendar.MINUTE);
        calendar.clear(Calendar.MILLISECOND);
        return calendar;
    }

    public void stop() {
        if (mTask != null) {
            mTask.cancel(true);
        }
    }

    private UpdateCheckBean getCheckBean() {
        if (mCheckBean == null) {
            if (mPreferences.contains(UPDATE_RESULT)) {
                mCheckBean = gson.fromJson(
                        mPreferences.getString(UPDATE_RESULT, "{}"),
                        UpdateCheckBean.class);
            }
        }
        return mCheckBean;
    }

    private class CheckUpdateTask extends
            AsyncTask<Void, Void, UpdateCheckBean> {
        private final boolean manual;

        public CheckUpdateTask(boolean manual) {
            this.manual = manual;
        }

        @Override
        protected UpdateCheckBean doInBackground(Void... params) {
            HttpEntity entity = null;
            try {
                HttpGet get = new HttpGet(UPDATE_CHECK_URL);
                HttpResponse response = client.execute(get);
                entity = response.getEntity();
                JsonParser parser = new JsonParser();
                JsonElement je = parser.parse(new InputStreamReader(entity.getContent()));
                UpdateCheckBean checkBean = gson.fromJson(je.getAsJsonObject().get("versionInfo"),
                        UpdateCheckBean.class);
                return checkBean;
            } catch (Exception e) {
            } finally {
                if (entity != null) {
                    try {
                        entity.consumeContent();
                    } catch (IOException e) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(UpdateCheckBean result) {
            super.onPostExecute(result);
            if (result != null) {
                mCheckBean = result;
                String json = gson.toJson(result);
                mPreferences.edit()
                        .putLong(UPDATE_CHECK_TIME, System.currentTimeMillis())
                        .putString(UPDATE_RESULT, json).commit();
                if (result.versionCode > mVersionCode && !TextUtils.equals(result.versionName, mVersionName)) {
                    UpdateEvent updateEvent = new UpdateEvent(TextUtils.equals(mCheckBean.versionName, mVersionName), mCheckBean.forceUpdate > 0, manual, mCheckBean.changeLog, mCheckBean.appUrl);
                    uiBus.post(updateEvent);
                }
            }
        }
    }
}
