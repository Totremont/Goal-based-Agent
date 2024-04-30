
package amongus;

import amongus.models.Room;
import java.util.HashMap;
import java.util.List;

//Subconjunto de atributos del estado del mapa que el agente conoce/entiende
public class ImpostorAgentState 
{
    private RoomState currentRoom;
    
    //Habitaciones conocidas / Submapa del agente
    private HashMap<String,RoomState> knownRooms;
    
    private List<String> crewKilled;
    
    //Nombre del tripulante vivo / última ubicación conocida
    private HashMap<String, String> knownCrew;
    
    //Estado de una habitación según observa el agente. Es un subconjunto del estado real RoomState en la clase Room
    private class RoomState
    {
        private String name;
        private List<String> neighbors;     //[O,N,E,S]
        
        //Última vez visitado
        private Long lastSeen;
        
        //Tripulantes encontrados
        private List<String> crewPresent;
          
    }

}
