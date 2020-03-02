package testcases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import muehletest.PositionGenerator;

class PositionGeneratorTest {

//	@Test
//	void testGenerateAllDifferent() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	void testStepMoverPieceConsideringSymmetries() {
//		fail("Not yet implemented");
//	}

	@Test
	void testMirrorSecondDiagonal() {
		int nrPieces = 5;
		int[] pieceIndex = { 0, 1, 5, 10, 20 };
		int bitRepresentation = 0;
		for (int i = 0; i < nrPieces; i++) {
			bitRepresentation |= 1 << (23 - pieceIndex[i]);
		}
		int[] pieceIndexMirrored = { 0, 7, 3, 14, 20 };
		int bitRepresentationMirrored = 0;
		for (int i = 0; i < nrPieces; i++) {
			bitRepresentationMirrored |= 1 << (23 - pieceIndexMirrored[i]);
		}
		PositionGenerator positionGenerator = new PositionGenerator(3, 3);
		System.out.println(bitRepresentation);
		assertEquals(positionGenerator.mirrorSecondDiagonal(bitRepresentation), bitRepresentationMirrored);
	}
	
	@Test
	void testRotate90Degrees() {
		int nrPieces = 5;
		int[] pieceIndex = { 0, 1, 7, 10, 22 };
//		int[] pieceIndex = { 0, 7 };
		int bitRepresentation = 0;
		for (int i = 0; i < nrPieces; i++) {
			bitRepresentation |= 1 << (23 - pieceIndex[i]);
		}
		int[] pieceIndexRotated = { 2, 3, 1, 12, 16 };
//		int[] pieceIndexRotated = { 2, 1 };
		int bitRepresentationRotated = 0;
		for (int i = 0; i < nrPieces; i++) {
			bitRepresentationRotated |= 1 << (23 - pieceIndexRotated[i]);
		}
		PositionGenerator positionGenerator = new PositionGenerator(3, 3);
		System.out.println(bitRepresentation);
		System.out.println(positionGenerator.rotate90Degrees(bitRepresentation));
		assertEquals(positionGenerator.rotate90Degrees(bitRepresentation), bitRepresentationRotated);
	}
	
	@Test
	void testSymmetries() {

		int[] pieceIndex = { 0, 2, 4, 6};
		PositionGenerator positionGenerator = new PositionGenerator(4, 3);
		positionGenerator.setMoverPieceIndex(pieceIndex);
		
		List<Integer> symmetries = new ArrayList<>();
		symmetries.add(1);
		symmetries.add(2);
		symmetries.add(3);
		symmetries.add(4);
		symmetries.add(5);
		symmetries.add(6);
		symmetries.add(7);
		System.out.println(positionGenerator.symmetries().get(0));
		assertEquals(positionGenerator.symmetries(), symmetries);
	}
	
	@Test
	void testSymmetriesNull() {

		int[] pieceIndex = { 2, 4, 6};
		PositionGenerator positionGenerator = new PositionGenerator(3, 3);
		positionGenerator.setMoverPieceIndex(pieceIndex);

		assertEquals(positionGenerator.symmetries(), null);
	}
	
	@Test
	void testCheckSymmetriesFalse() {

		int[] pieceIndex = { 0, 2, 4};
		PositionGenerator positionGenerator = new PositionGenerator(3, 3);
		positionGenerator.setWaiterPieceIndex(pieceIndex);
		List<Integer> symmetries = new ArrayList<>();
		symmetries.add(1);
		symmetries.add(2);
		symmetries.add(3);
		symmetries.add(4);
		symmetries.add(5);
		symmetries.add(6);
		symmetries.add(7);

		assertEquals(positionGenerator.checkSymmetries(symmetries), false);
	}
	
	@Test
	void testCheckSymmetriesTrue() {

		int[] pieceIndex = { 0, 6, 14};
		PositionGenerator positionGenerator = new PositionGenerator(3, 3);
		positionGenerator.setWaiterPieceIndex(pieceIndex);
		List<Integer> symmetries = new ArrayList<>();
		symmetries.add(2);

		assertEquals(positionGenerator.checkSymmetries(symmetries), true);
	}

}
