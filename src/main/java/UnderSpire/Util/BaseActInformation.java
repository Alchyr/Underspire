package UnderSpire.Util;

//For loading from json
public class BaseActInformation {
    public String CHECK_INFO;
    public String[][] ACT_OPTIONS; //Arraylist of possible act option sets; Add "* " before options automatically
    public int[] STATE_OPTIONS; //The act options/state changes linked to a certain state (to avoid repition of same things)
    public int SPAREABLE_STATE; //Any state >= this state is sparable
    public String[][] ACT_TEXTS; //Text that appears when doing certain act options; Undertale String ID
    public int[][] STATE_CHANGES; //Array of Array of state changes based on act options
    public String[][] STATE_TEXTS; //What shows up at start of turn based on current state, randomly chosen from array
    //Should be an Undertale String ID for UndertaleText constructor
}
