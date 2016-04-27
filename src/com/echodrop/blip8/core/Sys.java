package com.echodrop.blip8.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import com.echodrop.blip8.gfx.ScreenPanel;
import com.echodrop.blip8.interfaces.IGraphicsObserver;
import com.echodrop.blip8.util.NumberUtils;
import com.echodrop.blip8.util.StringUtils;

public class Sys {

	private byte[] mem;
	private byte[] reg;
	private boolean[] keys;
	private char addressReg;
	private char pc;
	private Stack<Character> stack;
	private boolean[][] screen;
	private List<IGraphicsObserver> observers;
	private Random gen;
	private boolean running;
	private int delayTimer;
	private int soundTimer;
	private Timer frameLimiter;

	public Sys() {
		init();
	}

	public void init() {
		this.mem = new byte[0xFFF];
		this.reg = new byte[16];
		this.stack = new Stack<>();
		this.screen = new boolean[64][32];
		this.keys = new boolean[0xF];
		this.pc = 0x200;
		this.observers = new ArrayList<>();
		this.gen = new Random();
		this.running = true;
		this.delayTimer = 0;
		this.soundTimer = 0;
		this.frameLimiter = new Timer();
	}

	public char fetch() {
		char b1 = (char) (mem[pc] & 0xFF);
		b1 <<= 8;
		pc++;
		char b2 = (char) (mem[pc] & 0xFF);
		pc++;
		return (char) (b1 | b2);
	}

	public boolean[][] getScreen() {
		return this.screen;
	}

	public void registerObserver(ScreenPanel sp) {
		observers.add(sp);
	}

	public void notifyAllObservers() {
		for (IGraphicsObserver igo : observers) {
			igo.updateDisplay();
		}
	}

	public void loadRom(byte[] rom) {
		for (int i = 0; i < rom.length; i++) {
			mem[0x200 + i] = rom[i];
		}
	}

	public void step() {
		char opcode = fetch();

		System.out.println("PC: " + "0x" + Integer.toHexString(pc - 0x200));
		System.out.println("0x" + Integer.toHexString(opcode & 0xFFFF));
		System.out.println();

		switch (opcode & 0xF000) {
		case 0:
			switch (opcode & 0x000F) {
			case 0:
				clrscrn();
				break;
			case 0xE:
				ret();
				break;
			}
			break;

		case 0x1000:
			jump(opcode);
			break;
		case 0x2000:
			call(opcode);
			break;
		case 0x3000:
			op3xnn(opcode);
			break;
		case 0x4000:
			op4xnn(opcode);
			break;
		case 0x5000:
			op5xy0(opcode);
			break;
		case 0x6000:
			op6xnn(opcode);
			break;
		case 0x7000:
			op7xnn(opcode);
			break;
		case 0x8000:
			switch (opcode & 0xF) {
			case 0:
				op8xy0(opcode);
				break;
			case 1:
				op8xy1(opcode);
				break;
			case 2:
				op8xy2(opcode);
				break;
			case 3:
				op8xy3(opcode);
				break;
			case 4:
				op8xy4(opcode);
				break;
			case 5:
				op8xy5(opcode);
				break;
			case 6:
				op8xy6(opcode);
				break;
			case 7:
				op8xy7(opcode);
				break;
			case 0xE:
				op8xye(opcode);
				break;
			}
			break;
		case 0x9000:
			op9xy0(opcode);
			break;
		case 0xA000:
			storeAddress(opcode);
			break;
		case 0xB000:
			opbnnn(opcode);
			break;
		case 0xC000:
			opcxnnn(opcode);
			break;
		case 0xD000:
			opdxyn(opcode);
			break;
		case 0xE000:
			switch (opcode & 0xFF) {
			case 0x9E:
				opex9e(opcode);
				break;
			case 0xA1:
				opexa1(opcode);
				break;
			}
			break;
		case 0xF000:
			switch (opcode & 0xFF) {
			case 0x07:
				opfx07(opcode);
				break;
			case 0x0A:
				opfx0a(opcode);
				break;
			case 0x15:
				opfx15(opcode);
				break;
			case 0x18:
				opfx18(opcode);
				break;
			case 0x1E:
				opfx1e(opcode);
				break;
			case 0x29:
				opfx29(opcode);
				break;
			case 0x33:
				opfx33(opcode);
				break;
			case 0x55:
				opfx55(opcode);
				break;
			case 0x65:
				opfx65(opcode);
				break;
			}
			break;
		}
		
		if(delayTimer > 0) {
			delayTimer--;
		}
		
		if(soundTimer > 0) {
			soundTimer--;
		}
	}

	private void opfx65(char opcode) {
		int topReg = (opcode & 0xF00) >> 8;
		for(int i = 0; i <= topReg; i++) {
			reg[i] = mem[addressReg + i];
		}
	}

	private void opfx55(char opcode) {
		int topReg = (opcode & 0xF00) >> 8;
		for(int i = 0; i <= topReg; i++) {
			mem[addressReg + i] = reg[i];
		}
	}

	private void opfx33(char opcode) {
		throw new RuntimeException("Opcode not yet implemented: 0x" + Integer.toHexString(opcode & 0xFFFF));
	}

	private void opfx29(char opcode) {
		throw new RuntimeException("Opcode not yet implemented: 0x" + Integer.toHexString(opcode & 0xFFFF));
	}

	private void opfx1e(char opcode) {
		byte r = (byte)((opcode & 0xF00) >> 8);
		addressReg += reg[r];
	}

	private void opfx18(char opcode) {
		soundTimer = reg[(opcode & 0xF00) >> 8];
	}

	private void opfx15(char opcode) {
		delayTimer = reg[(opcode & 0xF00) >> 8];
	}

	private void opfx0a(char opcode) {
		throw new RuntimeException("Opcode not yet implemented: 0x" + Integer.toHexString(opcode & 0xFFFF));
	}

	private void opfx07(char opcode) {
		reg[(opcode & 0xF00) >> 8] = (byte)delayTimer;
	}

	private void opexa1(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		boolean keyPressed = keys[r1];
		if(!keyPressed) {
			pc += 2;
		}
	}

	private void opex9e(char opcode) {
		throw new RuntimeException("Opcode not yet implemented: 0x" + Integer.toHexString(opcode & 0xFFFF));
	}

	private void opdxyn(char opcode) {
		byte rows = (byte) (opcode & 0xF);
		int x = reg[(opcode & 0xF00) >> 8];
		int y = reg[(opcode & 0xF0) >> 4];

		for (int i = 0; i < rows; i++) {
			byte rowData = mem[addressReg + i];
			String bin = StringUtils.zeroLeftPad(Integer.toBinaryString(rowData & 0xFF), 8);
			for(int j = 0; j < 8; j++) {
				int pixelX = (x + j) % 64;
				int pixelY = (y + i) % 32;
				
				boolean pixelToggle = bin.charAt(j) == '1';
				
				if(screen[pixelX][pixelY] && pixelToggle) {
					screen[pixelX][pixelY] = false;
					reg[0xF] = 1;
				} else if(pixelToggle) {
					screen[pixelX][pixelY] = true;
				}
			}
		}
		notifyAllObservers();
	}

	private void opcxnnn(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int val = (opcode & 0xFF);
		reg[r1] = (byte) (val & gen.nextInt(255));
	}

	private void opbnnn(char opcode) {
		char address = (char) (reg[0x0] + (opcode & 0xFFF));
		pc = address;
	}

	private void storeAddress(char opcode) {
		addressReg = (char) (opcode & 0xFFF);
	}

	private void op9xy0(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		if (reg[r1] != reg[r2]) {
			pc += 2;
		}
	}

	private void op8xye(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		reg[0xF] = (byte) (NumberUtils.readBit(0, reg[r1]) ? 1 : 0);
		reg[r1] <<= 1;
	}

	private void op8xy7(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		reg[0xF] = (byte) (NumberUtils.byteSubtractionBorrow(reg[r2], reg[r1]) ? 0 : 1);
		reg[r1] = (byte) (reg[r2] - reg[r1]);
	}

	private void op8xy6(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		reg[0xF] = (byte) (NumberUtils.readBit(7, reg[r1]) ? 1 : 0);
		reg[r1] >>= 1;
	}

	private void op8xy5(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		reg[0xF] = (byte) (NumberUtils.byteSubtractionBorrow(reg[r1], reg[r2]) ? 0 : 1);
		reg[r1] = (byte) (reg[r1] - reg[r2]);
	}

	private void op8xy4(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		reg[0xF] = (byte) (NumberUtils.byteAdditionOverflow(reg[r1], reg[r2]) ? 1 : 0);
		reg[r1] = (byte) (reg[r1] + reg[r2]);
	}

	private void op8xy3(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		reg[r1] = (byte) (reg[r1] ^ reg[r2]);
	}

	private void op8xy2(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		reg[r1] = (byte) (reg[r1] & reg[r2]);
	}

	private void op8xy1(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		reg[r1] = (byte) (reg[r1] | reg[r2]);
	}

	private void op8xy0(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		reg[r1] = reg[r2];
	}

	private void op4xnn(char opcode) {
		int r = ((opcode & 0xF00) >> 8);
		byte val = (byte) (opcode & 0xFF);
		if (reg[r] != val) {
			pc += 2;
		}
	}

	private void op7xnn(char opcode) {
		int r = ((opcode & 0xF00) >> 8);
		byte val = (byte) (opcode & 0xFF);
		reg[r] += val;
	}

	private void op6xnn(char opcode) {
		int r = ((opcode & 0xF00) >> 8);
		reg[r] = (byte) (opcode & 0xFF);
	}

	private void op5xy0(char opcode) {
		int r1 = ((opcode & 0xF00) >> 8);
		int r2 = ((opcode & 0xF0) >> 4);
		if (reg[r1] == reg[r2]) {
			pc += 2;
		}
	}

	private void op3xnn(char opcode) {
		int r = ((opcode & 0xF00) >> 8);
		byte val = (byte) (opcode & 0xFF);
		if (reg[r] == val) {
			pc += 2;
		}
	}

	private void call(char opcode) {
		stack.push(pc);
		char address = (char) (opcode & 0xFFF);
		pc = address;
	}

	private void jump(char opcode) {
		char address = (char) (opcode & 0xFFF);
		pc = address;
	}

	private void clrscrn() {
		for (int i = 0; i < 64; i++) {
			for (int j = 0; j < 32; j++) {
				screen[i][j] = false;
			}
		}
	}

	private void ret() {
		pc = stack.pop();
	}

	public void beginDispatch() {
		frameLimiter.schedule(new TimerTask() {
			
			@Override
			public void run() {
				step();
			}
		}, new Date(), 1);

	}

}
