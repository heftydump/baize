package tomsoft.baize;

import java.util.Stack;
import tomsoft.baize.action.*;

public class Frame {
    private int id;
    private Stack<Action> history;

    public Frame( int id, Stack<Action> frameHistory ) {
        this.id = id;
        history = new Stack<Action>();
        for( Action a : frameHistory )
            history.push(a);
    }
    public int getId() { return this.id; }
    public Stack<Action> getActionHistory() { return this.history; }
}
