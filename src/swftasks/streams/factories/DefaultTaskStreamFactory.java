/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.streams.factories;

import swftasks.TaskStreamFactory;
import swfjparser.GWFFile;
import swftasks.TaskInputFactory;
import swftasks.streams.DefaultTaskStream;
import swftasks.TaskStream;
import swftasks.RigidTaskComplicity;
import swftasks.TaskComplicityFactory;

/**
 *
 * @author HerrSergio
 */
public class DefaultTaskStreamFactory implements TaskStreamFactory {

    private TaskInputFactory taskInputFactory;
    private TaskComplicityFactory taskComplicityFactory;

    public DefaultTaskStreamFactory(TaskInputFactory taskInputFactory, TaskComplicityFactory taskComplicityFactory) {
        this.taskInputFactory = taskInputFactory;
        this.taskComplicityFactory = taskComplicityFactory;
    }
        
    @Override
    public DefaultTaskStream get(GWFFile file) {
        return new DefaultTaskStream(taskInputFactory.get(file), taskComplicityFactory.get(file));
    }
    
}
