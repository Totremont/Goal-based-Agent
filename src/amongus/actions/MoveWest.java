

package amongus.actions;

import amongus.ImpostorAgentState;
import amongus.models.Room;
import amongus.models.enums.Cardinal;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;


public class MoveWest extends SearchAction 
{
    public final Long ENERGY_COST = 1l;
    
    /*
        DECISION_COST Mide la conveniencia de ejecutar una acción para alcanzar el objetivo.
        Acciones que más acercan al agente a ganar tienen menor coste. 
        Ej: Matar, Sabotear -> 1 ; Moverse habitación nueva  -> 2 ; -> Moverse habitación vieja -> 8;
    */    
    public Long DECISION_COST = 2l;
    
    @Override   //Ejecución imaginaria, para arbol de búsqueda
    public SearchBasedAgentState execute(SearchBasedAgentState s) 
    {
        ImpostorAgentState agentState = (ImpostorAgentState) s;
        String westRoom = agentState.getCurrentRoom().getNeighbors().get(Cardinal.OESTE.ordinal());
        Long energy = agentState.getEnergy();
        
        var westRoomState = agentState.getKnownRooms().get(westRoom);
        if(westRoomState == null)   //Si no conozco la habitación a la que me dirijo
        {
            //Creo un state con información desconocida
            westRoomState = agentState.new RoomState(westRoom,null,-1,null);
        }
        
        agentState.setCurrentRoom(westRoomState);
        agentState.setEnergy(energy);
        
    }

    @Override
    public Double getCost() 
    {
        Long cost = DECISION_COST + ENERGY_COST;
        return cost.doubleValue();
    }

    @Override   //Ejecución real sobre el mundo
    public EnvironmentState execute(AgentState ast, EnvironmentState est) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
