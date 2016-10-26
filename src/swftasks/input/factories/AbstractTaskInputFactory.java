/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input.factories;

import swftasks.TaskInputFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;
import java.util.stream.Collectors;
import mymath.DistributionFactory;
import swfjparser.GWFFile;
import swftasks.input.CombinedTaskInput;
import swftasks.input.NeverTaskInput;
import swftasks.TaskInput;

/**
 *
 * @author HerrSergio
 */
public abstract class AbstractTaskInputFactory implements TaskInputFactory {
    
    private TreeSet<Integer> width;
    private DistributionFactory factory;

    public AbstractTaskInputFactory(DistributionFactory factory, int ... width) {
        this.width = new TreeSet<>(Arrays.stream(width).mapToObj(i -> (Integer)i).collect(Collectors.toList()));
        this.width.add(Integer.MAX_VALUE);
        this.factory = factory;
    }
    
    protected void begin(GWFFile file) {
        
    }
    
    protected abstract TaskInput extract(GWFFile file, int minWidth, int maxWidth, DistributionFactory factory);

    protected void end(GWFFile file) {
        
    }

    
    @Override
    public TaskInput get(GWFFile file) {
        ArrayList<TaskInput> inputs = new ArrayList<>();
        int last = 0;
        begin(file);
        for (int j : width) {
            TaskInput taskInput = extract(file, last + 1, j, factory);
            if (taskInput != null) {
                inputs.add(taskInput);
            }
            last = j;
        }
        end(file);
        if (inputs.size() == 0) {
            return new NeverTaskInput();
        } else if (inputs.size() == 1) {
            return inputs.get(0);
        } else {
            return new CombinedTaskInput(inputs.toArray(new TaskInput[inputs.size()]));
        }
    }    

    TreeSet<Integer> getWidth() {
        return width;
    }

    DistributionFactory getFactory() {
        return factory;
    }   
    
}
