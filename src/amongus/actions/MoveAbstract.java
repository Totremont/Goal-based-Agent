

package amongus.actions;

//Clase con la lógica para moverse en cualquier dirección

import amongus.GameState;
import amongus.ImpostorAgentState;
import amongus.models.enums.Cardinal;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public abstract class MoveAbstract extends SearchAction 
{  
    protected final Long ENERGY_COST = 1l;
    
    /*
        DECISION_COST Mide la conveniencia de ejecutar una acción para alcanzar el objetivo.
        Acciones que más acercan al agente a ganar tienen menor coste. 
        Ej: Matar, Sabotear -> 1 ; Moverse habitación nueva  -> 2 ; -> Moverse habitación vieja -> 8;
    */    
    protected Long DECISION_COST = 2l;
    
    protected Cardinal direction;

    public MoveAbstract(Cardinal direction) {
        this.direction = direction;
    }
     
    @Override   //Ejecución imaginaria, para arbol de búsqueda
    public SearchBasedAgentState execute(SearchBasedAgentState s) 
    {
        ImpostorAgentState agentState = (ImpostorAgentState) s;
        String nextRoom = agentState.getCurrentRoom().getNeighbors().get(this.direction.ordinal());
        
        //Si no hay habitación, abortar
        if(nextRoom == null) return null;
        
        Long energy = agentState.getEnergy();
        
        var nextRoomState = agentState.getKnownRooms().get(nextRoom);
        if(nextRoomState == null)   //Si no conozco la habitación a la que me dirijo
        {
            //Creo un state con información desconocida
            nextRoomState = agentState.new RoomState(nextRoom,null,-1,null,null);
            
            agentState.setLastAction(ActionType.MOVE_TO_UNKNOWN);
        }
        else if(agentState.getPreviousRoom().getName() == nextRoomState.getName())  //Si es la habitación anterior
        { 
            agentState.setLastAction(ActionType.MOVE_TO_PREVIOUS); 
        } 
        else agentState.setLastAction(ActionType.MOVE_TO_KNOWN); //Si es una habitación conocida aleatoria
        
        agentState.setCurrentRoom(nextRoomState);
        agentState.setEnergy(energy);
        
        agentState.setNextAction(true);
        
        return agentState;
        
    }
    
    @Override
    public Double getCost() 
    {
        Long cost = DECISION_COST + ENERGY_COST;
        return cost.doubleValue();
    }

    @Override   //Ejecución real sobre el mundo
    public EnvironmentState execute(AgentState ast, EnvironmentState est) 
    {
        var agentState = (ImpostorAgentState) ast;
        //Modificamos el estado del agente
        if(this.execute(agentState) == null) return null;
        
        //Modificamos el estado del ambiente
        GameState gameState = (GameState) est;
        
        gameState.setAgentRoom(agentState.getCurrentRoom().getName());
        gameState.setAgentEnergy(agentState.getEnergy());
        
        return gameState;       
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
}
