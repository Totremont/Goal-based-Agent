

package amongus.actions;

//Clase con la lógica para moverse en cualquier dirección

import amongus.GameState;
import amongus.ImpostorAgentState;
import amongus.models.AgentRoomState;
import amongus.models.enums.Cardinal;
import amongus.utils.Utils;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;

public abstract class MoveAbstract extends SearchAction 
{  
    protected final Long ENERGY_COST = 1l;
    
    /*
        En el arbol de búsqueda, las acciones no consumen energía.
    */
    
    protected Cardinal direction;

    public MoveAbstract(Cardinal direction) {
        this.direction = direction;
    }
     
    @Override   //Ejecución imaginaria, para arbol de búsqueda
    public SearchBasedAgentState execute(SearchBasedAgentState s) 
    {
        ImpostorAgentState agentState = (ImpostorAgentState) s;
        
        if(!Utils.energyPreCondition(agentState, ENERGY_COST)) return null;
       
        
        String nextRoom = agentState.getCurrentRoom().getNeighbors().get(this.direction.ordinal());
        
        //Si no hay habitación, abortar
        if(nextRoom == null) return null;
        
        var nextRoomState = agentState.getKnownRooms().get(nextRoom);
        if(nextRoomState == null)   //Si no conozco la habitación a la que me dirijo
        {
            //Creo un state con información desconocida | Nota: Esto tirarías excepcion si el agente no conociese el mapa entero de entrada
            nextRoomState = new AgentRoomState(nextRoom,null,-1,null,null); 
            
        }
        
        //System.out.println("Me moví a: " + nextRoom);
        
        agentState.setCurrentRoom(nextRoomState);       
        
        //Utils.energyPostCondition(agentState, ENERGY_COST);
               
        
        return agentState;
        
    }
    
    @Override
    public Double getCost() 
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override   //Ejecución real sobre el mundo
    public EnvironmentState execute(AgentState ast, EnvironmentState est) 
    {
        var agentState = (ImpostorAgentState) ast;
               
        //Modificamos el estado del agente
        if(this.execute(agentState) == null) return null;
        
        Utils.energyPostCondition(agentState, ENERGY_COST);
        
        //Modificamos el estado del ambiente
        GameState gameState = (GameState) est;
        
        gameState.setAgentRoom(agentState.getCurrentRoom().getName());
        gameState.setAgentEnergy(agentState.getEnergy());
        
        WorldAction.advanceGame(gameState);
        
        return gameState;       
    }

    @Override
    public String toString() 
    {
        return "Me muevo hacia el: " + direction.name();
    }
    
    
}
