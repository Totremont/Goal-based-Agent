

package amongus.models;


public class CrewMemberState {
    
    private final CrewMember crew;
    private Boolean isAlive;

    public CrewMemberState(CrewMember crew, Boolean isAlive) {
        this.crew = crew;
        this.isAlive = isAlive;
        this.crew.setState(this);
    }

    public Boolean getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(Boolean isAlive) {
        this.isAlive = isAlive;
    }
    
    

}
