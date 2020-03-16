package muehletest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MuehleTest {

	public static void main(String[] args) {

		PositionIterator positionIterator = new PositionIterator();
		positionIterator.init(4, 3);

//		Position position = positionIterator.getNextPlayerPosition();	
//		position.printPos();

		List<String[]> stringRepresentation = new ArrayList<String[]>();

		Position position = positionIterator.getNextPlayerPosition();
		int posCount = 1;

		while (position != null) {
			stringRepresentation.add(position.getStringRepresentation());
			position = positionIterator.getNextPlayerPosition();
			posCount += 1;
		}

		int boardsPerRow = 6;
		int startPos = posCount - 78;
		
		
		System.out.println("Anzahl Positionen: " + posCount);

		for (int nRows = 0; nRows < 13; nRows++) {
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

//		PositionGenerator generator = new PositionGenerator(5, 5);
//		List<Position> permutations = generator.generateAllDifferent();
//		
//		System.out.println("Anzahl Permutationen: " + permutations.size());

//		List<String[]> stringRepresentation = new ArrayList<String[]>();
//
//		for (Position position : permutations) {
//			stringRepresentation.add(position.getStringRepresentation());
//		}
//
//		// Output to console:
//
//		System.out.println("Darstellung der Permutationen:");
//		int boardsPerRow = 6;
//
//		for (int i = 0; i < 60 / boardsPerRow + 1; i++) {
//			System.out.println();
//			String rowString;
//			for (int j = 0; j < 7; j++) {
//				rowString = "   ";
//				for (int k = 0; k < boardsPerRow; k++) {
//					rowString += stringRepresentation.get(i * boardsPerRow + k)[j] + "      ";
//					if (i * boardsPerRow + k + 1 == stringRepresentation.size())
//						break;
//				}
//				System.out.println(rowString);
//			}
//		}
//
//		System.out.println();
//		System.out.println("*".repeat(25) + "   usw.   " + "*".repeat(25));
//		System.out.println();
//
//		for (int i = 0; i < 60 / boardsPerRow; i++) {
//			System.out.println();
//			String rowString;
//			for (int j = 0; j < 7; j++) {
//				rowString = "   ";
//				for (int k = 0; k < boardsPerRow; k++) {
//					rowString += stringRepresentation.get(stringRepresentation.size() - 60 //
//							+ i * boardsPerRow + k)[j] + "      ";
//				}
//				System.out.println(rowString);
//			}
//		}

}
