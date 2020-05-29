package on.night.data.model;

public class FratStructure {

    private String fratName;
    private boolean open;
    private FratEvent fratEvent;

    public FratStructure(String name) {
        fratName = name;
        open = false;
        fratEvent = null;

    }

    public void openFrat(){
        open = true;
    }

    public void closeFrat() {
        open = false;
    }

    public boolean getOpenStatus() {
        return open;
    }

    public String getFratName() {
        return fratName;
    }

    public void setFratEvent(FratEvent fratEvent) {
        this.fratEvent = fratEvent;
    }

    public FratEvent getFratEvent() {
        return fratEvent;
    }

    public String toString(){
        if (open) {
            return fratName + " is open!";
        } else {
            return fratName + " is closed!";
        }
    }
}
