package distributions;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by HerrSergio on 17.08.2016.
 */
public interface Distribution extends Serializable {
    double next(Random random);
}
