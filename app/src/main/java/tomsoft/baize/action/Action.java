package tomsoft.baize.action;
import tomsoft.baize.*;

public abstract class Action {
    protected String type;     // type of action (not really used)
    protected int id;          // who performed the action?
    protected Ball b;          // on which ball?

    public Action() {
    }
    public Action(Player p, Ball b) {
        this.id = p.getId();
        this.b = b;
    }
    public Action(Player p) {
        this.id = p.getId();
    }

    // GETTERS and SETTERS
    public String getType() { return type; }
    public int getPlayerID() { return id; }
    public Ball getBall() { return b; }
}
