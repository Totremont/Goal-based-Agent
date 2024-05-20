package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import amongus.Amongus;
import amongus.GameState;
import amongus.models.CrewMemberState;
import amongus.models.RoomState;
import java.awt.Font;

public class GUI extends JFrame implements KeyListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private MapPanel mapPanel;

	public final HashMap<String, Point> mapPixelsImpostor = new HashMap<>();
	public final HashMap<String, Point> mapPixelsCrewMember = new HashMap<>();

	private JLabel impostorLabel;
	private HashMap<String, JLabel> tripulantesLabels = new HashMap<>();
	private JLabel killAvailableLabel;
	private JLabel sabotageAvailableLabel;
	private JLabel iconExtrasensorialLabel;
	private JLabel textGameTimeLabel;
	private JLabel textGameTimeNumberLabel;

	private List<GameState> environmentStates;
	private Boolean resultadoMeta;

	private Timer timer;
	private int currentStateIndex = 0;

	private HashMap<String, List<Point>> availablePoints;

	private boolean isManualControl = false; // Controla el modo de simulación
	private long lastPressProcessed = 0;

	public GUI(List<GameState> environmentStates, Boolean resultadoMeta) {

		this.environmentStates = environmentStates;
		this.resultadoMeta = resultadoMeta;

		// Ventana principal
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana
		setVisible(true);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		// Agrega el panel del mapa
		mapPanel = new MapPanel();
		mapPanel.setLayout(null);
		contentPane.add(mapPanel, BorderLayout.CENTER);

		setGameMapPixels(this.mapPixelsImpostor); // Seteo posiciones para el impostor

		availablePoints = new HashMap<>();
		setInitialPoints();

		GameState state = environmentStates.get(currentStateIndex);

		// Agrego agente
		ImageIcon iconoImpostor = new ImageIcon(GUI.class.getResource("/gui/impostorKnifeRed.png"));
		Image imagenImpostor = iconoImpostor.getImage().getScaledInstance(70, 70, Image.SCALE_SMOOTH);
		iconoImpostor = new ImageIcon(imagenImpostor);

		impostorLabel = new JLabel(iconoImpostor);
		impostorLabel.setBounds(mapPixelsImpostor.get(state.getAgentRoom().getName()).x,
				mapPixelsImpostor.get(state.getAgentRoom().getName()).y, 70, 70);
		mapPanel.add(impostorLabel);

		// Agrego Crewmates
		for (String key : state.getCrews().keySet()) {
			int id = Integer.parseInt(key.split("#")[1]);
			int imageNumber = (id % 10) + 1; // número de imagen (repetido si es más de 10)
			JLabel tripulante = new JLabel("tripulante" + id);
			tripulante.setName("tripulante" + id);

			String imageName;
			if (state.getCrewStates().get(id).isAlive())
				imageName = "/gui/crewmate";
			else
				imageName = "/gui/deadcrewmate";
			ImageIcon iconoTripulante = new ImageIcon(GUI.class.getResource(imageName + imageNumber + ".png"));
			Image imagenTripulante = iconoTripulante.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			iconoTripulante = new ImageIcon(imagenTripulante);
			tripulante = new JLabel(iconoTripulante);
			tripulante.setBounds(this.getAvailablePoint(state.getCrewStates().get(id).getCurrentRoom().getName()).x,
					this.getAvailablePoint(state.getCrewStates().get(id).getCurrentRoom().getName()).y, 30, 30);
			mapPanel.add(tripulante);
			tripulantesLabels.put(key, tripulante); // Guardo el JLabel en el HashMap
		}
		;

		// Agrego killAvailable;
		String imageNameKill;
		if (state.getCrewStates().stream()
				.anyMatch(x -> x.getCurrentRoom().getName().equals(state.getAgentRoom().getName()) && x.isAlive()))
			imageNameKill = "/gui/kill.png";
		else
			imageNameKill = "/gui/killOff.png";
		ImageIcon iconoKillAvailable = new ImageIcon(GUI.class.getResource(imageNameKill));
		Image imagenKillAvailable = iconoKillAvailable.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		iconoKillAvailable = new ImageIcon(imagenKillAvailable);

		killAvailableLabel = new JLabel(iconoKillAvailable);
		killAvailableLabel.setBounds(1140, 590, 80, 80);
		mapPanel.add(killAvailableLabel);

		// Agrego sabotageAvailable
		String imageNameSabotage;
		if (state.getAgentRoom().getState().isSabotable())
			imageNameSabotage = "/gui/sabotage.png";
		else
			imageNameSabotage = "/gui/sabotageOff.png";
		ImageIcon iconoSabotageAvailable = new ImageIcon(GUI.class.getResource(imageNameSabotage));
		Image imagenSabotageAvailable = iconoSabotageAvailable.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		iconoSabotageAvailable = new ImageIcon(imagenSabotageAvailable);
		sabotageAvailableLabel = new JLabel(iconoSabotageAvailable);
		sabotageAvailableLabel.setBounds(1240, 590, 80, 80);
		mapPanel.add(sabotageAvailableLabel);

		// Agrego extrasensorialAvailable
		String imageNameExtrasensorial;
		if (state.isOmniscientAgent())
			imageNameExtrasensorial = "/gui/extrasensorialAmarillo.png";
		else
			imageNameExtrasensorial = "/gui/extrasensorialOff.png";
		ImageIcon iconExtrasensorial = new ImageIcon(GUI.class.getResource(imageNameExtrasensorial));
		Image imagenExtrasensorial = iconExtrasensorial.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
		iconExtrasensorial = new ImageIcon(imagenExtrasensorial);
		iconExtrasensorialLabel = new JLabel(iconExtrasensorial);
		iconExtrasensorialLabel.setBounds(60, 570, 130, 130);
		mapPanel.add(iconExtrasensorialLabel);

		// Gametime
		textGameTimeLabel = new JLabel("GAME TIME");
		textGameTimeLabel.setFont(new Font("In your face, Joffrey! ", Font.BOLD, 50));
		textGameTimeLabel.setBounds(50, 40, 160, 60);
		textGameTimeLabel.setForeground(Color.BLUE);
		mapPanel.add(textGameTimeLabel);

		textGameTimeNumberLabel = new JLabel(" " + state.getGameTime().toString());
		textGameTimeNumberLabel.setFont(new Font("In your face, Joffrey! ", Font.BOLD, 50));
		textGameTimeNumberLabel.setBounds(96, 100, 70, 60);
		textGameTimeNumberLabel.setForeground(Color.BLUE);
		textGameTimeNumberLabel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2)); // Borde
		mapPanel.add(textGameTimeNumberLabel);

		// Selección del modo de simulación
		int dialogResult = JOptionPane.showConfirmDialog(null, "¿Controlar la simulación manualmente?",
				"Modo de Simulación", JOptionPane.YES_NO_OPTION);
		if (dialogResult == JOptionPane.YES_OPTION) {
			isManualControl = true;
			// Agrego KeyListener al JFrame
			addKeyListener(this);
			setFocusable(true);
			setFocusTraversalKeysEnabled(false);
		} else {
			isManualControl = false;
			// Inicio Timer para simulación automática
			startTimer();
		}

		// Agrego KeyListener al JFrame
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);

	}

	private void restartSimulation(List<GameState> gameHistory, Boolean finalResult) {
		// Limpia ventana actual
		dispose();
		// Reinicio la simulación con una nueva instancia de la GUI
		new Amongus().startGraphicSimulation(gameHistory, finalResult);
	}

	private void startTimer() {
		timer = new Timer(1500, e -> {
			currentStateIndex++; // Avanzo al siguiente estado
			if (currentStateIndex < environmentStates.size()) {
				updateState(currentStateIndex); // Actualizo la interfaz con el nuevo estado
			} else if (currentStateIndex == environmentStates.size()) { // Estado Final Resultado
				showResult(resultadoMeta);
			} else {
				timer.stop(); // Final de la lista de estados
			}
		});

		if (!isManualControl) {
			timer.start();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (System.currentTimeMillis() - lastPressProcessed > 100) {
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_RIGHT) {
				// Flecha derecha: avanzo al siguiente estado
				currentStateIndex++;
				if (currentStateIndex < environmentStates.size()) {
					updateState(currentStateIndex);
				} else if (currentStateIndex == environmentStates.size()) {
					// Muestro resultado final
					showResult(resultadoMeta);
				}
			} else if (key == KeyEvent.VK_LEFT) {
				// Flecha izquierda: retrocedo al estado anterior
				currentStateIndex--;
				if (currentStateIndex >= 0) {
					updateState(currentStateIndex);
				}
			}

			lastPressProcessed = System.currentTimeMillis();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	private void updateState(int stateIndex) {

		GameState state = environmentStates.get(stateIndex);
		GameState stateAnterior = environmentStates.get(stateIndex);
		if (stateIndex > 0)
			stateAnterior = environmentStates.get(stateIndex - 1);

		// Actualizo GameTime
		textGameTimeNumberLabel.setText(" " + state.getGameTime().toString());

		// Actualizo el impostor
		updateImpostor(state.getAgentRoom().getName());

		// Actualizo los tripulantes
		for (int i = 0; i < state.getCrewStates().size(); i++) {
			if (!state.getCrewStates().get(i).isAlive()) { // Esta muerto
				if (stateAnterior.getCrewStates().get(i).isAlive()) // Ya estaba muerto?
					killCrewMember(state.getCrewStates().get(i)); // Lo mato
			} else { // Esta vivo, veo si cambio de posicion
				if (!state.getCrewStates().get(i).getCurrentRoom().getName()
						.equals(stateAnterior.getCrewStates().get(i).getCurrentRoom().getName())) {
					updateCrewMember(state.getCrewStates().get(i));
				}
			}
		}

		// Actualizo los íconos de kill y sabotage
		updateKillIcon(state);
		updateSabotageIcon(state);
		// Actualizo borde rojo de Room saboteable
		updateTaskIcon(state);
		// Actualizo icono extrasensorial
		updateExtrasensorialIcon(state);

		mapPanel.revalidate();
		mapPanel.repaint();
	}

	private void killCrewMember(CrewMemberState crewState) {
		JLabel crewMemberLabel = tripulantesLabels.get(crewState.getCrew().getName());

		// Verifico si el tripulante está muerto
		int id = Integer.parseInt(crewState.getCrew().getName().split("#")[1]);
		int imageNumber = (id % 10) + 1; // Obtengo el número de imagen (repetido si es más de 10)
		GameState state = environmentStates.get(currentStateIndex); // Obtengo el estado actual
		boolean isDead = !state.getCrewStates().get(Integer.parseInt(crewState.getCrew().getName().split("#")[1]))
				.isAlive();
		String imageName;
		if (isDead)
			imageName = "/gui/deadcrewmate";
		else
			imageName = "/gui/crewmate";

		ImageIcon iconoTripulante = new ImageIcon(GUI.class.getResource(imageName + imageNumber + ".png"));
		Image imagenTripulante = iconoTripulante.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
		iconoTripulante = new ImageIcon(imagenTripulante);
		crewMemberLabel.setIcon(iconoTripulante);

	}

//Borde amarillo al obtener Extrasensorial
	private void exposeCrews(GameState state, Boolean mostrar) {

		for (int i = 0; i < state.getCrewStates().size(); i++) {
			boolean isAlive = state.getCrewStates()
					.get(Integer.parseInt(state.getCrewStates().get(i).getCrew().getName().split("#")[1])).isAlive();
			if (isAlive) {
				JLabel crewMemberLabel = tripulantesLabels.get(state.getCrewStates().get(i).getCrew().getName());
				int id = Integer.parseInt(state.getCrewStates().get(i).getCrew().getName().split("#")[1]);
				int imageNumber = (id % 10) + 1; // Obtengo el número de imagen (repetido si es más de 10)
				String finalNombre;
				if (mostrar)
					finalNombre = "aura.png";
				else
					finalNombre = ".png";
				ImageIcon iconoTripulante = new ImageIcon(
						GUI.class.getResource("/gui/crewmate" + imageNumber + finalNombre));
				Image imagenTripulante = iconoTripulante.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
				iconoTripulante = new ImageIcon(imagenTripulante);
				crewMemberLabel.setIcon(iconoTripulante);
			}

		}
	}

	private void updateCrewMember(CrewMemberState crewState) {

		// Rango de posiciones
		Point newPositionRango = getAvailablePoint(crewState.getCurrentRoom().getName());
		JLabel crewMemberLabel = tripulantesLabels.get(crewState.getCrew().getName());
		crewMemberLabel.setLocation(newPositionRango);
	}

	private void updateImpostor(String roomName) {
		Point newPosition = mapPixelsImpostor.get(roomName);
		impostorLabel.setLocation(newPosition);

	}

	private void updateKillIcon(GameState state) {

		String imageNameKill;
		if (state.getCrewStates().stream()
				.anyMatch(x -> x.getCurrentRoom().getName().equals(state.getAgentRoom().getName()) && x.isAlive()))
			imageNameKill = "/gui/kill.png";
		else
			imageNameKill = "/gui/killOff.png";
		ImageIcon iconoKillAvailable = new ImageIcon(GUI.class.getResource(imageNameKill));
		Image imagenKillAvailable = iconoKillAvailable.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		iconoKillAvailable = new ImageIcon(imagenKillAvailable);
		killAvailableLabel.setIcon(iconoKillAvailable);

	}

	private void updateSabotageIcon(GameState state) {
		String imageNameSabotage;
		RoomState agentRoomState = state.getRoomStates().stream()
				.filter(x -> x.getRoom().getName().equals(state.getAgentRoom().getName())).findFirst().get();
		if (agentRoomState.isSabotable())
			imageNameSabotage = "/gui/sabotage.png";
		else
			imageNameSabotage = "/gui/sabotageOff.png";
		ImageIcon iconoSabotageAvailable = new ImageIcon(GUI.class.getResource(imageNameSabotage));
		Image imagenSabotageAvailable = iconoSabotageAvailable.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
		iconoSabotageAvailable = new ImageIcon(imagenSabotageAvailable);
		sabotageAvailableLabel.setIcon(iconoSabotageAvailable);

	}

	private void updateExtrasensorialIcon(GameState state) {

		String imageNameExtrasensorial;
		if (state.isOmniscientAgent()) {
			imageNameExtrasensorial = "/gui/extrasensorialAmarillo.png";
			exposeCrews(state, true);
		} else {
			imageNameExtrasensorial = "/gui/extrasensorialOff.png";
			exposeCrews(state, false);
		}
		ImageIcon iconExtrasensorial = new ImageIcon(GUI.class.getResource(imageNameExtrasensorial));
		Image imagenExtrasensorial = iconExtrasensorial.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
		iconExtrasensorial = new ImageIcon(imagenExtrasensorial);
		iconExtrasensorialLabel.setIcon(iconExtrasensorial);
	}

	private void updateTaskIcon(GameState state) {
		//
		List<String> listaRoomSabotages = state.getRoomStates().stream().filter(x -> x.isSabotable())
				.map(x -> x.getRoom().getName()).toList();

		if (listaRoomSabotages.size() == 2) {
			if (!listaRoomSabotages.contains("Armas")) {
				mapPanel.setBackgroundImage("/gui/nuevoFondoArmasTask.png");
			}
			if (!listaRoomSabotages.contains("Electricidad")) {
				mapPanel.setBackgroundImage("/gui/nuevoFondoElectricidadTask.png");
			}
			if (!listaRoomSabotages.contains("Reactor")) {
				mapPanel.setBackgroundImage("/gui/nuevoFondoReactorTask.png");
			}

		} else if (listaRoomSabotages.size() == 1) {

			if (listaRoomSabotages.contains("Armas")) {

				mapPanel.setBackgroundImage("/gui/nuevoFondoArmasTaskLeft.png");
			}
			if (listaRoomSabotages.contains("Electricidad")) {
				mapPanel.setBackgroundImage("/gui/nuevoFondoElectricidadTaskLeft.png");
			}
			if (listaRoomSabotages.contains("Reactor")) {
				mapPanel.setBackgroundImage("/gui/nuevoFondoReactorTaskLeft.png");
			}
		} else if (listaRoomSabotages.size() == 0) {
			mapPanel.setBackgroundImage("/gui/nuevaImagenFondo.png");
		}

	}

	private void showResult(Boolean agentWin) {

		GameState state = environmentStates.getLast();
		Color textColor;
		if (agentWin) {
			mapPanel.setBackgroundImage("/gui/victoryTextoOtro.png"); // VICTORY
			textColor = Color.BLUE;
		} else {
			mapPanel.setBackgroundImage("/gui/defeatTextoOtro.png"); // DEFEAT
			textColor = Color.RED;
		}

		impostorLabel.setVisible(false);
		for (String key : environmentStates.getLast().getCrews().keySet()) {
			tripulantesLabels.get(key).setVisible(false);
		}
		killAvailableLabel.setVisible(false);
		sabotageAvailableLabel.setVisible(false);
		iconExtrasensorialLabel.setVisible(false);
		textGameTimeLabel.setVisible(false);
		textGameTimeNumberLabel.setVisible(false);

		int cantidadSabotages = state.getSabotages().size();
		int cantidadCrewmembers = state.getCrews().size();
		Long cantidadSabotagesNow = state.getRoomStates().stream().filter(x -> x.isSabotable()).count();
		Long cantidadCrewmembersNow = state.getCrewStates().stream().filter(x -> x.isAlive()).count();

		JLabel textSabotagesGoalLabel = new JLabel(
				"  " + (cantidadSabotages - cantidadSabotagesNow) + "/" + cantidadSabotages + " sabotages completed.");
		textSabotagesGoalLabel.setFont(new Font("VCR OSD Mono", Font.BOLD, 20));
		textSabotagesGoalLabel.setBounds(510, 430, 330, 32);
		textSabotagesGoalLabel.setOpaque(true);
		textSabotagesGoalLabel.setBackground(Color.BLACK);
		textSabotagesGoalLabel.setForeground(textColor);
		mapPanel.add(textSabotagesGoalLabel);

		JLabel textCrewmembersGoalLabel = new JLabel("  " + (cantidadCrewmembers - cantidadCrewmembersNow) + "/"
				+ cantidadCrewmembers + " crewmembers killed.");
		textCrewmembersGoalLabel.setFont(new Font("VCR OSD Mono", Font.BOLD, 20));
		textCrewmembersGoalLabel.setBounds(510, 480, 330, 32);
		textCrewmembersGoalLabel.setOpaque(true);
		textCrewmembersGoalLabel.setBackground(Color.BLACK);
		textCrewmembersGoalLabel.setForeground(textColor);
		mapPanel.add(textCrewmembersGoalLabel);

		// Botón para reiniciar la simulación
		JButton restartButton = new JButton("REINICIAR SIMULACION");
		restartButton.setFont(new Font("VCR OSD Mono", Font.BOLD, 14));
		restartButton.setBackground(Color.decode("0x8dffff"));
		restartButton.setForeground(Color.BLACK);
		restartButton.setFocusPainted(false);
		restartButton.addActionListener(e -> restartSimulation(this.environmentStates, this.resultadoMeta));
		restartButton.setBounds(575, 580, 200, 50);
		mapPanel.add(restartButton);

		mapPanel.revalidate();
		mapPanel.repaint();
	}

	private void setInitialPoints() {
		// Habitaciones, 3 posiciones para cada una
		availablePoints.put("Reactor", Arrays.asList(new Point(290, 295), new Point(270, 262), new Point(270, 324)));
		availablePoints.put("Motor inferior",
				Arrays.asList(new Point(377, 423), new Point(395, 452), new Point(373, 482)));
		availablePoints.put("Electricidad",
				Arrays.asList(new Point(574, 384), new Point(594, 417), new Point(568, 448)));
		availablePoints.put("Depósito", Arrays.asList(new Point(724, 534), new Point(762, 506), new Point(747, 449)));
		availablePoints.put("Comunicación",
				Arrays.asList(new Point(881, 525), new Point(862, 559), new Point(898, 558)));
		availablePoints.put("Escudos", Arrays.asList(new Point(982, 501), new Point(1008, 475), new Point(994, 446)));
		availablePoints.put("Navegación",
				Arrays.asList(new Point(1174, 262), new Point(1196, 291), new Point(1171, 312)));
		availablePoints.put("Oxígeno", Arrays.asList(new Point(912, 277), new Point(938, 259), new Point(920, 244)));
		availablePoints.put("Armas", Arrays.asList(new Point(1009, 143), new Point(981, 108), new Point(987, 172)));
		availablePoints.put("Cafetería", Arrays.asList(new Point(755, 111), new Point(799, 157), new Point(748, 204)));
		availablePoints.put("Administración",
				Arrays.asList(new Point(881, 351), new Point(912, 379), new Point(854, 409)));
		availablePoints.put("Hospital", Arrays.asList(new Point(549, 294), new Point(600, 287), new Point(573, 261)));
		availablePoints.put("Motor superior",
				Arrays.asList(new Point(371, 195), new Point(393, 163), new Point(383, 125)));
		availablePoints.put("Seguridad", Arrays.asList(new Point(438, 332), new Point(462, 309), new Point(454, 274)));

		// Pasillos, 3 posiciones para cada uno
		availablePoints.put("Pasillo oeste centro",
				Arrays.asList(new Point(346, 327), new Point(362, 307), new Point(350, 278)));
		availablePoints.put("Pasillo oeste superior",
				Arrays.asList(new Point(503, 139), new Point(544, 160), new Point(564, 143)));
		availablePoints.put("Pasillo oeste inferior",
				Arrays.asList(new Point(468, 487), new Point(520, 527), new Point(489, 527)));
		availablePoints.put("Pasillo central",
				Arrays.asList(new Point(737, 315), new Point(760, 341), new Point(740, 362)));
		availablePoints.put("Pasillo este centro",
				Arrays.asList(new Point(1033, 259), new Point(1051, 329), new Point(1009, 325)));
		availablePoints.put("Pasillo este inferior",
				Arrays.asList(new Point(893, 476), new Point(844, 461), new Point(911, 461)));
	}

	public Point getAvailablePoint(String roomName) {
		List<Point> points = availablePoints.get(roomName);
		if (points == null || points.isEmpty()) {
			// Si no quedan puntos disponibles, reinicio la lista
			setInitialPoints();
			points = availablePoints.get(roomName);
		}
		// Elijo punto aleatorio de los disponibles
		Random rand = new Random();
		int index = rand.nextInt(points.size());
		Point selectedPoint = points.get(index);
		// Elimino el punto seleccionado de la lista disponibles
		availablePoints.put(roomName,
				points.stream().filter(p -> !p.equals(selectedPoint)).collect(Collectors.toList()));
		return selectedPoint;
	}

	private static void setGameMapPixels(HashMap<String, Point> mapPixels) {

		int labelSizeOriginal = 30;
		int labelSizeNuevo = 70;
		// Por cambio de resolucion
		int ajuste = (labelSizeNuevo - labelSizeOriginal) / 2; // Ajuste para centrar la imagen
		int ajusteYAdicional = 15; // Ajuste extra en la coordenada y

		// Habitaciones para impostor
		mapPixels.put("Motor superior", new Point(363 - ajuste, 163 - ajuste - ajusteYAdicional));
		mapPixels.put("Motor inferior", new Point(353 - ajuste, 452 - ajuste - ajusteYAdicional));
		mapPixels.put("Hospital", new Point(534 - ajuste, 265 - ajuste - ajusteYAdicional));
		mapPixels.put("Cafetería", new Point(685 - ajuste, 159 - ajuste - ajusteYAdicional));
		mapPixels.put("Armas", new Point(950 - ajuste, 138 - ajuste - ajusteYAdicional));
		mapPixels.put("Navegación", new Point(1140 - ajuste, 291 - ajuste - ajusteYAdicional));
		mapPixels.put("Oxígeno", new Point(892 - ajuste, 259 - ajuste - ajusteYAdicional));
		mapPixels.put("Escudos", new Point(960 - ajuste, 470 - ajuste - ajusteYAdicional));
		mapPixels.put("Comunicación", new Point(836 - ajuste, 540 - ajuste - ajusteYAdicional));
		mapPixels.put("Depósito", new Point(692 - ajuste, 506 - ajuste - ajusteYAdicional));
		mapPixels.put("Electricidad", new Point(538 - ajuste, 448 - ajuste - ajusteYAdicional));
		mapPixels.put("Administración", new Point(824 - ajuste, 353 - ajuste - ajusteYAdicional));
		mapPixels.put("Reactor", new Point(250 - ajuste, 295 - ajuste - ajusteYAdicional));
		mapPixels.put("Seguridad", new Point(430 - ajuste, 300 - ajuste - ajusteYAdicional));

		// Pasillos para impostor
		mapPixels.put("Pasillo oeste superior", new Point(525 - ajuste, 152 - ajuste - ajusteYAdicional));
		mapPixels.put("Pasillo oeste centro", new Point(345 - ajuste, 302 - ajuste - ajusteYAdicional));
		mapPixels.put("Pasillo oeste inferior", new Point(465 - ajuste, 524 - ajuste - ajusteYAdicional));
		mapPixels.put("Pasillo central", new Point(737 - ajuste, 341 - ajuste - ajusteYAdicional));
		mapPixels.put("Pasillo este centro", new Point(1041 - ajuste, 290 - ajuste - ajusteYAdicional));
		mapPixels.put("Pasillo este inferior", new Point(865 - ajuste, 472 - ajuste - ajusteYAdicional));

	}
}
