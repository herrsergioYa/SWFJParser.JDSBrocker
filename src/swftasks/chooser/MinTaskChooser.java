/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks.chooser;

import java.util.Collection;
import java.util.IdentityHashMap;
import swftasks.Task;

/**
 *
 * @author HerrSergio
 */
public class MinTaskChooser extends AbstarctTaskChooser {

    public MinTaskChooser() {
        super(false);
    }

    @Override
    public IdentityHashMap<? extends Task, Integer> choose(Collection<? extends Task> tasks, int width) {
        IdentityHashMap<Task, Integer> map = new IdentityHashMap<>();
        for (Task task : tasks) {
            int w = task.getMinWidth();
            if (w > width) {
                continue;
            }
            width -= w;
            map.put(task, w);
            if (width == 0) {
                break;
            }
        }
        return map;
    }
    
    

    @Override
    protected int getWidth(Task task) {
        return task.getMinWidth();
    }

    @Override
    public int onIncome(Task task, Collection<? extends Task> tasks, int width) {
        if (task.getMinWidth() <= width) {
            return task.getMinWidth();
        } else {
            return -1;
        }
    }
    
    
}
