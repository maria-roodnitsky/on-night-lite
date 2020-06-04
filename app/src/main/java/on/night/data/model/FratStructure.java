package on.night.data.model;

/**
 * Frat Structure to be held in the database
 */
public class FratStructure {

    // Private instance variables name, nickname, and openstatus
    private String Name;
    private String Nickname;
    private boolean Open;

    /**
     *  Constructor for a fratstructure!
     * @param Name
     * @param Open
     * @param Nickname
     */
    public FratStructure(String Name, boolean Open, String Nickname) {
        this.Name = Name;
        this.Open = Open;
        this.Nickname = Nickname;
    }

    /**
     * Setter for open status
     * @param status
     */
    public void setOpenStatus(boolean status) {
        Open = status;
    }

    /**
     * Getter for open status
     * @return
     */
    public boolean getOpenStatus() {
        return Open;
    }

    /**
     * Getter for Frat Name
     * @return
     */
    public String getFratName() {
        return Name;
    }

    /**
     * Getter for Frat Nickname
     * @return
     */
    public String getFratNickname() {
        return Nickname;
    }

    /**
     * To String override for string representation of a Frat Structure
     * @return
     */
    public String toString(){
        if (Open) {
            return Name + " is open!";
        } else {
            return Name + " is closed!";
        }
    }
}
