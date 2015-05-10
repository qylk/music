package com.qylk.music.update;

public class UpdateEvent {
    public final boolean isFix;
    public final boolean forceUpdate;
    public final boolean manual;
    public final String changeLog;
    public final String url;

    public UpdateEvent(boolean isFix, boolean forceUpdate, boolean manual, String changeLog, String url) {
        this.isFix = isFix;
        this.forceUpdate = forceUpdate;
        this.manual = manual;
        this.changeLog = changeLog;
        this.url = url;
    }
}
