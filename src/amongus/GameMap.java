
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

public class GameMap extends Environment 
{
    private GameMapState state = new GameMapState(this);
    
    public static int MAX_ENERGY = 150;
    public static int MIN_ENERGY = 30;
    public static int MAX_PLAYERS = 10;
    public static int MIN_PLAYERS = 7;

    @Override
    public EnvironmentState getEnvironmentState() 
    {
        return this.state;
    }
    
    @Override
    public Perception getPercept() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }    
    
}
