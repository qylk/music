package com.qylk.music.core.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ComponentBus extends Bus {

    private final Executor mExecutorService = Executors.newSingleThreadExecutor();

    public ComponentBus() {
        super(ThreadEnforcer.ANY);
    }

    @Override
    public void post(final Object event) {
        mExecutorService.execute(new Runnable() {
            @Override
            public void run() {
                ComponentBus.super.post(event);
            }
        });
    }
}
