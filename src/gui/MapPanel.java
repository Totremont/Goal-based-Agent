package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MapPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image backgroundImage;

	public MapPanel() {

		// Cargo la imagen ajustada al tamaño del area de contenido
		backgroundImage = new ImageIcon(GUI.class.getResource("/gui/nuevoFondoAllTasks.png")).getImage();
		setPreferredSize(new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this)));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}

	// Para cambiar la imagen de fondo
	public void setBackgroundImage(String imagePath) {
		backgroundImage = new ImageIcon(GUI.class.getResource(imagePath)).getImage();
		setPreferredSize(new Dimension(backgroundImage.getWidth(this), backgroundImage.getHeight(this)));
		revalidate();
		repaint();
	}

}
