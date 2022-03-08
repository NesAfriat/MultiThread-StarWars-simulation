package bgu.spl.mics.application.passiveObjects;


/**
 * Passive data-object representing a Diary - in which the flow of the battle is recorded.
 * We are going to compare your recordings with the expected recordings, and make sure
 * that your output makes sense.
 * <p>
 * Do not add to this class nothing but a single constructor, getters and setters.
 */
public class Diary {
    private static Diary diary=new Diary();
    private int totalAttacks;
    private long HanSoloFinish;
    private long C3POFinish;
    private long R2D2Deactivate;
    private long LeiaTerminate;
    private long HanSoloTerminate;
    private long C3POTerminate;
    private long R2D2Terminate;
    private long LandoTerminate;

    private Diary()
    {
        totalAttacks=0;
    }
    public static Diary getDiary()
    {
        return diary;
    }


    public void setTotalAttacks(int totalAttacks) {
        this.totalAttacks = totalAttacks;
    }

    public void setHanSoloFinish(long hanSoloFinish) {
        HanSoloFinish = hanSoloFinish;
    }

    public void setC3POFinishiii(long c3POFinishiii) {
        C3POFinish = c3POFinishiii;
    }

    public void setR2D2Deactivate(long r2D2Deactivate) {
        R2D2Deactivate = r2D2Deactivate;
    }
    public void setTerminate(long timeInMillis,String name)
    {
        switch (name)
        {
            case("Leia"):
                LeiaTerminate= timeInMillis;
                break;
            case("Han"):
                HanSoloTerminate= timeInMillis;
                break;
            case("C3PO"):
                C3POTerminate= timeInMillis;
                break;
            case("R2D2"):
                R2D2Terminate= timeInMillis;
                break;
            case("Lando"):
                LandoTerminate= timeInMillis;
                break;
        }
    }



    //TODO delete getters


    public int getTotalAttacks() {
        return totalAttacks;
    }

    public long getHanSoloFinish() {
        return HanSoloFinish;
    }

    public long getC3POFinish() {
        return C3POFinish;
    }

    public long getR2D2Deactivate() {
        return R2D2Deactivate;
    }

    public long getLeiaTerminate() {
        return LeiaTerminate;
    }

    public long getHanSoloTerminate() {
        return HanSoloTerminate;
    }

    public long getC3POTerminate() {
        return C3POTerminate;
    }

    public long getR2D2Terminate() {
        return R2D2Terminate;
    }

    public static void setDiary(Diary diary) {
        Diary.diary = diary;
    }

    public long getLandoTerminate() {
        return LandoTerminate;
    }
}
