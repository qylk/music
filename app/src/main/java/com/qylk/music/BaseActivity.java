package com.qylk.music;

import com.qylk.music.core.bus.BackgroundBus;
import com.qylk.music.core.bus.UiBus;
import com.qylk.music.update.UpdateEventHandler;
import roboguice.activity.RoboActivity;

import javax.inject.Inject;

/**
 * Created by qylk on 15/5/10.
 */
public class BaseActivity extends RoboActivity {
    @Inject
    protected UiBus uiBus;
    @Inject
    protected BackgroundBus backgroundBus;
    private UpdateEventHandler updateEventHandler = new UpdateEventHandler(this);

    @Override
    public void onStart() {
        super.onStart();
        uiBus.register(this);
        uiBus.register(updateEventHandler);
    }

    @Override
    public void onStop() {
        uiBus.unregister(this);
        uiBus.unregister(updateEventHandler);
        super.onStop();
    }
}
