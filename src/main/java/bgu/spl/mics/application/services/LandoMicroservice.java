package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.terminationMessage;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    long finishTime;
    long duration;
    public LandoMicroservice(long duration) {

        super("Lando");
        this.duration = duration;
    }

    @Override
    protected void initialize() {

        subscribeEvent(BombDestroyerEvent.class, (BombDestroyerEvent c)->{

            try {
                finishTime =c.activate(duration);
            } catch (InterruptedException e) { }
            complete(c, true);
            sendBroadcast(new terminationMessage());
        });



    }



}
