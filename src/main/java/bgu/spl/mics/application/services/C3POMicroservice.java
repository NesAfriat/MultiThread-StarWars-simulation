package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.C3POLastAttack;


/**


 * C3POMicroservices is in charge of the handling {@li
 * This class may not hold references for objects which it is not responsible for:
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class C3POMicroservice extends MicroService {
	
    public C3POMicroservice() {
        super("C3PO");
    }
    private int attackCount;
    @Override
    protected void initialize() {
        attackCount=0;
        subscribeEvent(AttackEvent.class, (AttackEvent c) -> {

            c.load();

            try {
                c.attack();
            } catch (InterruptedException e) {

            }
            complete(c, true);

            attackCount++;
            if(c.isLastAttack()){
                diary.setC3POFinishiii(System.currentTimeMillis());
                sendBroadcast(new C3POLastAttack(attackCount));
            }

        });
    }
}
