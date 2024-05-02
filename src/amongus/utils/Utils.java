

package amongus.utils;

import java.util.function.BiFunction;


public class Utils 
{    
    //Obtiene un número entre min y max
    public static BiFunction<Integer,Integer,Long> randomBetween = (max,min) -> Math.round((Math.random()*max + min) % (max + 1));
}
