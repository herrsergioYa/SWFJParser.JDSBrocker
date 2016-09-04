/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable.amdal;

import swftasks.moldable.AbstractMoldableTask;
import swftasks.RigidTaskComplicity;


/**
 *
 * @author HerrSergio
 */
public class AmdalMoldableTask extends AbstractMoldableTask {

    private double square1, seq;

    public AmdalMoldableTask(double incomeTime, int minWidth, int maxWidth, double square1, double seq) {
        super(incomeTime, minWidth, maxWidth);
        this.square1 = square1;
        this.seq = seq;
    }    
    
    public AmdalMoldableTask(double incomeTime, int minWidth, int maxWidth, double length, int width, double seq) {
        this(incomeTime, minWidth, maxWidth, length /(seq + (1 - seq) / width), seq);
    }
    
    @Override
    public double getLength(int width) {
        return square1 * (seq + (1 - seq) / width);
    }
    
}
