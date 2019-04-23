package tomsoft.baize;
import tomsoft.baize.action.*;
import java.util.Stack;

public class Match {
    public static final int red=1, yel=2, grn=3, brn=4, blu=5, pnk=6, blk=7;

    private Player[] plyr;  // players taking part
    private Ball[] ball;    // balls on the table
    private int goal;       // number of frames needed for win
    private int turn;       // who's at the table?
    private int frameCount; // how many frames?

    enum State {
        RED,
        COLOUR,
        CLEARANCE
    }
    private State currentState;

    private Stack<Action> history;  // action history

    public Match(String p1, String p2, int bestOf) {
        plyr = new Player[2];
        plyr[0] = new Player(p1, 0);
        plyr[1] = new Player(p2, 1);

        ball = new Ball[8];
        ball[1] = new Ball(1,15);
        for( int i=2; i<8; i++ )
            ball[i] = new Ball(i,1);

        history = new Stack<Action>();

        goal = (bestOf + 1)/2;
        turn = 0;
        frameCount = 0;
    }

    public Player player( int playerId ) { return plyr[playerId]; }
    public Ball ball( int index ) { return ball[index]; }
    public int goal() { return goal; }
    public int atTable() { return turn; }
    public State state() { return currentState; }

    // FRAME METHODS
    public void newFrame() {
        // starts a new frame
        rerack();
        history.clear();
        plyr[0].miss(); plyr[1].miss();
        currentState = State.RED;
    }
    public void score(Ball potted) {
        // deals with pots
        plyr[turn].pot(potted);
        if( potted.getValue() == red )
            potted.removeOne();
    }
    public void miss() {
        // ends player turn
        plyr[turn].miss();
        next();
    }
    public void foul(Ball fouled) {
        // deals with fouls

    }
    public void next() {
        // switches turn
        turn = ( turn < 1 ) ? turn + 1 : 0;
    }
    public void proceed() {
        // continues frame

    }
    public void endFrame() {
        // ends the frame
        int winner;
        if( plyr[0].getScore() != plyr[1].getScore() ) {
            winner = (plyr[0].getScore() > plyr[1].getScore()) ? 0 : 1;
            plyr[winner].win(new Frame(frameCount, history));
        }
        newFrame();
    }

    // BALL METHODS
    public void rerack() {
        ball[1].setQuantity(15);
        for( int i=2; i<8; i++ )
            ball[i].setQuantity(1);
    }


}
