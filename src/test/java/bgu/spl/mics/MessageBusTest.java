package bgu.spl.mics;

import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.terminationMessage;
import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageBusTest {
    MessageBusImpl mb;
    MicroService han = new HanSoloMicroservice();
    MicroService C3P0 = new C3POMicroservice();
    @BeforeEach
    public void setUp() {
        mb = MessageBusImpl.getMessegeBus();
    }
    @AfterEach
    void tearDown (){
        mb.broadcastSubscribers.clear();
        mb.eventsSubscribers.clear();
        mb.msQueue.clear();
        mb.eventsFuture.clear();

    }
    /* Important notes - data structures to implement
    broadcastSubscribers - keeps the existed broadcasts with the participated microServices
    eventSubscribers - keeps the types of event with the microservice type who can handle it
    msQueue - keeps the registered microservices with their queue of waiting messages
    eventsFurture - keeps every event with its associated future
     */

    @Test
    void sendBroadcast() throws InterruptedException {
        mb.register(han);
        mb.register(C3P0);
        terminationMessage global = new terminationMessage();
        mb.subscribeBroadcast(terminationMessage.class,han);
        mb.subscribeBroadcast(terminationMessage.class,C3P0);
        assertTrue(mb.msQueue.get(han.getName()).isEmpty());
        assertTrue(mb.msQueue.get(C3P0.getName()).isEmpty());
        mb.sendBroadcast(global);
        assertEquals(mb.msQueue.get(han.getName()).remove(),global);
        assertEquals(mb.msQueue.get(C3P0.getName()).remove(),global);
    }

    @Test
    void subscribeEvent() {
        mb.register(han);
        mb.register(C3P0);
        assertTrue(mb.eventsSubscribers.isEmpty());
        mb.subscribeEvent(AttackEvent.class,C3P0);
        assertFalse(mb.eventsSubscribers.isEmpty());
        assertFalse(mb.eventsSubscribers.get(AttackEvent.class).contains(han));
        mb.subscribeEvent(AttackEvent.class,han);
        assertTrue(mb.eventsSubscribers.get(AttackEvent.class).contains(han));
    }

    @Test
    void subscribeBroadcast() {
        mb.register(han);
        mb.register(C3P0);
        assertTrue(mb.broadcastSubscribers.isEmpty());
        mb.subscribeBroadcast(terminationMessage.class,C3P0);
        assertFalse(mb.broadcastSubscribers.isEmpty());
        assertFalse(mb.broadcastSubscribers.get(terminationMessage.class).contains(han));
        mb.subscribeBroadcast(terminationMessage.class,han);
        assertTrue(mb.broadcastSubscribers.get(terminationMessage.class).contains(han));
    }


    @Test
    void complete() throws InterruptedException {
        AttackEvent attack1= new AttackEvent();
        mb.subscribeEvent(AttackEvent.class,han);
        mb.register(han);
        assertTrue(mb.eventsFuture.isEmpty());
        Future f1 = mb.sendEvent(attack1);
        assertFalse(mb.eventsFuture.get(attack1).isDone());
        mb.complete(attack1, true);
        assertTrue(mb.eventsFuture.get(attack1).isDone());




    }

    @Test
    void sendEvent() throws InterruptedException {
        mb.register(han);
        AttackEvent attack1= new AttackEvent();
        mb.subscribeEvent(AttackEvent.class, han);
        assertTrue(mb.msQueue.get("Han").isEmpty());
        mb.sendEvent(attack1);
        assertFalse(mb.msQueue.get("Han").isEmpty());
        assertEquals(attack1,mb.msQueue.get("Han").remove());

    }


    @Test
    void awaitMessage() throws InterruptedException {
        AttackEvent attack1= new AttackEvent();
        AttackEvent attack2= new AttackEvent();
        mb.subscribeEvent(AttackEvent.class, han);
        mb.register(han);

        Future<Boolean> booleanFuture1 = mb.sendEvent(attack1);
        Future<Boolean> booleanFuture2 = mb.sendEvent(attack2);
        Message message2=null,message1;
        try{
            message1 = mb.awaitMessage(han);
            assertTrue(message1.equals(attack1));
            assertFalse(message1.equals(attack2));
        }catch (InterruptedException e) {
        } ;


    }
}
//no need to test: register (still can be part of other tests), unregister, subscribeEvent.
