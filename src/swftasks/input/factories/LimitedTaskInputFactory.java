/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.input.factories;

import swftasks.TaskInputFactory;
import swfjparser.GWFFile;
import swftasks.input.LimitedTaskInput;

/**
 *
 * @author HerrSergio
 */
public class LimitedTaskInputFactory  implements TaskInputFactory {

    private TaskInputFactory taskInputFactory;
    private int limit;

    public LimitedTaskInputFactory(TaskInputFactory taskInputFactory, int limit) {
        this.taskInputFactory = taskInputFactory;
        this.limit = limit;
    }
        
    @Override
    public LimitedTaskInput get(GWFFile file) {
        return new LimitedTaskInput(taskInputFactory.get(file), limit);
    }
    
}
