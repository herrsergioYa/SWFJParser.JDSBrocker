/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks;

import swfjparser.GWFFile;
import swftasks.TaskComplicity;

/**
 *
 * @author HerrSergio
 */
public interface TaskComplicityFactory {
    TaskComplicity get(GWFFile file);
}
