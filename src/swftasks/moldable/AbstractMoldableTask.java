/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable;

import swftasks.Task;

/**
 *
 * @author HerrSergio
 */
public abstract class AbstractMoldableTask implements Task {

    private double incomeTime; 
    private int minWidth, maxWidth;

    public AbstractMoldableTask(double incomeTime, int minWidth, int maxWidth) {
        this.incomeTime = incomeTime;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
    }
    
    
    @Override
    public double getIncomeTime() {
        return incomeTime;
    }

    @Override
    public int getMinWidth() {
        return minWidth;
    }

    @Override
    public int getMaxWidth() {
        return maxWidth;
    }

    @Override
    public double getSquare(int width) {
        return getLength(width) * width;
    }
    
}
