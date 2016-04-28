/**
 * StringUtils.java
 * 
 * @author anewkirk
 * 
 * Licensing information can be found in the root directory of the project.
 */

package com.echodrop.blip8.util;

public class StringUtils {
	
	/**
	 * Left-pads a string with zeros until it is of length size
	 */
	public static String zeroLeftPad(String s, int size) {
		String result = s;
		while (result.length() < size) {
			result = "0" + result;
		}
		return result;
	}

}
