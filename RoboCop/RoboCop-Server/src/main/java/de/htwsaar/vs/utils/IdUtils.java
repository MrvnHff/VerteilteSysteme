package de.htwsaar.vs.utils;

public abstract class IdUtils {

	public static int[] extractCoordinates(String cellId) {
		String strCoordinates[];
		int	intCoordinates[] = new int[2];
		strCoordinates = cellId.split("/");
		intCoordinates[0] =  Integer.parseInt(strCoordinates[0]);
		intCoordinates[1] =  Integer.parseInt(strCoordinates[1]);
		return intCoordinates;
	}
	
	public static String createIdString(int row, int col) {
		return row + "/" + col;
	}
}
