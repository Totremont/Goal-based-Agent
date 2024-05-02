
package amongus;

import amongus.models.AgentRoomState;
import amongus.models.Room;
import amongus.models.RoomState;
import amongus.models.Sabotage;
import amongus.models.enums.Cardinal;
import amongus.models.enums.RoomType;
import amongus.utils.Pair;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import frsf.cidisi.faia.state.EnvironmentState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Representa el juego en su conjunto
public class Game extends Environment 
{
    public final HashMap<String,Room> map = new HashMap<>();
    public final HashMap<String, Sabotage> sabotages = new HashMap<>();
    
    private final ImpostorAgent agent;

    // -- Parámetros de juego
    public int MAX_ENERGY = 150;
    public int MIN_ENERGY = 30;
    public int MAX_CREW = 10;
    public int MIN_CREW = 7;
    public int MAX_CREW_STEP_TIME = 3;
    public int MIN_CREW_STEP_TIME = 1;
    public int MAX_AGENT_SENSOR_STEP_TIME = 5;
    public int MIN_AGENT_SENSOR_STEP_TIME = 3;
    
    private final GameState state;

    public Game(int maxEnergy, int minEnergy, int maxCrew, int minCrew) 
    {
        super();
        MAX_ENERGY = maxEnergy;
        MIN_CREW = minCrew;
        MAX_CREW = maxCrew;
        MIN_CREW = minCrew;
        
        //Setear mapa del juego
        setGameMap(this.map,this.sabotages);
        
        //Setear estado inicial
        this.state = new GameState(this);
        
        //Crear agente - Mapeando información a su formato
        //Mapear mapa
        HashMap<String,AgentRoomState> gameRooms = new HashMap<>();
        this.map.forEach((key,val) -> 
        {
            List<String> neighbors = val.getNeighbors().stream().map(it -> it.getName()).toList();
            String sabotage = val.getSabotage() != null ? val.getSabotage().getName() : null;
            var roomState = new AgentRoomState(val.getName(),neighbors,-1l,null,sabotage);
            gameRooms.put(key,roomState);
        });
        
        //Mapear tripulantes
        HashMap<String,Pair<String,Long>> gameCrew = new HashMap<>();
        this.state.getCrews().stream().forEach(crew -> 
        {
           gameCrew.put(crew.getName(), new Pair(null,-1l));
        });
        
        //Mapear sabotages
        List<String> sabotages = new ArrayList<>();
        this.sabotages.forEach((key,val) -> 
        {
            sabotages.add(key);
        });
                
        agent = new ImpostorAgent(gameRooms,gameCrew,sabotages);
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
       
       String sabotage = roomState.isSabotable() ? roomState.getRoom().getSabotage().getName() : null;
    
       
       ImpostorAgentPerc agentPerc = new ImpostorAgentPerc(
               agentLocation.getName(), state.getAgentEnergy(), 
               neighbors,crewPresent, sabotage, state.getGameTime(), state.isAgentSensorAvail()
        );
       
       return agentPerc;
       
    } 
    
    //Inicializa información estática de los ambientes
    private static void setGameMap(HashMap<String,Room> map, HashMap<String,Sabotage> sabotages)
    {    
        //Secciones
        map.put("Motor superior", new Room("Motor superior", RoomType.SECCION));     
        map.put("Motor Inferior", new Room("Motor Inferior", RoomType.SECCION));
        map.put("Hospital",       new Room("Hospital", RoomType.SECCION));
        map.put("Cafetería",      new Room("Cafetería", RoomType.SECCION));
        map.put("Armas",          new Room("Armas", RoomType.SECCION));
        map.put("Navegación",     new Room("Navegación", RoomType.SECCION));
        map.put("Oxígeno",        new Room("Oxígeno", RoomType.SECCION));
        map.put("Escudos",        new Room("Escudos", RoomType.SECCION));
        map.put("Comunicación",   new Room("Comunicación", RoomType.SECCION));
        map.put("Depósito",       new Room("Depósito", RoomType.SECCION));
        map.put("Electricidad",   new Room("Electricidad", RoomType.SECCION));
        map.put("Administración", new Room("Administración", RoomType.SECCION));
        map.put("Reactor",        new Room("Reactor", RoomType.SECCION));
        map.put("Seguridad",      new Room("Seguridad", RoomType.SECCION));
        
        //Pasillos
        map.put("Pasillo oeste superior",     new Room("Pasillo oeste superior", RoomType.PASILLO));
        map.put("Pasillo oeste centro",       new Room("Pasillo oeste centro", RoomType.PASILLO));
        map.put("Pasillo oeste inferior",     new Room("Pasillo oeste inferior", RoomType.PASILLO));
        map.put("Pasillo central",            new Room("Pasillo central", RoomType.PASILLO));
        map.put("Pasillo este centro",        new Room("Pasillo este centro", RoomType.PASILLO));
        map.put("Pasillo este inferior",      new Room("Pasillo este inferior", RoomType.PASILLO));
        
        //Tuberias
        map.put("Tubería RMS",        new Room("Tubería RMS", RoomType.TUBERIA));
        map.put("Tubería RMI",        new Room("Tubería RMI", RoomType.TUBERIA));
        map.put("Tubería SHE",        new Room("Tubería SHE", RoomType.TUBERIA));
        map.put("Tubería CAPEC",      new Room("Tubería CAPEC", RoomType.TUBERIA));
        map.put("Tubería AN",         new Room("Tubería AN", RoomType.TUBERIA));
        map.put("Tubería EN",         new Room("Tubería EN", RoomType.TUBERIA));
        
        //Adyacencias - Recordar que el vecino también añade al primero, no es necesario declararlo.
        map.get("Motor superior").addNeighbor(map.get("Pasillo oeste superior"), Cardinal.ESTE);
        map.get("Motor superior").addNeighbor(map.get("Pasillo oeste centro"), Cardinal.SUR);
        //map.get("Motor superior").addNeighbor(map.get("Tubería RMS"), Cardinal.TUB);
        
        map.get("Motor inferior").addNeighbor(map.get("Pasillo oeste inferior"), Cardinal.ESTE);
        map.get("Motor inferior").addNeighbor(map.get("Pasillo oeste centro"), Cardinal.NORTE);
        //map.get("Motor inferior").addNeighbor(map.get("Tubería RMI"), Cardinal.TUB);
                    
        map.get("Electricidad").addNeighbor(map.get("Pasillo oeste inferior"), Cardinal.SUR);
        sabotages.put("Electricidad", new Sabotage("Electricidad"));
        map.get("Electricidad").setSabotage(sabotages.get("Electricidad"));
        
        map.get("Seguridad").addNeighbor(map.get("Pasillo oeste centro"), Cardinal.OESTE);
        //map.get("Seguridad").addNeighbor(map.get("Tubería SHE"), Cardinal.TUB);
        
        map.get("Reactor").addNeighbor(map.get("Pasillo oeste centro"), Cardinal.ESTE);
        sabotages.put("Reactor", new Sabotage("Reactor"));
        map.get("Reactor").setSabotage(sabotages.get("Reactor"));
        
        map.get("Hospital").addNeighbor(map.get("Pasillo oeste superior"), Cardinal.NORTE);
        //map.get("Hospital").addNeighbor(map.get("Tubería SHE"), Cardinal.TUB);
        
        map.get("Cafetería").addNeighbor(map.get("Pasillo oeste superior"), Cardinal.OESTE);
        map.get("Cafetería").addNeighbor(map.get("Armas"), Cardinal.ESTE);
        map.get("Cafetería").addNeighbor(map.get("Pasillo central"), Cardinal.SUR);
        
        map.get("Armas").addNeighbor(map.get("Pasillo este centro"), Cardinal.SUR);
        sabotages.put("Armas", new Sabotage("Armas"));
        map.get("Armas").setSabotage(sabotages.get("Armas"));
        
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
