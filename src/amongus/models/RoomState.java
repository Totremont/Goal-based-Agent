

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
    public RoomState(Room room) 
    {
        this.room = room;
        room.setState(this);
        this.agentPresent = false;
        this.isSabotable = room.getSabotage() != null;
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
    
    //Constructor para clonar
    public RoomState(Room room, Boolean agentPresent, List<CrewMember> currentMembers, Boolean isSabotable, Boolean copyFlag) 
    {
        this.room = room;
        //room.setState(this);
        this.agentPresent = agentPresent;
        this.isSabotable = isSabotable;
        this.currentMembers.addAll(currentMembers);
    }

    @Override
    public RoomState clone()
    {
        RoomState newState = new RoomState(this.room,this.agentPresent, this.currentMembers,this.isSabotable,true);
        
        return newState;
    }
    
    
    
    

    
    

}
