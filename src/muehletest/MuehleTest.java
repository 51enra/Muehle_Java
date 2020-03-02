package muehletest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MuehleTest {

	public static void main(String[] args) {

//		Position testPos = new Position(new byte[] { (byte) 255, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) (128+32) });
//		Position testPos2 = new Position(new byte[] { (byte) 32, (byte) 8, (byte) 96, (byte) 4, (byte) 2, (byte) -128 });
//		Position testPos = new Position(new byte[] { (byte) 99, (byte) 0, (byte) 34, (byte) 0, (byte) 68, (byte) 0 });
// 0110011, 0100100, 0010010

//		System.out.println(testPos.findLastFreeFieldBefore(23));

		int[] moverCount = {10, 11, 12};//{3, 4, 4, 5, 5}; 
		int[] waiterCount = {10, 11, 12};//{3, 3, 4, 3, 4};//
		
		System.out.println("Mover,Waiter,Count,Time (ms)");
		
		for (int i = 0; i< moverCount.length; i++) {
				long before = System.currentTimeMillis();
				PositionGenerator generator = new PositionGenerator(moverCount[i], waiterCount[i]);
				generator.generateAllDifferent();
				long elapsed = System.currentTimeMillis() - before;
				System.out.println(moverCount[i] + "," + waiterCount[i] + "," + generator.getPositionCount() + "," + elapsed);
		}

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

}
