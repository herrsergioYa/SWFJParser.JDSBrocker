/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.complicity;

import java.util.Random;
import swftasks.RigidTaskComplicity;
import swftasks.Task;

/**
 *
 * @author HerrSergio
 */
public class DeterministicTaskComplicityDistribution extends ComplicityDistribution implements RigidTaskComplicity {
    private double[] data;
    private int index;

    public DeterministicTaskComplicityDistribution(boolean square, double[] data) {
        super(square, null);
        this.data = data;
    }
    
    @Override
    public double get(Random random, int width, boolean square) {
        if(this.isSquare() == square)
            return data[index ++ % data.length];
        else if(square)
            return data[index ++ % data.length] * width;
        else
            return data[index ++ % data.length] / width;
    }
    
}
