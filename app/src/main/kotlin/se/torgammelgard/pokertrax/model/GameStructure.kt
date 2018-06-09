package se.torgammelgard.pokertrax.model

/**
 * TODO: Class header comment.
 */
class GameStructure {
    var id: Long = 0
    // blinds and ante stored in cents
    var small_blind: Int = 0
    var big_blind: Int = 0
    var ante: Int = 0

    override fun toString(): String {
        val str: String
        val sb = small_blind.toDouble() / 100
        val bb = big_blind.toDouble() / 100
        val ante = this.ante.toDouble() / 100
        /*String sbStr = Double.toString((double) small_blind / 100);
        String bbStr = Double.toString((double) big_blind / 100);
        String anteStr = Double.toString((double) ante / 100);
        String returnString = sbStr + " - " + bbStr;*/
        if (small_blind % 100 == 0 && big_blind % 100 == 0)
            str = String.format("%.0f - %.0f", sb, bb)
        else
            str = String.format("%.2f - %.2f", sb, bb)
        return if (this.ante == 0)
            String.format("%s", str)
        else
            String.format("%s ante %.2f", str, ante)
    }
}
