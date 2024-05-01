

package amongus.models;


public class CrewMemberState {
    
    private final CrewMember crew;
    private Boolean isAlive;
    private Room currentRoom;

    public CrewMemberState(CrewMember crew, Boolean isAlive, Room room) {
        this.crew = crew;
        this.isAlive = isAlive;
        this.crew.setState(this);
        this.currentRoom = room;
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) 
    {
        this.isAlive = isAlive;
        
        //Si no estoy vivo quitarme de la habitación (Pero guardar última habitación en este estado)
        if(!this.isAlive) currentRoom.getState().deleteMember(this.crew);
    }

    public CrewMember getCrew() {
        return crew;
    }
    
    
    
    
    
    

}
