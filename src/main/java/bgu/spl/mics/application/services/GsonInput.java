package bgu.spl.mics.application.services;

import bgu.spl.mics.application.passiveObjects.Attack;

public class GsonInput {
    Attack[] attacks;
    long R2D2;
    long Lando;
    int Ewoks;

    public Attack[] getAttacks() {
        return attacks;
    }

    public long getR2D2() {
        return R2D2;
    }

    public long getLando() {
        return Lando;
    }

    public int getEwoks() {
        return Ewoks;
    }
}
