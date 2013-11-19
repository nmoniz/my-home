package com.natercio.myhome.profiler;

/**
 * Created with IntelliJ IDEA.
 * User: natercio
 * Date: 9/27/13
 * Time: 1:07 PM
 */

public abstract class ProfilerTask {

    private boolean finished = false;

    private synchronized void setFinished(boolean finished) {
        this.finished = finished;
    }

    protected abstract void task();

    public synchronized boolean isFinished() {
        return finished;
    }

    public void execute() {
        if (!isFinished()) {
            task();
            setFinished(true);
        }
    }

}
