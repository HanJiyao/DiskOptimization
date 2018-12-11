package algorithm;
import java.util.Properties;

public class DiskParameter {
    private int previous;
    private int current;
    private int sequence[];
    private int cylinders;

    public int getPrevious() {
        return previous;
    }

    public void setPrevious(int previous) {
        this.previous = previous;
    }

    int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    int[] getSequence() {
        return sequence;
    }

    public void setSequence(int[] sequence) {
        this.sequence = sequence;
    }

    public int getCylinders() {
        return cylinders;
    }

    public void setCylinders(int cylinders) {
        this.cylinders = cylinders;
    }

    DiskParameter(Properties p){
        this.cylinders = Integer.parseInt(p.getProperty("Cylinders"));
        this.current = Integer.parseInt(p.getProperty("Position.Current"));
        this.previous = Integer.parseInt(p.getProperty("Position.Previous"));
        String token[] = p.getProperty("Sequence").split(",");
        sequence = new int[token.length];
        for(int i=0;i<token.length;i++){
            sequence[i] = Integer.parseInt(token[i]);
        }
    }
}
