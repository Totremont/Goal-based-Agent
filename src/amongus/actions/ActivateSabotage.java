

package amongus.actions;

import amongus.GameState;
import amongus.ImpostorAgentState;
import amongus.utils.Utils;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import frsf.cidisi.faia.state.AgentState;
import frsf.cidisi.faia.state.EnvironmentState;


public class ActivateSabotage extends SearchAction 
{
    
    public final Long ENERGY_COST = 1l;

    @Override
    public SearchBasedAgentState execute(SearchBasedAgentState s) 
    {
        var agentState = (ImpostorAgentState) s;
        
        if(!Utils.energyPreCondition(agentState, ENERGY_COST)) return null;
        
        //Si no es saboteable, salir
        if(!agentState.getCurrentRoom().isSabotable()) return null;
        
        String sabotage = agentState.getCurrentRoom().getSabotage();
        
        agentState.addDoneSabotage(sabotage);
        
        //Quitar sabotage
        agentState.getCurrentRoom().setSabotage(null);
        
        Utils.energyPostCondition(agentState, ENERGY_COST);
        
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
        
        GameState gameState = (GameState) est;
        
        gameState.removeSabotage(agentState.getDoneSabotages().get(agentState.getDoneSabotages().size() - 1));
        
        gameState.setAgentEnergy(agentState.getEnergy());
        
        return gameState;
        
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
