package com.we.timetrack.util;

import java.nio.ByteBuffer;
import java.util.UUID;

/*
 * Utilits to convert UUID class
 */
public class UuidUtils {
	
	/*
	 * Convert binary form GUID to UUID.class
	 */
	public static UUID asUuid(byte[] bytes) {
		byte[] result = convertBytes(bytes);
	    ByteBuffer bb = ByteBuffer.wrap(result);
	    long firstLong = bb.getLong();
	    long secondLong = bb.getLong();
	    
	    return new UUID(firstLong, secondLong);
	}

	/*
	 * Convert UUID.class to byte array with sequence
	 * [3] [2] [1] [0] - [5] [4] - [7] [6] - [8] [9] - [10] [11] [12] [13] [14] [15]
	 */
	private static byte[] asBytes(UUID uuid) {
	    ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
	    bb.putLong(uuid.getMostSignificantBits());
	    bb.putLong(uuid.getLeastSignificantBits());
	    
	    return bb.array();
	}
	
	/*
	 * Convert byte array UUID to string in format
	 * \ [0] \ [1] \ [2] \ [3] \ [4] \ [5] \ [6] \ [7] \ [8] \ [9] \ [10] \ [11] \ [12] \ [13] \ [14] \ [15]
	 */
	public static String convertToByteString(byte[] objectGUID) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < objectGUID.length; i++) {
			String transformed = prefixZeros((int) objectGUID[i] & 0xFF);
	        result.append("\\");
	        result.append(transformed);
	    }
		
		return result.toString();
	}
	
	/*
	 * Convert UUID.class to string in format
	 * \ [0] \ [1] \ [2] \ [3] \ [4] \ [5] \ [6] \ [7] \ [8] \ [9] \ [10] \ [11] \ [12] \ [13] \ [14] \ [15]
	 */
	public static String convertToByteString(UUID uuid) {
		byte[] bytes = asBytes(uuid);
		byte[] objectGUID = convertBytes(bytes);
	    StringBuilder result = new StringBuilder();
	   
	    for (int i = 0; i < objectGUID.length; i++) {
	    	String transformed = prefixZeros((int) objectGUID[i] & 0xFF);
	        result.append("\\");
	        result.append(transformed);
	    }
	    
	    return result.toString();
	}
	
	/*
	 * Make byte array to sequence
	 * [3] [2] [1] [0] - [5] [4] - [7] [6] - [8] [9] - [10] [11] [12] [13] [14] [15]
	 */
	private static byte[] convertBytes(byte[] bytes){
		byte[] result = new byte[16];
		result[0] = bytes[3];
		result[1] = bytes[2];
		result[2] = bytes[1];
		result[3] = bytes[0];
		result[4] = bytes[5];
		result[5] = bytes[4];
		result[6] = bytes[7];
		result[7] = bytes[6];
		result[8] = bytes[9];
		result[9] = bytes[8];
		result[10] = bytes[10];
		result[11] = bytes[11];
		result[12] = bytes[12];
		result[13] = bytes[13];
		result[14] = bytes[14];
		result[15] = bytes[15];
		return result;
	}

	/*
	 * Add zero to string if value <= 0xF
	 */
	private static String prefixZeros(int value){
		if (value <= 0xF){
			StringBuilder str = new StringBuilder("0");
			str.append(Integer.toHexString(value));
		
			return str.toString();
		} else {
			return Integer.toHexString(value);
		}
	}
	
}
