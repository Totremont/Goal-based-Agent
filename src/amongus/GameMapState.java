
package amongus;

import amongus.models.CrewMember;
import amongus.models.CrewMemberState;
import amongus.models.Room;
import amongus.models.RoomState;
import amongus.models.Sabotage;
import frsf.cidisi.faia.state.EnvironmentState;
import java.util.HashMap;
import java.util.List;
import java.lang.Math;
import java.util.ArrayList;

//El estado asocia un ambiente del mapa con su correspondiente estado
public class GameMapState extends EnvironmentState 
{
    private HashMap<String,Room> map;
    private List<CrewMember> crews;
    private GameMap environment;

    public GameMapState(GameMap environment) 
    {
        this.initState();
        this.environment = environment;
    }
    
    
    @Override
    public void initState() 
    {
        this.map = getMap();
        
        double agentEnergy = Math.round((Math.random()*environment.MAX_ENERGY + environment.MIN_ENERGY) % (environment.MAX_ENERGY + 1));
        double totalPlayers = Math.round((Math.random()*environment.MAX_PLAYERS + environment.MIN_PLAYERS) % (environment.MAX_PLAYERS + 1));
        boolean sensorAvailable = Math.round(Math.random()) == 0;   //True or false
        
        List<String> roomNames = new ArrayList<>();    
        map.forEach((key,val) -> 
        {
            roomNames.add(key);    
            //Crear el initial state de cada ambiente
            new RoomState(val,false);
        });
        
        //Ambiente inicial del agente
        int agentInitialIndex = (int) Math.round(Math.random()*roomNames.size());
        String agentRoomName = roomNames.get(agentInitialIndex);
        RoomState agentRoomState = map.get(agentRoomName).getState();
        agentRoomState.setAgentPresent(true);
        
        //Distribuir tripulantes en el mapa
        for(int i = 0; i < totalPlayers; i++)
        {
            CrewMember crew = new CrewMember(i);
            CrewMemberState crewState = new CrewMemberState(crew,true);
            crews.add(crew);
            
            int crewInitialIndex = (int) Math.round(Math.random()*roomNames.size());
            String crewRoomName = roomNames.get(crewInitialIndex);
              
            RoomState crewRoomState = map.get(crewRoomName).getState();
            
            crewRoomState.addMember(crew);
                 
        }  
    }  

    @Override
    public String toString() 
    {
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
