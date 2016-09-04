/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable.ideal;

import java.util.Random;
import swftasks.Task;
import swftasks.moldable.AbstractMoldableTaskComplicity;
import swftasks.RigidTaskComplicity;
import swftasks.TaskComplicity;

/**
 *
 * @author HerrSergio
 */
@Deprecated
public class IdealMoldableTaskComplicity extends AbstractMoldableTaskComplicity {

    public IdealMoldableTaskComplicity(RigidTaskComplicity taskComplicity, int minWidth, int maxWidth) {
        super(taskComplicity, minWidth, maxWidth);
    }

    public IdealMoldableTaskComplicity(RigidTaskComplicity taskComplicity) {
        super(taskComplicity);
    }
  

    @Override
    public IdealMoldableTask get(double incomeTime, int category, Random random) {
        return new IdealMoldableTask(incomeTime, getMinWidth(), getMaxWidth(), getLength(random, category), category);
    }
        
}
