
package amongus;

import frsf.cidisi.faia.simulator.SearchBasedAgentSimulator;

public class Amongus 
{
    public static void main(String[] args) 
    {
        //Iniciar el juego con parámetros por defecto
        Game game = new Game();
        
        SearchBasedAgentSimulator simulator = new SearchBasedAgentSimulator(game,game.getAgent());
        
        simulator.start();
        
    }
    
}
