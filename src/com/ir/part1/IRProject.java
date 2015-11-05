package com.ir.part1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.store.FSDirectory;

import com.ir.part2.authHub.StartAuthorityHubCalculations;
import com.ir.part2.pageRank.CalculatePageRankResults;

public class IRProject {
	static Term term;
	static TermDocs termDocs;
	static TermDocs tdocs;

	static double val1 = 0;
	static double val2 = 0;
	static double val3 = 0;
	static TreeMap<Integer, Double> containsTfResults= new TreeMap<Integer, Double>();
	static TreeMap<Integer, Double> newMapForCombiningResults;
	static TreeMap<Integer, Double> finalTfResults= new TreeMap<Integer, Double>();
	static TreeMap<Integer, Double> finalTfIdfDistances= new TreeMap<Integer, Double>();
	
	static CommonProjectMethods cpm = new CommonProjectMethods();

	public static void main(String arg[]) throws Exception {
		String input = "";
		String input_arr[];
		Scanner sc = new Scanner(System.in);
		TreeMap<Integer, Double> tfIdfResults;

		System.out.println("Enter input query-->");

		while(!(input = sc.nextLine().toLowerCase()).equals("quit")) {
			input_arr = input.split(" ");
			
			// TFIDF Implementation
			tfIdfResults = computeTFandTFIDFResults(input_arr);
			
			// Authority and Hubs Implementation
			StartAuthorityHubCalculations gam = new StartAuthorityHubCalculations();
			gam.computingAuthHubResults(tfIdfResults);
//			computeAuthorityAndHub(tfIdfResults);
			
			// Page Rank Implementation
			CalculatePageRankResults cprr = new CalculatePageRankResults();
			cprr.computePageRankResults(tfIdfResults);
		}
	}
/*	
	public static void computeAuthorityAndHub(TreeMap<Integer, Double> tfIdfResults) {
		StartAuthorityHubCalculations gam = new StartAuthorityHubCalculations();
		gam.generateAdjMat(tfIdfResults);
	}
*/	
	public static TreeMap<Integer, Double> computeTFandTFIDFResults(String input_arr[]) throws Exception {
		HashMap<Integer, Integer> freqOfQueryInDocs = new HashMap<Integer,Integer>();
		HashMap<Integer, Double> tfIdfDocTermMat = new HashMap<Integer, Double>();
		TreeMap<Integer, Double> containsTfIdfResults = new TreeMap<Integer, Double>();

		HashMap<Integer, Double> tfNorms= new HashMap<Integer,Double>();
		tfNorms = cpm.getValuesFromTXTFile("tfDocNorms.txt");
		HashMap<Integer, Double> tfIdfNorms= new HashMap<Integer,Double>();
		tfIdfNorms = cpm.getValuesFromTXTFile("tfIdfDocNorms.txt");

		ArrayList<TreeMap<Integer,Double>> listContainingTfResults = new ArrayList<TreeMap<Integer,Double>>();
		ArrayList<TreeMap<Integer,Double>> listContainingTfIdfResults = new ArrayList<TreeMap<Integer,Double>>();

		for(String word : input_arr) {
			term = new Term("contents", word);

			freqOfQueryInDocs=getFrequenciesOfQuery(term);							// Step 2

			/***************************TF COMPUTATION STARTED********************************/

			containsTfResults=getTfDistances(freqOfQueryInDocs, tfNorms);			// Step 6

			if(listContainingTfResults.size()==0)
				listContainingTfResults.add(containsTfResults);
			else
				listContainingTfResults = combineMaps(listContainingTfResults, containsTfResults);

			/***************************TF-IDF COMPUTATION STARTED****************************/

			tfIdfDocTermMat=generateTfIdfDocTermMat(freqOfQueryInDocs, term);		// Step 5

			containsTfIdfResults=getTfIdfDistances(tfIdfDocTermMat, tfIdfNorms);	// Step 7

			if(listContainingTfIdfResults.size()==0)
				listContainingTfIdfResults.add(containsTfIdfResults);
			else
				listContainingTfIdfResults = combineMaps(listContainingTfIdfResults, containsTfIdfResults);
		}

		finalTfResults = cpm.sortMapsByValue(listContainingTfResults.get(0));

		cpm.printResults(finalTfResults, "TF Results-->");
		System.out.println();

		finalTfIdfDistances = cpm.sortMapsByValue(listContainingTfIdfResults.get(0));

		cpm.printResults(finalTfIdfDistances, "TF-IDF Results-->");

		System.out.println();
		return finalTfIdfDistances;
	}

	public static ArrayList<TreeMap<Integer, Double>> combineMaps(ArrayList<TreeMap<Integer, Double>> listContainingTfAndTfIdfResults, TreeMap<Integer, Double> containsTfAndTfIdfResults) {
		newMapForCombiningResults = new TreeMap<Integer, Double>();
		int docNum = 0;

		for(TreeMap<Integer, Double> map:listContainingTfAndTfIdfResults) {
			for(Entry<Integer, Double> map1:map.entrySet()) {
				docNum = map1.getKey();
				val1 = map1.getValue();

				if(containsTfAndTfIdfResults.containsKey(docNum)) {
					val2 = containsTfAndTfIdfResults.get(docNum);
					val3 = val1 + val2;
					newMapForCombiningResults.put(docNum, val3);
					containsTfAndTfIdfResults.remove(docNum);
				} else
					newMapForCombiningResults.put(docNum, val1);
			}
		}

		containsTfAndTfIdfResults.putAll(newMapForCombiningResults);
		listContainingTfAndTfIdfResults = new ArrayList<TreeMap<Integer,Double>>();
		listContainingTfAndTfIdfResults.add(containsTfAndTfIdfResults);

		return listContainingTfAndTfIdfResults;
	}

	public static HashMap<Integer,Integer> getFrequenciesOfQuery(Term t) throws Exception {
		IndexReader r = IndexReader.open(FSDirectory.open(new File("index")));

		tdocs = r.termDocs(t);
		HashMap<Integer, Integer> freqOfQueryInDocs = new HashMap<Integer, Integer>();
		while(tdocs.next())
		{
			freqOfQueryInDocs.put(tdocs.doc(), tdocs.freq());
		}
		return freqOfQueryInDocs;
	}

	public static TreeMap<Integer, Double> getTfDistances(HashMap<Integer, Integer> freqOfQueryInDocs, HashMap<Integer, Double> tfNorms) throws Exception {
		TreeMap<Integer, Double> tfDistances= new TreeMap<Integer, Double>();
		double freq;
		double tf;
		double tfNorm;
		int docNum;

		for(Entry<Integer, Integer> e: freqOfQueryInDocs.entrySet()) {
			freq=e.getValue();
			docNum=e.getKey();
			tfNorm = tfNorms.get(docNum);
			tf=freq/tfNorm;
			tfDistances.put(docNum, tf);
		}
		return tfDistances;
	}

	public static HashMap<Integer, Double> generateTfIdfDocTermMat(HashMap<Integer, Integer> freqOfQueryInDocs, Term t) throws Exception {
		int docNum;
		double tfValue;
		int valueTemp;
		HashMap<Integer, Double> tfIdfDocTermMat = new HashMap<Integer, Double>();
		IndexReader r = IndexReader.open(FSDirectory.open(new File("index")));

		for(Entry<Integer, Integer> e: freqOfQueryInDocs.entrySet()) {
			docNum=e.getKey();
			valueTemp=e.getValue();
			try {
				tfValue= valueTemp*Math.log(25054/r.docFreq(t));
				tfIdfDocTermMat.put(docNum, tfValue);
			} catch(Exception ex) {
				// Do Nothing
			}			
		}
		return tfIdfDocTermMat;
	}

	public static TreeMap<Integer, Double> getTfIdfDistances(HashMap<Integer, Double> tfIdfDocTermMat, HashMap<Integer, Double> tfIdfNorms) throws Exception
	{
		TreeMap<Integer, Double> tfIdfDistances= new TreeMap<Integer, Double>();
		double tfIdfValue;
		double tfIdf;
		double tfIdfNorm;
		int docNum;

		for(Entry<Integer, Double> e: tfIdfDocTermMat.entrySet()) {
			tfIdfValue=e.getValue();
			docNum=e.getKey();
			tfIdfNorm = tfIdfNorms.get(docNum);
			tfIdf=tfIdfValue/tfIdfNorm;
			tfIdfDistances.put(docNum, tfIdf);
		}
		return tfIdfDistances;
	}
}
