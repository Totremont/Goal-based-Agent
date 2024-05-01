
package amongus;

import amongus.models.Room;
import amongus.models.RoomState;
import amongus.models.Sabotage;
import amongus.models.enums.Cardinal;
import amongus.models.enums.RoomType;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import frsf.cidisi.faia.state.EnvironmentState;
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
    
    private final GameState state = new GameState(this);

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
    
    //Percepcion que el mundo le da al agente. Transforma los datos a un formato que el agente entiende (DTO)
    @Override
    public Perception getPercept() 
    {
       Room agentLocation = state.getAgentRoom();   //Incluye el estado de la habitación.
       RoomState roomState = agentLocation.getState();
       
       List<String> neighbors = agentLocation.getNeighbors().stream().map(it -> it.getName()).toList();
       List<String> crewPresent = roomState.getCurrentMembers().stream().map(it -> it.getName()).toList();
    
       
       ImpostorAgentPerc agentPerc = new ImpostorAgentPerc(
               agentLocation.getName(), state.getAgentEnergy(), 
               neighbors,crewPresent, roomState.getIsSabotable(), state.getGameTime()
        );
       
       return agentPerc;
       
    } 
    
    //Inicializa información estática de los ambientes
    private static void setGameMap(HashMap<String,Room> map)
    {    
        //Secciones
        map.put("Motor superior", new Room("Motor superior", RoomType.SECCION,null));     
        map.put("Motor Inferior", new Room("Motor Inferior", RoomType.SECCION,null));
        map.put("Hospital",       new Room("Hospital", RoomType.SECCION,null));
        map.put("Cafetería",      new Room("Cafetería", RoomType.SECCION,null));
        map.put("Armas",          new Room("Armas", RoomType.SECCION,new Sabotage("Armas")));
        map.put("Navegación",     new Room("Navegación", RoomType.SECCION,null));
        map.put("Oxígeno",        new Room("Oxígeno", RoomType.SECCION,null));
        map.put("Escudos",        new Room("Escudos", RoomType.SECCION,null));
        map.put("Comunicación",   new Room("Comunicación", RoomType.SECCION,null));
        map.put("Depósito",       new Room("Depósito", RoomType.SECCION,null));
        map.put("Electricidad",   new Room("Electricidad", RoomType.SECCION,new Sabotage("Electricidad")));
        map.put("Administración", new Room("Administración", RoomType.SECCION,null));
        map.put("Reactor",        new Room("Reactor", RoomType.SECCION,new Sabotage("Reactor")));
        map.put("Seguridad",      new Room("Seguridad", RoomType.SECCION,null));
        
        //Pasillos
        map.put("Pasillo oeste superior",     new Room("Pasillo oeste superior", RoomType.PASILLO,null));
        map.put("Pasillo oeste centro",       new Room("Pasillo oeste centro", RoomType.PASILLO,null));
        map.put("Pasillo oeste inferior",     new Room("Pasillo oeste inferior", RoomType.PASILLO,null));
        map.put("Pasillo central",            new Room("Pasillo central", RoomType.PASILLO,null));
        map.put("Pasillo este centro",        new Room("Pasillo este centro", RoomType.PASILLO,null));
        map.put("Pasillo este inferior",      new Room("Pasillo este inferior", RoomType.PASILLO,null));
        
        //Tuberias
        map.put("Tubería RMS",        new Room("Tubería RMS", RoomType.TUBERIA,null));
        map.put("Tubería RMI",        new Room("Tubería RMI", RoomType.TUBERIA,null));
        map.put("Tubería SHE",        new Room("Tubería SHE", RoomType.TUBERIA,null));
        map.put("Tubería CAPEC",      new Room("Tubería CAPEC", RoomType.TUBERIA,null));
        map.put("Tubería AN",         new Room("Tubería AN", RoomType.TUBERIA,null));
        map.put("Tubería EN",         new Room("Tubería EN", RoomType.TUBERIA,null));
        
        //Adyacencias - Recordar que el vecino también añade al primero, no es necesario declararlo.
        map.get("Motor superior").addNeighbor(map.get("Pasillo oeste superior"), Cardinal.ESTE);
        map.get("Motor superior").addNeighbor(map.get("Pasillo oeste centro"), Cardinal.SUR);
        //map.get("Motor superior").addNeighbor(map.get("Tubería RMS"), Cardinal.TUB);
        
        map.get("Motor inferior").addNeighbor(map.get("Pasillo oeste inferior"), Cardinal.ESTE);
        map.get("Motor inferior").addNeighbor(map.get("Pasillo oeste centro"), Cardinal.NORTE);
        //map.get("Motor inferior").addNeighbor(map.get("Tubería RMI"), Cardinal.TUB);
                    
        map.get("Electricidad").addNeighbor(map.get("Pasillo oeste inferior"), Cardinal.SUR);
        
        map.get("Seguridad").addNeighbor(map.get("Pasillo oeste centro"), Cardinal.OESTE);
        //map.get("Seguridad").addNeighbor(map.get("Tubería SHE"), Cardinal.TUB);
        
        map.get("Reactor").addNeighbor(map.get("Pasillo oeste centro"), Cardinal.ESTE);
        
        map.get("Hospital").addNeighbor(map.get("Pasillo oeste superior"), Cardinal.NORTE);
        //map.get("Hospital").addNeighbor(map.get("Tubería SHE"), Cardinal.TUB);
        
        map.get("Cafetería").addNeighbor(map.get("Pasillo oeste superior"), Cardinal.OESTE);
        map.get("Cafetería").addNeighbor(map.get("Armas"), Cardinal.ESTE);
        map.get("Cafetería").addNeighbor(map.get("Pasillo central"), Cardinal.SUR);
        
        map.get("Armas").addNeighbor(map.get("Pasillo este centro"), Cardinal.SUR);
        
        map.get("Oxígeno").addNeighbor(map.get("Pasillo este centro"), Cardinal.ESTE);
        
        map.get("Navegación").addNeighbor(map.get("Pasillo este centro"), Cardinal.OESTE);
        
        map.get("Escudos").addNeighbor(map.get("Pasillo este centro"), Cardinal.NORTE);
        map.get("Escudos").addNeighbor(map.get("Pasillo este inferior"), Cardinal.OESTE);
        
        map.get("Comunicación").addNeighbor(map.get("Pasillo este inferior"), Cardinal.NORTE);
        
        map.get("Depósito").addNeighbor(map.get("Pasillo este inferior"), Cardinal.ESTE);
        map.get("Depósito").addNeighbor(map.get("Pasillo central"), Cardinal.NORTE);
        map.get("Depósito").addNeighbor(map.get("Pasillo oeste inferior"), Cardinal.OESTE);
        
    }
    
    
    
}
