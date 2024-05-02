

package amongus;

import amongus.actions.ActionType;
import frsf.cidisi.faia.agent.search.GoalTest;
import frsf.cidisi.faia.state.AgentState;

/* Prueba de meta dura (final).
-Matar a todos los tripulantes
-Realizar todos los sabotajes

Prueba de meta blanda (intermedia, para tomar la sgte acción):
-Matar a un tripulante
-Realizar un sabotaje
-Buscar a un tripulante conocido. --> Según heurística
-Encontrar una nueva sección del mapa.
-Entrar a una sgte habitación aleatoria o quedarse. -> Según heurística */

public class GameGoal extends GoalTest 
{
    
    public GameState gameState;

    public GameGoal(GameState gameState) {
        this.gameState = gameState;
    }
    
    

    @Override
    public boolean isGoalState(AgentState agentState) 
    {
        var agent = (ImpostorAgentState) agentState;
        
        //Prueba de meta final
        boolean crewKilledTest = agent.getCrewKilled().size() == gameState.getCrews().size();
        boolean doneSabotageTest = agent.getDoneSabotages().size() == gameState.getSabotages().size();
        
        if(crewKilledTest && doneSabotageTest) return true;
        
        //Cualquier siguiente acción es válida
        return agent.isNextAction();
        
    }

    
}
