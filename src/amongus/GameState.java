
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
import java.util.ArrayList;

//El GameState contiene el estado completo del juego
public class GameState extends EnvironmentState 
{
    private final Game environment;
    
    //Atributos del mundo
    private final HashMap<String,Room> map;
    private final HashMap<String,Sabotage> sabotages;
    private final List<RoomState> roomStates = new ArrayList<>();   
    private final HashMap<String, CrewMember> crews = new HashMap<>();
    private final List<CrewMemberState> crewStates = new ArrayList<>();   
    private Long gameTime;
    
    //Atributos del agente
    private Room agentRoom;
    private Long agentEnergy;
    private boolean agentSensorAvail; 
    private Long agentSensorLastTime; //Cuando estuvo activo por última vez
    
    //Se activa cuando el juego debe darle información extrasensorial al agente (en la sgte percepción)
    //Sucede si el agente acciona su sensor
    private boolean omniscientAgent;

    public GameState(Game environment) 
    {
        //Setear información estática
        this.environment = environment;
        this.map = this.environment.map;
        this.sabotages = this.environment.sabotages;
        
        //Setear estado inicial del juego
        this.initState();
    }
    
    
    @Override
    public void initState() 
    {
        //Tiempo inicial
        this.gameTime = 0l;
        
        //Energía del agente, cantidad de tripulantes, etc...
        Long agentEnergy = Utils.randomBetween.apply(environment.MAX_ENERGY,environment.MIN_ENERGY);
        Long totalCrew = Utils.randomBetween.apply(environment.MAX_CREW,environment.MIN_CREW);
        
        boolean agentExtraSensor = Math.round(Math.random()) == 0;   //Sensor activado inicialmente?
        
        List<String> roomNames = new ArrayList<>();    
        map.forEach((key,val) -> 
        {
            roomNames.add(key);               
            roomStates.add(new RoomState(val)); //Crear el initial state de cada ambiente
        });
        
        //Estado inicial del agente
        int agentInitialIndex = Utils.randomBetween.apply(map.size() - 1,0).intValue();
        String agentRoomName = roomNames.get(agentInitialIndex);
        
        this.agentRoom = map.get(agentRoomName);
        this.agentRoom.getState().setAgentPresent(true);
        this.agentEnergy = agentEnergy;
        this.agentSensorAvail = agentExtraSensor;
        this.agentSensorLastTime = 0l;
        
        //Distribuir tripulantes en el mapa
        for(int i = 0; i < totalCrew; i++)
        {
            
            int crewInitialIndex = Utils.randomBetween.apply(map.size() - 1,0).intValue();
            String crewRoomName = roomNames.get(crewInitialIndex);            
            Room crewRoom = map.get(crewRoomName);  
            
            CrewMember crew = new CrewMember(i);
            CrewMemberState crewState = new CrewMemberState(crew,crewRoom,this.gameTime);
            crewRoom.getState().addMember(crew);
            crewStates.add(crewState);
            crews.put(crew.getName(),crew);
        }  
    } 
    
    /*-- 
        Setters que permiten modificar el estado del juego. Usado por acciones del agente y el WorldAction
        Nota: SOLO GameState se encarga de modificar el estado del juego. 
    */
    
    public void setAgentRoom(String newAgentRoom) 
    {
        RoomState currentRoomState = getAgentRoom().getState();
        currentRoomState.setAgentPresent(false);
        
        this.agentRoom = map.get(newAgentRoom);
        this.agentRoom.getState().setAgentPresent(true);
       
    }

    public void setAgentEnergy(Long agentEnergy) 
    {
        this.agentEnergy = agentEnergy;
    }

    public void addCrewKilled(String name)
    {
        CrewMember crew = this.crews.get(name);
        crew.getState().setIsAlive(false);
        crew.getState().getCurrentRoom().getState().deleteMember(crew);
    }
    
    public void setCrewRoom(String crewName, String roomName)
    {
        CrewMember crew = this.crews.get(crewName);
        Room newRoom = this.map.get(roomName);
        crew.getState().getCurrentRoom().getState().deleteMember(crew);
        crew.getState().setCurrentRoom(newRoom);
        newRoom.getState().addMember(crew);
    }
    
    public void removeSabotage(String name) //Cuando se completa un sabotaje
    {
        this.sabotages.get(name).getRoom().getState().setIsSabotable(false);
    }

    public void setAgentSensorAvail(boolean agentSensorAvail) 
    {    
        this.agentSensorAvail = agentSensorAvail;
    }

    public void setGameTime(Long gameTime) 
    {
        this.gameTime = gameTime;
    }

    public void setAgentSensorLastTime(Long agentSensorLastTime) 
    {
        this.agentSensorLastTime = agentSensorLastTime;
    }
    
    public void setOmniscientAgent(boolean omniscientAgent) 
    {
        this.omniscientAgent = omniscientAgent;
    }
     
    // -- Getters
    public HashMap<String, Room> getMap() 
    {
        return map;
    }

    public List<RoomState> getRoomStates() 
    {
        return roomStates;
    }

    public HashMap<String,CrewMember> getCrews() 
    {
        return crews;
    }

    public Game getEnvironment() 
    {
        return environment;
    }

    public List<CrewMemberState> getCrewStates() 
    {
        return crewStates;
    }

    public Room getAgentRoom() 
    {
        return agentRoom;
    }

    public Long getAgentEnergy() 
    {
        return agentEnergy;
    }
    
    public Long getAgentSensorLastTime() 
    {
        return agentSensorLastTime;
    }

    public boolean isAgentSensorAvail() 
    {
        return agentSensorAvail;
    }

    public Long getGameTime() 
    {
        return gameTime;
    } 
    
    public boolean isOmniscientAgent() 
    {
        return omniscientAgent;
    }
        
    public HashMap<String,Sabotage> getSabotages()
    {
        return this.sabotages;
    }
    
    @Override
    public String toString() 
    {
        StringBuilder text = new StringBuilder("--Mundo | Tiempo: ").append(this.gameTime).append("--\n");
        text.append("¿Dónde está cada tripulante?: \n");
        this.crewStates.forEach(state -> 
        {
            text
                    .append("Me llamo: ")
                    .append(state.getCrew().getName())
                    .append(" y estoy en: ")
                    .append(state.getCurrentRoom().getName());
                    if(!state.isAlive()) text.append(" <-- MUERTO");
                    text.append("\n");
        });
        
        return text.toString();
        
    }
    
    
    
    
    
    
    

}
