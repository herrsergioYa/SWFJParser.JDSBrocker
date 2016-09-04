/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks;

import swftasks.Task;

/**
 *
 * @author HerrSergio
 */
public class RigidTask implements Task {
    private double incomeTime;
    private double square;
    private int width;

    public RigidTask(double incomeTime, double square, int width) {
        this.incomeTime = incomeTime;
        this.square = square;
        this.width = width;
    }

    @Override
    public double getIncomeTime() {
        return incomeTime;
    }

    public void setIncomeTime(double incomeTime) {
        this.incomeTime = incomeTime;
    }

    public double getSquare() {
        return square;
    }

    public void setSquare(double square) {
        this.square = square;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    
    public double getLength() {
        return getSquare()/getWidth();
    }

    public void setLength(double square) {
        setSquare(getWidth() * square);
    }

    @Override
    public int getMinWidth() {
        return getWidth();
    }

    @Override
    public int getMaxWidth() {
        return getWidth();
    }

    @Override
    public double getLength(int width) {
        if(width != getWidth())
            throw new IllegalArgumentException();
        return getLength();
    }

    @Override
    public double getSquare(int width) {
        if(width != getWidth())
            throw new IllegalArgumentException();
        return getSquare();
    }
    
}
