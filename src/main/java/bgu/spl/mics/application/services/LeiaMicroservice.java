package bgu.spl.mics.application.services;

import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MessageBusImpl;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.terminationMessage;
import bgu.spl.mics.application.passiveObjects.Attack;

/**
 * LeiaMicroservices Initialized with Attack objects, and sends them as
 * { AttackEvents}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvents}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LeiaMicroservice extends MicroService {
	private Attack[] attacks;
	
    public LeiaMicroservice(Attack[] attacks) {
        super("Leia");
		this.attacks = attacks;
    }

    @Override
    protected void initialize() {
        AttackEvent atk;
        for(int i =0; i<attacks.length-2;i++){
            atk = new AttackEvent();
            atk.setAtk(attacks[i]);
            sendEvent(atk);
        }
        for(int j = attacks.length-2; j<attacks.length;j++){
            atk = new AttackEvent();
            atk.setAtk(attacks[j]);
            atk.setLast();
            sendEvent(atk);
        }


    }

}
