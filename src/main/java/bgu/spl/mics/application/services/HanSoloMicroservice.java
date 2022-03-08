package bgu.spl.mics.application.services;


import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.C3POLastAttack;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.terminationMessage;
import bgu.spl.mics.application.passiveObjects.Diary;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    private int attackCounter;

    public HanSoloMicroservice() {
        super("Han");
    }


    @Override
    protected void initialize() {
        attackCounter=0;
        subscribeEvent(AttackEvent.class, (AttackEvent c) -> {

            c.load();

            try {
                c.attack();
            } catch (InterruptedException e) {
            }
            complete(c, true);

            attackCounter++;
            if (c.isLastAttack()) {
                diary.setHanSoloFinish(System.currentTimeMillis());
            }
        });
        subscribeBroadcast(C3POLastAttack.class,(C3POLastAttack c)-> {
            this.attackCounter= this.attackCounter+c.getTotalAttacksOfC3PO();
            diary.setTotalAttacks(this.attackCounter);
            sendEvent(new DeactivationEvent());
        });
    }
}

