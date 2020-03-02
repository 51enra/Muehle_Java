package muehletest;

/**
 * Encodes a 9 men's morris map of black and white pieces into an array of 6
 * bytes (first 3 for mover, second 3 for waiter). b1: outer ring, b2: middle
 * ring, b3: inner ring. Bit positions on each ring clockwise, starting upper
 * left corner. Bit value 1 = piece set; 0 = field empty.
 * 
 * @author sea1
 *
 */
public class Position {

	private byte[] encoding;

	// Position encoding per byte:
	// 0 1 2
	// 7// 3
	// 6 5 4

	public static final Position EMPTY_BOARD = new Position(
			new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 });

	public static final Position[] SINGLE_PIECE_MOVER = { //
			new Position(new byte[] { (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 16, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 64, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 128, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 8, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 16, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 64, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 128, (byte) 0, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 4, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 8, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 16, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 32, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 64, (byte) 0, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 128, (byte) 0, (byte) 0, (byte) 0 }), //
	};

	public static final Position[] SINGLE_PIECE_WAITER = { //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 8, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 16, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 32, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 64, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 128, (byte) 0, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 8, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 16, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 32, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 64, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 128, (byte) 0 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 2 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 4 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 8 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 16 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 32 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 64 }), //
			new Position(new byte[] { (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 128 }), //
	};

	public Position(byte[] encoding) {
		this.encoding = encoding;
	}

	public byte[] getEncoding() {
		return encoding;
	}

	public void setEncoding(byte[] encoding) {
		this.encoding = encoding;
	}

	public void placeMoverPiece(int index) {
		// No check if field is empty! Will be set by OR!
		if (index < 0 || index > 23) {
			throw new IllegalArgumentException("findNextFree: Invalid startIndex " + index + "!");
		}
		byte[] p1 = this.getEncoding();
		byte[] p2 = SINGLE_PIECE_MOVER[index].getEncoding();
		int byteCount = index / 8;
		p1[byteCount] |= p2[byteCount];
	}

	/**
	 * Places a 'waiter' piece on the 9mm board at the position indicated by index.
	 * 
	 * @param index the index of the field, counted clockwise, start top left, outer
	 *              to inner (0..23)
	 */

	public void placeWaiterPiece(int index) {
		// No check if field is empty! Will be set by OR!
		if (index < 0 || index > 23) {
			throw new IllegalArgumentException("findNextFree: Invalid startIndex " + index + "!");
		}
		byte[] p1 = this.getEncoding();
		byte[] p2 = SINGLE_PIECE_WAITER[index].getEncoding();
		int byteCount = index / 8 + 3;
		p1[byteCount] |= p2[byteCount];
	}

	/**
	 * Removes a 'mover' piece from the 9mm board at the position indicated by
	 * index.
	 * 
	 * @param index the index of the field, counted clockwise, start top left, outer
	 *              to inner (0..23)
	 */

	public void removeMoverPiece(int index) {
		// No check if field is occupied! Will be flipped by XOR!
		if (index < 0 || index > 23) {
			throw new IllegalArgumentException("findNextFree: Invalid startIndex!");
		}
		byte[] p1 = this.getEncoding();
		byte[] p2 = SINGLE_PIECE_MOVER[index].getEncoding();
		int byteCount = index / 8;
		p1[byteCount] ^= p2[byteCount];
	}

//	public int getMoverPieceIndex(int pieceNr) {
//		
//		if (pieceNr < 0) {
//			throw new IllegalArgumentException("getMoverPieceIndex: Negative pieceNr!");
//		}
//
//		int index;
//		int integerRepresentation = this.encoding[0] + this.encoding[1] * 256 + this.encoding[2] * 65536;
//		for (index = 0; index < 24; index++) {
//			if ((integerRepresentation & 1) == 1) {
//				pieceNr -= 1;
//				integerRepresentation >>>= 1;
//			}
//			if (pieceNr < 0) {
//				break;
//			}
//		}
//
//		return index;
//	}
//	
//	public int getWaiterPieceIndex(int pieceNr) {
//		
//		if (pieceNr < 0) {
//			throw new IllegalArgumentException("getWaiterPieceIndex: Negative pieceNr!");
//		}
//
//		int index;
//		int integerRepresentation = this.encoding[3] + this.encoding[4] * 256 + this.encoding[5] * 65536;
//		for (index = 0; index < 24; index++) {
//			if ((integerRepresentation & 1) == 1) {
//				pieceNr -= 1;
//				integerRepresentation >>>= 1;
//			}
//			if (pieceNr < 0) {
//				break;
//			}
//		}
//
//		return index;
//	}

	/**
	 * Removes a 'waiter' piece from the 9mm board at the position indicated by
	 * index.
	 * 
	 * @param index the index of the field, counted clockwise, start top left, outer
	 *              to inner (0..23)
	 */

	public void removeWaiterPiece(int index) {
		// No check if field is occupied! Will be flipped by XOR!
		if (index < 0 || index > 23) {
			throw new IllegalArgumentException("findNextFree: Invalid startIndex!");
		}
		byte[] p1 = this.getEncoding();
		byte[] p2 = SINGLE_PIECE_WAITER[index].getEncoding();
		int byteCount = index / 8 + 3;
		p1[byteCount] ^= p2[byteCount];
	}

	public Position logicalOrMover(Position position) {
		byte[] p1 = this.getEncoding();
		byte[] p2 = position.getEncoding();
		return new Position(new byte[] { (byte) (p1[0] | p2[0]), (byte) (p1[1] | p2[1]), (byte) (p1[2] | p2[2]) });
	}

	public Position logicalOrWaiter(Position position) {
		byte[] p1 = this.getEncoding();
		byte[] p2 = position.getEncoding();
		return new Position(new byte[] { (byte) (p1[3] | p2[3]), (byte) (p1[4] | p2[4]), (byte) (p1[5] | p2[5]) });
	}

	public Position logicalAndMover(Position position) {
		byte[] p1 = this.getEncoding();
		byte[] p2 = position.getEncoding();
		return new Position(new byte[] { (byte) (p1[0] & p2[0]), (byte) (p1[1] & p2[1]), (byte) (p1[2] & p2[2]) });
	}

	public Position logicalAndWaiter(Position position) {
		byte[] p1 = this.getEncoding();
		byte[] p2 = position.getEncoding();
		return new Position(new byte[] { (byte) (p1[3] & p2[3]), (byte) (p1[4] & p2[4]), (byte) (p1[5] & p2[5]) });
	}

	/**
	 * Finds the next free field on the 9mm board where neither a 'mover' nor a
	 * 'waiter' piece is placed
	 * 
	 * @param startIndex field where the search starts; counted clockwise, start top
	 *                   left, outer to inner (0..23)
	 */

	public int findNextFreeField(int startIndex) {
		if (startIndex > 23) {
			throw new IllegalArgumentException("findNextFree: Invalid startIndex!");
		}
		int byteCount = startIndex / 8;
		int bitCount = startIndex % 8;
		int bitFlag;
		do {
			bitFlag = 1 << bitCount;
			while (bitFlag != 0 && (this.getEncoding()[byteCount] & bitFlag) != 0
					|| (this.getEncoding()[byteCount + 3] & bitFlag) != 0) {
				bitFlag <<= 1;
				bitCount += 1;
			}
			if (bitFlag == 0) {
				bitCount = 0;
				byteCount += 1;
			}
		} while (bitFlag == 0 && byteCount < 3);
		if (bitFlag == 0) {
			return 24; // no free position found after startIndex
		}
		return byteCount * 8 + bitCount;
	}

	/**
	 * Finds the last free field on the 9mm board where neither a 'mover' nor a
	 * 'waiter' piece is placed.
	 * 
	 * @return the index (0..23) of the field; 24 if no free field found
	 */

	public int findLastFreeField() {
		int byteCount = 2;
		int bitFlag;
		do {
			bitFlag = 128;
			while (bitFlag != 0 && (this.getEncoding()[byteCount] & bitFlag) != 0
					|| (this.getEncoding()[byteCount + 3] & bitFlag) != 0) {
				bitFlag >>>= 1;
			}
			if (bitFlag == 0) {
				byteCount -= 1;
			}
		} while (bitFlag == 0 && byteCount >= 0);
		if (bitFlag == 0) {
			return 24; // no free position found
		}
		int bitCount = 0;
		do {
			bitCount += 1;
			bitFlag >>>= 1;
		} while (bitFlag > 0);

		return byteCount * 8 + bitCount;
	}
	

	/**
	 * Finds the last free field on the 9mm board where neither a 'mover' nor a
	 * 'waiter' piece is placed before a given field.
	 * 
	 * @return the index (0..23) of the field; 24 if no free field found
	 */

	public int findLastFreeFieldBefore(int index) {
		int byteCount = index/8;
		int bitCount = index % 8;
		int bitFlag;
		if (bitCount == 0) {
			bitFlag = 128;
			byteCount -=1;
			if (byteCount < 0) {
				return 24;
			}
		} else {
			bitFlag = 1<<(bitCount - 1);
		}
		do {
			while (bitFlag != 0 && (this.getEncoding()[byteCount] & bitFlag) != 0
					|| (this.getEncoding()[byteCount + 3] & bitFlag) != 0) {
				bitFlag >>>= 1;
			}
			if (bitFlag == 0) {
				byteCount -= 1;
			}
		} while (bitFlag == 0 && byteCount >= 0);
		if (bitFlag == 0) {
			return 24; // no free position found
		}
		bitCount = 0;
		while (bitFlag > 1) {
			bitCount += 1;
			bitFlag >>>= 1;
		}

		return byteCount * 8 + bitCount;
	}

	/**
	 * Provides an equivalent permutation to a given 9mm board position
	 * 
	 * @return a 9mm board position with pieces in the inner and outer ring swapped
	 */

	public Position flipInnerOuter() {

		byte[] flip = new byte[6];
		flip[0] = this.getEncoding()[2];
		flip[1] = this.getEncoding()[1];
		flip[2] = this.getEncoding()[0];
		flip[3] = this.getEncoding()[5];
		flip[4] = this.getEncoding()[4];
		flip[5] = this.getEncoding()[3];
		return new Position(flip);
	}

	/**
	 * Provides an equivalent permutation to a given 9mm board position
	 * 
	 * @return a 9mm board position with all pieces rotated clockwise by 90 degrees
	 */

	public Position rotateRightBy90Deg() {
		// Corresponds to clockwise 90 degree rotation
		byte[] targetEncoding = new byte[6];
		for (int i = 0; i < 6; i++) {
			targetEncoding[i] = rotateRightBy2(this.encoding[i]);
		}
		return new Position(targetEncoding);
	}

	private byte rotateRightBy2(byte b) {
		// logical right shift should set bit 7 to 0 but doesn't - check negative
		// integers!
		byte c = (byte) ((b << 2) | (b >>> 6 & 3));
		return c;
	}

	private byte rotateLeftBy1(byte b) {
		// logical right shift should set bit 7 to 0 but doesn't????
		byte c = (byte) ((b >>> 1 & 127) | (b << 7));
		return c;
	}

	public Position rotateRightBy180Deg() {
		// TODO Implement directly by bit shift (faster)
		return this.rotateRightBy90Deg().rotateRightBy90Deg();
	}

	public Position rotateRightBy270Deg() {
		// TODO Implement directly by bit shift (faster)
		return this.rotateRightBy90Deg().rotateRightBy90Deg().rotateRightBy90Deg();
	}

	/**
	 * Provides an equivalent permutation to a given 9mm board position
	 * 
	 * @return a 9mm board position with all pieces mirrored on the 2nd diagonal of
	 *         the board
	 */

	public Position mirrorSecondDiagonal() {
		byte[] targetEncoding = new byte[6];
		for (int i = 0; i < 6; i++) {
			targetEncoding[i] = mirrorSecondDiagonal(this.encoding[i]);
		}
		return new Position(targetEncoding);
	}

	public byte mirrorSecondDiagonal(byte b) {
		byte c = rotateLeftBy1(b);
		return reverse(c);
	}

	private byte reverse(byte b) {

		int c = 0;
		int bitSwitch = 1;
		int filter = 128;
		for (int i = 0; i < 8; i++) {
			if ((b & filter) != 0) {
				c = c | bitSwitch;
			}
			filter >>>= 1;
			bitSwitch <<= 1;
		}

		return (byte) c;
	}
	

	// printPos Example: (/ in the middle protects against ctrl-shift-f; should be
	// /space/ really).
	// W------+------B
	// | B----W----B |
	// | | +--+--W | |
	// +-+-B/////+-+ W
	// | | +--+--+ | |
	// | +----+----+ |
	// +------+------W

	public void printPos() {
		String[] stringRepresentation = this.getStringRepresentation();
		System.out.println();
		for (int i = 0; i < 7; i++) {
			System.out.println(stringRepresentation[i]);
		}
	}

	/**
	 * Create a string representation of the 9mm board for console printing.
	 * 
	 * @return a String array; each element represents one row (0-6) of the 9mm
	 *         board
	 */
	public String[] getStringRepresentation() {
		String[][] rowSymbols = new String[7][6];

		for (int row = 0; row < 3; row++) {
			rowSymbols[row] = getLinePiecesSymbols(this.encoding[row], this.encoding[row + 3]);
		}
		Position bottomRow = this.mirrorSecondDiagonal().rotateRightBy90Deg().rotateRightBy90Deg().rotateRightBy90Deg();
		for (int row = 4; row < 7; row++) {
			rowSymbols[row] = getLinePiecesSymbols(bottomRow.encoding[6 - row], bottomRow.encoding[6 - row + 3]);
		}
		// Middle row pieces are encoded by bits 7 and 4 of every encoding byte.
		byte middleRowWhite = 0;
		for (int i = 0; i < 3; i++) {
			if ((this.encoding[i] & 128) != 0) {
				middleRowWhite |= (1 << i);
			}
			if ((this.encoding[i] & 8) != 0) {
				middleRowWhite |= (32 >> i);
			}
		}
		byte middleRowBlack = 0;
		for (int i = 0; i < 3; i++) {
			if ((this.encoding[3 + i] & 128) != 0) {
				middleRowBlack |= (1 << i);
			}
			if ((this.encoding[3 + i] & 8) != 0) {
				middleRowBlack |= (32 >> i);
			}
		}
		rowSymbols[3] = getLinePiecesSymbols(middleRowWhite, middleRowBlack);

		String[] stringRepresentation = new String[7];
		stringRepresentation[0] = rowSymbols[0][0] + "------" + rowSymbols[0][1] + "------" + rowSymbols[0][2];
		stringRepresentation[1] = "| " + rowSymbols[1][0] + "----" + rowSymbols[1][1] + "----" + rowSymbols[1][2]
				+ " |";
		stringRepresentation[2] = "| | " + rowSymbols[2][0] + "--" + rowSymbols[2][1] + "--" + rowSymbols[2][2]
				+ " | |";
		stringRepresentation[3] = rowSymbols[3][0] + "-" + rowSymbols[3][1] + "-" + rowSymbols[3][2] + "     "
				+ rowSymbols[3][3] + "-" + rowSymbols[3][4] + "-" + rowSymbols[3][5];
		stringRepresentation[4] = "| | " + rowSymbols[4][0] + "--" + rowSymbols[4][1] + "--" + rowSymbols[4][2]
				+ " | |";
		stringRepresentation[5] = "| " + rowSymbols[5][0] + "----" + rowSymbols[5][1] + "----" + rowSymbols[5][2]
				+ " |";
		stringRepresentation[6] = rowSymbols[6][0] + "------" + rowSymbols[6][1] + "------" + rowSymbols[6][2];

		return stringRepresentation;
	}

	private String[] getLinePiecesSymbols(byte white, byte black) {
		String[] piecesSymbols = new String[6]; // max. 6 pieces per row (in middle row)
		int bitPos = 1;
		for (int i = 0; i < 6; i++) {
			if ((white & bitPos) != 0) {
				piecesSymbols[i] = "W";
				if ((black & bitPos) != 0) {
					System.out.println("Inconsistent position definition - double field occupancy!");
				}
			} else if ((black & bitPos) != 0) {
				piecesSymbols[i] = "B";
			} else {
				piecesSymbols[i] = "+";
			}
			bitPos <<= 1;
		}
		return piecesSymbols;
	}

	@Override
	public Position clone() {
		byte[] encoding = new byte[6];
		for (int i = 0; i < 6; i++) {
			encoding[i] = this.getEncoding()[i];
		}
		return new Position(encoding);
	}
	
	// does this make sense?
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		for (int i = 0; i < 6; i++) {
			result = prime * result + this.getEncoding()[i];
		}
		return result;
	}

	@Override
	public boolean equals(Object obj) {

		if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}
		Position pos = (Position) obj;
		for (int i = 0; i < 6; i++) {
			if (pos.getEncoding()[i] != this.getEncoding()[i])
				return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String str = "";
		for (int i = 0; i < 6; i++) {
			str += "encoding[" + i + "] = " + this.getEncoding()[i] + ", ";
		}
		return str;
	}

}
