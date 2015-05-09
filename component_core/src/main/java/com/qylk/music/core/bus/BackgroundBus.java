package com.qylk.music.core.bus;

import android.os.Looper;
import android.os.Process;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BackgroundBus extends Bus {

    private final Executor mExecutorService = Executors.newFixedThreadPool(3, new LowPriorityThreadFactory());

    public BackgroundBus() {
        super(ThreadEnforcer.ANY);
    }

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mExecutorService.execute(new Runnable() {
                @Override
                public void run() {
                    BackgroundBus.super.post(event);
                }
            });
        } else {
            super.post(event);
        }
    }

    private static class LowPriorityThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(final Runnable r) {
            return new Thread(new Runnable() {

                @Override
                public void run() {
                    Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                    r.run();
                }
            });
        }
    }
}
