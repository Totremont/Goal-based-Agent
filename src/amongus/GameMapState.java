
package amongus;

import amongus.models.CrewMember;
import amongus.models.Room;
import frsf.cidisi.faia.state.EnvironmentState;
import java.util.HashMap;
import java.util.List;

//El estado asocia un ambiente del mapa con su correspondiente estado
public class GameMapState extends EnvironmentState 
{
    private HashMap<Room, Room.RoomState> rooms = new HashMap();

    public GameMapState(List<Room> rooms) 
    {
        rooms.stream().forEach(room -> 
        {
            this.rooms.put(room, room.new RoomState());
        });
    }
    
    @Override
    public void initState() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
