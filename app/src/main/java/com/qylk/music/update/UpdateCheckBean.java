package com.qylk.music.update;

/**
 * {<br />
 * &nbsp; versionInfo:&nbsp;{<br />
 * &nbsp; &nbsp; versionCode: 12,<br />
 * &nbsp; &nbsp; changeLog: "changeLog here",<br />
 * &nbsp; &nbsp; versionName: "v2.3",<br />
 * &nbsp; &nbsp; appUrl: "http://www.qylk.com/v1/music_v2_3.apk",<br />
 * &nbsp; &nbsp; forceUpdate: 0<br />
 * &nbsp; }<br />
 * }
 */
public class UpdateCheckBean {
    int versionCode;
    String changeLog;
    String versionName;
    String appUrl;
    int forceUpdate;
}