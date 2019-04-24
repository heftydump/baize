package tomsoft.baize;

import java.util.Stack;

public class Player
{
    private int id;
    private String name;
    private int score;
    private Stack<Break> breaks;
    private Stack<Frame> frames;

    public Player(String name, int id) {
        this.id = id;
        this.name = name;
        this.score = 0;
        breaks = new Stack<Break>();
        frames = new Stack<Frame>();
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

    public void miss() {
        breaks.push(new Break());
    }
    public void pot(Ball potted) {
        addScore(potted.getValue());
        currentBreak().add(potted);
    }
    public void win(Frame f) {
        frames.push(f);
    }

}
