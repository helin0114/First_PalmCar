package com.cango.palmcartreasure.update;

/**
 * Created by cango on 2017/7/11.
 */

public interface ProgressListener {
    void update(long bytesRead, long contentLength, boolean done);
}
