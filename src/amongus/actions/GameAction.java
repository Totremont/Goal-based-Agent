

package amongus.actions;

import amongus.GameState;
import amongus.models.CrewMemberState;
import amongus.models.Room;
import amongus.utils.Utils;
import java.util.List;

/*  Representa el movimiento del juego, independientemente del jugador, que siempre se ejecuta en cada ciclo.
    Nota : Las acciones del agente se ejecutan -antes- de la acción del juego.
*/

public class GameAction 
{
    public GameState advanceGame(GameState gameState)
    {
        List<CrewMemberState> crewStates = gameState.getCrewStates();
        
        //Avanzamos tiempo de juego porque esta nueva información pertenece al siguiente ciclo.
        gameState.setGameTime(gameState.getGameTime() + 1);
        
        //Tarda: MIN_CREW_STEP_TIME en moverse como mínimo y MAX_CREW_STEP_TIME como máximo
        crewStates.stream().forEach(it -> 
        {
            
            int currentTime = gameState.getGameTime().intValue();
            
            //Tiempo a esperar antes de poder moverse
            int nextMoveWait = it.getLastMoveTime().intValue() + gameState.getEnvironment().MIN_CREW_STEP_TIME;
            
            //Tiempo máximo para moverse | Se suma + 1 porque si no le falta 1 ciclo.
            int nextMoveTime = it.getLastMoveTime().intValue() + gameState.getEnvironment().MAX_CREW_STEP_TIME + 1;
            
            //Ya pasó el tiempo mínimo?
            int diff = currentTime - nextMoveWait;
            
            boolean shouldMove = diff > 0 && Utils.randomBetween.apply(nextMoveTime - currentTime,0) == 0;
            
            if(shouldMove)
            {   
                //Obtenemos nueva habitación para el tripulante
                Room newRoom = it.getCurrentRoom().getNeighbors().stream().filter(neigh -> neigh != null).findAny().get();
                it.getCurrentRoom().getState().deleteMember(it.getCrew());
                it.setCurrentRoom(newRoom);
                it.setLastMoveTime(gameState.getGameTime());
            }
        });
        
        if(!gameState.isAgentSensorAvail())
        {
            //Comprobar si se debe habilitar el sensor
            int currentTime = gameState.getGameTime().intValue();
            
            //Tiempo a esperar
            int nextMoveWait = gameState.getAgentSensorLastTime().intValue() + gameState.getEnvironment().MIN_AGENT_SENSOR_STEP_TIME;
            
            //Tiempo máximo para activar
            int nextMoveTime = gameState.getAgentSensorLastTime().intValue() + gameState.getEnvironment().MAX_AGENT_SENSOR_STEP_TIME + 1;
            
            //Ya pasó el tiempo mínimo?
            int diff = currentTime - nextMoveWait;
            
            boolean shouldActivate = diff > 0 && Utils.randomBetween.apply(nextMoveTime - currentTime,0) == 0;
            
            if(shouldActivate)
            {   
                gameState.setAgentSensorAvail(true);
                gameState.setAgentSensorLastTime(gameState.getGameTime());
            }
        }
        
        return gameState;
        
    }
}
