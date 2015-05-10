package com.qylk.music;

import com.qylk.music.core.bus.BackgroundBus;
import com.qylk.music.core.bus.UiBus;
import roboguice.fragment.RoboListFragment;

import javax.inject.Inject;

/**
 * Created by qylk on 15/5/10.
 */
public class BaseListFragment extends RoboListFragment {
    @Inject
    protected UiBus uiBus;
    @Inject
    protected BackgroundBus backgroundBus;

    @Override
    public void onStart() {
        super.onStart();
        uiBus.register(this);
    }

    @Override
    public void onStop() {
        uiBus.unregister(this);
        super.onStop();
    }
}
