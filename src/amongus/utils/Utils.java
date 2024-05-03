

package amongus.utils;

import amongus.ImpostorAgentState;
import java.util.function.BiFunction;


public class Utils 
{    
    //Obtiene un número entre min y max
    public static BiFunction<Integer,Integer,Long> randomBetween = (max,min) -> Math.round((Math.random()*max + min) % (max + 1));
    
    //Pre y post condiciones de energía
    public static boolean energyPreCondition(ImpostorAgentState agentState, Long requiredEnergy)
    {
        return (agentState.getEnergy() - requiredEnergy) < 0;
    }
    
    public static void energyPostCondition(ImpostorAgentState agentState, Long requiredEnergy)
    {
        agentState.setEnergy(agentState.getEnergy() - requiredEnergy);
    }
}
