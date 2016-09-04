/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package swftasks;

import java.util.Random;

/**
 *
 * @author HerrSergio
 */
public interface Task {

    double getIncomeTime();

    int getMinWidth();

    int getMaxWidth();

    double getLength(int width);

    double getSquare(int width);

    default int getAvgWidth(boolean up) {
        if (up) {
            return (int) ((getMinWidth() + 1L + getMaxWidth()) >> 1);
        } else {
            return (int) ((getMinWidth() + 0L + getMaxWidth()) >> 1);
        }
    }

    default RigidTask toRigidTask(int width) {
        return new RigidTask(getIncomeTime(), getSquare(width), width);

    }

    default RigidTask toRigidTask(Random random) {
        return toRigidTask(getMinWidth() + random.nextInt(getMaxWidth() - getMinWidth() + 1));
    }
}
