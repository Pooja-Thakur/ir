package com.ir.part2.authHub;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.ir.part1.CommonProjectMethods;

public class StartAuthorityHubCalculations {
	LinkAnalysis linkAnalysis = new LinkAnalysis();
	static CommonFunctions cf = new CommonFunctions();

	public void computingAuthHubResults(TreeMap<Integer, Double> tfIdfResults) {
		CommonProjectMethods cpm = new CommonProjectMethods();
		
		int rootSetSize = 10;
		
		ArrayList<Integer> rootSet = constructRootSet(tfIdfResults, rootSetSize);
		LinkedHashMap<Integer, Integer> baseSet = generateBaseSet(rootSet);
		double [][]adjacencyMatrix = createAdjacencyMatrix(baseSet);
		double [][]transposeAdjacencyMatrix = transposeAdjacencyMatrix(adjacencyMatrix);

		// Final Calculation of the Results
		CalculateAuthorityHubResults cahr = new CalculateAuthorityHubResults();
		TreeMap<Integer, Double> authorityResults = cahr.calculateResults(adjacencyMatrix, transposeAdjacencyMatrix, baseSet);
		TreeMap<Integer, Double> hubResults = cahr.calculateResults(transposeAdjacencyMatrix, adjacencyMatrix, baseSet);

		// Printing Authority Results
		cpm.printResults(authorityResults, "Authority Results-->");
		System.out.println();

		// Printing Hub Results
		cpm.printResults(hubResults, "Hub Results-->");
		System.out.println();
	}

	public ArrayList<Integer> constructRootSet(TreeMap<Integer, Double> tfIdfResults, int rootSetSize) {
		/* Constructing Root Set */
		
		ArrayList<Integer> rootSet = new ArrayList<Integer>();
		int i = 0;

		if(tfIdfResults.size() <= rootSetSize)
			rootSetSize = tfIdfResults.size();

		for(Entry<Integer, Double> entry:tfIdfResults.entrySet()) {
			if(i<rootSetSize) {
				rootSet.add(entry.getKey());
				i++;
			} else
				break;
		}

		return rootSet;
	}

	public LinkedHashMap<Integer, Integer> generateBaseSet(ArrayList<Integer> rootSet) {
		/* Constructing Base Set */
		
		int i = 0;
		int[] links;
		int[] citations;

		LinkedHashMap<Integer, Integer> baseSet = new LinkedHashMap<Integer, Integer>();

		for(Integer rootSet1:rootSet) {
			links = linkAnalysis.getLinks(rootSet1);
			for(int link:links) {
				if(!baseSet.containsKey(link)) {
//					baseSet.put(i, link);
					baseSet.put(link, i);
					i++;
				}
			}

			citations = linkAnalysis.getCitations(rootSet1);
			for(int citation:citations) {
				if(!baseSet.containsKey(citation)) {
//					baseSet.put(i, citation);
					baseSet.put(citation, i);
					i++;
				}
			}
		}
		return baseSet;
	}

	public double[][] createAdjacencyMatrix(LinkedHashMap<Integer, Integer> baseSet) {
		/* Constructing Adjacency Matrix */
		
		int[] links;

		int arrSize = baseSet.size();

		double adjacencyMatrix[][] = new double[arrSize][];
		int linkAndCitation = 0;
		int index = 0;

		for(int i=0; i<arrSize; i++) 
			adjacencyMatrix[i] = new double[arrSize];

		for(int i=0; i<arrSize; i++) {
			linkAndCitation = cf.getKeyByValue(baseSet, i);
//			System.out.println(linkAndCitation);
//			linkAndCitation = baseSet.get(i);
			links = linkAnalysis.getLinks(linkAndCitation);

			for(int lac:links) {
				try {
//					index = cf.getKeyByValue(baseSet, lac);
					index = baseSet.get(lac);
					adjacencyMatrix[index][i] = 1;
				} catch(Exception e) {
					// Do Nothing
				}
			}
		}
/*		
		for(int i=0; i<arrSize; i++) {
			for(int j=0; j<arrSize; j++) {
				System.out.print(adjacencyMatrix[i][j] + "   ");
			}
			System.out.println();
		}
*/
		return adjacencyMatrix;
	}

	public double[][] transposeAdjacencyMatrix(double[][] adjacencyMatrix) {
		/* Transpose of Adjacency Matrix. */
		
		int matrixSize = adjacencyMatrix.length;
		double[][] transposeAdjacencyMatrix = new double[matrixSize][];

		for(int i=0; i<matrixSize; i++)
			transposeAdjacencyMatrix[i] = new double[matrixSize];

		for(int i=0; i<matrixSize; i++) {
			for(int j=0; j<matrixSize; j++) {
				transposeAdjacencyMatrix[i][j] = adjacencyMatrix[j][i];
			}
		}

		return transposeAdjacencyMatrix;
	}
}
