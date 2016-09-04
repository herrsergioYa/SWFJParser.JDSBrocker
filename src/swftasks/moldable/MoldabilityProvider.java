/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.moldable;

import java.util.Random;
import swftasks.RigidTask;
import swftasks.Task;

/**
 *
 * @author HerrSergio
 */
public interface MoldabilityProvider {
    Task getTask(RigidTask task, int minWidth, int maxWidth, Random random);
}
