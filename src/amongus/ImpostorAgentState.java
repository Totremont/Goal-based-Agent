
package amongus;

import amongus.actions.ActionType;
import amongus.models.Room;
import amongus.utils.Pair;
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
    
    //Nombre del tripulante vivo / última ubicación conocida - tiempo 
    private final HashMap<String, Pair<String,Long>> knownCrew = new HashMap<>();
    
    private final List<String> sabotages = new ArrayList<>();
    
    private final List<String> doneSabotages = new ArrayList<>();
    
    private Long gameTime;
    
    private Long energy;
    
    private boolean sensorAvailable;
    
    private ActionType lastAction;
    
    //Si el estado actual es producto de una acción futura (Acción generada en el arbol pero no aplicada al juego)
    private boolean nextAction;
    
    //Estado de una habitación según observa el agente. Es un subconjunto del estado real RoomState en la clase Room
    public class RoomState
    {
        private String name;
        private final List<String> neighbors = new ArrayList<>();     //[O,N,E,S]
        
        //Última vez visitado
        private Long lastSeen;
        
        //Tripulantes encontrados
        private List<String> crewPresent = new ArrayList<>();
        
        private String sabotage;

        public RoomState(String name, List<String> neighbors, long lastSeen, List<String> crewPresent, String sabotage) 
        {
            this.name = name;
            this.neighbors.addAll(neighbors);
            this.lastSeen = lastSeen;
            this.crewPresent.addAll(crewPresent);
            this.sabotage = sabotage;
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
        
        public boolean isSabotable()
        {
            return this.sabotage != null;
        }
        
        public String getSabotage()
        {
            return this.sabotage;
        }
        
        public void setSabotage(String name)
        {
            this.sabotage = name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLastSeen(Long lastSeen) {
            this.lastSeen = lastSeen;
        }

        public void setCrewPresent(List<String> crewPresent) {
            this.crewPresent = crewPresent;
        }
        
        
          
    }
     
    @Override   //Actualizamos estado en base a cambios en el mundo
    public void updateState(Perception p) 
    {
        ImpostorAgentPerc agentPerc = (ImpostorAgentPerc) p;
        RoomState roomState = this.knownRooms.get(agentPerc.getCurrentRoomSensor());
        
        //Es una nueva habitación
        if(roomState == null)
        {
            roomState = new RoomState(
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
        
        this.gameTime = agentPerc.getGameTime();
        
        this.energy = agentPerc.getEnergySensor();
        
        this.sensorAvailable = agentPerc.isExtraSensorAvail();
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
    
    public void addDoneSabotage(String name)
    {
        this.doneSabotages.add(name);
    }

    public List<String> getDoneSabotages() {
        return doneSabotages;
    }

    public ActionType getLastAction() {
        return lastAction;
    }

    public void setLastAction(ActionType lastAction) {
        this.lastAction = lastAction;
    }

    public boolean isNextAction() {
        return nextAction;
    }

    public void setNextAction(boolean nextAction) {
        this.nextAction = nextAction;
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
