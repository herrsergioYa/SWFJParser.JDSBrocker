/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable.ideal;

import java.util.Random;
import swftasks.RigidTask;
import swftasks.Task;
import swftasks.moldable.MoldabilityProvider;

/**
 *
 * @author HerrSergio
 */
public class IdealProvider implements MoldabilityProvider {

    @Override
    public IdealMoldableTask getTask(RigidTask task, int minWidth, int maxWidth, Random random) {
        return new IdealMoldableTask(task.getIncomeTime(), minWidth, maxWidth, task.getLength(), task.getWidth());
    }
    
}
