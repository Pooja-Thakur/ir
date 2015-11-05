package com.ir.part2.authHub;

import java.util.Map;
import java.util.Map.Entry;

public class CommonFunctions {
	public double[][] create1DMatrix(int size) {
		double[][] oneDMatrix = new double[size][1];

		for(int i=0; i<size; i++)
			oneDMatrix[i][0] = 1;

		return oneDMatrix;
	}

	public double[][] normalizeMatrixN1(double[][] matrix) {
		double sum = 0;

		for(int i=0; i<matrix.length; i++) {
			sum = sum + matrix[i][0]*matrix[i][0];
	//		System.out.println(matrix[i][0]);
		}
		
		sum = Math.sqrt(sum);

		for(int i=0; i<matrix.length; i++)
			matrix[i][0] = matrix[i][0]/sum;

		return matrix;
	}
	
	public double[][] matrixMultiplicationNNN1(double[][] multipliedMatrix, double[][] oneDMatrix, int size) {
		double[][] multipledMatrixN1 = new double[size][1];

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < 1; j++) {
				for (int k = 0; k < size; k++) {
					multipledMatrixN1[i][j] = multipledMatrixN1[i][j] + multipliedMatrix[i][k] * oneDMatrix[k][j];
				}
			}
		}
/*		
		for(int i=0; i<size; i++)
			for(int j=0; j<25; j++) {
				System.out.println(multipledMatrixN1[i][j]);
			}
			
*/
		multipledMatrixN1 = normalizeMatrixN1(multipledMatrixN1);
/*		
		int count=0;
		for(int i=0; i<size; i++)
			for(int j=0; j<1; j++) {
				if(multipledMatrixN1[i][j] > 0)
					count++;
			}
			
		System.out.println(count);
*/
		return multipledMatrixN1;
	}

	public int getKeyByValue(Map<Integer, Integer> map, int value) {
		for (Entry<Integer, Integer> entry : map.entrySet()) {
			if (value == entry.getValue()) {
				return entry.getKey();
			}
		}
		System.out.println("In here");
		return 0;
	}
}
