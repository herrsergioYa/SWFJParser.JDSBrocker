package distributions;

import java.io.Serializable;
import java.util.Random;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by HerrSergio on 17.08.2016.
 */
public interface Distribution extends Serializable {
    double next(Random random);
    
    default double getDF(double t, boolean cdf) {
        throw new RuntimeException();
    }
}
