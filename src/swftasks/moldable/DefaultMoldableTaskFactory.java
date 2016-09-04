/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable;

import swfjparser.GWFFile;
import swftasks.RigidTaskComplicityFactory;

/**
 *
 * @author HerrSergio
 */
public class DefaultMoldableTaskFactory extends AbstractMoldableTaskComplicityFactory {

    private MoldabilityProvider provider;

    public DefaultMoldableTaskFactory(RigidTaskComplicityFactory taskComplicityFactory, int minWidth, int maxWidth, MoldabilityProvider provider) {
        super(taskComplicityFactory, minWidth, maxWidth);
        this.provider = provider;
    }

    public DefaultMoldableTaskFactory(RigidTaskComplicityFactory taskComplicityFactory, MoldabilityProvider provider) {
        super(taskComplicityFactory);
        this.provider = provider;
    }
    
    @Override
    public DefaultMoldableTaskComplicity get(GWFFile file) {
        return new DefaultMoldableTaskComplicity(getTaskComplicityFactory().get(file), getMinWidth(), getMaxWidth(), provider);
    }
    
}
