
package amongus;

import frsf.cidisi.faia.simulator.SearchBasedAgentSimulator;
import java.util.List;

public class Amongus 
{
    public static void main(String[] args) 
    {
        //Iniciar el juego con parámetros por defecto
        Game game = new Game();
        
        SearchBasedAgentSimulator simulator = new SearchBasedAgentSimulator(game,game.getAgent());
        
        simulator.start();
        
        System.out.println("--Último estado--");
        System.out.println(game.getEnvironmentState().toString());
        System.out.println(game.getAgent().getAgentState().toString());
        
        //Podemos obtener el registro acá
        List<GameState> gameHistory = game.getGameHistory();
        
        
    }
    
}
