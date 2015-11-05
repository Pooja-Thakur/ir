package com.ir.part2.pageRank;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

import com.ir.part2.authHub.CommonFunctions;
import com.ir.part2.authHub.LinkAnalysis;

public class ComputePageRankTable {

	static CommonFunctions cmo = new CommonFunctions();
	
	public static double[][] constructAdjacencyMatrix(int size, double c) {
		double[][] adjacencyMatrix = new double[size][size];
		
		LinkAnalysis la = new LinkAnalysis();
		
		int i = 0;
		int citations[];
		int citationSize = 0;
		double prob = 0d;
		
		for(i = 0; i < size; i++) {
			citations = la.getCitations(i);
			citationSize = citations.length;
			prob = c/citationSize;
			
			for(int j:citations) {
				adjacencyMatrix[j][i] = prob;
			}			
		}

		return adjacencyMatrix;
	}
	
	public static double[][] computeMStarMatrix(double[][] adjacencyMatrix, int size, double c) {
		double val = (1-c)/size;
		
		for(int i=0; i<size; i++)
			for(int j=0; j<size; j++)
				adjacencyMatrix[i][j] += val;		

		return adjacencyMatrix;
	}
	
	public static double[][] computePageRanks(double[][] mStarMatrix, double[][] oneDMatrix, int size) {
		for(int i=0; i<50; i++) 
			oneDMatrix = cmo.matrixMultiplicationNNN1(mStarMatrix, oneDMatrix, size);
		
		return oneDMatrix;
	}
	
	public static void storePageRankValues(double[][] resultMatrix, int size) throws Exception {
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("035CalculatedPageRankValues.txt", true)));
		
		for(int i=0; i<size; i++) 
			out.println(i + ":" + resultMatrix[i][0]);			
			
		out.close();
	}
	
	public static void main(String[] args) throws Exception {
		double c = 0.35;
		int size = 25054;
		double[][] adjacencyMatrix = constructAdjacencyMatrix(size, c);
		double[][] mStarMatrix = computeMStarMatrix(adjacencyMatrix, size, c);
		double[][] oneDMatrix = cmo.create1DMatrix(size);
		double[][] resultMatrix = computePageRanks(mStarMatrix, oneDMatrix, size);
		storePageRankValues(resultMatrix, size);
	}
}
