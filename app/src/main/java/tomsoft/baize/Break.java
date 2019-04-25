package tomsoft.baize;

import java.util.Stack;

public class Break {
    Stack<Ball> balls;

    public Break() {
        balls = new Stack<>();
    }
    public Break( Break brk ) {
        balls = new Stack<>();
        for( Ball b : brk.balls )
            this.balls.push(b);
    }

    public void add(Ball b) {
        balls.push(b);
    }
    public void remove() {
        balls.pop();
    }
    public Ball peek() { return balls.peek(); }
    public void clear() {
        balls.clear();
    }

    public int getBreak() {
        int sum = 0;
        for( Ball i : balls )
            sum += i.getValue();
        return sum;
    }
    public int getPottedQuantity(Ball b) {
        int sum = 0;
        for( Ball i : balls)
            sum =  (i.getValue() == b.getValue() ) ? sum + 1 : sum ;
        return sum;
    }
    public boolean isEmpty() { return balls.isEmpty(); }
}
