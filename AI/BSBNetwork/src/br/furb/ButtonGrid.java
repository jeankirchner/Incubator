package br.furb;

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Grid para representar as imagens
 * 
 * @author jeank
 * 
 */
public class ButtonGrid {

	private boolean canFire = false;
	private ButtonGridListener listener;

	JPanel panel = new JPanel();
	JButton[][] grid;

	public ButtonGrid(int width, int height, ButtonGridListener list) {
		this.listener = list;

		this.panel.setLayout(new GridLayout(width, height));
		this.grid = new JButton[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				this.grid[x][y] = new JButton();
				this.grid[x][y].setBounds(0, 0, 20, 20);
				this.panel.add(this.grid[x][y]);

				final int i = x;
				final int j = y;

				this.grid[x][y].addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent e) {
						ButtonGrid.this.canFire = false;
					}

					@Override
					public void mousePressed(MouseEvent e) {
						ButtonGrid.this.canFire = true;
						ButtonGrid.this.fireChange(i, j, ButtonGrid.this.grid[i][j]);
					}

					@Override
					public void mouseExited(MouseEvent e) {
					}

					@Override
					public void mouseEntered(MouseEvent e) {
						ButtonGrid.this.fireChange(i, j, ButtonGrid.this.grid[i][j]);
					}

					@Override
					public void mouseClicked(MouseEvent e) {
					}
				});
			}
		}
		this.panel.setBounds(0, 0, width * 30, height * 30);

		this.panel.setVisible(true);
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public JButton[][] getGrid() {
		return this.grid;
	}

	public void fireChange(int x, int y, JButton button) {
		if (this.canFire && this.listener != null) {
			this.listener.cellChanged(x, y, button);
		}
	}

}