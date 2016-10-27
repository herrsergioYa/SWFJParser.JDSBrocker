/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input;

import java.util.Random;
import swftasks.TaskInput;

/**
 *
 * @author HerrSergio
 */
public class DeterministicTaskInput implements TaskInput {

    private int index = -1, limit = -1;
    private double cycle;
    private double[][] data; 

    public DeterministicTaskInput(double[][] data, double cycle, int limit) {
        this.limit = limit;
        this.cycle = cycle;
        this.data = data;
    }

    public DeterministicTaskInput(double[][] data, double cycle) {
        this(data, cycle, -1);
    }
        
    
    @Override
    public double getTime() {
        if(!has())
            return Double.POSITIVE_INFINITY;
        return data[index % data.length][0] + cycle * (index / data.length);
    }

    @Override
    public int getCategory() {
        return (int) Math.round(data[index % data.length][1]);
    }

    @Override
    public boolean next(Random random) {
        index++;
        return has();
    }

    @Override
    public boolean has() {
        return data.length > 0 && (limit < 0 || index < limit);
    }
    
}
