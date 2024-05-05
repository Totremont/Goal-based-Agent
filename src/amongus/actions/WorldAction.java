

package amongus.actions;

import amongus.GameState;
import amongus.models.CrewMemberState;
import amongus.models.Room;
import amongus.utils.Utils;
import java.util.List;

/*  Representa el movimiento del juego, independientemente del jugador, que siempre se ejecuta en cada ciclo.
    Nota : Las acciones del agente se ejecutan -antes- de la acción del juego.
*/

public class WorldAction 
{
    public static GameState advanceGame(GameState gameState)
    {
        System.out.println("==Juego avanza==");
        List<CrewMemberState> crewStates = gameState.getCrewStates();
        
        //Avanzamos tiempo de juego porque esta nueva información pertenece al siguiente ciclo.
        gameState.setGameTime(gameState.getGameTime() + 1);
        
        //Tarda: MIN_CREW_STEP_TIME en moverse como mínimo y MAX_CREW_STEP_TIME como máximo
        crewStates.stream().filter(it -> it.isAlive()).forEach(it -> 
        {
            
            Long currentTime = gameState.getGameTime();
            
            //Tiempo a esperar antes de poder moverse
            int nextMoveWait = it.getLastMoveTime().intValue() + gameState.getEnvironment().MIN_CREW_STEP_TIME;
            
            //Tiempo máximo para moverse
            int nextMoveTime = it.getLastMoveTime().intValue() + gameState.getEnvironment().MAX_CREW_STEP_TIME;
            
            //Ya pasó el tiempo mínimo?
            Long minDiff = currentTime - nextMoveWait;
            
            Long maxDiff = nextMoveTime - currentTime;
            
            boolean shouldMove = minDiff >= 0 && Utils.randomBetween.apply(maxDiff.intValue(),0) == 0l;
            
            if(shouldMove)
            {   
                //Obtenemos nueva habitación para el tripulante
                List<Room> availableRooms = it.getCurrentRoom().getNeighbors().stream().filter(neigh -> neigh != null).toList();
                
                Long index = Utils.randomBetween.apply(availableRooms.size() - 1,0);
                
                Room newRoom = availableRooms.get(index.intValue());
                
                gameState.setCrewRoom(it.getCrew().getName(), newRoom.getName(),currentTime);
                
            }
        });
        
        if(!gameState.isOmniscientAgent())  
        {
            //Comprobar si se debe habilitar el sensor
            int currentTime = gameState.getGameTime().intValue();
            
            //Tiempo a esperar
            int nextMoveWait = gameState.getAgentSensorLastTime().intValue() + gameState.getEnvironment().MIN_AGENT_SENSOR_STEP_TIME;
            
            //Tiempo máximo para activar
            int nextMoveTime = gameState.getAgentSensorLastTime().intValue() + gameState.getEnvironment().MAX_AGENT_SENSOR_STEP_TIME + 1;
            
            //Ya pasó el tiempo mínimo?
            int diff = currentTime - nextMoveWait;
            
            boolean shouldActivate = diff > 0 && Utils.randomBetween.apply(nextMoveTime - currentTime,0) == 0l;
            
            if(shouldActivate)
            {   
                //gameState.setAgentSensorAvail(true);
                gameState.setAgentSensorLastTime(gameState.getGameTime());
                gameState.setOmniscientAgent(true);     //Se lo activa automáticamente
            }
        } 
        else gameState.setOmniscientAgent(false);  //Sacarselo en la siguiente percepción
        
        return gameState;
        
    }
}
