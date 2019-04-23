package tomsoft.baize.action;
import tomsoft.baize.*;

public class Foul extends Action {
    public Foul(Player p, Ball b) {
        super(p, b);
        type = "Foul";
    }
}
