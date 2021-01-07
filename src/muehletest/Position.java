package muehletest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	/**
	 * Generate position encoding from integer index arrays (value of [0,23] for
	 * each piece). Arrays don't need to be sorted by field index.
	 * 
	 * @param playerPieceIndex   Length of array = number of player pieces
	 * @param opponentPieceIndex Length of array = number of opponent pieces
	 */

	public Position(int[] playerPieceIndex, int[] opponentPieceIndex) {
		// No parameter range checks or bit collision checks done!!!!
		this.encoding = new byte[6];
		if (playerPieceIndex != null) {
			for (int i = 0; i < playerPieceIndex.length; i++) {
				int byteNr = playerPieceIndex[i] / 8;
				int bitNr = playerPieceIndex[i] % 8;
				this.encoding[byteNr] |= 1 << bitNr;
			}
		}
		if (opponentPieceIndex != null) {
			for (int i = 0; i < opponentPieceIndex.length; i++) {
				int byteNr = opponentPieceIndex[i] / 8;
				int bitNr = opponentPieceIndex[i] % 8;
				this.encoding[byteNr + 3] |= 1 << bitNr;
			}
		}
	}
	
	/**
	 * Generate position encoding from Lists of Integers (value of [0,23] for
	 * each piece). Lists don't need to be sorted by field index.
	 * 
	 * @param playerPieceIndex   Size of List = number of player pieces
	 * @param opponentPieceIndex Size of List = number of opponent pieces
	 */

	public Position(List<Integer> playerPieceIndex, List<Integer> opponentPieceIndex) {
		// No parameter range checks or bit collision checks done!!!!
		this.encoding = new byte[6];
		if (playerPieceIndex != null) {
			for (int field : playerPieceIndex) {
				int byteNr = field / 8;
				int bitNr = field % 8;
				this.encoding[byteNr] |= 1 << bitNr;
			}
		}
		if (opponentPieceIndex != null) {
			for (int field: opponentPieceIndex) {
				int byteNr = field / 8;
				int bitNr = field % 8;
				this.encoding[byteNr + 3] |= 1 << bitNr;
			}
		}
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
	 * Get indices of fields occupied by player pieces for a 9mm position encoded in bytes.
	 * 
	 * @return a List of Integers with the indices [0..23] of player pieces.
	 * The List is sorted by increasing index. 
	 */
	
	public List<Integer> getPlayerPieceIndices() {

		Position currentPosition = this.clone();
		List<Integer> playerPieceIndexList = new ArrayList<>();

		for (int i = 0; i < 24; i++) {
			int byteNr = i / 8;
			if ((currentPosition.encoding[byteNr] & 0x01) == 1) {
				playerPieceIndexList.add(i);
			}
			currentPosition.encoding[byteNr] = (byte) (currentPosition.encoding[byteNr] >>> 1);
		}
		return playerPieceIndexList;
	}
	
	/**
	 * Get indices of fields occupied by opponent pieces for a 9mm position encoded in bytes.
	 * 
	 * @return a List of Integers with the indices [0..23] of opponent pieces.
	 * The List is sorted by increasing index. 
	 */
	
	public List<Integer> getOpponentPieceIndices() {

		Position currentPosition = this.clone();
		List<Integer> opponentPieceIndexList = new ArrayList<>();

		for (int i = 0; i < 24; i++) {
			int byteNr = i / 8;
			if ((currentPosition.encoding[byteNr + 3] & 0x01) == 1) {
				opponentPieceIndexList.add(i);
			}
			currentPosition.encoding[byteNr + 3] = (byte) (currentPosition.encoding[byteNr + 3] >>> 1);
		}
		return opponentPieceIndexList;
	}
	
	/**
	 * Get all free field indices (fields not occupied by either a player or an opponent piece).
	 * 
	 * @return A List of Integer indices [0..23] of all free fields for the given position
	 */
	
	List<Integer> getFreeFields() {
		List<Integer> freeFields = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 8; j++) {
				if (((this.encoding[i]>>>j) & 0x01) == 0
						&& ((this.encoding[i+3]>>>j) & 0x01) == 0) {
					freeFields.add(i*8+j);
				}
			}
		}
		return freeFields;
	}
	
	
 /**
  * Get indices of free fields which can be reached in one draw from a given field index.
  * No check whether the given field is occupied by a player or opponent piece.
  * 
  * @param index The index [0..23] of the field for which free neighbors shall be found.
  * @return a List of Integer indices of free neighbor fields
  */
	
	List<Integer> getFreeNeighborFields(int index) {
        List<Integer> neighbors = new ArrayList<>();
        neighbors.add(index / 8 * 8 + (index + 1) % 8);
		neighbors.add(index / 8 * 8 + (index + 7) % 8);
		if (index % 2 > 0) {
			if (index < 16) {
				neighbors.add(index + 8);
			}
			if (index > 8) {
				neighbors.add(index - 8);
			}			
		}
		List<Integer> freeNeighbors = new ArrayList<Integer>();
		for (int neighbor : neighbors) {
			int byteNr = neighbor / 8;
			int bitNr = neighbor % 8;
			if (((this.encoding[byteNr]>>>bitNr) & 0x01) == 0
					&& ((this.encoding[byteNr+3]>>>bitNr) & 0x01) == 0) {
				freeNeighbors.add(neighbor);
			}
		}
		return freeNeighbors;
	}

	/**
	 * Calculate which free fields belong to an open mill (one field can be part of
	 * up to two mills)
	 * 
	 * @param party 'p' for player; 'o' (or any other character) for opponent
	 * @return the set of indices [0,23] of mill-closing fields for the requested
	 *         party.
	 */

	public Set<Integer> getMillClosingFields(char party) {
		int offset = 3;
		if (party == 'p') {
			offset = 0;
		}
		Set<Integer> millClosingFields = new HashSet<>();
		// Array for checking 'vertical' mill positions
		int[] columnChecker = new int[4];
		for (int i = 0; i < 3; i++) {

			int ring = this.encoding[i + offset] & 0xff;
			for (int j = 0; j < 4; j++) {
				int rowChecker = ring & 0x07;
				if (rowChecker == 6) {
					millClosingFields.add(i * 8 + j * 2);
				}
				if (rowChecker == 5) {
					millClosingFields.add(i * 8 + 2 * j + 1);
				}
				if (rowChecker == 3) {
					millClosingFields.add(i * 8 + (j + 1) * 2);
				}
				if ((rowChecker & 0x02) > 0) {
					columnChecker[j] = columnChecker[j] | (0x01 << i);
				}
				ring = ((ring >>> 2) | (ring << 6)) & 0xff;
			}
		}
		for (int j = 0; j < 4; j++) {
			if (columnChecker[j] == 6) {
				millClosingFields.add(2 * j + 1);
			}
			if (columnChecker[j] == 5) {
				millClosingFields.add(8 + 2 * j + 1);
			}
			if (columnChecker[j] == 3) {
				millClosingFields.add(16 + 2 * j + 1);
			}
		}
		// remove fields which are occupied by the other party
		Set<Integer> occupiedFields = new HashSet<>();
		for (Integer field : millClosingFields) {
			int byteNr = field / 8;
			int bitNr = field % 8;
			if ((this.encoding[3 + byteNr - offset] & (0x01 << bitNr)) != 0) {
              occupiedFields.add(field);
			}
		}
		millClosingFields.removeAll(occupiedFields);
		
		return millClosingFields;
	}

	/**
	 * Calculate the number of closed mills of either Mover or Waiter.
	 * 
	 * @param party 'm' for mover; 'w' (or any other character) for waiter
	 * @return the number closed mills for the requested party (int).
	 */

	public int getNumberOfMills(char party) {
		// TODO Needed to exclude positions from further analysis
		// e.g. one or more closed mills with more than 8 opponent pieces
		return 0;
	}

	/**
	 * Calculate which pieces belong to mills of either the mover or the waiter
	 * 
	 * @param party 'p' for player; 'o' (or any other character) for opponent
	 * @return a Set of indices of all fields (0..23) which belong to a mill in the
	 *         given position
	 */

	public List<Integer> getPiecesInMills(char party) {
		int offset = 3;
		if (party == 'p') {
			offset = 0;
		}
		List<Integer> millPieces = new ArrayList<>();
		// Array for checking 'vertical' mill positions
		int[] columnChecker = new int[4];
		for (int i = 0; i < 3; i++) {

			int ring = this.encoding[i + offset] & 0xff;
			for (int j = 0; j < 4; j++) {
				int startField = 2 * j + 8 * i;
				if ((ring & 0x07) == 7) {
					millPieces.addAll(Arrays.asList(startField, startField + 1));
					if (j < 3) {
						millPieces.add(startField + 2);
					} else { // last field on fourth side of board equals first field on first side}
						millPieces.add(startField - 6);
					}
				}
				if ((ring & 0x02) > 0) {
					columnChecker[j] = columnChecker[j] | (0x01 << i);
				}
				ring = ((ring >>> 2) | (ring << 6)) & 0xff;
			}
		}
		for (int j = 0; j < 4; j++) {
			int startField = 2 * j + 1;
			if ((columnChecker[j] & 0x07) == 7) {
				millPieces.addAll(Arrays.asList(startField, startField + 8, startField + 16));
			}
		}
		return millPieces;
	}

	/**
	 * For a set of indices indicating fields where the Player could close mills,
	 * calculate how many are not blocked by opponent pieces.
	 * 
	 * @return the number of free fields where the Player can close a mill.
	 */

	public int getNrOpenMills(Set<Integer> millClosingFields) {
		int nrOpenMills = millClosingFields.size();
		for (int fieldIndex : millClosingFields) {
			int byteNr = fieldIndex / 8;
			int bitNr = fieldIndex % 8;
			if ((this.encoding[byteNr + 3] & (1 << bitNr)) != 0) {
				nrOpenMills -= 1;
			}
		}
		return nrOpenMills;
	}

	/**
	 * Swap player and opponent pieces and transform the resulting position to
	 * "normal" form, meaning the variant created by PositionIterator if different
	 * symmetrical variations exist.
	 */

	public Position transpose() {
		// swap player and opponent pieces
		byte[] transEncoding = new byte[6];
		for (int i = 0; i < 3; i++) {
			transEncoding[i] = this.encoding[i + 3];
			transEncoding[i + 3] = this.encoding[i];
		}
		Position transPosition = new Position(transEncoding);
		// transform to "normal" form
		return transPosition.transform();
	}

	// Transform:
	// "Best corner" to be transformed to index position 0 out of [0,23]. Possibly
	// mirror on 2nd diagonal (+/-)
	// "Best corner" determined by: player pieces at as low as possible index
	// values;
	// in case of equal ranking of corners also consider opponent pieces.
	// Operations: RR90, RR180, RR270 (rotate right by x degs.); MSD (mirror 2nd
	// diagonal); IO (flip inner/outer)
	// 0+: no transf.; 0-: MSD / 2+: RR270; 2-: RR270+MSD / 4+: RR180; 4-: RR180+MSD
	// 6+: RR90; 6-: RR90+MSD / 16+: IO; 16-: IO+MSD / 18+: IO+RR270; 18-:
	// IO+RR270+MSD / etc. 20, 22

	public Position transform() {
		Position transPosition = this.clone();
		int corner = topLeftCorner();
		boolean mirror = false;
		if (corner > 23) {
			mirror = true;
			corner -= 24;
		}
		if (corner > 15) {
			transPosition = transPosition.flipInnerOuter();
			corner -= 16;
		}
		int lastCornerOnRing = 6;
		while (corner > 0 && corner <= lastCornerOnRing) {
			transPosition = transPosition.rotateRightBy90Deg();
			lastCornerOnRing -= 2;
		}
		if (mirror) {
			return transPosition.mirrorSecondDiagonal();
		} else {
			return transPosition;
		}
	}

	public int topLeftCorner() {

		// Check Player pieces in inner and outer ring
		List<Integer> bestCorner = new ArrayList<>();

		int[] ringByte = { this.encoding[0], this.encoding[2] };
		for (int i = 0; i < 8; i++) {
			// check corner fields
			for (int j = 0; j < 2; j++) {
				if ((i % 2 == 0) && (ringByte[j] & 0x01) == 1) {
					bestCorner.add(j * 16 + i);
				}
				ringByte[j] >>>= 1;
			}
		}
		if (bestCorner.size() == 0) { // No corner pieces - all corners equal
			for (int i = 0; i < 4; i++) {
				bestCorner.add(2 * i);
				bestCorner.add(16 + 2 * i);
			}
		}
		// Check direction by looking at middle fields
		List<Integer> bestCornerPlus = new ArrayList<>();
		List<Integer> bestCornerMinus = new ArrayList<>();
		// Find at least one corner+direction combination
		// If more than 1 equivalent corner + direction, try to eliminate by
		// going through rings and check match to bestCorners
		for (int corner : bestCorner) {
			bestCornerPlus.add(corner);
			bestCornerMinus.add(corner);
		}
		int ringOffset = 0;
		int shift = 1; // Corner itself has already been checked
		while (ringOffset < 3 && bestCornerPlus.size() + bestCornerMinus.size() > 1) {
			while (shift < 8 && bestCornerPlus.size() + bestCornerMinus.size() > 1) {
				List<Integer> cornersFoundPlus = checkPieceAtShiftOnRing(bestCornerPlus, shift, ringOffset, 'p');
				List<Integer> cornersFoundMinus = checkPieceAtShiftOnRing(bestCornerMinus, -shift, ringOffset, 'p');
				if (cornersFoundPlus.size() > 0 || cornersFoundMinus.size() > 0) {
					bestCornerPlus = cornersFoundPlus;
					bestCornerMinus = cornersFoundMinus;
				}
				shift += 1;
			}
			shift = 0;
			ringOffset += 1;
		}
		ringOffset = 0;
		shift = 0;
		while (ringOffset < 3 && bestCornerPlus.size() + bestCornerMinus.size() > 1) {
			while (shift < 8 && bestCornerPlus.size() + bestCornerMinus.size() > 1) {
				List<Integer> cornersFoundPlus = checkPieceAtShiftOnRing(bestCornerPlus, shift, ringOffset, 'o');
				List<Integer> cornersFoundMinus = checkPieceAtShiftOnRing(bestCornerMinus, -shift, ringOffset, 'o');
				if (cornersFoundPlus.size() > 0 || cornersFoundMinus.size() > 0) {
					bestCornerPlus = cornersFoundPlus;
					bestCornerMinus = cornersFoundMinus;
				}
				shift += 1;
			}
			shift = 0;
			ringOffset += 1;
		}
		if (bestCornerPlus.size() > 0) {
			return bestCornerPlus.get(0);
		} else {
			return bestCornerMinus.get(0) + 24;
		}
	}

	private List<Integer> checkPieceAtShiftOnRing(List<Integer> bestCorners, int shift, int ringOffset, char party) {
		// shift between 0 and +-7; offset between 0 (same ring) and 2 (inner--> outer /
		// outer--> inner)
		int encodingStart = 0;
		if (party == 'o') {
			encodingStart = 3;
		}
		List<Integer> cornersFound = new ArrayList<>();
		for (int corner : bestCorners) {
			int byteNr;
			if (corner < 8) {
				byteNr = corner / 8 + ringOffset + encodingStart;
			} else {
				byteNr = corner / 8 - ringOffset + encodingStart;
			}
			int ringByte = this.encoding[byteNr];
			if ((ringByte >>> ((corner + shift + 8) % 8) & 0x01) == 1) {
				cornersFound.add(corner);
			}
		}
		return cornersFound;
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
		int byteCount = index / 8;
		int bitCount = index % 8;
		int bitFlag;
		if (bitCount == 0) {
			bitFlag = 128;
			byteCount -= 1;
			if (byteCount < 0) {
				return 24;
			}
		} else {
			bitFlag = 1 << (bitCount - 1);
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
