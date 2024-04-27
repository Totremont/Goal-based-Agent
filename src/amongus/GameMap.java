
package amongus;

import amongus.models.Room;
import amongus.models.Sabotage;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import java.util.ArrayList;
import java.util.List;

//Representa los elementos estáticos del mapa, los dinámicos se representan con su estado

public class GameMap extends Environment 
{
    private static List<Room> rooms;
    private static GameMapState state;

    public GameMap(List<Room> rooms) {
        this.rooms = rooms;
        
    }
    
    //Función que se llama cuando comienza el juego (randomiza el inicio)
    public void setStart()
    {
        
    }
    
    
    
    @Override
    public Perception getPercept() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    //Inicializa información estática de los ambientes
    private List<Room> getRooms()
    {
        List<Room> rooms = new ArrayList<>();
        
        //Secciones
        rooms.add(new Room("Motor superior", Room.RoomType.SECCION,null));
        rooms.add(new Room("Motor Inferior", Room.RoomType.SECCION,null));
        rooms.add(new Room("Hospital", Room.RoomType.SECCION,null));
        rooms.add(new Room("Cafetería", Room.RoomType.SECCION,null));
        rooms.add(new Room("Armas", Room.RoomType.SECCION,new Sabotage("Armas")));
        rooms.add(new Room("Navegación", Room.RoomType.SECCION,null));
        rooms.add(new Room("Oxígeno", Room.RoomType.SECCION,null));
        rooms.add(new Room("Escudos", Room.RoomType.SECCION,null));
        rooms.add(new Room("Comunicación", Room.RoomType.SECCION,null));
        rooms.add(new Room("Depósito", Room.RoomType.SECCION,null));
        rooms.add(new Room("Electricidad", Room.RoomType.SECCION,new Sabotage("Electricidad")));
        rooms.add(new Room("Administración", Room.RoomType.SECCION,null));
        rooms.add(new Room("Reactor", Room.RoomType.SECCION,new Sabotage("Reactor")));
        rooms.add(new Room("Seguridad", Room.RoomType.SECCION,null));
        
        //Pasillos
        rooms.add(new Room("Pasillo oeste superior", Room.RoomType.PASILLO,null));
        rooms.add(new Room("Pasillo oeste centro", Room.RoomType.PASILLO,null));
        rooms.add(new Room("Pasillo oeste inferior", Room.RoomType.PASILLO,null));
        rooms.add(new Room("Pasillo central", Room.RoomType.PASILLO,null));
        rooms.add(new Room("Pasillo este centro", Room.RoomType.PASILLO,null));
        rooms.add(new Room("Pasillo este inferior", Room.RoomType.PASILLO,null));
        
        //Tuberias
        rooms.add(new Room("Tubería RMS", Room.RoomType.TUBERIA,null));
        rooms.add(new Room("Tubería RMI", Room.RoomType.TUBERIA,null));
        rooms.add(new Room("Tubería SHE", Room.RoomType.TUBERIA,null));
        rooms.add(new Room("Tubería CAPEC", Room.RoomType.TUBERIA,null));
        rooms.add(new Room("Tubería AN", Room.RoomType.TUBERIA,null));
        rooms.add(new Room("Tubería EN", Room.RoomType.TUBERIA,null));
        
        //Adyacencias -- TODO
        
    }
    
    
}
