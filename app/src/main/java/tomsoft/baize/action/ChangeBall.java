package tomsoft.baize.action;
import tomsoft.baize.*;

// Used for changing quantity of given ball on table
// (typically just reds). Pass NEW ball to constructor
// with quantity = number of balls added/removed!!

public class ChangeBall extends Action {
    public ChangeBall(Player p, Ball b) {
        super(p, b);
        this.type = "ChangeBall";
    }
}
