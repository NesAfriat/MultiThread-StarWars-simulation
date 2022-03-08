package bgu.spl.mics.application.passiveObjects;

import bgu.spl.mics.Future;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EwokTest {
    public Ewok myEwok;
    @BeforeEach
    public void setUp(){

        int i = 1;
        myEwok = new Ewok(i);
    }

    @Test
    void acquire() {
        assertTrue(myEwok.available);
        myEwok.acquire();
        assertFalse(myEwok.available);

    }

    @Test
    void release() {
        assertTrue(myEwok.available);
        myEwok.acquire();
        assertFalse(myEwok.available);
        myEwok.release();
        assertTrue(myEwok.available);

    }
}