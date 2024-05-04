
package amongus;

import amongus.actions.ActivateSensor;
import amongus.actions.Kill;
import amongus.actions.MoveEast;
import amongus.actions.MoveNorth;
import amongus.actions.MoveSouth;
import amongus.actions.MoveWest;
import amongus.models.AgentRoomState;
import amongus.utils.Pair;
import frsf.cidisi.faia.agent.Action;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.Problem;
import frsf.cidisi.faia.agent.search.SearchAction;
import frsf.cidisi.faia.agent.search.SearchBasedAgent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class ImpostorAgent extends SearchBasedAgent 
{
    private ImpostorAgentState myState;
    private GameGoal myGoal;
    private Vector<SearchAction> myActions = new Vector<>();
    
    public ImpostorAgent(HashMap<String,AgentRoomState> gameRooms, HashMap<String, Pair<String,Long>> gameCrew, List<String> sabotages, GameGoal goal)
    {
        //Estado del agente
        this.myState = new ImpostorAgentState(gameRooms,gameCrew,sabotages);
        this.setAgentState(state);
        
        //Acciones
        myActions.add(new MoveNorth());
        myActions.add(new MoveEast());
        myActions.add(new MoveSouth());
        myActions.add(new MoveWest());
        myActions.add(new Kill());
        myActions.add(new ActivateSensor());
        
        //Mi objectivo
        this.myGoal = goal;
        
        //Mi problema
        Problem problem = new Problem(this.myGoal,this.myState,this.myActions);
        
        this.setProblem(problem);
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
