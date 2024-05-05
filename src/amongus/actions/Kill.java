

package amongus.actions;

import amongus.GameState;
import amongus.ImpostorAgentState;
import amongus.utils.Utils;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;


public class Kill extends SearchAction 
{
    private final Long ENERGY_COST = 1l;

    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) 
    {
        var agentState = (ImpostorAgentState) s;
        
        if(!Utils.energyPreCondition(agentState, ENERGY_COST)) return null;
        
        //Si no hay nadie, abortar
        if(agentState.getCurrentRoom().getCrewPresent().isEmpty()) return null;
        
        //Sacamos un tripulante aleatorio
        String crew = agentState.getCurrentRoom().getCrewPresent().stream().findAny().get();
        
        agentState.addCrewKilled(crew);
             
        agentState.getCurrentRoom().deleteCrew(crew);
     
        agentState.getAliveCrew().remove(crew);
        
        //Utils.energyPostCondition(agentState, ENERGY_COST);
        
        return agentState;
        
    }

    @Override
    public Double getCost() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public EnvironmentState execute(AgentState ast, EnvironmentState est) 
    {
        var agentState = (ImpostorAgentState) ast;       
        
        if(this.execute(agentState) == null) return null;
        
        Utils.energyPostCondition(agentState, ENERGY_COST);
        
        var gameState = (GameState) est;
        
        //Buscar último asesinado
        String crewKilled = agentState.getCrewKilled().get(agentState.getCrewKilled().size() - 1);
        
        gameState.addCrewKilled(crewKilled);
        
        gameState.setAgentEnergy(agentState.getEnergy());
        
        WorldAction.advanceGame(gameState);
        
        return gameState;
        
    }

    @Override
    public String toString() 
    {
        return "Voy a asesinar a alguien";
    }

    
}
