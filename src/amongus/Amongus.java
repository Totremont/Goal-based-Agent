
package amongus;

import frsf.cidisi.faia.simulator.SearchBasedAgentSimulator;
import gui.GUI;

import java.util.List;

import javax.swing.JFrame;

public class Amongus {
	public static void main(String[] args) {
		// Iniciar el juego con parámetros por defecto
		Game game = new Game();

		SearchBasedAgentSimulator simulator = new SearchBasedAgentSimulator(game, game.getAgent());

		simulator.start();

		System.out.println("--Último estado--");
		System.out.println(game.getEnvironmentState().toString());
		System.out.println(game.getAgent().getAgentState().toString());

		// Podemos obtener el registro acá
		List<GameState> gameHistory = game.getGameHistory();
		gameHistory.add((GameState) game.getEnvironmentState());

		GameGoal gameGoal = new GameGoal((GameState) game.getEnvironmentState());
		Boolean finalResult = !gameGoal.agentFailed(game.getAgent().getAgentState());

		new Amongus().startGraphicSimulation(gameHistory, finalResult);

	}

	public void startGraphicSimulation(List<GameState> gameHistory, Boolean finalResult) {
		GUI frame = new GUI(gameHistory, finalResult);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximizo la ventana
		frame.setVisible(true);
	}
}
