

package amongus.utils;

import amongus.ImpostorAgentState;
import amongus.models.AgentRoomState;
import java.util.HashMap;
import java.util.function.BiFunction;


public class Utils 
{    
    //Obtiene un número entre min y max
    public static BiFunction<Integer,Integer,Long> randomBetween = (max,min) -> 
    {
        Double value = min + (Math.random()*(max-min));
        return Math.round(value);
    };
    
    //Pre y post condiciones de energía
    public static boolean energyPreCondition(ImpostorAgentState agentState, Long requiredEnergy)
    {
        boolean comply = (agentState.getEnergy() - requiredEnergy) >= 0;
        return comply;
    }
    
    public static void energyPostCondition(ImpostorAgentState agentState, Long requiredEnergy)
    {
        agentState.setEnergy(agentState.getEnergy() - requiredEnergy);
    }
    
    //Estos métodos hacen un deep copy (crean nuevas instancias de los attr en vez de copiar sus referencias)
    public static HashMap<String,AgentRoomState> copyAgentRooms(HashMap<String,AgentRoomState> rooms)
    {
        var copyRooms = new HashMap<String,AgentRoomState>();
        rooms.forEach((key,val) -> 
        {
            copyRooms.put(key,val.clone());
        });
        
        return copyRooms;
    }

    public static HashMap<String,Pair<String,Long>> copyAgentAliveCrew(HashMap<String,Pair<String,Long>> crew)
    {
        var copyCrew = new HashMap<String,Pair<String,Long>>();
        crew.forEach((key,val) -> 
        {
            copyCrew.put(key,val.clone());
        });
        
        return copyCrew;
    }
    
}
