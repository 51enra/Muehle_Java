package testcases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import muehletest.Position;

class PositionTest {

	@Test
	void testGetMillClosingFields() {
		Position testPos = new Position(new byte[] { (byte) 0x8b, (byte) 0x00, (byte) 0x08, (byte) 0, (byte) 0x18, (byte) 0 });
        // 10001011, 0, 00001000, 0, 00011000, 0
		Set<Integer> playerWins = new HashSet<Integer>(Arrays.asList(2,6,11));
		Set<Integer> opponentWins = new HashSet<Integer>(Arrays.asList(10));
		assertEquals(playerWins, testPos.getMillClosingFields('p'));
		assertEquals(opponentWins, testPos.getMillClosingFields('o'));
	}
	
	@Test
	void testGetMillClosingFields2() {
		Position testPos = new Position(new byte[] { (byte) 0x8b, (byte) 0x60, (byte) 0x08, (byte) 0, (byte) 0x18, (byte) 0 });
        // 10001011, 01100000, 00001000, 0, 00011000, 0
		Set<Integer> playerWins = new HashSet<Integer>(Arrays.asList(2,6,11,12));
		Set<Integer> opponentWins = new HashSet<Integer>(Arrays.asList(10));
		assertEquals(playerWins, testPos.getMillClosingFields('p'));
		assertEquals(opponentWins, testPos.getMillClosingFields('o'));
	}
	
	@Test
	void testGetNrOpenMills() {
		//fail("Not yet implemented");
		Position testPos2 = new Position(new byte[] { (byte) 0x8b, (byte) 0x00, (byte) 0x08, (byte) 0, (byte) 0x18, (byte) 0 });
        // 10001011, 0, 00001000, 0, 00011000, 0
		Position testPos2b = new Position(new byte[] { (byte) 0x8b, (byte) 0x60, (byte) 0x08, (byte) 0, (byte) 0x18, (byte) 0 });
        // 10001011, 01100000, 00001000, 0, 00011000, 0
		Position testPos4 = new Position(new byte[] { (byte) 0x8b, (byte) 0x60, (byte) 0x08, (byte) 0, (byte) 0x83, (byte) 0xc3 });
        // 10001011, 01100000, 00001000, 0, 10000011, 11000011
		Position testPos1 = new Position(new byte[] { (byte) 0x8b, (byte) 0x60, (byte) 0x08, (byte) 0x40, (byte) 0x18, (byte) 0 });
        // 10001011, 01100000, 00001000, 01000000, 00011000, 0
		Position testPos0 = new Position(new byte[] { (byte) 0x8b, (byte) 0x60, (byte) 0x08, (byte) 0x44, (byte) 0x18, (byte) 0 });
        // 10001011, 01100000, 00001000, 01000100, 00011000, 0
		assertEquals(2, testPos2.getNrOpenMills(testPos2.getMillClosingFields('p')));
		assertEquals(2, testPos2b.getNrOpenMills(testPos2b.getMillClosingFields('p')));
		assertEquals(4, testPos4.getNrOpenMills(testPos4.getMillClosingFields('p')));
		assertEquals(1, testPos1.getNrOpenMills(testPos1.getMillClosingFields('p')));
		assertEquals(0, testPos0.getNrOpenMills(testPos0.getMillClosingFields('p')));
	}	
	
	@Test
	void testGetPiecesInMills() {
		Position testPos = new Position(new byte[] { (byte) 0xcb, (byte) 0x08, (byte) 0x08, (byte) 0, (byte) 0x78, (byte) 0x1c });
        // 11001011, 0, 0, 0, 00011000
		
		Set<Integer> mTestSet = new HashSet<Integer>(Arrays.asList(0,3,6,7,11,19));
		Set<Integer> wTestSet = new HashSet<Integer>(Arrays.asList(12,13,14,18,19,20));
		
		assertEquals(mTestSet, testPos.getPiecesInMills('p'));
		assertEquals(wTestSet, testPos.getPiecesInMills('o'));
	}
	
	@Test
	void testTopLeftCornerPlus() {
		Position testPos0 = new Position(new byte[] { (byte) 0x0b, (byte) 0x08, (byte) 0x08, (byte) 0, (byte) 0x78, (byte) 0x1c });
        // 00001011, 00001000, 00001000, 0, 01111000, 00011100
		Position testPos2 = new Position(new byte[] { (byte) 0x8c, (byte) 0x08, (byte) 0x08, (byte) 0, (byte) 0x78, (byte) 0x1c });
        // 10001100, 00001000, 00001000, 0, 01111000, 00011100
		Position testPos16 = new Position(new byte[] { (byte) 0x08, (byte) 0x08, (byte) 0x0b, (byte) 0, (byte) 0x78, (byte) 0x1c });
        // 00001000, 00001000, 00001011, 0, 01111000, 00011100
		Position testPos4 = new Position(new byte[] { (byte) 0xaa, (byte) 0x20, (byte) 0xaa, (byte) 0x1c, (byte) 0x78, (byte) 0 });
        // 10101010, 00100000, 10101010, 00011100, 01111000, 0 
		Position testPos20 = new Position(new byte[] { (byte) 0xaa, (byte) 0x20, (byte) 0xaa, (byte) 0, (byte) 0x78, (byte) 0x1c });
        // 10101010, 00100000, 10101010, 0, 01111000, 00011100
		Position testPos22 = new Position(new byte[] { (byte) 0xaa, (byte) 0x55, (byte) 0xaa, (byte) 0, (byte) 0xc1, (byte) 0x40 });
        // 10101010, 01010101, 10101010, 0, 11000001, 0100 0000
		assertEquals(0, testPos0.topLeftCorner());
		assertEquals(2, testPos2.topLeftCorner());
		assertEquals(16, testPos16.topLeftCorner());
		assertEquals(4, testPos4.topLeftCorner());		
		assertEquals(20, testPos20.topLeftCorner());
		assertEquals(22, testPos22.topLeftCorner());
	}
	
	void testTopLeftCornerMinus() {
		Position testPos24 = new Position(new byte[] { (byte) 0xd0, (byte) 0x10, (byte) 0x10, (byte) 0, (byte) 0x1e, (byte) 0x38 });
        // 11010000, 00010000, 00010000, 0, 00011110, 00111000
		Position testPos26 = new Position(new byte[] { (byte) 0x31, (byte) 0x10, (byte) 0x10, (byte) 0, (byte) 0x1e, (byte) 0x38 });
        // 00110001, 00010000, 00010000, 0, 00011110, 00111000
		Position testPos40 = new Position(new byte[] { (byte) 0x10, (byte) 0x10, (byte) 0xd0, (byte) 0, (byte) 0x1e, (byte) 0x38 });
        // 00010000, 00010000, 11010000, , 0, 00011110, 00111000
		Position testPos28 = new Position(new byte[] { (byte) 0x55, (byte) 0x04, (byte) 0x55, (byte) 0x38, (byte) 0x1e, (byte) 0 });
        // 01010101, 00000100, 01010101, 00111000, 00011110, 0 
		Position testPos44 = new Position(new byte[] { (byte) 0x55, (byte) 0x04, (byte) 0x55, (byte) 0, (byte) 0x1e, (byte) 0x38 });
        // 01010101, 00000100, 01010101, 0, 00011110, 00111000
		Position testPos46 = new Position(new byte[] { (byte) 0x55, (byte) 0xaa, (byte) 0x55, (byte) 0, (byte) 0x83, (byte) 0x02 });
        // 01010101, 10101010, 01010101, 0, 10000011, 0000 0010
		assertEquals(24, testPos24.topLeftCorner());
		assertEquals(26, testPos26.topLeftCorner());
		assertEquals(40, testPos40.topLeftCorner());
		assertEquals(28, testPos28.topLeftCorner());		
		assertEquals(44, testPos44.topLeftCorner());
		assertEquals(46, testPos46.topLeftCorner());
	}
	
	@Test
	void testTopLeftCornerNoCollision() {
		Position testPos16 = new Position(new byte[] { (byte) 0x08, (byte) 0x08, (byte) 0x0b, (byte) 0, (byte) 0x70, (byte) 0x14 });
        // 00001000, 00001000, 00001011, 0, 01110000, 00010100
		Position testPos4 = new Position(new byte[] { (byte) 0xaa, (byte) 0x20, (byte) 0xaa, (byte) 0x14, (byte) 0x58, (byte) 0 });
        // 10101010, 00100000, 10101010, 00010100, 01011000, 0 
		Position testPos20 = new Position(new byte[] { (byte) 0xaa, (byte) 0x20, (byte) 0xaa, (byte) 0, (byte) 0x58, (byte) 0x14 });
        // 10101010, 0010 0000, 1010 1010, 0, 0101 1000, 0001 0100
		Position testPos22 = new Position(new byte[] { (byte) 0xaa, (byte) 0x55, (byte) 0xaa, (byte) 0, (byte) 0x82, (byte) 0x40 });
        // 10101010, 01010101, 10101010, 0, 10000010, 0100 0000
		Position testPos26 = new Position(new byte[] { (byte) 0xaa, (byte) 0x02, (byte) 0xaa, (byte) 0x14, (byte) 0x58, (byte) 0 });
        // 10101010, 00000010, 10101010, 00010100, 01011000, 0 
		Position testPos18 = new Position(new byte[] { (byte) 0x55, (byte) 0x04, (byte) 0x55, (byte) 0, (byte) 0x1a, (byte) 0x28 });
        // 01010101, 00000100, 01010101, 0, 00011010, 00101000
		Position testPos42 = new Position(new byte[] { (byte) 0x55, (byte) 0x55, (byte) 0x55, (byte) 0, (byte) 0x82, (byte) 0x02 });
        // 01010101, 01010101, 01010101, 0, 10000010, 0000 0010
		assertEquals(16, testPos16.topLeftCorner());
		assertEquals(4, testPos4.topLeftCorner());
		assertEquals(20, testPos20.topLeftCorner());
		assertEquals(22, testPos22.topLeftCorner());
		assertEquals(26, testPos26.topLeftCorner());
		assertEquals(18, testPos18.topLeftCorner());
		assertEquals(42, testPos42.topLeftCorner());
	}

}
