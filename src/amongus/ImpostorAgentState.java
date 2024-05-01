
package amongus;

import amongus.models.Room;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Subconjunto de datos del mapa que el agente conoce y guarda
public class ImpostorAgentState extends SearchBasedAgentState 
{
    private RoomState currentRoom;
    
    //Habitación de la que vengo
    private RoomState previousRoom;
    
    //Habitaciones conocidas / Submapa del agente
    private final HashMap<String,RoomState> knownRooms = new HashMap<>();
    
    private final List<String> crewKilled = new ArrayList<>();
    
    //Nombre del tripulante vivo / última ubicación conocida
    private final HashMap<String, String> knownCrew = new HashMap<>();
    
    private final List<String> sabotages = new ArrayList<>();
    
    private Long gameTime;
    
    private Long energy;
    
    private boolean sensorAvailable;
    
    //Estado de una habitación según observa el agente. Es un subconjunto del estado real RoomState en la clase Room
    public class RoomState
    {
        private String name;
        private final List<String> neighbors = new ArrayList<>();     //[O,N,E,S]
        
        //Última vez visitado
        private Long lastSeen;
        
        //Tripulantes encontrados
        private List<String> crewPresent = new ArrayList<>();

        public RoomState(String name, List<String> neighbors, long lastSeen, List<String> crewPresent) 
        {
            this.name = name;
            this.neighbors.addAll(neighbors);
            this.lastSeen = lastSeen;
            this.crewPresent.addAll(crewPresent);
        }

        public String getName() {
            return name;
        }

        public List<String> getNeighbors() {
            return neighbors;
        }

        public Long getLastSeen() {
            return lastSeen;
        }

        public List<String> getCrewPresent() {
            return crewPresent;
        }
        
        public void deleteCrew(String name)
        {
            this.crewPresent = this.crewPresent.stream().filter(it -> it != name).toList();
        }
          
    }
     
    @Override   //Actualizamos estado en base a cambios en el mundo
    public void updateState(Perception p) 
    {
        ImpostorAgentPerc agentPerc = (ImpostorAgentPerc) p;
        RoomState roomState = new RoomState(
                agentPerc.getCurrentRoomSensor(), 
                agentPerc.getCardinalSensor(),
                agentPerc.getGameTime(),
                agentPerc.getCrewPresentSensor());
        
        //Actualizamos ubicación actual
        this.previousRoom = this.currentRoom;
        this.currentRoom = roomState;
        
        //Actualizamos estado de las habitaciones conocidas
        this.knownRooms.put(agentPerc.getCurrentRoomSensor(),roomState);
        
        //Actualizamos estado de agentes conocidos
        agentPerc.getCrewPresentSensor().stream().forEach(it -> 
        {
            this.knownCrew.put(it,agentPerc.getCurrentRoomSensor());
        });
        
        this.gameTime = agentPerc.getGameTime();
        
        this.energy = agentPerc.getEnergySensor();
        
        this.sensorAvailable = agentPerc.isExtraSensorAvailable();
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

    @Override
    public void initState() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public RoomState getCurrentRoom() {
        return currentRoom;
    }

    public HashMap<String, RoomState> getKnownRooms() {
        return knownRooms;
    }

    public List<String> getCrewKilled() {
        return crewKilled;
    }

    public HashMap<String, String> getKnownCrew() {
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

    public void setCurrentRoom(RoomState currentRoom) 
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

    public RoomState getPreviousRoom() {
        return previousRoom;
    }
    
    public void addCrewKilled(String name)
    {
        this.crewKilled.add(name);
    }

    public boolean isSensorAvailable() {
        return sensorAvailable;
    }

    public void setSensorAvailable(boolean sensorAvailable) {
        this.sensorAvailable = sensorAvailable;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
