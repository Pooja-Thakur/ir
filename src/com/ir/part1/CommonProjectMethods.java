package com.ir.part1;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Map.Entry;

public class CommonProjectMethods {

	public HashMap<Integer, Double> getValuesFromTXTFile(String fileName) throws Exception {
		Scanner input = new Scanner(System.in);
		File file = new File(fileName);
		input = new Scanner(file);
		int key;
		double value;
		String line;
		String arr[];
		HashMap<Integer, Double> norms= new HashMap<Integer, Double>();
		while(input.hasNextLine()){
			line = input.nextLine();
			arr=line.split(":");
			key=Integer.parseInt(arr[0]);
			value=Double.parseDouble(arr[1]);
			norms.put(key, value);
		}
		input.close();
		return norms;
	}

	public TreeMap<Integer, Double> sortMapsByValue(TreeMap<Integer, Double> containsTfResults) {
		ValueComparator bvc =  new ValueComparator(containsTfResults);
		TreeMap<Integer,Double> sorted_map = new TreeMap<Integer,Double>(bvc);
		sorted_map.putAll(containsTfResults);
		return  sorted_map;
	}

	public void printResults(TreeMap<Integer, Double> map, String message) {
		System.out.println(message);
		int val=0;

		for(Entry<Integer, Double> entry:map.entrySet()) {
			if(val==10)
				break;
			else {
				System.out.println(entry.getKey());
//				System.out.println("Doc Num "+(val+1)+"-->"+entry.getKey()+"-->"+entry.getValue());
//				System.out.println("Doc Num "+(val+1)+"-->"+entry.getKey());
				val++;

				if(val == map.size())
					break;
			}
		}
	}
}
