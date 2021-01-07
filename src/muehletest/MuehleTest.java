package muehletest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MuehleTest {

	public static void main(String[] args) {

		PositionIterator positionIterator = new PositionIterator();
		positionIterator.init(3, 3);

		PositionAnalysis positionAnalysis = new PositionAnalysis(3);
//		List<Position> positions = positionAnalysis.findGameEndPositions(3);
		List<String[]> stringRepresentation = new ArrayList<String[]>();

//		while (position != null) {
//			position = positionIterator.getNextPlayerPosition();
//			positions.add(position);
//		}
        for(int i=0; i<100; i++) {
        	positionIterator.getNextPlayerPosition();
        }

		List<Position> positions = positionIterator.findLosingOpponentPositions(null, 0);
		
		Position position = positions.get(5);		
		position.printPos();
		System.out.println();
		
		List<Position> predecessorPositions = positionAnalysis.findAllPredecessors(position);
		int posCount = predecessorPositions.size();
		System.out.println("Anzahl Positionen: " + posCount);
		

		int boardsPerRow = 6;

		for (int i = 0; i < 4; i++) {
//		  	if (positions.get(i).getEncoding()[2] > 0) {
				stringRepresentation.add(predecessorPositions.get(i).getStringRepresentation());
//			}
		}

//		System.out.println(stringRepresentation.size());

		int startPos = 0; // stringRepresentation.size() - 384;

		for (int nRows = 0; nRows < 3; nRows++) {
			System.out.println();
			String rowString;
			for (int j = 0; j < 7; j++) {
				rowString = "   ";
				for (int k = 0; k < boardsPerRow; k++) {
					rowString += stringRepresentation.get(startPos + nRows * boardsPerRow + k)[j] + "      ";
					if (startPos + nRows * boardsPerRow + k + 1 >= stringRepresentation.size())
						break;
				}
				System.out.println(rowString);
			}
		}

	}

//		int[] moverCount = {5, 4, 6};//{3, 4, 4, 5, 5}; 
//		int[] waiterCount = {5, 4, 6};//{3, 3, 4, 3, 4};//
//		
//		System.out.println("Mover,Waiter,Count,Time (ms)");
//		
//		for (int i = 0; i< moverCount.length; i++) {
//				long before = System.currentTimeMillis();
//				PositionGenerator generator = new PositionGenerator(moverCount[i], waiterCount[i]);
//				generator.generateAllDifferent();
//				long elapsed = System.currentTimeMillis() - before;
//				System.out.println(moverCount[i] + "," + waiterCount[i] + "," + generator.getPositionCount() + "," + elapsed);
//		}

}
