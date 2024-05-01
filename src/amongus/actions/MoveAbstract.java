

package amongus.actions;

//Clase con la lógica para moverse en cualquier dirección

import amongus.ImpostorAgentState;
import amongus.models.enums.Cardinal;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;

public abstract class MoveAbstract extends SearchAction 
{  
    private Cardinal direction;

    public MoveAbstract(Cardinal direction) {
        this.direction = direction;
    }
     
    @Override   //Ejecución imaginaria, para arbol de búsqueda
    public SearchBasedAgentState execute(SearchBasedAgentState s) 
    {
        ImpostorAgentState agentState = (ImpostorAgentState) s;
        String nextRoom = agentState.getCurrentRoom().getNeighbors().get(this.direction.ordinal());
        Long energy = agentState.getEnergy();
        
        var nextRoomState = agentState.getKnownRooms().get(nextRoom);
        if(nextRoomState == null)   //Si no conozco la habitación a la que me dirijo
        {
            //Creo un state con información desconocida
            nextRoomState = agentState.new RoomState(nextRoom,null,-1,null);
        }
        
        agentState.setCurrentRoom(nextRoomState);
        agentState.setEnergy(energy);
        
        return agentState;
        
    }
}
