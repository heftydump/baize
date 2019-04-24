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
    public String lastAction() { return (!history.isEmpty()) ? history.peek().getType() : "None"; }

    // FRAME METHODS
    public void newFrame() {
        // starts a new frame
        rerack();
        history.clear();
        plyr[0].setScore(0); plyr[1].setScore(0);
        plyr[0].miss(); plyr[1].miss();
        currentState = State.RED;
    }
    public void score(Ball potted) {
        // deals with pots

        // is 'potted' the ball on?
        if( currentState == State.RED && potted.getValue() != red
                || currentState == State.COLOUR && potted.getValue() < yel
                || currentState == State.CLEARANCE && potted.getValue() != lowestAvailableColour() )
            return;
        history.push(new Pot(plyr[turn], potted));
        plyr[turn].pot(potted);
        if( potted.getValue() == red ) {
            potted.removeOne();
            currentState = State.COLOUR;
        }
        else if( potted.getValue() > red ) {
            if( currentState == State.CLEARANCE )
                potted.removeOne();
            currentState = (ball[red].getQuantity() < 1) ? State.CLEARANCE : State.RED;
        }
        proceed();
    }
    public void miss() {
        // ends player turn
        plyr[turn].miss();
        history.push(new Miss(plyr[turn]));
        next();
        currentState = ( ball[red].getQuantity() < 1 ) ? State.CLEARANCE : State.RED;
        proceed();
    }
    public void foul(Ball fouled) {
        // deals with fouls
        plyr[turn].miss();
        history.push(new Foul(plyr[turn], fouled));
        next();
        plyr[turn].addScore( (fouled.getValue() < 4) ? 4 : fouled.getValue() );
        currentState = ( ball[red].getQuantity() < 1 ) ? State.CLEARANCE : State.RED;
    }
    public void next() {
        // switches turn
        turn = ( turn < 1 ) ? turn + 1 : 0;
    }
    public void proceed() {
        // continues frame
        if( ball[blk].getQuantity() == 0 )
            if( difference() == 0 )
                ball[blk].addOne();
            else
                endFrame();
    }
    public void undo() {
        if( history.isEmpty() )
            return;

        Action lastAction = history.pop();
        if( lastAction instanceof Pot ) {
            // remove potted points from score, remove potted ball from break
            plyr[ lastAction.getPlayerID() ].subtractScore( lastAction.getBall().getValue() );
            plyr[ lastAction.getPlayerID() ].currentBreak().remove();
            // if clearance or red, place back on table
            if( currentState == State.CLEARANCE || lastAction.getBall().getValue() == red )
                lastAction.getBall().addOne();
        }
        else if( lastAction instanceof Miss || lastAction instanceof Foul ) {
            // pop most recent break, subtract penalty points if foul
            if( lastAction instanceof Foul )
                plyr[turn].subtractScore( ( lastAction.getBall().getValue() < 4 ) ? 4 : lastAction.getBall().getValue() );
            plyr[ lastAction.getPlayerID() ].breakPop();
            // set turn back to previous player
            turn = lastAction.getPlayerID();
        }
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
        // put all the balls back on the table
        ball[1].setQuantity(15);
        for( int i=2; i<8; i++ )
            ball[i].setQuantity(1);
    }

    // MATCH-RELATED METHODS
    public int lowestAvailableColour() {
        // returns value of lowest available colour (-1 if table empty)
        for( int i=2; i<8; i++ ) {
            if (ball[i].getQuantity() > 0)
                return ball[i].getValue();
        }
        return -1;
    }
    public int difference() {
        return java.lang.Math.abs(plyr[0].getScore()-plyr[1].getScore());
    }
    public int remaining() {
        int sum = ball[red].getQuantity()*8;
        for( int i=2; i<8; i++ )
            sum += ball[i].getValue()*ball[i].getQuantity();
        return sum;
    }
}
