package com.echodrop.blip8.main;

import java.io.IOException;

import javax.swing.JFrame;

import com.echodrop.blip8.core.Sys;
import com.echodrop.blip8.gfx.ScreenPanel;
import com.echodrop.blip8.util.FileUtils;

public class Driver {
	
	public static void main(String[] args) throws IOException {
		
		Sys chip8 = new Sys();
		chip8.loadRom(FileUtils.readBytes("stars.ch8"));
		ScreenPanel sp = new ScreenPanel(chip8);
		chip8.registerObserver(sp);
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame window = new JFrame();
				window.add(sp);
				window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				window.setLocationRelativeTo(null);
				window.setResizable(false);
				window.pack();
				window.setVisible(true);
			}
		});
		
		while(true) {
			chip8.step();
		}
		
	}

}
