

package amongus.actions;

import amongus.GameState;
import amongus.ImpostorAgentState;
import amongus.models.CrewMemberState;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;


public class Kill extends SearchAction 
{

    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) 
    {
        var agentState = (ImpostorAgentState) s;
        
        //Si no hay nadie, abortar
        if(agentState.getCurrentRoom().getCrewPresent().isEmpty()) return null;
        
        //Sacamos un tripulante aleatorio
        String crew = agentState.getCurrentRoom().getCrewPresent().stream().findAny().get();
        
        agentState.addCrewKilled(crew);
        
        agentState.getCurrentRoom().deleteCrew(crew);
        
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
        
        var gameState = (GameState) est;
        
        String crewKilled = agentState.getCrewKilled().get(agentState.getCrewKilled().size() - 1);
        
        gameState.addCrewKilled(crewKilled);
        
        return gameState;
        
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    
}
