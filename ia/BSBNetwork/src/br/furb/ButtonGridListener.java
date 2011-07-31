package br.furb;

import javax.swing.JButton;

/**
 * Interface de eventos para as grids
 * 
 * @author jeank
 * 
 */
public interface ButtonGridListener {

	void cellChanged(int x, int y, JButton but);

}
