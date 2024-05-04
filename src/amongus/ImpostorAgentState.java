
package amongus;

import amongus.models.AgentRoomState;
import amongus.utils.Pair;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Subconjunto de datos del mapa que el agente conoce y guarda
public class ImpostorAgentState extends SearchBasedAgentState 
{
    private AgentRoomState currentRoom;
    private AgentRoomState previousRoom;    //Habitación de la que vengo
    
    //Habitaciones conocidas / Submapa del agente -> Nota: Se asume que conoce todo
    private final HashMap<String,AgentRoomState> knownRooms = new HashMap<>();
    
    private final List<String> crewKilled = new ArrayList<>();
    
    //Nombre del tripulante vivo / última ubicación conocida - tiempo 
    private final HashMap<String, Pair<String,Long>> knownCrew = new HashMap<>();
    
    private final List<String> sabotages = new ArrayList<>();       //Total a cumplir 
    private final List<String> doneSabotages = new ArrayList<>();
    
    private Long gameTime;
    private Long energy;
    private boolean sensorAvailable;
    

    //Cuando se crea el primer estado se inicializa con información estática
    public ImpostorAgentState(HashMap<String,AgentRoomState> gameRooms, HashMap<String, Pair<String,Long>> gameCrew, List<String> sabotages)
    {
        this.knownRooms.putAll(knownRooms);
        this.knownCrew.putAll(gameCrew); 
        this.sabotages.addAll(sabotages);
    }
      
    @Override   //Actualizamos estado en base a cambios en el mundo
    public void updateState(Perception p) 
    {
        ImpostorAgentPerc agentPerc = (ImpostorAgentPerc) p;
        AgentRoomState roomState = this.knownRooms.get(agentPerc.getCurrentRoomSensor());
        
        //Es una nueva habitación
        if(roomState == null)
        {
            roomState = new AgentRoomState(
                agentPerc.getCurrentRoomSensor(), 
                agentPerc.getCardinalSensor(),
                agentPerc.getGameTime(),
                agentPerc.getCrewPresentSensor(),
                agentPerc.getSabotage());
            
            this.previousRoom = this.currentRoom;
            this.currentRoom = roomState;
            this.knownRooms.put(agentPerc.getCurrentRoomSensor(),roomState);
        }
        else
        {
            roomState.setSabotage(agentPerc.getSabotage());
            roomState.setCrewPresent(agentPerc.getCrewPresentSensor());
            roomState.setLastSeen(agentPerc.getGameTime());
            
            //Si me moví de habitación
            if(roomState.getName() != agentPerc.getCurrentRoomSensor())
            {
                this.previousRoom = this.currentRoom;
                this.currentRoom = roomState;
            }
        }
        
        //Actualizamos estado de agentes vistos
        agentPerc.getCrewPresentSensor().stream().forEach(it -> 
        {
            this.knownCrew.put(it,new Pair(agentPerc.getCurrentRoomSensor(),agentPerc.getGameTime()));
        });
        
        //Si tengo información extrasensorial
        if(agentPerc.isExtraInfoAvail())
        {
            agentPerc.getExtraSensor().stream().forEach(it -> 
            {
                this.knownCrew.put(it.getFirst(),new Pair(it.getSecond(),agentPerc.getGameTime()));
            });
        }
        
        this.gameTime = agentPerc.getGameTime();
        
        this.energy = agentPerc.getEnergySensor();
        
        this.sensorAvailable = agentPerc.isExtraSensorAvail();
    }
    
    // -- Setters que modifican el estado cuando se ejecuta una acción
    
    public void setCurrentRoom(AgentRoomState currentRoom) 
    {
        this.previousRoom = this.currentRoom;
        this.currentRoom = currentRoom;
    }

    public void setGameTime(Long gameTime) {
        this.gameTime = gameTime;
    }

    public void setEnergy(Long energy) {
        this.energy = energy;
    }
    
    public void setSensorAvailable(boolean sensorAvailable) {
        this.sensorAvailable = sensorAvailable;
    }
    
    public void addDoneSabotage(String name)
    {
        this.doneSabotages.add(name);
    }
    
    public void addCrewKilled(String name)
    {
        this.crewKilled.add(name);
    }

    @Override
    public boolean equals(Object obj) 
    {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public SearchBasedAgentState clone() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    //--Getters

    public AgentRoomState getCurrentRoom() {
        return currentRoom;
    }

    public HashMap<String, AgentRoomState> getKnownRooms() {
        return knownRooms;
    }

    public List<String> getCrewKilled() {
        return crewKilled;
    }

    public HashMap<String, Pair<String,Long>> getKnownCrew() {
        return knownCrew;
    }

    public List<String> getSabotages() {
        return sabotages;
    }

    public Long getGameTime() {
        return gameTime;
    }

    public Long getEnergy() {
        return energy;
    }

    public AgentRoomState getPreviousRoom() {
        return previousRoom;
    }

    public boolean isSensorAvailable() {
        return sensorAvailable;
    }

    public List<String> getDoneSabotages() {
        return doneSabotages;
    }
 
    
    @Override
    public void initState() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
