/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.chooser;

import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;
import swftasks.Task;

/**
 *
 * @author HerrSergio
 */
public interface TaskChooser {
    IdentityHashMap<? extends Task, Integer> choose(Collection<? extends Task> queue, int width);
    int onIncome(Task task, Collection<? extends Task> queue, int width);
}
