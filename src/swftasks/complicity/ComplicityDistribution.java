/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.complicity;

import distributions.Distribution;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author HerrSergio
 */
public class ComplicityDistribution implements Serializable {
    
    private boolean square;
    private Distribution distribution;

    public ComplicityDistribution(boolean square, Distribution distribution) {
        this.square = square;
        this.distribution = distribution;
    }

    public boolean isSquare() {
        return square;
    }

    public Distribution getDistribution() {
        return distribution;
    }
    
    public double get(Random random, int width, boolean square) {
        if(square == isSquare()) {   
            return getDistribution().next(random);
        } else if(square) {
            return getDistribution().next(random) * width;            
        } else {
            return getDistribution().next(random) / width;
        }
    }
     
}
