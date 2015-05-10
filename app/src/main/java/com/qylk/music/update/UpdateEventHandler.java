package com.qylk.music.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;
import com.qylk.music.R;
import com.squareup.otto.Subscribe;

import java.io.File;

public class UpdateEventHandler implements DialogInterface.OnClickListener {
    private final Activity activity;

    public UpdateEventHandler(Activity activity) {
        this.activity = activity;
    }

    private static void startInstallActivity(Context context, String filePath) {
        File installFile = null;
        if (!TextUtils.isEmpty(filePath)) {
            installFile = new File(filePath);
        }
        if (installFile == null || !installFile.exists()) {
            Toast.makeText(context, String.format("对不起，找不到安装文件，请到官网下载最新版本~"), Toast.LENGTH_LONG).show();
        } else {
            try {
                Intent notificationIntent = new Intent();
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                notificationIntent.setAction(Intent.ACTION_VIEW);
                notificationIntent.setDataAndType(Uri.fromFile(installFile), "application/vnd.android.package-archive");
                context.startActivity(notificationIntent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "对不起，安装失败，请稍候再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            startInstallActivity(activity, "/mnt/sdcard/music.apk");
        }
    }

    @Subscribe
    public void handleUpdateEvent(UpdateEvent event) {
        showDialog(event);
    }

    public Dialog showDialog(UpdateEvent event) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setPositiveButton("安装", this);

        builder.setTitle("一个新版本已准备好安装");
        builder.setNegativeButton("稍后", this);
        builder.setCancelable(true);

        AlertDialog dialog = builder.create();
        TextView textView = (TextView) LayoutInflater.from(dialog.getContext()).inflate(
                R.layout.update_dialog_textview, null);
        textView.setText(TextUtils.isEmpty(event.changeLog) ? null : Html.fromHtml(event.changeLog));
        dialog.setView(textView);
        dialog.show();
        return dialog;
    }
}
