

package amongus.models;

import java.util.ArrayList;
import java.util.List;


public class RoomState
{
    private final ArrayList<CrewMember> currentMembers = new ArrayList<>();
    private final Room room;
    private Boolean agentPresent;
    private Boolean isSabotable;

    //Initial state
    public RoomState(Room room, Boolean agentPresent) 
    {
        this.agentPresent = agentPresent;
        this.room = room;
        room.setState(this);
        isSabotable = room.getSabotage() != null;
    }

    public void addMember(CrewMember crew) 
    {
        this.currentMembers.add(crew);
    }
    
    public void deleteMember(CrewMember crew)
    {
        this.currentMembers.remove(crew);
    }

    public void setAgentPresent(Boolean agentPresent) {
        this.agentPresent = agentPresent;
    }

    public void setIsSabotable(Boolean isSabotable) {
        this.isSabotable = isSabotable;
    }

    public ArrayList<CrewMember> getCurrentMembers() {
        return currentMembers;
    }

    public Room getRoom() {
        return room;
    }

    public Boolean getAgentPresent() {
        return agentPresent;
    }

    public Boolean isSabotable() {
        return isSabotable;
    }
    
    

    
    

}
