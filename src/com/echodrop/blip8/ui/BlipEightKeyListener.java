package com.echodrop.blip8.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.echodrop.blip8.core.Sys;

public class BlipEightKeyListener implements KeyListener {
	
	private Sys chip8;
	
	public BlipEightKeyListener(Sys chip8) {
		this.chip8 = chip8;
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		byte key = getKeyValue(arg0);
		if(key > -1) {
			chip8.keyDown(key);
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			chip8.init();
		}
		
		byte key = getKeyValue(arg0);
		if(key > -1) {
			chip8.keyUp(key);
		}
	}
	
	private byte getKeyValue(KeyEvent k) {
		byte key = -1;
		
		switch(k.getKeyCode()) {
		case KeyEvent.VK_1:
			key = (byte)0xA;
			break;
		case KeyEvent.VK_2:
			key = (byte)0xB;
			break;
		case KeyEvent.VK_3:
			key = (byte)0xC;
			break;
		case KeyEvent.VK_4:
			key = (byte)0xD;
			break;
		case KeyEvent.VK_Q:
			key = (byte)1;
			break;
		case KeyEvent.VK_W:
			key = (byte)2;
			break;
		case KeyEvent.VK_E:
			key = (byte)3;
			break;
		case KeyEvent.VK_R:
			key = (byte)0xE;
			break;
		case KeyEvent.VK_A:
			key = (byte)4;
			break;
		case KeyEvent.VK_S:
			key = (byte)5;
			break;
		case KeyEvent.VK_D:
			key = (byte)6;
			break;
		case KeyEvent.VK_F:
			key = (byte)0xF;
			break;
		case KeyEvent.VK_Z:
			key = (byte)7;
			break;
		case KeyEvent.VK_X:
			key = (byte)8;
			break;
		case KeyEvent.VK_C:
			key = (byte)9;
			break;
		case KeyEvent.VK_V:
			key = (byte)0;
			break;
		}
		return key;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// Implementing KeyListener
	}

}
