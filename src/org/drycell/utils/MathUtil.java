package org.drycell.utils;

import java.util.Random;
import java.util.TreeMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class MathUtil {

	public static double evaluateEquation(String s)  {
		//	        ScriptEngineManager man = new ScriptEngineManager();
		//	        ScriptEngine jsEngine = man.getEngineByName("JavaScript");
		//	        try {
		//	        	Object num = jsEngine.eval(s);
		//	        	if(num instanceof Double){
		//	        		num = ((Double) num).intValue();
		//	        	}
		//	            return (int) num;
		//	        } catch (ScriptException e) {
		//	            return 0;
		//	        }
		return (double) new MathEval().evaluate(s);
	}

	public static double round (double value, int precision) {
		int scale = (int) Math.pow(10, precision);
		return (double) Math.round(value * scale) / scale;
	}

	public static int randInt(int min, int max) {

		Random rand = new Random();

		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}
	
    private final static TreeMap<Integer, String> map = new TreeMap<Integer, String>();

    static {

        map.put(1000, "M");
        map.put(900, "CM");
        map.put(500, "D");
        map.put(400, "CD");
        map.put(100, "C");
        map.put(90, "XC");
        map.put(50, "L");
        map.put(40, "XL");
        map.put(10, "X");
        map.put(9, "IX");
        map.put(5, "V");
        map.put(4, "IV");
        map.put(1, "I");

    }

    public final static String toRoman(int number) {
        int l =  map.floorKey(number);
        if ( number == l ) {
            return map.get(number);
        }
        return map.get(l) + toRoman(number-l);
    }
    
    public static int value(char r) 
    { 
        if (r == 'I') 
            return 1; 
        if (r == 'V') 
            return 5; 
        if (r == 'X') 
            return 10; 
        if (r == 'L') 
            return 50; 
        if (r == 'C') 
            return 100; 
        if (r == 'D') 
            return 500; 
        if (r == 'M') 
            return 1000; 
        return -1; 
    } 
  
    // Finds decimal value of a given romal numeral 
    public static int romanToDecimal(String str) 
    { 
        // Initialize result 
        int res = 0; 
  
        for (int i=0; i<str.length(); i++) 
        { 
            // Getting value of symbol s[i] 
            int s1 = value(str.charAt(i)); 
  
            // Getting value of symbol s[i+1] 
            if (i+1 <str.length()) 
            { 
                int s2 = value(str.charAt(i+1)); 
  
                // Comparing both values 
                if (s1 >= s2) 
                { 
                    // Value of current symbol is greater 
                    // or equalto the next symbol 
                    res = res + s1; 
                } 
                else
                { 
                    res = res + s2 - s1; 
                    i++; // Value of current symbol is 
                    // less than the next symbol 
                } 
            } 
            else
            { 
                res = res + s1; 
                i++; 
            } 
        } 
  
        return res; 
    } 


}
