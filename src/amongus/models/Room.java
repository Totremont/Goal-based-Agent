
package amongus.models;

import amongus.models.enums.Cardinal;
import amongus.models.enums.RoomType;
import java.util.ArrayList;
import java.util.List;

//Esta clase contiene la información estática de un ambiente

public class Room 
{
    private final String name;
    private final RoomType type;
    
    private Sabotage sabotage = null;
    private final List<Room> neighbors = new ArrayList<>();
    private RoomState state;

    public Room(String name, RoomType type) 
    {
        this.name = name;
        this.type = type;
        for (int i = 0; i < 5; i++) 
        {
            this.neighbors.add(null);    //Creamos las posiciones [O,N,E,S,T]
        }
    }

    public void setState(RoomState state) {
        this.state = state;
    }
    
    public void setSabotage(Sabotage sabotage)
    {
        this.sabotage = sabotage;
        if(sabotage != null)
        {
            this.sabotage.setRoom(this);
        }
    }
    
    
    public Room neighborAt(Cardinal cardinal)
    {
        return neighbors.get(cardinal.ordinal());
    }

    //Añade un nuevo vecino y le notifico al mismo que me añada a mi
    public void addNeighbor(Room room, Cardinal cardinal)
    {
        this.neighbors.set(cardinal.ordinal(), room);
        room.addMe(this, opposite(cardinal));
    }
    
    //Cuando un vecino me pide a mi que lo añada
    void addMe(Room room, Cardinal cardinal)
    {
        if(!this.neighbors.contains(room))
        {
            this.neighbors.set(cardinal.ordinal(), room);
        }
        else System.out.printf("El vecino %s ya existe en %s",room.getName(), this.getName());
    }
     
    
    //Obtiene la dirección opuesta a la dada
    private static Cardinal opposite(Cardinal cardinal)
    {
        if(cardinal.equals(Cardinal.TUB)) return Cardinal.TUB;
        
        int index = cardinal.ordinal() > 1 ? cardinal.ordinal() - 2 : cardinal.ordinal() + 2;
        return Cardinal.values()[index];
    }
    
    
    public String getName() {
        return name;
    }

    public List<Room> getNeighbors() {
        return neighbors;
    }

    public RoomType getType() {
        return type;
    }

    public Sabotage getSabotage() {
        return sabotage;
    }

    public RoomState getState() {
        return state;
    }
    
    
    
    
    
}
