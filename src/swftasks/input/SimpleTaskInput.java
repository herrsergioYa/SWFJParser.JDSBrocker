/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input;

import swftasks.TaskInput;
import distributions.Distribution;
import distributions.PhaseDistribution;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author HerrSergio
 */
public class SimpleTaskInput implements TaskInput { 
    private double[] width;
    private Distribution input;
    private int w;
    private double time;
    private int limit;
    private int count;

    public SimpleTaskInput(double[] width, Distribution input, int limit) {
        this.width = width;
        this.input = input;
        this.limit = limit;
    }

    public SimpleTaskInput(double[] width, Distribution input) {
        this(width, input, -1);
    }
    
    public SimpleTaskInput(Distribution input, int limit) {
        this(new double[] {0.0, 1.0}, input, limit);
    }

    public SimpleTaskInput(Distribution input) {
        this(input, -1);
    }   

    protected void calculate(Random random) {
        w = width.length;
        while(w == width.length)
            w = PhaseDistribution.sim(random, width);
        time += input.next(random);
        count++;
    }
    
    @Override
    public double getTime() {
        return time;
    }

    @Override
    public int getCategory() {
        return w;
    }

    @Override
    public boolean next(Random random) {
        calculate(random);
        return has();
    }

    @Override
    public boolean has() {
        return time < Double.POSITIVE_INFINITY && (limit < 0 || count <= limit);
    }
    
    
    
}
