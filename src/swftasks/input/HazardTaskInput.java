/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input;

import swftasks.TaskInput;
import java.util.Random;
import swfjparser.SWFFile;

/**
 *
 * @author HerrSergio
 */
public class HazardTaskInput implements TaskInput {
    private TaskInput input;
    private double[] hazard;
    private double step;
    private double time, value;

    public HazardTaskInput(TaskInput input, double[] hazard, double step) {
        this.input = input;
        this.hazard = hazard;
        this.step = step;
    }

    @Override
    public double getTime() {
        return time;
    }

    @Override
    public int getCategory() {
        return input.getCategory();
    }

    @Override
    public boolean next(Random random) {
        boolean ret = input.next(random);
        if(ret) {
            double buf = input.getTime();
            time = SWFFile.getTime(hazard, step, time, buf - value);
            value = buf;
        }
        return ret;
    }

    @Override
    public boolean has() {
        return input.has();
    }

    public TaskInput getInput() {
        return input;
    }    
}
