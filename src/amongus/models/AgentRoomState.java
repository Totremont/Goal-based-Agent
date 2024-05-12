

package amongus.models;

import java.util.ArrayList;
import java.util.List;


//Estado de una habitación según observa el agente. Es un subconjunto del estado real RoomState asociado a la clase Room
public class AgentRoomState
{
    private String name;
    private final ArrayList<String> neighbors = new ArrayList<>(5);

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

    @Override
    public AgentRoomState clone() 
    {
        List<String> crew = ((List<String>)this.crewPresent.clone());
        List<String> neigh = ((List<String>)this.neighbors.clone());
        return new AgentRoomState(this.name,neigh,this.lastSeen,crew,this.sabotage);
    }

    @Override
    public boolean equals(Object obj) 
    {
        if(!(obj instanceof AgentRoomState)) return false;
        
        var other = (AgentRoomState) obj;
        
        return 
        (
                other.getName().equals(this.name) 
                && other.getNeighbors().equals(this.neighbors)
                && other.getLastSeen().equals(this.lastSeen)
                && other.getCrewPresent().equals(this.crewPresent)
                && other.isSabotable() == this.isSabotable()
        );
    }
    
    //--Setter
    
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
    
    public void addCrew(String name)
    {
        this.crewPresent.add(name);
    }
    
    public void deleteCrew(String name)
    {
        this.crewPresent.remove(name);
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

    public ArrayList<String> getCrewPresent() {
        return crewPresent;
    }

    public boolean isSabotable()
    {
        return this.sabotage != null;
    }

    public String getSabotage()
    {
        return this.sabotage;
    }
    
    public boolean isCrewPresent()
    {
        return !this.crewPresent.isEmpty();
    }
}
