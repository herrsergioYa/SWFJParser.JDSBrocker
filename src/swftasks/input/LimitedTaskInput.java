/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input;

import swftasks.TaskInput;
import java.util.Random;

/**
 *
 * @author HerrSergio
 */
public class LimitedTaskInput implements TaskInput {
    
    private TaskInput taskInput;
    private int limit, count;

    public LimitedTaskInput(TaskInput taskInput, int limit) {
        this.taskInput = taskInput;
        this.limit = limit;
    }

    @Override
    public double getTime() {
        return taskInput.getTime();
    }

    @Override
    public int getCategory() {
        return taskInput.getCategory();
    }

    @Override
    public boolean next(Random random) {
        count++;
        return taskInput.next(random);
    }

    @Override
    public boolean has() {
        return (limit < 0 || count <= limit) && taskInput.has();
    }

    public TaskInput getTaskInput() {
        return taskInput;
    }

    public int getLimit() {
        return limit;
    }

    public int getCount() {
        return count;
    }
    
    
    
    
}
