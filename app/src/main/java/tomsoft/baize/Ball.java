package tomsoft.baize;
import tomsoft.baize.*;

public class Ball {
    private int value;
    private int quantity;

    public Ball(int value, int quantity) {
        this.value = value;
        this.quantity = quantity;
    }
    public Ball(Ball b) {
        this.value = b.value;
        this.quantity = b.quantity;
    }

    public int getValue() { return this.value; }
    public int getQuantity() { return this.quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public void addOne() { this.quantity++; }
    public void removeOne() { if( this.quantity > 0 ) this.quantity--; }
}
