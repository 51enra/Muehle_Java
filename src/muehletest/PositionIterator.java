package muehletest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PositionIterator {
	private int nrPlayerPieces;
	private int nrOpponentPieces;

	public static final int FIELDNR = 24;
	private final int fieldNrExcludingInner = 16;
	// Distinguishable fields for 1st piece considering board symmetries
	private final int[] firstPlayerPieceIndex = { 0, 1, 8, 9 };

	// Internal variables to keep track of iterating through positions;
	// One array element for each pieces.
	private int[] playerPieceIndex;
	private int firstPlayerPieceStepCounter;
	private List<Integer> playerPieceSymmetries;
	private int[] opponentPieceIndex;
	private int[] opponentPieceStepCounter;

	public void init(int nrPlayerPieces, int nrOpponentPieces) {
		if (nrPlayerPieces < 3) {
			nrPlayerPieces = 3;
			System.out.println("Minimum of 3 pieces allowed; set nrPlayerPieces to 3.");
		}
		if (nrOpponentPieces < 3) {
			nrOpponentPieces = 3;
			System.out.println("Minimum of 3 pieces allowed; set nrOpponentPieces to 3.");
		}
		this.nrPlayerPieces = nrPlayerPieces;
		this.nrOpponentPieces = nrOpponentPieces;

		this.playerPieceIndex = new int[nrPlayerPieces];
		this.opponentPieceIndex = new int[nrOpponentPieces];
		this.opponentPieceStepCounter = new int[nrOpponentPieces];
		// Initialize playerPieceIndex to 'Reset' position
		this.firstPlayerPieceStepCounter = -1;
		for (int i = 1; i < nrPlayerPieces; i++) {
			this.playerPieceIndex[i] = FIELDNR - nrPlayerPieces + i;
		}
		// Initialize opponentPieceStepCounter to 'Reset' position
		this.opponentPieceStepCounter[0] = -1;
		for (int i = 1; i < nrOpponentPieces; i++) {
			this.opponentPieceStepCounter[i] = FIELDNR - nrPlayerPieces - nrOpponentPieces + i;
		}
	}

	public Position getNextPlayerPosition() {

		int i = nrPlayerPieces - 1;
		while (i > 0 && playerPieceIndex[i] >= fieldNrExcludingInner - nrPlayerPieces + i) {
			if (playerPieceIndex[0] <= 1 && i > nrPlayerPieces / 2
					&& playerPieceIndex[i] < FIELDNR - nrPlayerPieces + i) {
				// Inner ring fields will produce redundant positions due to inner-outer
				// symmetries except if i fulfills above conditions
				break;
			} else {
				i--;
			}
		}

		if (i == 0) {
			if (firstPlayerPieceStepCounter < firstPlayerPieceIndex.length - 1) {
				// build new position for all pieces; all pieces on subsequent fields
				firstPlayerPieceStepCounter += 1;
				playerPieceIndex[0] = firstPlayerPieceIndex[firstPlayerPieceStepCounter];
			} else { // No further steps possible - 'Reset' position reached
				firstPlayerPieceStepCounter = -1;
				return null;
			}
		} else {
			playerPieceIndex[i] += 1;
		}

		for (int j = i + 1; j < nrPlayerPieces; j++) {
			playerPieceIndex[j] = playerPieceIndex[j - 1] + 1;
		}

		playerPieceSymmetries = symmetries();

		if (playerPieceSymmetries != null) {
			return new Position(this.playerPieceIndex, null);
		} else {
			return getNextPlayerPosition();
		}
	}

	public List<Position> findLosingOpponentPositions(Set<Integer> millClosingFields, int requiredOptions) {
		List<Position> losingOpponentPosition = new ArrayList<>();
		int[] freeFieldArray = new int[FIELDNR - nrPlayerPieces];
		int i = 0;
		for (int j = 0; j < freeFieldArray.length; j++) {
			while ((i < nrPlayerPieces) && (j + i == playerPieceIndex[i])) {
				i += 1;
			}
			freeFieldArray[j] = j + i;
		}
		// Reset opponent position
		for (int j = 0; j < nrOpponentPieces; j++) {
			opponentPieceStepCounter[j] = j;
			opponentPieceIndex[j] = freeFieldArray[j];
		}
		opponentPieceStepCounter[nrOpponentPieces - 1] -= 1;

		Position position = getNextPosition(freeFieldArray);
		while (position != null) {
			if (position.getNrOpenMills(millClosingFields) >= requiredOptions) {
				losingOpponentPosition.add(position);
			}
			position = getNextPosition(freeFieldArray);
		}
		return losingOpponentPosition;
	}

	public Position getNextPosition(int[] freeFieldArray) {

		int i = nrOpponentPieces - 1;
		while ((i >= 0) && (opponentPieceStepCounter[i] == FIELDNR - nrPlayerPieces - nrOpponentPieces + i)) {
			i--;
		}
		if (i == -1) {
			return null;
		}

		opponentPieceStepCounter[i] += 1;
		opponentPieceIndex[i] = freeFieldArray[opponentPieceStepCounter[i]];
		for (int j = i + 1; j < nrOpponentPieces; j++) {
			opponentPieceStepCounter[j] = opponentPieceStepCounter[j - 1] + 1;
			opponentPieceIndex[j] = freeFieldArray[opponentPieceStepCounter[j]];
		}

		if (checkSymmetries(playerPieceSymmetries)) {
			return getNextPosition(freeFieldArray);
		} else {
			return new Position(this.playerPieceIndex, this.opponentPieceIndex);
		}
	}

	/**
	 * Provides a list of symmetries of a 9mm mover piece configuration, encoded as
	 * Integers. Returns null if a configuration symmetric to this appears earlier
	 * in the generation sequence.
	 * 
	 * NOTE: Does NOT consider inner-outer symmetry! This must be handled elsewhere.
	 * 
	 * @return 1=2nd diagonal, 2=90 deg rotation, 3=1o2, 4=2o2, 5=1o2o2, 6=2o2o2,
	 *         7=1o2o2o2
	 */

	public List<Integer> symmetries() {

		List<Integer> foundSymmetries = new ArrayList<>();
		int bitRepresentationBase = 0;
		for (int i = 0; i < this.nrPlayerPieces; i++) {
			bitRepresentationBase += 1 << (FIELDNR - playerPieceIndex[i] - 1);
		}
		int bitRepresentationMirrored = mirrorSecondDiagonal(bitRepresentationBase);

		if (bitRepresentationMirrored > bitRepresentationBase) {
			return null;
		}
		if (bitRepresentationMirrored == bitRepresentationBase) {
			foundSymmetries.add(1);
		}
		int bitRepresentation = bitRepresentationBase;
		for (int i = 1; i < 4; i++) {
			bitRepresentation = rotate90Degrees(bitRepresentation);
			bitRepresentationMirrored = rotate90Degrees(bitRepresentationMirrored);

			if (bitRepresentation > bitRepresentationBase) {
				return null;
			}
			if (bitRepresentation == bitRepresentationBase) {
				foundSymmetries.add(2 * i);
			}
			if (bitRepresentationMirrored > bitRepresentationBase) {
				return null;
			}
			if (bitRepresentationMirrored == bitRepresentationBase) {
				foundSymmetries.add(2 * i + 1);
			}
		}

		return foundSymmetries;
	}

	/**
	 * Determines the generation order between 9mm waiter piece configurations under
	 * given symmetry transformations. A false value indicates that no symmetric
	 * configuration appeared earlier in the generation sequence.
	 * 
	 * @param symmetries a list of Integer encoded symmetries (1=2nd diagonal, 2=90
	 *                   deg rotation, 3=1o2, 4=2o2, 5=1o2o2, 6=2o2o2, 7=1o2o2o2)
	 * @return true if the configuration is symmetric to an earlier generated
	 *         configuration, false otherwise
	 */

	public boolean checkSymmetries(List<Integer> symmetries) {

		int bitRepresentationBase = 0;
		for (int i = 0; i < this.nrOpponentPieces; i++) {
			bitRepresentationBase |= 1 << (FIELDNR - opponentPieceIndex[i] - 1);
		}

		for (int i = 0; i < symmetries.size(); i++) {

			int bitRepresentationTransformed = bitRepresentationBase;
			if ((symmetries.get(i) & 1) > 0) {
				bitRepresentationTransformed = mirrorSecondDiagonal(bitRepresentationBase);
			}
			int rotate = symmetries.get(i) / 2;
			while (rotate > 0) {
				bitRepresentationTransformed = rotate90Degrees(bitRepresentationTransformed);
				rotate -= 1;
			}
			if (bitRepresentationTransformed > bitRepresentationBase) {
				return true;
			}
		}
		return false;
	}

	public int mirrorSecondDiagonal(int bitRepresentation) {

		int bitRepresentationMirrored = 0;
		for (int i = 0; i < FIELDNR / 8; i++) {
			int oneByte = (bitRepresentation >>> (i * 8)) & 0xff;
			int oneByteMirrored = 0;
			for (int j = 0; j < 7; j++) {
				oneByteMirrored |= ((oneByte >>> j) & 0x01) << (6 - j);
			}
			oneByteMirrored |= oneByte & 0x80;
			bitRepresentationMirrored |= oneByteMirrored << (i * 8);
		}
		return bitRepresentationMirrored;
	}

	public int rotate90Degrees(int bitRepresentation) {
		int bitRepresentationRotated = 0;
		for (int i = 0; i < FIELDNR / 8; i++) {
			int oneByte = (bitRepresentation >>> (i * 8)) & 0xff;
			int oneByteRotated = (oneByte >>> 2) | (oneByte << 6) & 0xff;
			bitRepresentationRotated |= oneByteRotated << (i * 8);
		}
		return bitRepresentationRotated;
	}

}
