/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable.ideal;

import swftasks.moldable.AbstractMoldableTask;

/**
 *
 * @author HerrSergio
 */
public class IdealMoldableTask extends AbstractMoldableTask {

    private double square;

    public IdealMoldableTask(double incomeTime, int minWidth, int maxWidth, double square) {
        super(incomeTime, minWidth, maxWidth);
        this.square = square;
    }
    
    public IdealMoldableTask(double incomeTime, int minWidth, int maxWidth, double length, int width) {
        this(incomeTime, minWidth, maxWidth, length * width);
    }

    public double getSquare() {
        return square;
    }

    @Override
    public double getSquare(int width) {
        return square; 
    }

    @Override
    public double getLength(int width) {
        return square / width;
    }    
    
}
