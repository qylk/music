package com.qylk.music.core.bus;

import android.os.Handler;
import android.os.Looper;
import com.squareup.otto.Bus;

/**
 * Created with IntelliJ IDEA.
 * User: Ponyets
 * Date: 13-2-19
 * Time: 下午3:01
 * All callback will be received in mainThread. This event uiBus should only be used to fire UI related event.
 */
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
