

package amongus.models;

import java.util.ArrayList;
import java.util.List;


//Estado de una habitación según observa el agente. Es un subconjunto del estado real RoomState asociado a la clase Room
public class AgentRoomState
{
    private String name;
    private final List<String> neighbors = new ArrayList<>();     //[O,N,E,S]

    //Última vez visitado
    private Long lastSeen;

    //Tripulantes encontrados
    private final ArrayList<String> crewPresent = new ArrayList<>();

    private String sabotage;

    public AgentRoomState(String name, List<String> neighbors, long lastSeen, List<String> crewPresent, String sabotage) 
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
        this.crewPresent.remove(name);
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
        this.crewPresent.addAll(crewPresent);
    }
}
