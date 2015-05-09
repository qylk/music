package com.qylk.music.core.bus;

public abstract class BaseResponse {
    public Result result;

    public enum Result {
        SUCCESS, TIMEOUT, ERROR
    }
}
