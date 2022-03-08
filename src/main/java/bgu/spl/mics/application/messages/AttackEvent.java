package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Attack;
import bgu.spl.mics.application.passiveObjects.Ewoks;

public class AttackEvent implements Event<Boolean> {
    Future<Boolean> f;
    private Attack atk;
    boolean completed;
    boolean lastAttack = false;
    Ewoks ewoks = Ewoks.getEwoks();

    public AttackEvent() {
        completed = false;
        this.f = new Future<Boolean>();

    }
    public void setLast()
    {
        this.lastAttack=true;
    }
    public boolean isLastAttack() {
        return lastAttack;
    }

    public void setLastAttack(boolean lastAttack) {
        this.lastAttack = lastAttack;
    }

    public void load () throws InterruptedException {
                ewoks.FetchIfFree(atk.getSerials());
        }

    public final Attack getAtk() {
        return atk;
    }

    public final void setAtk(Attack atk) {
        this.atk = atk;
    }
    public void attack() throws InterruptedException {
    Thread.sleep(atk.getDuration());
    ewoks.release(atk.getSerials());

    }



}
