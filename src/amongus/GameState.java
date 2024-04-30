
package amongus;

import amongus.models.CrewMember;
import amongus.models.CrewMemberState;
import amongus.models.Room;
import amongus.models.RoomState;
import amongus.models.Sabotage;
import frsf.cidisi.faia.state.EnvironmentState;
import java.util.HashMap;
import java.util.List;
import java.lang.Math;
import java.util.ArrayList;
import java.util.function.BiFunction;

//El estado asocia un ambiente del mapa con su correspondiente estado
public class GameState extends EnvironmentState 
{
    private final HashMap<String,Room> map;
    
    private List<RoomState> roomStates = new ArrayList<>();
    private List<CrewMember> crews;
    private Game environment;

    public GameState(Game environment) 
    {
        this.initState();
        this.environment = environment;
        this.map = this.environment.map;
    }
    
    
    @Override
    public void initState() 
    {
        BiFunction<Integer,Integer,Long> randomBetween = (max,min) -> Math.round((Math.random()*max + min) % (max + 1));
        
        Long agentEnergy = randomBetween.apply(environment.MAX_ENERGY,environment.MIN_ENERGY);
        Long totalCrew = randomBetween.apply(environment.MAX_CREW,environment.MIN_CREW);
        
        boolean sensorAvailable = Math.round(Math.random()) == 0;   //Sensor activado inicialmente?
        
        List<String> roomNames = new ArrayList<>();    
        map.forEach((key,val) -> 
        {
            roomNames.add(key);
            
            //Crear el initial state de cada ambiente
            roomStates.add(new RoomState(val,false));
        });
        
        //Ambiente inicial del agente
        int agentInitialIndex = (int) Math.round(Math.random()*roomNames.size());
        String agentRoomName = roomNames.get(agentInitialIndex);
        RoomState agentRoomState = map.get(agentRoomName).getState();
        agentRoomState.setAgentPresent(true);
        
        //Distribuir tripulantes en el mapa
        for(int i = 0; i < totalPlayers; i++)
        {
            CrewMember crew = new CrewMember(i);
            CrewMemberState crewState = new CrewMemberState(crew,true);
            crews.add(crew);
            
            int crewInitialIndex = (int) Math.round(Math.random()*roomNames.size());
            String crewRoomName = roomNames.get(crewInitialIndex);
              
            RoomState crewRoomState = map.get(crewRoomName).getState();
            
            crewRoomState.addMember(crew);
                 
        }  
    }  

    @Override
    public String toString() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

}
