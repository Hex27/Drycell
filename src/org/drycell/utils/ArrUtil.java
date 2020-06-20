package org.drycell.utils;

import org.bukkit.Material;

public class ArrUtil {
	
	public static Object[] mergeArr(Object[]... arrs){
		int totalLength = 0;
		int index = 0;
		for(Object[] arr:arrs){
			totalLength += arr.length;
		}
		Object[] res = new Object[totalLength];
		for(Object[] arr:arrs){
			for(Object mat:arr){
				res[index] = mat;
				index++;
			}
		}
		
		return res;
	}

}
