
package amongus;

import amongus.models.Room;
import amongus.models.Sabotage;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Representa los elementos estáticos del mapa, los dinámicos se representan con su estado

public class GameMap extends Environment 
{
    private static HashMap<String,Room> rooms;
    private static GameMapState state;


    //Función que se llama cuando comienza el juego (randomiza el inicio)
    public void setStart()
    {
        
    }
    
    
    
    @Override
    public Perception getPercept() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    //Inicializa información estática de los ambientes
    private HashMap<String,Room> getMap()
    {
        HashMap<String,Room> rooms = new HashMap<>();
        
        //Secciones
        rooms.put("Motor superior", new Room("Motor superior", Room.RoomType.SECCION,null));     
        rooms.put("Motor Inferior", new Room("Motor Inferior", Room.RoomType.SECCION,null));
        rooms.put("Hospital",       new Room("Hospital", Room.RoomType.SECCION,null));
        rooms.put("Cafetería",      new Room("Cafetería", Room.RoomType.SECCION,null));
        rooms.put("Armas",          new Room("Armas", Room.RoomType.SECCION,new Sabotage("Armas")));
        rooms.put("Navegación",     new Room("Navegación", Room.RoomType.SECCION,null));
        rooms.put("Oxígeno",        new Room("Oxígeno", Room.RoomType.SECCION,null));
        rooms.put("Escudos",        new Room("Escudos", Room.RoomType.SECCION,null));
        rooms.put("Comunicación",   new Room("Comunicación", Room.RoomType.SECCION,null));
        rooms.put("Depósito",       new Room("Depósito", Room.RoomType.SECCION,null));
        rooms.put("Electricidad",   new Room("Electricidad", Room.RoomType.SECCION,new Sabotage("Electricidad")));
        rooms.put("Administración", new Room("Administración", Room.RoomType.SECCION,null));
        rooms.put("Reactor",        new Room("Reactor", Room.RoomType.SECCION,new Sabotage("Reactor")));
        rooms.put("Seguridad",      new Room("Seguridad", Room.RoomType.SECCION,null));
        
        //Pasillos
        rooms.put("Pasillo oeste superior",     new Room("Pasillo oeste superior", Room.RoomType.PASILLO,null));
        rooms.put("Pasillo oeste centro",       new Room("Pasillo oeste centro", Room.RoomType.PASILLO,null));
        rooms.put("Pasillo oeste inferior",     new Room("Pasillo oeste inferior", Room.RoomType.PASILLO,null));
        rooms.put("Pasillo central",            new Room("Pasillo central", Room.RoomType.PASILLO,null));
        rooms.put("Pasillo este centro",        new Room("Pasillo este centro", Room.RoomType.PASILLO,null));
        rooms.put("Pasillo este inferior",      new Room("Pasillo este inferior", Room.RoomType.PASILLO,null));
        
        //Tuberias
        rooms.put("Tubería RMS",        new Room("Tubería RMS", Room.RoomType.TUBERIA,null));
        rooms.put("Tubería RMI",        new Room("Tubería RMI", Room.RoomType.TUBERIA,null));
        rooms.put("Tubería SHE",        new Room("Tubería SHE", Room.RoomType.TUBERIA,null));
        rooms.put("Tubería CAPEC",      new Room("Tubería CAPEC", Room.RoomType.TUBERIA,null));
        rooms.put("Tubería AN",         new Room("Tubería AN", Room.RoomType.TUBERIA,null));
        rooms.put("Tubería EN",         new Room("Tubería EN", Room.RoomType.TUBERIA,null));
        
        //Adyacencias - Recordar que el vecino también añade al primero, no es necesario declararlo.
        rooms.get("Motor superior").addNeighbor(rooms.get("Pasillo oeste superior"), Room.Cardinal.ESTE);
        rooms.get("Motor superior").addNeighbor(rooms.get("Pasillo oeste centro"), Room.Cardinal.SUR);
        //rooms.get("Motor superior").addNeighbor(rooms.get("Tubería RMS"), Room.Cardinal.TUB);
        
        rooms.get("Motor inferior").addNeighbor(rooms.get("Pasillo oeste inferior"), Room.Cardinal.ESTE);
        rooms.get("Motor inferior").addNeighbor(rooms.get("Pasillo oeste centro"), Room.Cardinal.NORTE);
        //rooms.get("Motor inferior").addNeighbor(rooms.get("Tubería RMI"), Room.Cardinal.TUB);
                    
        rooms.get("Electricidad").addNeighbor(rooms.get("Pasillo oeste inferior"), Room.Cardinal.SUR);
        
        rooms.get("Seguridad").addNeighbor(rooms.get("Pasillo oeste centro"), Room.Cardinal.OESTE);
        //rooms.get("Seguridad").addNeighbor(rooms.get("Tubería SHE"), Room.Cardinal.TUB);
        
        rooms.get("Reactor").addNeighbor(rooms.get("Pasillo oeste centro"), Room.Cardinal.ESTE);
        
        rooms.get("Hospital").addNeighbor(rooms.get("Pasillo oeste superior"), Room.Cardinal.NORTE);
        //rooms.get("Hospital").addNeighbor(rooms.get("Tubería SHE"), Room.Cardinal.TUB);
        
        rooms.get("Cafetería").addNeighbor(rooms.get("Pasillo oeste superior"), Room.Cardinal.OESTE);
        rooms.get("Cafetería").addNeighbor(rooms.get("Armas"), Room.Cardinal.ESTE);
        rooms.get("Cafetería").addNeighbor(rooms.get("Pasillo central"), Room.Cardinal.SUR);
        
        rooms.get("Armas").addNeighbor(rooms.get("Pasillo este centro"), Room.Cardinal.SUR);
        
        rooms.get("Oxígeno").addNeighbor(rooms.get("Pasillo este centro"), Room.Cardinal.ESTE);
        
        rooms.get("Navegación").addNeighbor(rooms.get("Pasillo este centro"), Room.Cardinal.OESTE);
        
        rooms.get("Escudos").addNeighbor(rooms.get("Pasillo este centro"), Room.Cardinal.NORTE);
        rooms.get("Escudos").addNeighbor(rooms.get("Pasillo este inferior"), Room.Cardinal.OESTE);
        
        rooms.get("Comunicación").addNeighbor(rooms.get("Pasillo este inferior"), Room.Cardinal.NORTE);
        
        rooms.get("Depósito").addNeighbor(rooms.get("Pasillo este inferior"), Room.Cardinal.ESTE);
        rooms.get("Depósito").addNeighbor(rooms.get("Pasillo central"), Room.Cardinal.NORTE);
        rooms.get("Depósito").addNeighbor(rooms.get("Pasillo oeste inferior"), Room.Cardinal.OESTE);
        
        return rooms;
    }
    
    
}
