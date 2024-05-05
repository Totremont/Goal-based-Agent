
package amongus;

import amongus.models.AgentRoomState;
import amongus.utils.Pair;
import amongus.utils.Utils;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.agent.search.SearchBasedAgentState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Subconjunto de datos del mapa que el agente conoce y guarda
public class ImpostorAgentState extends SearchBasedAgentState 
{
    private AgentRoomState currentRoom;
    
    //Habitación de la que vengo
    private AgentRoomState previousRoom;
    
    //Habitaciones conocidas / Submapa del agente -> Nota: Actualmente se asume que conoce todos los lugares
    private final HashMap<String,AgentRoomState> knownRooms = new HashMap<>();
    
    private final ArrayList<String> crewKilled = new ArrayList<>();
    
    //Nombre del tripulante vivo / última ubicación conocida - tiempo de avistamiento
    private final HashMap<String, Pair<String,Long>> aliveCrew = new HashMap<>();
    
    private final ArrayList<String> requiredSabotages = new ArrayList<>();
    private final ArrayList<String> doneSabotages = new ArrayList<>();
    
    private Long gameTime;
    private Long energy;
    private boolean sensorAvailable;
    
    //Cuando se crea el primer estado se inicializa con información estática
    public ImpostorAgentState(HashMap<String,AgentRoomState> gameRooms, HashMap<String, Pair<String,Long>> gameCrew, List<String> sabotages)
    {
        this.knownRooms.putAll(gameRooms);
        this.aliveCrew.putAll(gameCrew); 
        this.requiredSabotages.addAll(sabotages);
        /*
        this.aliveCrew.forEach((key,val) -> 
        {
            this.knownRooms.get(val.getFirst()).addCrew(key);
        });
        */
    }
    
    
    @Override
    public SearchBasedAgentState clone() 
    {
        var knownRoomsClone = Utils.copyAgentRooms(this.knownRooms);
        var aliveCrewClone = Utils.copyAgentAliveCrew(this.aliveCrew);
        List<String> crewKilledClone = ((List<String>)this.crewKilled.clone());
        List<String> doneSabotagesClone = ((List<String>)this.doneSabotages.clone());
        
        ImpostorAgentState newAgent = new ImpostorAgentState(knownRoomsClone,aliveCrewClone,this.requiredSabotages);
        newAgent.setCurrentRoom(knownRoomsClone.get(this.getCurrentRoom().getName()));
        
        AgentRoomState previousRoomClone = this.previousRoom != null ? knownRoomsClone.get(this.getPreviousRoom().getName()) : null;
        newAgent.setPreviousRoom(previousRoomClone);
        newAgent.setCrewKilled(crewKilledClone);
        newAgent.setDoneSabotages(doneSabotagesClone);
        newAgent.setGameTime(this.gameTime);
        newAgent.setEnergy(this.energy);
        newAgent.setSensorAvailable(this.sensorAvailable);
        
        return newAgent;          
    }
    
    @Override
    public boolean equals(Object obj)   //Las listas y mapas comparan sus valores con los equals de sus atributos
    {
        if(!(obj instanceof ImpostorAgentState)) return false;
        
        ImpostorAgentState other = (ImpostorAgentState) obj;
        
        boolean previousRoomEquality = other.getPreviousRoom() == null && this.previousRoom == null;
        if(other.getPreviousRoom() != null && this.previousRoom != null && other.getPreviousRoom().equals(this.previousRoom))
        {
            previousRoomEquality = true;
        }
        
        boolean result =
        (
                other.getCurrentRoom().equals(this.currentRoom)
                //Si la previous room es null en las 2 o igual.
                && previousRoomEquality
                && other.getKnownRooms().equals(this.knownRooms)
                && other.getCrewKilled().equals(this.crewKilled)
                && other.getAliveCrew().equals(this.aliveCrew)
                && other.getRequiredSabotages().equals(this.requiredSabotages)
                && other.getDoneSabotages().equals(this.doneSabotages)
                && other.getGameTime().equals(this.gameTime)
                && other.getEnergy().equals(this.energy)
                && other.isSensorAvailable() == this.sensorAvailable
        );
        
        return result;

    }
    
    //Actualizamos estado en base a percepción dada por Game
    @Override
    public void updateState(Perception p) 
    {
        //System.out.println("Actualizando estado--");
        ImpostorAgentPerc agentPerc = (ImpostorAgentPerc) p;
        AgentRoomState roomState = this.knownRooms.get(agentPerc.getCurrentRoomSensor());
        
        //Es una nueva habitación | Esta lógica no aplica actualmente (conoce todas)
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
            //roomState.setCrewPresent(agentPerc.getCrewPresentSensor());
            roomState.setLastSeen(agentPerc.getGameTime());
            
            this.previousRoom = this.currentRoom;
            this.currentRoom = roomState;
            
        }
        
        //Actualizamos estado de agentes vistos en esta habitación
        updateCrewLocation(roomState, agentPerc.getCrewPresentSensor(), agentPerc.getGameTime());
        
        //Si tengo información extrasensorial, ubico a todos los tripulantes
        if(agentPerc.isExtraInfoAvail())
        {
            agentPerc.getExtraSensor().stream().forEach(it ->   //First : Tripulante, Second : Ubicación
            {
                String oldRoomName = this.aliveCrew.get(it.getFirst()).getFirst();
                String newRoomName = it.getSecond();
                
                //Si está en una nueva habitación, borrar de la anterior.
                if(!oldRoomName.equals(newRoomName))
                {
                    AgentRoomState oldRoom = this.knownRooms.get(oldRoomName);
                    oldRoom.deleteCrew(it.getFirst());
                    
                    AgentRoomState newRoom = this.knownRooms.get(newRoomName);
                    newRoom.addCrew(it.getFirst());
                }
                
                //Actualizar lista
                this.aliveCrew.put(it.getFirst(),new Pair(newRoomName,agentPerc.getGameTime()));
                
            });
        }
        
        this.gameTime = agentPerc.getGameTime();
        
        this.energy = agentPerc.getEnergySensor();
        
        this.sensorAvailable = agentPerc.isExtraSensorAvail();
    }
    
    /*
        Actualiza la ubicación de los tripulantes.
        Si un tripulante cambió de habitación, sacarlo de la anterior y ponerlo en la nueva.
        Si su ubicación cambió y no se la conoce (si se entró en una ubicación donde antes estaba y ahora no), 
        ponerlo en otro lugar aleatorio.
    
    */
    
    private void updateCrewLocation(AgentRoomState currentRoom, List<String> newCrew, Long gameTime)
    {
        //Pedimos los tripulantes que conocíamos antes
        List<String> oldCrew = (List<String>) currentRoom.getCrewPresent().clone();
        
        //Revisamos los que están ahora
        newCrew.stream().forEach(crew -> 
        {
            //Buscamos su última ubicación conocida
            String oldRoomName = this.aliveCrew.get(crew).getFirst();
            if(!currentRoom.getName().equals(oldRoomName))    //Si estaba en otra ubicación
            {
                AgentRoomState oldRoom = this.knownRooms.get(oldRoomName);
                oldRoom.deleteCrew(crew);
                currentRoom.addCrew(crew);
            }
            
            //Lo eliminamos de la lista vieja -> Esto nos deja con una lista de los tripulantes que antes estaban y ahora no.
            oldCrew.remove(crew);
            this.aliveCrew.put(crew,new Pair(currentRoom.getName(),gameTime));
        });
        
        //Movemos a una ubicación adyacente a los que no conocemos
        oldCrew.stream().forEach(it -> 
        {
            currentRoom.deleteCrew(it);
            List<String> availRooms = currentRoom.getNeighbors().stream().filter(neigh -> neigh != null).toList();
            Long index = Utils.randomBetween.apply(availRooms.size() - 1, 0);
            AgentRoomState newRoom = this.knownRooms.get(availRooms.get(index.intValue()));
            newRoom.addCrew(it);
            this.aliveCrew.put(it,new Pair(newRoom.getName(),gameTime));
        });
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
        //System.out.println("Hice un sabotaje!");
    }
    
    public void addCrewKilled(String name)
    {
        this.crewKilled.add(name);
        //System.out.println("Maté a uno!");
    }
    
    //Setters para clonar
    
    public void setPreviousRoom(AgentRoomState room)
    {
        this.previousRoom = room;
    }
    
    public void setCrewKilled(List<String> crew)
    {
        this.crewKilled.clear();
        this.crewKilled.addAll(crew);
    }
    
    public void setDoneSabotages(List<String> sab)
    {
        this.doneSabotages.clear();
        this.doneSabotages.addAll(sab);
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

    public HashMap<String, Pair<String,Long>> getAliveCrew() {
        return aliveCrew;
    }

    public List<String> getRequiredSabotages() {
        return requiredSabotages;
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
    public void initState() 
    {
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    @Override
    public String toString() 
    {
        StringBuilder text = new StringBuilder("--Estado | Tiempo: ").append(this.getGameTime()).append("--\n");
        text.append("Sabotajes restantes: ").append(this.requiredSabotages.size()).append("\n");
        text.append("Me encuentro en: ").append(this.currentRoom.getName()).append("\n");
        text.append("Vengo de: ").append(this.previousRoom != null ? this.previousRoom.getName() : "Empecé acá").append("\n");
        text.append("Tripulantes presentes: ").append(this.currentRoom.getCrewPresent().size()).append("\n");
        text.append("Tripulantes asesinados: ").append(this.crewKilled.size()).append("\n");
        text.append("Puedo sabotear?: ")
                .append(this.currentRoom.isSabotable() ? "SI" : "NO").append("\n");
        text.append("Sabotajes hechos: ").append(this.doneSabotages.size()).append("\n");
        text.append("Sabotajes restantes: ").append(this.requiredSabotages.size()).append("\n");
        text.append("Mi energía restante: ").append(this.getEnergy()).append("\n");
        text.append("Tengo capacidad extrasensorial? : ")
                .append(this.isSensorAvailable() ? "SI\n" : "NO\n").append("\n");
        
        return text.toString();
        
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
