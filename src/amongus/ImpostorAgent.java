
package amongus;

import amongus.models.AgentRoomState;
import amongus.utils.Pair;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgent;
import java.util.HashMap;
import java.util.List;


public class ImpostorAgent extends SearchBasedAgent 
{
    private ImpostorAgentState state;
    
    public ImpostorAgent(HashMap<String,AgentRoomState> gameRooms, HashMap<String, Pair<String,Long>> gameCrew, List<String> sabotages)
    {
        this.state = new ImpostorAgentState(gameRooms,gameCrew,sabotages);
        this.setAgentState(state);
    }
    
    @Override
    public Action selectAction() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void see(Perception p) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    

}
