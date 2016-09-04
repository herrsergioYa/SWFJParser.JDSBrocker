/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input;

import swftasks.TaskInput;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 *
 * @author HerrSergio
 */
public class CombinedTaskInput implements TaskInput {

    private PriorityQueue<TaskInput> inputs;
    private ArrayList<TaskInput> buf; 
    
    private class InputComparer implements Comparator<TaskInput> {

        @Override
        public int compare(TaskInput t, TaskInput t1) {
            boolean h = t.has();
            boolean h1 = t1.has();
            if(!h && !h1)
                return 0;
            else if(!h)
                return +1;
            else if(!h1)
                return -1;
            else
                return Double.compare(t.getTime(), t1.getTime());
        }
    }

    public CombinedTaskInput(TaskInput ... inputs) {
        this.buf = new ArrayList<>(inputs.length);
        for(TaskInput input : inputs) {
            this.buf.add(input);
        }
    }
    
    
    @Override
    public double getTime() {
        return inputs.peek().getTime();
    }

    @Override
    public int getCategory() {
        return inputs.peek().getCategory();
    }

    @Override
    public boolean next(Random random) {
        adjustNull(random);
        if (!has()) {
            return false;
        }
        TaskInput input = inputs.poll();
        input.next(random);
        inputs.add(input);
        return has();
    }

    @Override
    public boolean has() {
        return inputs.size() > 0 && inputs.peek().has();
    }
    
    protected boolean adjustNull(Random random) {
        if (inputs == null) {
            inputs = new PriorityQueue<>(new InputComparer());
            buf.stream().forEach(ti -> ti.next(random));
            inputs.addAll(buf);
            buf = null;
            return true;
        }
        return false;
    }
}
