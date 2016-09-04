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
public abstract class AbstarctTaskChooser implements TaskChooser {
    
    private boolean allowCut;

    public AbstarctTaskChooser(boolean allowCut) {
        this.allowCut = allowCut;
    }

    @Override
    public IdentityHashMap<? extends Task, Integer> choose(Collection<? extends Task> queue, int width) {
        IdentityHashMap<Task, Integer> map = new IdentityHashMap<>();
        for (Task task : queue) {
            int w = getWidth(task);
            if (w > width) {
                if (allowCut && width >= task.getMinWidth()) {
                    w = width;
                } else {
                    continue;
                }
            }
            width -= w;
            map.put(task, w);
            if (width == 0) {
                break;
            }
        }
        for (Task task : queue) {
            if (width == 0) {
                break;
            }
            if (task.getMinWidth() <= width) {
                int w = Math.min(width, getWidth(task));
                map.put(task, w);
                width -= w;
            }
        }
        return map;
    }

    protected abstract int getWidth(Task task);

    @Override
    public int onIncome(Task task, Collection<? extends Task> queue, int width) {
        if (task.getMinWidth() <= width) {
            return Math.min(width, getWidth(task));
        } else {
            return -1;
        }
    }
    
}
