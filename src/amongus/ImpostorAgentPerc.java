
package amongus;

import amongus.models.enums.Cardinal;
import amongus.utils.Pair;
import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import java.util.ArrayList;
import java.util.List;

//Como ve el mundo nuestro agente
public class ImpostorAgentPerc extends Perception 
{
    private String currentRoomSensor;
    private List<String> cardinalSensor = new ArrayList<>(5);    //[O,N,E,S]
    private List<String> crewPresentSensor = new ArrayList<>();
    private Long energySensor;
    private String sabotageSensor;
    private Long gameTimeSensor;
    private boolean extraSensorAvail;
    
    //Lista de pares entre un tripulante y su ubicación (extrasensorial).
    private List<Pair<String,String>> extraSensor = new ArrayList<>();
    private boolean extraInfoAvail;
    
    
    public ImpostorAgentPerc     //Constructor normal
    (
        String currentRoom, Long energy, List<String> neighbors, 
        List<String> crewPresent, String sabotage, Long gameTime, boolean extraSensorAvail
    ) 
    {
        this.currentRoomSensor = currentRoom;
        this.energySensor = energy;
        this.cardinalSensor.addAll(neighbors);
        this.crewPresentSensor.addAll(crewPresent);
        this.sabotageSensor = sabotage;
        this.gameTimeSensor = gameTime;
        this.extraSensorAvail = extraSensorAvail;
        this.extraInfoAvail = false;
    }
        
    public ImpostorAgentPerc    //Constructor especial extrasensorial
    (
        String currentRoom, Long energy, List<String> neighbors, 
        List<String> crewPresent, String sabotage, Long gameTime, 
        boolean extraSensorAvail, List<Pair<String,String>> extraSensor
            
    ) 
    {
        this(currentRoom,energy,neighbors,crewPresent,sabotage,gameTime,extraSensorAvail);
        //Colocar cada tripulante en su lugar
        this.extraSensor.addAll(extraSensor);
        this.extraInfoAvail = true;
    }

    public String getCurrentRoomSensor() {
        return currentRoomSensor;
    }

    public List<String> getCardinalSensor() {
        return cardinalSensor;
    }

    public List<String> getCrewPresentSensor() {
        return crewPresentSensor;
    }

    public Long getEnergySensor() {
        return energySensor;
    }

    public String getSabotage() {
        return sabotageSensor;
    }

    public Long getGameTime() {
        return gameTimeSensor;
    }

    public boolean isExtraSensorAvail() {
        return extraSensorAvail;
    }
    
    public boolean isExtraInfoAvail() {
        return extraInfoAvail;
    }

    public List<Pair<String, String>> getExtraSensor() {
        return extraSensor;
    }
    
    
    
    @Override   //Innecesario puesto que el juego crea e inicializa las percepciones.
    public void initPerception(Agent agent, Environment environment) {}

    @Override
    public String toString() 
    {
        StringBuilder text = new StringBuilder("--Nueva percepción--\n");
        text.append("Mi sensor dice que estoy en: ").append(this.currentRoomSensor).append("\n");
        text.append("Mi sensor dice que los adyacentes son: \n");
        for(int i = 0; i < this.cardinalSensor.size(); i++)
        {
            Cardinal cardinal = Cardinal.values()[i];
            text.append(cardinal.name()).append(": ").append(this.cardinalSensor.get(i)).append("\n");
        }
        text.append("Mi sensor dice hay tripulantes: ").append(this.crewPresentSensor.size()).append("\n");
        text.append("Recibí información extra-sensorial?: ").append(this.extraInfoAvail ? "SI\n" : "NO\n");
        text.append("Otra info---\n");
        return text.toString();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    

}
