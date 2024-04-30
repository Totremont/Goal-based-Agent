
package amongus;

import amongus.models.Room;
import amongus.models.Sabotage;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import frsf.cidisi.faia.state.EnvironmentState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Representa los elementos estáticos del mapa, los dinámicos se representan con su estado

public class Game extends Environment 
{
    public final HashMap<String,Room> map = new HashMap<>();

    public int MAX_ENERGY = 150;
    public int MIN_ENERGY = 30;
    public int MAX_CREW = 10;
    public int MIN_CREW = 7;
    
    private GameState state = new GameState(this);

    public Game(int maxEnergy, int minEnergy, int maxCrew, int minCrew) 
    {
        super();
        MAX_ENERGY = maxEnergy;
        MIN_CREW = minCrew;
        MAX_CREW = maxCrew;
        MIN_CREW = minCrew;
        
        //Setear mapa del juego
        setGameMap(this.map);
    }
    
    

    @Override
    public EnvironmentState getEnvironmentState() 
    {
        return this.state;
    }
    
    @Override
    public Perception getPercept() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    } 
    
    //Inicializa información estática de los ambientes
    private static void setGameMap(HashMap<String,Room> map)
    {    
        //Secciones
        map.put("Motor superior", new Room("Motor superior", Room.RoomType.SECCION,null));     
        map.put("Motor Inferior", new Room("Motor Inferior", Room.RoomType.SECCION,null));
        map.put("Hospital",       new Room("Hospital", Room.RoomType.SECCION,null));
        map.put("Cafetería",      new Room("Cafetería", Room.RoomType.SECCION,null));
        map.put("Armas",          new Room("Armas", Room.RoomType.SECCION,new Sabotage("Armas")));
        map.put("Navegación",     new Room("Navegación", Room.RoomType.SECCION,null));
        map.put("Oxígeno",        new Room("Oxígeno", Room.RoomType.SECCION,null));
        map.put("Escudos",        new Room("Escudos", Room.RoomType.SECCION,null));
        map.put("Comunicación",   new Room("Comunicación", Room.RoomType.SECCION,null));
        map.put("Depósito",       new Room("Depósito", Room.RoomType.SECCION,null));
        map.put("Electricidad",   new Room("Electricidad", Room.RoomType.SECCION,new Sabotage("Electricidad")));
        map.put("Administración", new Room("Administración", Room.RoomType.SECCION,null));
        map.put("Reactor",        new Room("Reactor", Room.RoomType.SECCION,new Sabotage("Reactor")));
        map.put("Seguridad",      new Room("Seguridad", Room.RoomType.SECCION,null));
        
        //Pasillos
        map.put("Pasillo oeste superior",     new Room("Pasillo oeste superior", Room.RoomType.PASILLO,null));
        map.put("Pasillo oeste centro",       new Room("Pasillo oeste centro", Room.RoomType.PASILLO,null));
        map.put("Pasillo oeste inferior",     new Room("Pasillo oeste inferior", Room.RoomType.PASILLO,null));
        map.put("Pasillo central",            new Room("Pasillo central", Room.RoomType.PASILLO,null));
        map.put("Pasillo este centro",        new Room("Pasillo este centro", Room.RoomType.PASILLO,null));
        map.put("Pasillo este inferior",      new Room("Pasillo este inferior", Room.RoomType.PASILLO,null));
        
        //Tuberias
        map.put("Tubería RMS",        new Room("Tubería RMS", Room.RoomType.TUBERIA,null));
        map.put("Tubería RMI",        new Room("Tubería RMI", Room.RoomType.TUBERIA,null));
        map.put("Tubería SHE",        new Room("Tubería SHE", Room.RoomType.TUBERIA,null));
        map.put("Tubería CAPEC",      new Room("Tubería CAPEC", Room.RoomType.TUBERIA,null));
        map.put("Tubería AN",         new Room("Tubería AN", Room.RoomType.TUBERIA,null));
        map.put("Tubería EN",         new Room("Tubería EN", Room.RoomType.TUBERIA,null));
        
        //Adyacencias - Recordar que el vecino también añade al primero, no es necesario declararlo.
        map.get("Motor superior").addNeighbor(map.get("Pasillo oeste superior"), Room.Cardinal.ESTE);
        map.get("Motor superior").addNeighbor(map.get("Pasillo oeste centro"), Room.Cardinal.SUR);
        //map.get("Motor superior").addNeighbor(map.get("Tubería RMS"), Room.Cardinal.TUB);
        
        map.get("Motor inferior").addNeighbor(map.get("Pasillo oeste inferior"), Room.Cardinal.ESTE);
        map.get("Motor inferior").addNeighbor(map.get("Pasillo oeste centro"), Room.Cardinal.NORTE);
        //map.get("Motor inferior").addNeighbor(map.get("Tubería RMI"), Room.Cardinal.TUB);
                    
        map.get("Electricidad").addNeighbor(map.get("Pasillo oeste inferior"), Room.Cardinal.SUR);
        
        map.get("Seguridad").addNeighbor(map.get("Pasillo oeste centro"), Room.Cardinal.OESTE);
        //map.get("Seguridad").addNeighbor(map.get("Tubería SHE"), Room.Cardinal.TUB);
        
        map.get("Reactor").addNeighbor(map.get("Pasillo oeste centro"), Room.Cardinal.ESTE);
        
        map.get("Hospital").addNeighbor(map.get("Pasillo oeste superior"), Room.Cardinal.NORTE);
        //map.get("Hospital").addNeighbor(map.get("Tubería SHE"), Room.Cardinal.TUB);
        
        map.get("Cafetería").addNeighbor(map.get("Pasillo oeste superior"), Room.Cardinal.OESTE);
        map.get("Cafetería").addNeighbor(map.get("Armas"), Room.Cardinal.ESTE);
        map.get("Cafetería").addNeighbor(map.get("Pasillo central"), Room.Cardinal.SUR);
        
        map.get("Armas").addNeighbor(map.get("Pasillo este centro"), Room.Cardinal.SUR);
        
        map.get("Oxígeno").addNeighbor(map.get("Pasillo este centro"), Room.Cardinal.ESTE);
        
        map.get("Navegación").addNeighbor(map.get("Pasillo este centro"), Room.Cardinal.OESTE);
        
        map.get("Escudos").addNeighbor(map.get("Pasillo este centro"), Room.Cardinal.NORTE);
        map.get("Escudos").addNeighbor(map.get("Pasillo este inferior"), Room.Cardinal.OESTE);
        
        map.get("Comunicación").addNeighbor(map.get("Pasillo este inferior"), Room.Cardinal.NORTE);
        
        map.get("Depósito").addNeighbor(map.get("Pasillo este inferior"), Room.Cardinal.ESTE);
        map.get("Depósito").addNeighbor(map.get("Pasillo central"), Room.Cardinal.NORTE);
        map.get("Depósito").addNeighbor(map.get("Pasillo oeste inferior"), Room.Cardinal.OESTE);
        
    }
    
    
    
}
