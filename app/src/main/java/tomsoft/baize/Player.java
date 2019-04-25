package tomsoft.baize;

import java.util.Stack;

public class Player
{
    private int id;
    private String name;
    private int score;
    private Stack<Break> breaks;
    private Stack<Frame> frames;

    // for statistics
    private int totalPoints;
    private int pottedBalls;
    private int shotCount;
    private Break highest;

    public Player(String name, int id) {
        this.id = id;
        this.name = name;
        this.score = 0;
        breaks = new Stack<Break>();
        frames = new Stack<Frame>();

        this.totalPoints = this.pottedBalls = this.shotCount = 0;
        highest = new Break();
    }

    public int getId() { return this.id; }
    public String getName() { return this.name; }

    public int getScore() { return this.score; }
    public void setScore(int score) { this.score = score; }
    public void addScore(int value) { this.score += value; }
    public void subtractScore(int value) {
        this.score -= value;
        if( this.score < 0 ) this.score = 0;
    }

    public Break currentBreak() { return breaks.peek(); }
    public void breakPop() { if( !breaks.isEmpty() ) breaks.pop(); }    // pops last break from stack

    public int getFrames() { return frames.size(); }
    public int getTotalPoints() { return totalPoints; }
    public void changeTotalPoints( int offset ) { totalPoints += offset; }
    public int getPottedBalls() { return pottedBalls; }
    public void changePottedBalls( int offset ) { pottedBalls += offset; }
    public void changeShotCount( int offset ) { shotCount += offset; }
    public float getPotSuccess() { return ( shotCount > 0 ) ? (float)pottedBalls/(float)shotCount : 0 ; }
    public Break getHighest() { return highest; }

    public void miss() {
        breaks.push(new Break());
        shotCount++;
    }
    public void pot(Ball potted) {
        addScore(potted.getValue());
        currentBreak().add(potted);
        totalPoints += potted.getValue();
        shotCount++;
        pottedBalls++;
        if( currentBreak().getBreak() >= highest.getBreak() )
            highest = currentBreak();
    }
    public void win(Frame f) {
        frames.push(f);
    }

}
