/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable.amdal;

import swfjparser.GWFFile;
import swftasks.RigidTaskComplicityFactory;
import swftasks.TaskComplicityFactory;
import swftasks.moldable.AbstractMoldableTaskComplicity;
import swftasks.moldable.AbstractMoldableTaskComplicityFactory;

/**
 *
 * @author HerrSergio
 */
@Deprecated
public class AmdalMoldableTaskComplicityFactory extends AbstractMoldableTaskComplicityFactory {

    private double seq = -1.0;

    public AmdalMoldableTaskComplicityFactory(RigidTaskComplicityFactory taskComplicityFactory, int minWidth, int maxWidth, double seq) {
        super(taskComplicityFactory, minWidth, maxWidth);
        this.seq = seq;
    }

    public AmdalMoldableTaskComplicityFactory(RigidTaskComplicityFactory taskComplicityFactory, double seq) {
        super(taskComplicityFactory);
        this.seq = seq;
    }

    public AmdalMoldableTaskComplicityFactory(RigidTaskComplicityFactory taskComplicityFactory, int minWidth, int maxWidth) {
        super(taskComplicityFactory, minWidth, maxWidth);
    }

    public AmdalMoldableTaskComplicityFactory(RigidTaskComplicityFactory taskComplicityFactory) {
        super(taskComplicityFactory);
    }
    
    
    @Override
    public AmdalMoldableTaskComplicity get(GWFFile file) {
        return new AmdalMoldableTaskComplicity(getTaskComplicityFactory().get(file), getMinWidth(), getMaxWidth(), seq);
    }
        
}
