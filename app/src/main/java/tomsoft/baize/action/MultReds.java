package tomsoft.baize.action;
import tomsoft.baize.*;

// Used when multiple reds are potted. Pass NEW red
// to constructor, with quantity = number of reds
// potted during shot!!

public class MultReds extends Action {
    public MultReds(Player p, Ball b) {
        super(p,b);
        this.type = "MultReds";
    }
}
