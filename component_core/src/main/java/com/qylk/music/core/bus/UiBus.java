package com.qylk.music.core.bus;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;


public class UiBus extends Bus {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    UiBus.super.post(event);
                }
            });
        }
    }
}
