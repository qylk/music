package com.qylk.music.core;

import com.qylk.music.core.bus.BackgroundBus;
import com.qylk.music.core.bus.ComponentBus;
import com.qylk.music.core.bus.UiBus;

/**
 * Created by qylk on 15/5/5.
 */
public class BusCenter {
    private static BusCenter sInstance = new BusCenter();

    private UiBus uiBus;
    private BackgroundBus backgroundBus;
    private ComponentBus componentBus;

    public static BusCenter getInstance() {
        return sInstance;
    }

    public UiBus provideUiBus() {
        if (uiBus == null) {
            synchronized (UiBus.class) {
                if (uiBus == null) {
                    uiBus = new UiBus();
                }
            }
        }
        return uiBus;
    }

    public BackgroundBus provideBackgroundBus() {
        if (backgroundBus == null) {
            synchronized (BackgroundBus.class) {
                if (backgroundBus == null) {
                    backgroundBus = new BackgroundBus();
                }
            }
        }
        return backgroundBus;
    }

    public ComponentBus provideComponentBus() {
        if (componentBus == null) {
            synchronized (ComponentBus.class) {
                if (componentBus == null) {
                    componentBus = new ComponentBus();
                }
            }
        }
        return componentBus;
    }
}
