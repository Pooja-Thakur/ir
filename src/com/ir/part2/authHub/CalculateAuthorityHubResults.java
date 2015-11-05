package com.ir.part2.authHub;

import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.ir.part1.CommonProjectMethods;

public class CalculateAuthorityHubResults {

	CommonFunctions cf = new CommonFunctions();
	CommonProjectMethods cpm = new CommonProjectMethods();
	
	public TreeMap<Integer, Double> calculateResults(double[][] matrix1, double[][] matrix2, LinkedHashMap<Integer, Integer> baseSet) {

		int size = matrix1.length;
		double[][] multipliedMatrixNN = new double[size][size];
		TreeMap<Integer, Double> finalAuthHubResults= new TreeMap<Integer, Double>();
		
		for(int i=0; i<size; i++) 
			for(int j=0; j<size; j++) 
				multipliedMatrixNN[i][j] = 0;

		multipliedMatrixNN = matrixMultiplicationNNNN(multipliedMatrixNN, matrix1, matrix2, size);
		
		double[][] finalResultMatrix = cf.create1DMatrix(size);

		for(int i=0; i<35; i++) {
			finalResultMatrix = cf.matrixMultiplicationNNN1(multipliedMatrixNN, finalResultMatrix, size);
		}
		
		TreeMap<Integer, Double> finalResultMap = new TreeMap<Integer, Double>();
		
		for(int i=0; i<size; i++) {
//			finalResultMap.put(baseSet.get(i), finalResultMatrix[i][0]);
			finalResultMap.put(cf.getKeyByValue(baseSet, i), finalResultMatrix[i][0]);
		}
		
		finalAuthHubResults = cpm.sortMapsByValue(finalResultMap);

		return finalAuthHubResults;
	}

	public double[][] matrixMultiplicationNNNN(double[][] multipliedMatrixNN, double[][] matrix1, double[][] matrix2, int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < size; k++) {
					multipliedMatrixNN[i][j] = multipliedMatrixNN[i][j] + matrix1[i][k] * matrix2[k][j];
				}
			}
		}

		return multipliedMatrixNN;
	}

	/*
		int[][] c = new int[rowsInA][columnsInB];
		for (int i = 0; i < rowsInA; i++) {
			for (int j = 0; j < columnsInB; j++) {
				for (int k = 0; k < columnsInA; k++) {
					c[i][j] = c[i][j] + a[i][k] * b[k][j];
				}
			}
		}
	 */
}
