/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable.ideal;

import swfjparser.GWFFile;
import swftasks.RigidTaskComplicityFactory;
import swftasks.moldable.AbstractMoldableTaskComplicity;
import swftasks.moldable.AbstractMoldableTaskComplicityFactory;

/**
 *
 * @author HerrSergio
 */
@Deprecated
public class IdealMoldableTaskComplicityFactory extends AbstractMoldableTaskComplicityFactory {

    public IdealMoldableTaskComplicityFactory(RigidTaskComplicityFactory taskComplicityFactory, int minWidth, int maxWidth) {
        super(taskComplicityFactory, minWidth, maxWidth);
    }

    public IdealMoldableTaskComplicityFactory(RigidTaskComplicityFactory taskComplicityFactory) {
        super(taskComplicityFactory);
    }

    @Override
    public IdealMoldableTaskComplicity get(GWFFile file) {
        return new IdealMoldableTaskComplicity(getTaskComplicityFactory().get(file), getMinWidth(), getMaxWidth());
    }
    
}
