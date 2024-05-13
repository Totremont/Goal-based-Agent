

package amongus;

import amongus.models.AgentRoomState;
import frsf.cidisi.faia.solver.search.IEstimatedCostFunction;
import frsf.cidisi.faia.solver.search.NTree;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class AgentHeuristic implements IEstimatedCostFunction 
{
    /*
        Es un número en formato VVDDSS (Asume que el valor máximo de c/u es 99)
        Donde: 
        -   VV : Tripulantes vivos
        -   DD : Sabotajes restantes
        -   SS : Distancia más cercana a un tripulante
        Al finalizar, el número quedaría 000000 -> 0;
                
    */
    @Override
    public double getEstimatedCost(NTree node) 
    {
        ImpostorAgentState agentState = (ImpostorAgentState) node.getAgentState();
        
        if(agentState.getCurrentRoom() == null) return Integer.MAX_VALUE;
        
        int sabotages = agentState.getRequiredSabotages().size() - agentState.getDoneSabotages().size();
        int alive = agentState.getAliveCrew().size();
        int distance = alive > 0 ? closest(agentState.getCurrentRoom(), agentState.getKnownRooms(), new ArrayList<>()) : 0;
        
        return (alive*10000 + sabotages*100 + distance);
          
    }
    
    //Distancia entre el agente y el tripulante más cercano
    //No se hace con respecto a TODOS los tripulantes porque requeriría demasiada CPU
    private static int closest(AgentRoomState agentRoom, HashMap<String,AgentRoomState> map, ArrayList<String> visited)
    {
        if(agentRoom.isCrewPresent()) return 0;

        List<String> neighbors = agentRoom.getNeighbors();

        List<Integer> aux = new ArrayList<>();
        
        visited.add(agentRoom.getName());

        for(int  i = 0; i < neighbors.size(); i++)
        {
            
            String neigh = neighbors.get(i);
            if(neigh == null || visited.contains(neigh)) continue;
            
            AgentRoomState neighRoom = map.get(neigh);
            aux.add(closest(neighRoom, map,visited));               
        }
        if(aux.isEmpty()) return 99;
        
        Collections.sort(aux);
        
        int val = aux.get(0) == 99 ? 99 : aux.get(0) + 1 ;
        return val;
    }
    
}
