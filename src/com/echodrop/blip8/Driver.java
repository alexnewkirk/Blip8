package com.echodrop.blip8;

import java.io.IOException;

import javax.swing.JFrame;

import com.echodrop.blip8.core.Sys;
import com.echodrop.blip8.ui.BlipEightKeyListener;
import com.echodrop.blip8.ui.ScreenPanel;
import com.echodrop.blip8.util.FileUtils;

public class Driver {
	
	public static void main(String[] args) throws IOException {
		
		if(args.length != 1) {
			throw new IllegalArgumentException("You must specify a ROM filename");
		}
		
		Sys chip8 = new Sys();
		chip8.loadRom(FileUtils.readBytes(args[0]));
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
				window.addKeyListener(new BlipEightKeyListener(chip8));
				window.setVisible(true);
			}
		});
		
		chip8.beginDispatch();
	}

}
