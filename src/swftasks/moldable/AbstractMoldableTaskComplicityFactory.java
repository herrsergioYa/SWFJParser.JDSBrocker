/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable;

import swfjparser.GWFFile;
import swftasks.RigidTaskComplicityFactory;
import swftasks.TaskComplicity;
import swftasks.TaskComplicityFactory;

/**
 *
 * @author HerrSergio
 */
public abstract class AbstractMoldableTaskComplicityFactory implements TaskComplicityFactory {

    private RigidTaskComplicityFactory taskComplicityFactory;
    private int minWidth;
    private int maxWidth;

    public AbstractMoldableTaskComplicityFactory(RigidTaskComplicityFactory taskComplicityFactory, int minWidth, int maxWidth) {
        this.taskComplicityFactory = taskComplicityFactory;
        this.minWidth = minWidth;
        this.maxWidth = maxWidth;
    }

    public AbstractMoldableTaskComplicityFactory(RigidTaskComplicityFactory taskComplicityFactory) {
        this(taskComplicityFactory, 1, Integer.MAX_VALUE);
    }
    
        
    @Override
    public abstract AbstractMoldableTaskComplicity get(GWFFile file);

    public int getMinWidth() {
        return minWidth;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public RigidTaskComplicityFactory getTaskComplicityFactory() {
        return taskComplicityFactory;
    }
    
    
}
