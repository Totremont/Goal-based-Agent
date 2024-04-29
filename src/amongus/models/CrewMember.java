

package amongus.models;


public class CrewMember 
{
    private final String name;
    private CrewMemberState state;

    public CrewMember(int id) {
        this.name = new StringBuilder("Tripulante: #").append(id).toString();
    }

    public CrewMemberState getState() {
        return state;
    }

    public void setState(CrewMemberState state) {
        this.state = state;
    }
    
    

}
