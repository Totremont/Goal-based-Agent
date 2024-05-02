

package amongus.actions;

import amongus.GameState;
import amongus.ImpostorAgentState;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;


public class Stay extends SearchAction 
{

    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) 
    {
        var agentState = (ImpostorAgentState) s;
        //Decrementar energía
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
        
        GameState gameState = (GameState) est;
        //Decrementar energía
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
