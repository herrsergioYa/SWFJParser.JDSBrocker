/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable;

import java.util.Random;
import swftasks.Task;
import swftasks.RigidTaskComplicity;
import swftasks.TaskComplicity;

/**
 *
 * @author HerrSergio
 */
public abstract class AbstractMoldableTaskComplicity implements TaskComplicity {
    
    private RigidTaskComplicity taskComplicity;
    private int maxWidth;
    private int minWidth;

    public AbstractMoldableTaskComplicity(RigidTaskComplicity taskComplicity, int minWidth, int maxWidth) {
        this.taskComplicity = taskComplicity;
        this.maxWidth = maxWidth;
        this.minWidth = minWidth;
    }

    public AbstractMoldableTaskComplicity(RigidTaskComplicity taskComplicity) {
        this(taskComplicity, 1, Integer.MAX_VALUE);
    }

    public abstract Task get(double incomeTime, int category, Random random);


    protected double getLength(Random random, int category) {
        return taskComplicity.get(random, category, false);
    }

    protected double getSquare(Random random, int category) {
        return taskComplicity.get(random, category, true);
    }
    
    public int getMaxWidth() {
        return maxWidth;
    }

    public int getMinWidth() {
        return minWidth;
    }
}
