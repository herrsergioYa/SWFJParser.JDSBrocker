/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable;

import java.util.Random;
import swftasks.RigidTask;
import swftasks.RigidTaskComplicity;
import swftasks.Task;

/**
 *
 * @author HerrSergio
 */
public class DefaultMoldableTaskComplicity extends AbstractMoldableTaskComplicity {

    private MoldabilityProvider provider;

    public DefaultMoldableTaskComplicity(RigidTaskComplicity taskComplicity, int minWidth, int maxWidth, MoldabilityProvider provider) {
        super(taskComplicity, minWidth, maxWidth);
        this.provider = provider;
    }

    public DefaultMoldableTaskComplicity(RigidTaskComplicity taskComplicity, MoldabilityProvider provider) {
        super(taskComplicity);
        this.provider = provider;
    }
    
    
    @Override
    public Task get(double incomeTime, int category, Random random) {
        RigidTask task = new RigidTask(incomeTime, getSquare(random, category), category);
        return provider.getTask(task, getMinWidth(), getMaxWidth(), random);
    }
    
}
