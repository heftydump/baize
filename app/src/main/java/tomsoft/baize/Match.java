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
        CLEARANCE,
        FREE_BALL
    }
    private Stack<State> stateHistory;
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
        stateHistory = new Stack<State>();

        goal = (bestOf + 1)/2;
        turn = 0;
        frameCount = 0;
    }

    public Player player( int playerId ) { return plyr[playerId]; }
    public Ball ball( int index ) { return ball[index]; }
    public int goal() { return goal; }
    public int atTable() { return turn; }
    public State state() { return stateHistory.peek(); }
    public Action lastAction() { return ( !history.isEmpty() ) ? history.peek() : new Miss(plyr[1]); }

    // FRAME METHODS
    public void newFrame() {
        // starts a new frame
        rerack();
        history.clear();
        plyr[0].setScore(0); plyr[1].setScore(0);
        plyr[0].miss(); plyr[1].miss();
        plyr[0].changeShotCount(-1); plyr[1].changeShotCount(-1);
        changeState(State.RED);
    }
    public void score(Ball potted) {
        // deals with pots

        // is 'potted' the ball on?
        if( state() == State.RED && potted.getValue() != red
                || state() == State.COLOUR && potted.getValue() < yel
                || state() == State.CLEARANCE && potted.getValue() != lowestAvailableColour()
                || state() == State.FREE_BALL && potted.getValue() != lowestAvailableBall())
            return;
        history.push(new Pot(plyr[turn], potted));
        plyr[turn].pot(potted);
        if( potted.getValue() == red || ( potted.getValue() > red && state() == State.CLEARANCE ) || state() == State.FREE_BALL )
            potted.removeOne();
        proceed();
    }
    public void miss() {
        // ends player turn
        history.push(new Miss(plyr[turn]));
        if( state() == State.FREE_BALL ) {
            int value = ( ball[red].getQuantity() < 1 ) ? lowestAvailableColour() : red ;
            ball[value].removeOne();
        }
        plyr[turn].miss();
        next();
        proceed();
    }
    public void foul(Ball fouled) {
        // deals with fouls
        plyr[turn].miss();
        history.push(new Foul(plyr[turn], fouled));
        if( state() == State.FREE_BALL ) {
            int value = ( ball[red].getQuantity() < 1 ) ? lowestAvailableColour() : red ;
            ball[value].removeOne();
        }
        next();
        plyr[turn].addScore( (fouled.getValue() < 4) ? 4 : fouled.getValue() );
        proceed();
    }
    public void next() {
        // switches turn
        turn = ( turn < 1 ) ? turn + 1 : 0;
    }
    public void proceed() {
        // continues frame, determines next state from action history
        if( ball[blk].getQuantity() == 0 ) {
            if (difference() == 0)
                ball[blk].addOne();
            else
                endFrame();
        }
        if( history.isEmpty() ) {
            changeState(State.RED);
            return;
        }
        if( history.peek() instanceof Pot ) {
            if( history.peek().getBall().getValue() == red )
                changeState(State.COLOUR);
            else
                changeState( ball[red].getQuantity() < 1 ? State.CLEARANCE : State.RED );
        }
        else if( history.peek() instanceof ChangeBall && state() == State.COLOUR )
            return;
        else if( history.peek() instanceof FreeBall ) {
            changeState(State.FREE_BALL);
            int value = (ball[red].getQuantity() > 0) ? 1 : lowestAvailableColour();
            ball[value].addOne();
        }
        else
            changeState(ball[red].getQuantity() < 1 ? State.CLEARANCE : State.RED);
    }
    public void undo() {
        if( history.isEmpty() )
            return;

        Action lastAction = history.pop();
        if( lastAction instanceof Pot ) {
            // remove potted points from score, remove potted ball from break
            plyr[ lastAction.getPlayerID() ].subtractScore( lastAction.getBall().getValue() );
            plyr[ lastAction.getPlayerID() ].currentBreak().remove();
            plyr[ lastAction.getPlayerID() ].changePottedBalls(-1);
            plyr[ lastAction.getPlayerID() ].changeTotalPoints(-lastAction.getBall().getValue());
            plyr[ lastAction.getPlayerID() ].changeShotCount(-1);
            // if clearance or red, place back on table
            if( lastAction.getBall().getValue() == red || lowestAvailableColour() > 2 )
                lastAction.getBall().addOne();
        }
        else if( lastAction instanceof Miss || lastAction instanceof Foul ) {
            // pop most recent break, subtract penalty points if foul
            if( lastAction instanceof Foul )
                plyr[turn].subtractScore( ( lastAction.getBall().getValue() < 4 ) ? 4 : lastAction.getBall().getValue() );
            plyr[ lastAction.getPlayerID() ].breakPop();
            plyr[ lastAction.getPlayerID() ].changeShotCount(-1);
            // set turn back to previous player
            turn = lastAction.getPlayerID();
        }
        else if( lastAction instanceof ChangeBall ) {
            int offset = lastAction.getBall().getQuantity();
            int value = lastAction.getBall().getValue();
            ball(value).setQuantity( ball(value).getQuantity() - offset );
        }
        else if( lastAction instanceof FreeBall ) {
            int value = ( ball[red].getQuantity() > 0 ) ? 1 : lowestAvailableColour();
            ball[value].removeOne();
        }
        proceed();
    }
    public void changeState(State st) { stateHistory.push(st); }
    public void concede() {
        next();
        plyr[turn].win(new Frame(frameCount, history));
        newFrame();
        frameCount++;
    }
    public void endFrame() {
        // ends the frame
        int winner;
        if( plyr[0].getScore() != plyr[1].getScore() ) {
            winner = (plyr[0].getScore() > plyr[1].getScore()) ? 0 : 1;
            plyr[winner].win(new Frame(frameCount, history));
        }
        newFrame();
        frameCount++;
    }

    // BALL METHODS
    public void rerack() {
        // put all the balls back on the table
        ball[1].setQuantity(15);
        for( int i=2; i<8; i++ )
            ball[i].setQuantity(1);
    }
    public void changeReds(int offset) {
        history.push(new ChangeBall(plyr[turn], new Ball(red, offset)));
        ball[red].setQuantity(ball[red].getQuantity()+offset);
        proceed();
    }

    // MATCH-RELATED METHODS
    public void freeBall() {
        if( history.isEmpty() || !( history.peek() instanceof Foul) )
            return;
        history.push(new FreeBall(plyr[turn]));
        proceed();
    }
    public int lowestAvailableColour() {
        // returns value of lowest available colour (-1 if table empty)
        for( int i=2; i<8; i++ ) {
            if (ball[i].getQuantity() > 0)
                return ball[i].getValue();
        }
        return -1;
    }
    public int lowestAvailableBall() {
        // returns value lowest available BALL (or -1)
        for( int i=1; i<8; i++ ) {
            if (ball[i].getQuantity() > 0) return ball[i].getValue();
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
    public int snookers() {
        double lowestFoul = ( lowestAvailableColour() < 4) ? 4 : lowestAvailableColour();
        return (int) java.lang.Math.ceil( ( difference()-remaining() ) / lowestFoul );
    }
}
