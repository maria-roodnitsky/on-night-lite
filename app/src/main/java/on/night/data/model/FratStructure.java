package on.night.data.model;

public class FratStructure {

    private String Name;
    private String Nickname;
    private boolean Open;
//    private FratEvent fratEvent;

    public FratStructure(String Name, boolean Open, String Nickname) {
        this.Name = Name;
        this.Open = Open;
        this.Nickname = Nickname;
//        fratEvent = null;

    }

    public void setOpenStatus(boolean status) {
        Open = status;
    }

    public boolean getOpenStatus() {
        return Open;
    }

    public String getFratName() {
        return Name;
    }

    public String getFratNickname() {
        return Nickname;
    }



//    public void setFratEvent(FratEvent fratEvent) {
//        this.fratEvent = fratEvent;
//    }

//    public FratEvent getFratEvent() {
//        return fratEvent;
//    }

    public String toString(){
        if (Open) {
            return Name + " is open!";
        } else {
            return Name + " is closed!";
        }
    }
}
