package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class BombDestroyerEvent implements Event<Boolean> {

    public long activate(long duration) throws InterruptedException {
        Thread.sleep(duration);
        return System.currentTimeMillis();
    }

}
