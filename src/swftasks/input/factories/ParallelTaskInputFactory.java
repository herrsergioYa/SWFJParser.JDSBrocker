/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import swfjparser.GWFFile;
import swftasks.ParallelFactory;
import swftasks.TaskInput;
import swftasks.TaskInputFactory;
import swftasks.complicity.ComplicityDistribution;
import swftasks.complicity.factories.AbstractRigidTaskComplicityFactory;
import swftasks.input.CombinedTaskInput;
import swftasks.input.NeverTaskInput;

/**
 *
 * @author HerrSergio
 */
public class ParallelTaskInputFactory implements TaskInputFactory, ParallelFactory {

    private ExecutorService service;
    private List<Future<TaskInput>> toAwait;
    private AbstractTaskInputFactory input;

    public ParallelTaskInputFactory(ExecutorService service, AbstractTaskInputFactory input) {
        this.service = service;
        this.input = input;
    }
    
    @Override
    public TaskInput get(GWFFile file) {
        if (toAwait == null) {
            beginGet(file);
        }
        ArrayList<TaskInput> inputs = new ArrayList<>();
        for (Future<TaskInput> input : toAwait) {
            try {
                TaskInput taskInput = input.get();
                if (taskInput != null) {
                    inputs.add(taskInput);
                }
            } catch (InterruptedException | ExecutionException ex) {
                Logger.getLogger(ParallelTaskInputFactory.class.getName()).log(Level.SEVERE, null, ex);
                throw new RuntimeException(ex);
            }
        }
        input.end(file);
        toAwait = null;
        if (inputs.size() == 0) {
            return new NeverTaskInput();
        } else if (inputs.size() == 1) {
            return inputs.get(0);
        } else {
            return new CombinedTaskInput(inputs.toArray(new TaskInput[inputs.size()]));
        }
    }

    @Override
    public void beginGet(GWFFile file) {
        if (toAwait == null) {
            input.begin(file);
            toAwait = new ArrayList<>();
            int last = 0;
            for (int j : input.getWidth()) {
                final int newLast = last + 1;
                toAwait.add(service.submit(() -> input.extract(file, newLast, j, input.getFactory())));
                last = j;
            }
        }
    }
}
