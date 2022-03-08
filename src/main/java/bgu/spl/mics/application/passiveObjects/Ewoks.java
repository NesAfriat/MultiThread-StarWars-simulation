package bgu.spl.mics.application.passiveObjects;


import java.util.HashMap;
import java.util.List;

/**
 * Passive object representing the resource manager.
 * <p>
 * This class must be implemented as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private methods and fields to this class.
 */
public class Ewoks {
    private static Ewoks ewoks = new Ewoks();
    private HashMap<Integer,Ewok > resources;

    private Ewoks() {
        ewoks = this;
        resources = new HashMap<Integer, Ewok>();
    }
    public void addEwok (Ewok e) {

        resources.put(e.serialNumber, e);
    }
   public synchronized boolean FetchIfFree(List<Integer> serials) throws InterruptedException {
        boolean isInterrupt=false;
        do {
            isInterrupt=false;
            for(Integer i: serials)
            {
                if(!resources.get(i).isAvailable()) {
                    isInterrupt=true;
                    wait();
                }
            }
        }
        while (isInterrupt);
       for(Integer i: serials) {
           fetch(i);
       }
       return true;
        }
    public synchronized void release(List<Integer> serials)
    {
        for(Integer i: serials) {
            resources.get(i).release();
        }
        notifyAll();
    }

    public void fetch (int i) {
        resources.get(i).acquire();
    }

    public static Ewoks getEwoks() {
        return ewoks;
    }
}
