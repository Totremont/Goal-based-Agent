
package amongus;

import frsf.cidisi.faia.agent.Agent;
import frsf.cidisi.faia.agent.Perception;
import frsf.cidisi.faia.environment.Environment;
import java.util.ArrayList;
import java.util.List;

//Como ve el mundo nuestro agente
public class ImpostorAgentPerc extends Perception 
{
    private String currentRoomSensor;
    private List<String> cardinalSensor = new ArrayList<>(4);    //[O,N,E,S]
    private List<String> crewPresentSensor = new ArrayList<>();
    private Long energySensor;
    private String sabotageSensor;
    private Long gameTimeSensor;
    private boolean extraSensorAvail;
    
    

    //Innecesario puesto que el juego crea e inicializa las percepciones.
    @Override
    public void initPerception(Agent agent, Environment environment) 
    {

    }

    public ImpostorAgentPerc(
            String currentRoom, Long energy, List<String> neighbors, 
            List<String> crewPresent, String sabotage, Long gameTime, boolean extraSensorAvail) 
    {
        this.currentRoomSensor = currentRoom;
        this.energySensor = energy;
        this.cardinalSensor.addAll(neighbors);
        this.crewPresentSensor.addAll(crewPresent);
        this.sabotageSensor = sabotage;
        this.gameTimeSensor = gameTime;
        this.extraSensorAvail = extraSensorAvail;
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
    
    
    
    
    
    
    
    
    
    
    

}
