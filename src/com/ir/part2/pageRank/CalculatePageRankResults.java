package com.ir.part2.pageRank;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ir.part1.CommonProjectMethods;

public class CalculatePageRankResults {
	CommonProjectMethods cpm = new CommonProjectMethods();
	
	public void computePageRankResults(TreeMap<Integer, Double> tfIdfResults) throws Exception {
		long startTime = System.currentTimeMillis();
		HashMap<Integer, Double> pageRankValues = new HashMap<Integer,Double>();
		pageRankValues = cpm.getValuesFromTXTFile("08CalculatedPageRankValues.txt");
		double w = 0d;
		double pageRankValue = 0d;
		int fileNum = 0;
		TreeMap<Integer, Double> pageRankResults = new TreeMap<Integer, Double>();
		
		
		for(Entry<Integer, Double> entry:tfIdfResults.entrySet()) {
			fileNum = entry.getKey();
			pageRankValue = w*entry.getValue() + (1-w)*pageRankValues.get(entry.getKey());
			pageRankResults.put(fileNum, pageRankValue);
		}
		
		pageRankResults = cpm.sortMapsByValue(pageRankResults);
		
		cpm.printResults(pageRankResults, "Page Rank Results-->");
		long endTime = System.currentTimeMillis();
		System.out.println("Time taken for Page Rank-->"+(endTime - startTime));
	}
}
