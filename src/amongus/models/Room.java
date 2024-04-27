

package amongus.models;

import java.util.ArrayList;
import java.util.List;

//Esta clase contiene la información estática de un ambiente

public class Room 
{
    private final String name;
    private List<Room> nextTo;
    private final RoomType type;
    private final Sabotage sabotage;
    
    
    public Room(String name, List<Room> nextTo, RoomType type , Sabotage sabotage) {
        this.name = name;
        this.nextTo = nextTo;
        this.type = type;
        this.sabotage = sabotage;
    }

    public Room(String name, RoomType type, Sabotage sabotage) {
        this.name = name;
        this.type = type;
        this.sabotage = sabotage;
    }

    public void setNextTo(List<Room> nextTo) {
        this.nextTo = nextTo;
    }
    
       
    public enum RoomType
    {
        SECCION, PASILLO, TUBERIA
    }
    
    public class RoomState
    {
        private List<CrewMember> currentMembers;
        private Boolean agentPresent;
        private List<Sabotage> usedSabotages;

        public RoomState(List<CrewMember> currentMembers, Boolean agentPresent) {
            this.currentMembers = currentMembers;
            this.agentPresent = agentPresent;
            this.usedSabotages = new ArrayList<>();
        }

        
        
        
    }
    
}
