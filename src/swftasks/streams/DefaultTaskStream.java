/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.streams;

import swftasks.TaskStream;
import swftasks.TaskInput;
import java.util.Random;
import swftasks.Task;
import swftasks.RigidTaskComplicity;
import swftasks.TaskComplicity;

/**
 *
 * @author HerrSergio
 */
public class DefaultTaskStream implements TaskStream {
    private TaskInput input;
    private TaskComplicity complicity;
    private Task info;
    private boolean firstTime = true;

    public DefaultTaskStream(TaskInput input, TaskComplicity complicity) {
        this.input = input;
        this.complicity = complicity;
    }

    @Override
    public Task get() {
        return info;
    }

    @Override
    public void next(Random random) {
        if(firstTime) {
            firstTime = false;
        } else {
            if(!input.has())
                return;
        }
        input.next(random);
        if(input.has()) {
            int width = input.getCategory();
            info = complicity.get(input.getTime(), input.getCategory(), random);
//new RigidTask(input.getTime(), complicity.get(random, width, true), width);
        } else {
            info = null;
        }
    }

    public TaskInput getInput() {
        return input;
    }

    public TaskComplicity getComplicity() {
        return complicity;
    }    
}
