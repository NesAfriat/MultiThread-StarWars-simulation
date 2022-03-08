package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

public class C3POLastAttack implements Broadcast {


    private int totalAttacksOfC3PO;
    public C3POLastAttack(int totalAttacks)
    {
        totalAttacksOfC3PO=totalAttacks;
    }
    public int getTotalAttacksOfC3PO() {
        return totalAttacksOfC3PO;
    }

}
