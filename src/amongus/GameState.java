
package amongus;

import amongus.models.CrewMember;
import amongus.models.CrewMemberState;
import amongus.models.Room;
import amongus.models.RoomState;
import amongus.models.Sabotage;
import amongus.utils.Utils;
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
    private final HashMap<String,Sabotage> sabotages;
    private final List<RoomState> roomStates = new ArrayList<>();
    
    private final List<CrewMember> crews = new ArrayList<>();
    private final List<CrewMemberState> crewStates = new ArrayList<>();
    
    private Room agentRoom;
    private Long agentEnergy;
    private boolean agentSensorAvail;
    
    //Cuando estuvo activo por última vez
    private Long agentSensorLastTime;
    
    private Long gameTime;
    
    //Se activa cuando el juego debe darle información extrasensorial al agente (en la sgte percepción)
    //Sucede si el agente acciona su sensor
    private boolean omniscientAgent;

    public GameState(Game environment) 
    {
        this.initState();
        this.environment = environment;
        this.map = this.environment.map;
        this.sabotages = this.environment.sabotages;
    }
    
    
    @Override
    public void initState() 
    {
        //Seteamos hora
        this.gameTime = 0l;
          
        Long agentEnergy = Utils.randomBetween.apply(environment.MAX_ENERGY,environment.MIN_ENERGY);
        Long totalCrew = Utils.randomBetween.apply(environment.MAX_CREW,environment.MIN_CREW);
        
        boolean agentExtraSensor = Math.round(Math.random()) == 0;   //Sensor activado inicialmente?
        
        List<String> roomNames = new ArrayList<>();    
        map.forEach((key,val) -> 
        {
            roomNames.add(key);
            
            //Crear el initial state de cada ambiente
            roomStates.add(new RoomState(val,false));
        });
        
        //Estado inicial del agente
        int agentInitialIndex = Utils.randomBetween.apply(map.size() - 1,0).intValue();
        String agentRoomName = roomNames.get(agentInitialIndex);
        RoomState agentRoomState = map.get(agentRoomName).getState();
        agentRoomState.setAgentPresent(true);
        
        this.agentEnergy = agentEnergy;
        this.agentSensorAvail = agentExtraSensor;
        
        //Distribuir tripulantes en el mapa
        for(int i = 0; i < totalCrew; i++)
        {
            
            int crewInitialIndex = Utils.randomBetween.apply(map.size() - 1,0).intValue();
            String crewRoomName = roomNames.get(crewInitialIndex);
              
            Room crewRoom = map.get(crewRoomName);
            
            CrewMember crew = new CrewMember(i);
            CrewMemberState crewState = new CrewMemberState(crew,true,crewRoom,gameTime);
            crewRoom.getState().addMember(crew);
            crewStates.add(crewState);
            crews.add(crew);
      
        }  
    } 
    
    //-- Setters para modificar el estado del juego
    
    //Cambiar habitación del agente
    public void setAgentRoom(Room agentRoom) 
    {
        RoomState currentRoomState = getAgentRoom().getState();
        currentRoomState.setAgentPresent(false);
        
        RoomState newRoomState = agentRoom.getState();
        newRoomState.setAgentPresent(true);
        
        this.agentRoom = agentRoom;
    }
    
    public void setAgentRoom(String agentRoom) 
    {
        Room room = this.map.get(agentRoom);
        this.setAgentRoom(room);
    }

    public void setAgentEnergy(Long agentEnergy) {
        this.agentEnergy = agentEnergy;
    }

    public void addCrewKilled(String name)
    {
        CrewMember crew = this.crews.stream().filter(it -> it.getName() == name).findFirst().get();
        crew.getState().setIsAlive(false);
    }
    
    public void removeSabotage(String name)
    {
        this.sabotages.get(name).getRoom().getState().setIsSabotable(false);
    }

    public void setAgentSensorAvail(boolean agentSensorAvail) {
        this.agentSensorAvail = agentSensorAvail;
    }

    public void setGameTime(Long gameTime) {
        this.gameTime = gameTime;
    }

    public Long getAgentSensorLastTime() {
        return agentSensorLastTime;
    }

    public void setAgentSensorLastTime(Long agentSensorLastTime) {
        this.agentSensorLastTime = agentSensorLastTime;
    }
    
    
    // -- Getters
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

    public Room getAgentRoom() 
    {
        if(agentRoom == null)
        {
            agentRoom = roomStates.stream().filter(it -> it.getAgentPresent()).findFirst().get().getRoom();
        }
        return agentRoom;
    }

    public Long getAgentEnergy() {
        return agentEnergy;
    }

    public boolean isAgentSensorAvail() {
        return agentSensorAvail;
    }

    public Long getGameTime() {
        return gameTime;
    } 
    
    @Override
    public String toString() 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean isOmniscientAgent() {
        return omniscientAgent;
    }

    public void setOmniscientAgent(boolean omniscientAgent) {
        this.omniscientAgent = omniscientAgent;
    }
    
    public HashMap<String,Sabotage> getSabotages()
    {
        return this.sabotages;
    }
    
    
    
    
    
    
    

}
