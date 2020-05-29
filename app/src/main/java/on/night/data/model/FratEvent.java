package on.night.data.model;

import java.sql.Time;

public class FratEvent {
    private Time openTime;
    private boolean openStatus;

    public FratEvent(Time time, boolean status) {
        openTime = time;
        openStatus = status;
    }

    public void setOpenTime(Time time) {
        openTime.setTime(time.getTime());
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenStatus(boolean status) {
        openStatus = status;
    }

    public boolean getOpenStatus() {
        return openStatus;
    }

    public String toString(){
        return "Frat opens at " + openTime.toString();
    }
}
