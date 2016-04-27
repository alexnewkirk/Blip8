package com.echodrop.blip8.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import com.echodrop.blip8.core.Sys;
import com.echodrop.blip8.interfaces.IGraphicsObserver;

public class ScreenPanel extends JPanel implements IGraphicsObserver {

	private static final long serialVersionUID = -5699590821022780915L;
	private Sys chip8;
	private boolean[][] screen;
	private int pixelSize = 8;
	private Color foreground = Color.GREEN;
	private Color background = Color.BLACK;

	public ScreenPanel(Sys chip8) {
		this.chip8 = chip8;
		this.screen = new boolean[64][32];
		this.setBackground(background);
		setPreferredSize(new Dimension(64 * pixelSize, 32 * pixelSize));
	}

	@Override
	protected void paintComponent(Graphics g) {
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 32; j++) {
				Color c = null;
				boolean pixel = screen[i][j];
				if(pixel) {
					c = foreground;
				} else {
					c = background;
				}

				g.setColor(c);
				g.fillRect(i * pixelSize, j * pixelSize, pixelSize, pixelSize);
			}
		}
	}

	@Override
	public void updateDisplay() {
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 32; j++) {
				screen[i][j] = chip8.getScreen()[i][j];
			}
		}
		this.repaint();
	}

}
