package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class DeactivationEvent implements Event<Boolean> {

    public void activate(long duration) throws InterruptedException {
        Thread.sleep(duration);
        throw new InterruptedException("Finished Deactivation");
    }

}
