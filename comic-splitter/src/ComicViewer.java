import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;

import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Gerenciador de tela para navegação e carregamento de imagens.
 * Classe MAIN.
 * 
 * @copyright (C) 2010 Jean Kirchner - TODOS OS DIREITOS RESERVADOS.
 * @local Blumenau, 02 de dezembro de 2010 
 * @author Jean Kirchner
 * @blog jeankirchner.blogspot.com
 * @email jean.kirchner@gmail.com
 */ 
public class ComicViewer extends JApplet {

	MediaTracker		tracker	= null;
	PixelGrabber		grabber	= null;

	// slider constraints
	static final int	TH_MIN	= 0;
	static final int	TH_MAX	= 255;
	static final int	TH_INIT	= 60;

	JPanel				imagePanel;
	private JLabel		origLabel;
	private JLabel		outputLabel;
	private Image		image;

	@Override
	public void init() {
		setSize(1000, 800);
		mountGUI();
	}

	private void mountGUI() {
		Container cont1 = getContentPane();
		cont1.removeAll();
		cont1.setBackground(Color.black);
		cont1.setLayout(new BorderLayout());
		Container cont = cont1;

		imagePanel = new JPanel();
		imagePanel.setLayout(new GridLayout(1, 2));
		imagePanel.setBackground(new Color(192, 204, 226));

		origLabel = new JLabel("", JLabel.CENTER);
		origLabel.setVerticalTextPosition(JLabel.BOTTOM);
		origLabel.setHorizontalTextPosition(JLabel.CENTER);
		origLabel.setForeground(Color.blue);
		imagePanel.add(origLabel);

		outputLabel = new JLabel("", JLabel.CENTER);
		outputLabel.setVerticalTextPosition(JLabel.BOTTOM);
		outputLabel.setHorizontalTextPosition(JLabel.CENTER);
		outputLabel.setForeground(Color.blue);
		imagePanel.add(outputLabel);

		cont.add(imagePanel, BorderLayout.CENTER);

		JPanel head = new JPanel();
		JButton btLeft = new JButton("<-");
		JButton btRight = new JButton("->");
		head.add(btLeft);
		head.add(btRight);

		cont.add(head, BorderLayout.NORTH);

		btLeft.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (ResourcesNames.hasPrev()) {
					image = getImageAndWait(ResourcesNames.prev());
					processImage();
				}
			}
		});

		btRight.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (ResourcesNames.hasNext()) {
					image = getImageAndWait(ResourcesNames.next());
					processImage();
				}
			}
		});

	}

	private void processImage() {

		origLabel.setIcon(new ImageIcon(image));

		new Thread() {
			@Override
			public void run() {
				IImage img = ComicDetection.detectComics(getIImageFromImage(image));

				final Image output = createImage(new MemoryImageSource(img.getWidth(), img.getHeight(), img.getData(), 0, img.getWidth()));

				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						outputLabel.setIcon(new ImageIcon(output));
					}

				});
			}

		}.start();
	}

	Image getImageAndWait(String name) {
		tracker = new MediaTracker(this);
		Image image = getImage(this.getCodeBase(), name);

		tracker.addImage(image, 0);
		try {
			tracker.waitForAll();
		} catch (InterruptedException e) {
			System.out.println("error: " + e);
		}

		int width = image.getWidth(this);
		int height = image.getHeight(this);

		image = image.getScaledInstance((int) (width / 1.7f), (int) (height / 1.7f), Image.SCALE_SMOOTH);

		return image;
	}

	IImage getIImageFromImage(Image img) {
		int width = img.getWidth(this);
		int height = img.getHeight(this);
		final int[] imageBytes = new int[width * height];

		PixelGrabber grabber = new PixelGrabber(image, 0, 0, width, height, imageBytes, 0, width);
		try {
			grabber.grabPixels();
		} catch (InterruptedException e2) {
			System.out.println("error: " + e2);
		}

		return new ImageData(width, height, imageBytes);
	}

}