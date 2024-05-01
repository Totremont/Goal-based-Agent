
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
    private final Game environment;
    private final HashMap<String,Room> map;
    private final List<CrewMember> crews = new ArrayList<>();
    
    private final List<RoomState> roomStates = new ArrayList<>();
    private final List<CrewMemberState> crewStates = new ArrayList<>();
    
    private Room agentRoom;
    private Long agentEnergy;
    private boolean agentExtraSensor;
    
    private long gameTime;

    public GameState(Game environment) 
    {
        this.initState();
        this.environment = environment;
        this.map = this.environment.map;
    }
    
    
    @Override
    public void initState() 
    {
        //Seteamos hora
        gameTime = 0;
        
        //Obtiene un número entre min y max
        BiFunction<Integer,Integer,Long> randomBetween = (max,min) -> Math.round((Math.random()*max + min) % (max + 1));
        
        Long agentEnergy = randomBetween.apply(environment.MAX_ENERGY,environment.MIN_ENERGY);
        Long totalCrew = randomBetween.apply(environment.MAX_CREW,environment.MIN_CREW);
        
        boolean agentExtraSensor = Math.round(Math.random()) == 0;   //Sensor activado inicialmente?
        
        List<String> roomNames = new ArrayList<>();    
        map.forEach((key,val) -> 
        {
            roomNames.add(key);
            
            //Crear el initial state de cada ambiente
            roomStates.add(new RoomState(val,false));
        });
        
        //Estado inicial del agente
        int agentInitialIndex = randomBetween.apply(map.size() - 1,0).intValue();
        String agentRoomName = roomNames.get(agentInitialIndex);
        RoomState agentRoomState = map.get(agentRoomName).getState();
        agentRoomState.setAgentPresent(true);
        this.agentEnergy = agentEnergy;
        this.agentExtraSensor = agentExtraSensor;
        
        //Distribuir tripulantes en el mapa
        for(int i = 0; i < totalCrew; i++)
        {
            
            int crewInitialIndex = randomBetween.apply(map.size() - 1,0).intValue();
            String crewRoomName = roomNames.get(crewInitialIndex);
              
            Room crewRoom = map.get(crewRoomName);
            
            CrewMember crew = new CrewMember(i);
            CrewMemberState crewState = new CrewMemberState(crew,true,crewRoom);
            crewRoom.getState().addMember(crew);
            crewStates.add(crewState);
            crews.add(crew);
      
        }  
    } 
    
    //-- Setters para modificar el estado del juego
    
    //En cuál habitación está el agente?
    public void setAgentRoom(Room agentRoom) {
        this.agentRoom = agentRoom;
    }

    public void setAgentEnergy(Long agentEnergy) {
        this.agentEnergy = agentEnergy;
    }

    public void setAgentExtraSensor(boolean agentExtraSensor) {
        this.agentExtraSensor = agentExtraSensor;
    }
    
    public HashMap<String, Room> getMap() {
        return map;
    }

    public List<RoomState> getRoomStates() {
        return roomStates;
    }

    public List<CrewMember> getCrews() {
        return crews;
    }

    public Game getEnvironment() {
        return environment;
    }

    public List<CrewMemberState> getCrewStates() {
        return crewStates;
    }

    public Room getAgentRoom() {
        return agentRoom;
    }

    public Long getAgentEnergy() {
        return agentEnergy;
    }

    public boolean isAgentExtraSensor() {
        return agentExtraSensor;
    }

    public long getGameTime() {
        return gameTime;
    }
    
    
    
    
    
    
    @Override
    public String toString() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    

}
