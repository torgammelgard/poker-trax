package se.torgammelgard.pokertrax;

/**
 * TODO: Class header comment.
 */
public class Game_Structure {
    private long id;
    // blinds and ante stored in cents
    private int small_blind;
    private int big_blind;
    private int ante;

    public Game_Structure(){}

    public int getAnte() {
        return ante;
    }

    public void setAnte(int ante) {
        this.ante = ante;
    }

    public int getSmall_blind() {
        return small_blind;
    }

    public void setSmall_blind(int small_blind) {
        this.small_blind = small_blind;
    }

    public int getBig_blind() {
        return big_blind;
    }

    public void setBig_blind(int big_blind) {
        this.big_blind = big_blind;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String str;
        double sb = (double) small_blind / 100;
        double bb = (double) big_blind / 100;
        double ante = (double) this.ante / 100;
        /*String sbStr = Double.toString((double) small_blind / 100);
        String bbStr = Double.toString((double) big_blind / 100);
        String anteStr = Double.toString((double) ante / 100);
        String returnString = sbStr + " - " + bbStr;*/
        if ((small_blind % 100) == 0 && (big_blind % 100) == 0)
            str =  String.format("%.0f - %.0f", sb, bb);
        else
            str = String.format("%.2f - %.2f", sb, bb);
        return (this.ante == 0) ?
                String.format("%s", str) :
                String.format("%s ante %.2f", str, ante);
    }
}
