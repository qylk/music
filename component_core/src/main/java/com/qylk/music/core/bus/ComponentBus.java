package com.qylk.music.core.bus;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: wangkang
 * Date: 14-11-04
 * Time: 上午11:24
 * To change this template use File | Settings | File Templates.
 */
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
