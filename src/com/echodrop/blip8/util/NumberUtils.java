/**
 * NumberUtils.java
 * 
 * @author anewkirk
 * 
 * Licensing information can be found in the root directory of the project.
 */

package com.echodrop.blip8.util;

/**
 * Contains miscellaneous utilities that are used throughout the codebase.
 */
public class NumberUtils {

	public static boolean byteAdditionOverflow(byte b1, byte b2) {
		int result = Byte.toUnsignedInt(b1) + Byte.toUnsignedInt(b2);
		return result > 255;
	}

	public static boolean byteSubtractionBorrow(byte b1, byte b2) {
		int b = Byte.toUnsignedInt(b1) >> 7;
		if(b > 0) {
			int r = Byte.toUnsignedInt(b1) & 0x7F;
			if(Byte.toUnsignedInt(b2) > r) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Reads the value of a specific bit from data
	 * Interprets binary value as MSB first
	 * 
	 * @return true if the specified bit is 1
	 */
	public static boolean readBit(int bit, byte data) {
		String bin = StringUtils.zeroLeftPad(Integer.toBinaryString(data & 0xFF), 8);
		return bin.charAt(bit) == '1';
	}

}
