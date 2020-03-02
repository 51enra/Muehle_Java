package muehletest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PositionGenerator {

	private List<Position> positions;

	private long positionCount;

	private final int nrMoverPieces;
	private final int nrWaiterPieces;

	private final int fieldNr = 24;
	private final int fieldNrExcludingInner = 16;

	// Initial indexes of fields to be occupied for combinations (3-3), (3-4), ...
//	private final int[][] moverStartField = {
//			{1,2,3,0,0,0,0,0,0},
//			{1,2,3,0,0,0,0,0,0}		
//	};
//	
//	private final int[][] waiterStartField = {
//			{4,5,6,0,0,0,0,0,0},
//			{4,5,6,7,0,0,0,0,0}		
//	};

	// Long positionCount : Anzahl der untersuchten Positionen
	// HashMap redundantPosition ~ basePosition --> save to file; Positionen, die
	// sich als redundant herausgestellt haben.

	// Internal variables to keep track of iterating through positions;
	// One array element for each pieces.

	private Position workingPosition;

	private int[] moverPieceIndex;
	private int[] waiterPieceIndex;

	public PositionGenerator(int nrMoverPieces, int nrWaiterPieces) {
		if (nrMoverPieces < 3) {
			nrMoverPieces = 3;
			System.out.println("Minimum of 3 pieces allowed; set nrMoverPieces to 3.");
		}
		if (nrWaiterPieces < 3) {
			nrWaiterPieces = 3;
			System.out.println("Minimum of 3 pieces allowed; set nrWaiterPieces to 3.");
		}
		this.nrMoverPieces = nrMoverPieces;
		this.nrWaiterPieces = nrWaiterPieces;

		this.moverPieceIndex = new int[nrMoverPieces];
		this.waiterPieceIndex = new int[nrWaiterPieces];

		// this.positions = new HashSet<Position>();
		this.positions = new ArrayList<>();
	}

	public List<Position> generateAllDifferent() {

		int[] firstMoverPieceIndex = { 0, 1, 8, 9 };
		for (int i = 0; i < firstMoverPieceIndex.length; i++) {

			this.moverPieceIndex[0] = firstMoverPieceIndex[i];
			this.stepMoverPieceConsideringSymmetries(1);
		}
		return this.positions;
	}

	public void stepMoverPieceConsideringSymmetries(int pieceNr) {

		int fieldStepperLimit = this.fieldNrExcludingInner;
		// Higher fields will produce redundant positions for some pieces due to
		// inner-outer symmetry
		if (this.moverPieceIndex[0] <= 1 && pieceNr >= this.getNrMoverPieces() / 2) {
			fieldStepperLimit = this.fieldNr;
		}

		for (int i = this.moverPieceIndex[pieceNr - 1] + 1; i < fieldStepperLimit; i++) {

			this.moverPieceIndex[pieceNr] = i;
			if (pieceNr < this.getNrMoverPieces() - 1) {
				stepMoverPieceConsideringSymmetries(pieceNr + 1);
			} else {
				List<Integer> symmetries = symmetries();
				if (symmetries != null) {

					int[] freeFieldArray = new int[fieldNr - this.nrMoverPieces];
					int pos = 0;
					int k = 0;
					for (int j = 0; j < freeFieldArray.length; j++) {
						while (k < this.nrMoverPieces && pos == this.moverPieceIndex[k]) {
							pos += 1;
							k += 1;
						}
						freeFieldArray[j] = pos;
						pos +=1;
					}
//					doPositionAnalysis();
					stepWaiterPieceConsideringSymmetries(0, symmetries, freeFieldArray);
				}
			}
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
		for (int i = 0; i < this.nrMoverPieces; i++) {
			bitRepresentationBase += 1 << (this.fieldNr - this.moverPieceIndex[i] - 1);
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
		for (int i = 0; i < this.nrWaiterPieces; i++) {
			bitRepresentationBase |= 1 << (this.fieldNr - this.waiterPieceIndex[i] - 1);
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
		for (int i = 0; i < this.fieldNr / 8; i++) {
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
		for (int i = 0; i < this.fieldNr / 8; i++) {
			int oneByte = (bitRepresentation >>> (i * 8)) & 0xff;
			int oneByteRotated = (oneByte >>> 2) | (oneByte << 6) & 0xff;
			bitRepresentationRotated |= oneByteRotated << (i * 8);
		}
		return bitRepresentationRotated;
	}

	public void stepWaiterPieceConsideringSymmetries(int pieceNr, List<Integer> symmetries, int[] freeFieldArray) {

		for (int i = 0; i < freeFieldArray.length; i++) {

			this.waiterPieceIndex[pieceNr] = freeFieldArray[i];

			if (pieceNr < this.getNrWaiterPieces() - 1) {

				int[] nextPieceFreeFieldArray = new int[freeFieldArray.length - i - 1];
				for (int j = 0; j < nextPieceFreeFieldArray.length; j++) {
					nextPieceFreeFieldArray[j] = freeFieldArray[i + j + 1];
				}
				stepWaiterPieceConsideringSymmetries(pieceNr + 1, symmetries, nextPieceFreeFieldArray);
			} else {

				if (!checkSymmetries(symmetries)) {
//					// Ignore this position and continue on first field of next inner ring
//					int jumpForwardIndex = (freeFieldArray[i] / 8 + 1) * 8;
//					do {
//						i++;
//					} while ((i < freeFieldArray.length - 1) && (freeFieldArray[i] < jumpForwardIndex));
//					i--;
//				} else {
					doPositionAnalysis();
				}
			}
		}
	}

	public List<Position> generateAll() {
		this.workingPosition = Position.EMPTY_BOARD.clone();
		for (int i = 0; i < this.nrMoverPieces; i++) {
			this.moverPieceIndex[i] = i;
			this.workingPosition.placeMoverPiece(this.moverPieceIndex[i]);
		}
		this.generateAllWaiterPiecePositions();
		this.stepMoverPieceThroughFields(0);
		return this.positions;
	}

	public void stepMoverPieceThroughFields(int pieceNr) {

		int currentField = this.moverPieceIndex[pieceNr];
		int nextField = currentField + 1;
		do {
			if (nextField <= this.workingPosition.findLastFreeField()) {
				// If next field blocked by another mover piece, move that first.
				if (this.workingPosition.findNextFreeField(this.moverPieceIndex[pieceNr]) != nextField) {
					if (pieceNr == this.nrMoverPieces - 1) {
						throw new IllegalArgumentException(
								"StepMoverPieceThroughFields: Invalid pieceNr " + pieceNr + 1 + "!");
					}
					stepMoverPieceThroughFields(pieceNr + 1);
				}
				this.workingPosition.removeMoverPiece(this.moverPieceIndex[pieceNr]);
				this.workingPosition.placeMoverPiece(nextField);
				this.moverPieceIndex[pieceNr] = nextField;
				generateAllWaiterPiecePositions();
				nextField += 1;
			}
		} while (nextField <= this.workingPosition.findLastFreeField());

		// Go back to original position + 1
		if (currentField < this.workingPosition.findLastFreeField()) {
			this.workingPosition.removeMoverPiece(this.moverPieceIndex[pieceNr]);
			this.workingPosition.placeMoverPiece(currentField + 1);
			this.moverPieceIndex[pieceNr] = currentField + 1;
			int i = 2;
			while (pieceNr < this.nrMoverPieces - 1) {
				pieceNr += 1;
				this.workingPosition.removeMoverPiece(this.moverPieceIndex[pieceNr]);
				this.workingPosition.placeMoverPiece(currentField + i);
				this.moverPieceIndex[pieceNr] = currentField + i;
				i += 1;
			}
		}
	}

	private void generateAllWaiterPiecePositions() {

		int freePos = 0;
		for (int i = 0; i < this.nrWaiterPieces; i++) {
			freePos = this.workingPosition.findNextFreeField(freePos);
			this.workingPosition.placeWaiterPiece(freePos);
			this.waiterPieceIndex[i] = freePos;
		}
		this.doPositionAnalysis();
		stepWaiterPieceThroughFields(0);

		for (int i = 0; i < this.nrWaiterPieces; i++) {
			this.workingPosition.removeWaiterPiece(this.waiterPieceIndex[i]);
			this.waiterPieceIndex[i] = 24;
		}
	}

	public void stepWaiterPieceThroughFields(int pieceNr) {

		int currentField = this.waiterPieceIndex[pieceNr];
		int nextField = currentField + 1;
		do {
			// Check if nextField is blocked by mover piece; then skip it
			for (int i = 0; i < this.nrMoverPieces; i++) {
				if (nextField == this.moverPieceIndex[i]) {
					nextField += 1;
				}
			}
			if (nextField <= this.workingPosition.findLastFreeField()) {

				// If next field blocked by another waiter piece, move that first.
				if (this.workingPosition.findNextFreeField(this.waiterPieceIndex[pieceNr]) != nextField) {
					if (pieceNr == this.nrWaiterPieces - 1) {
						throw new IllegalArgumentException(
								"StepWaiterPieceThroughFields: Invalid pieceNr " + pieceNr + 1 + "!");
					}
					stepWaiterPieceThroughFields(pieceNr + 1);
				}

				this.workingPosition.removeWaiterPiece(this.waiterPieceIndex[pieceNr]);
				this.workingPosition.placeWaiterPiece(nextField);
				this.waiterPieceIndex[pieceNr] = nextField;

				doPositionAnalysis();
				nextField += 1;
			}
		} while (nextField <= this.workingPosition.findLastFreeField());

		// If there is a free field after the original position, go back there
		currentField += 1;
		int i = 0;
		// Check if currentField is blocked by mover piece; then skip it
		while ((i < this.nrMoverPieces) && (currentField >= this.moverPieceIndex[i])) {
			if (currentField == this.moverPieceIndex[i]) {
				currentField += 1;
			}
			i += 1;
		}

		if (currentField < this.waiterPieceIndex[pieceNr]) {

			this.workingPosition.removeWaiterPiece(this.waiterPieceIndex[pieceNr]);
			this.workingPosition.placeWaiterPiece(currentField);
			this.waiterPieceIndex[pieceNr] = currentField;
			// move subsequent waiter pieces to subsequent free fields
			while (pieceNr < this.nrWaiterPieces - 1) {
				pieceNr += 1;
				currentField += 1;
				// Check if currentField is blocked by mover piece; then skip it
				while ((i < this.nrMoverPieces) && (currentField >= this.moverPieceIndex[i])) {
					if (currentField == this.moverPieceIndex[i]) {
						currentField += 1;
					}
					i += 1;
				}

				this.workingPosition.removeWaiterPiece(this.waiterPieceIndex[pieceNr]);
				this.workingPosition.placeWaiterPiece(currentField);
				this.waiterPieceIndex[pieceNr] = currentField;
			}
		}
	}

	private void doPositionAnalysis() {
		byte[] encoding = new byte[6];
		int moverBits = 0;
		int waiterBits = 0;
		for (int i = 0; i < this.nrMoverPieces; i++) {
			moverBits |= 1 << this.moverPieceIndex[i];
		}

		for (int i = 0; i < this.nrWaiterPieces; i++) {
			waiterBits |= 1 << this.waiterPieceIndex[i];
		}
		for (int i = 0; i < 3; i++) {
			encoding[i] = (byte) (moverBits & 0xff);
			moverBits >>>= 8;
			encoding[i + 3] = (byte) (waiterBits & 0xff);
			waiterBits >>>= 8;
		}
//		Position position = new Position(encoding);
//		this.positions.add(position);
//		this.positions.add(this.workingPosition.clone());
		this.positionCount += 1;

	}

	public Set<Position> generatePermutations(Position position) {
		Set<Position> permutationSet = new HashSet<>();
//		permutationSet.add(new Position(position.getEncoding()));
		permutationSet.add(position);
		permutationSet.add(position.flipInnerOuter());
		permutationSet.add(position.mirrorSecondDiagonal());
		permutationSet.add(position.rotateRightBy90Deg());
		permutationSet.add(position.rotateRightBy180Deg());
		permutationSet.add(position.rotateRightBy270Deg());
		permutationSet.add(position.flipInnerOuter().mirrorSecondDiagonal());
		permutationSet.add(position.flipInnerOuter().rotateRightBy90Deg());
		permutationSet.add(position.flipInnerOuter().rotateRightBy180Deg());
		permutationSet.add(position.flipInnerOuter().rotateRightBy270Deg());
		permutationSet.add(position.mirrorSecondDiagonal().rotateRightBy90Deg());
		permutationSet.add(position.mirrorSecondDiagonal().rotateRightBy180Deg());
		permutationSet.add(position.mirrorSecondDiagonal().rotateRightBy270Deg());
		permutationSet.add(position.flipInnerOuter().mirrorSecondDiagonal().rotateRightBy90Deg());
		permutationSet.add(position.flipInnerOuter().mirrorSecondDiagonal().rotateRightBy180Deg());
		permutationSet.add(position.flipInnerOuter().mirrorSecondDiagonal().rotateRightBy270Deg());
		return permutationSet;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public long getPositionCount() {
		return positionCount;
	}

	public int getNrMoverPieces() {
		return nrMoverPieces;
	}

	public int getNrWaiterPieces() {
		return nrWaiterPieces;
	}

	public int[] getMoverPieceIndex() {
		return moverPieceIndex;
	}

	public int[] getWaiterPieceIndex() {
		return waiterPieceIndex;
	}

	public void setMoverPieceIndex(int[] moverPieceIndex) {
		if (moverPieceIndex.length != this.nrMoverPieces) {
			throw new IllegalArgumentException("SetMoverPieceIndex: Wrong array length!");
		}
		this.moverPieceIndex = moverPieceIndex;
	}

	public void setWaiterPieceIndex(int[] waiterPieceIndex) {
		if (waiterPieceIndex.length != this.nrWaiterPieces) {
			throw new IllegalArgumentException("SetWaiterPieceIndex: Wrong array length!");
		}
		this.waiterPieceIndex = waiterPieceIndex;
	}

	// Iterationsstrategie:
	// Generiere Initialposition; der Reihe nach erst alle Mover Steine, dann alle
	// Waiter Steine einsetzen
	// Der Bit-Sequenz in Position folgend (Außen nach innen, von links oben im
	// Uhrzeigersinn).
	// Den am weitesten "rechts" in der Bitsequenz liegenden (Waiter-) Stein durch
	// die verbleibenden freien Felder schieben
	// Wenn alle Felder durchlaufen sind, den vorletzten Stein eins nach rechts
	// bewegen
	// Ab dahinter wieder den letzten Stein durch alle verbleibenden freien Felder
	// schieben
	// usw. bis alle Waiter Möglichkeiten durchlaufen wurden
	// Danach wird der hinterste Mover Stein bewegt.
	// Die Waiter Steine werden auf den nun freien Feldern von links nach rechts neu
	// verteilt.
	// Dann beginnt die nächste Waiter-Permutations-Sequenz
	// Usw. abwechselnd nächste Mover-Position, dann Waiter-Sequenz durchlaufen.
	// Bei jeder Stellung jeweils alle 16 Symmetrie-Positionen ermitteln und in ein
	// Hash-Set eintragen
	// (Dabei wird nicht immer jede der 16 Positionen einen neuen Eintrag im
	// Hash-Set erzeugen).
	// Bei jeder Stellung vor weiterer Bearbeitung abfragen, ob sie bereits im
	// Hash-Set enthalten ist.
	// Spezialfall 3 / 3 Pieces: wenn die 3 waiter pieces eine Mühle bilden, hatte
	// der Mover zuvor 4 Steine.
	// (Bei mehr als 3 Waiter pieces kommt es darauf an, wann eine Mühle geschlossen
	// wurde)
	// Spezialfall 3 / 3 Pieces: Stellungen mit offener Mover-Mühle = Mover gewinnt
	// Spezialfall 3 / 3 Pieces: Stellungen mit 2 offenen Waiter-Mühlen = Waiter
	// gewinnt
	// Generell Mover verliert (Gasser):
	// - Wenn er sich in der Position nicht bewegen kann (blockiert ist - geht nicht
	// bei nur 3 Steinen?);
	// - Wenn er nur 3 Steine hat und
	// - - zwei offene gegnerische Mühlen nicht mit einem Stein schließen kann
	// - - oder die Mühlen einen gemeinsamen Stein haben und er keine eigene Mühle
	// schließen kann, um den Stein zu entfernen
	// - - oder er einen Stein aus einer blockierten gegnerischen Mühle entfernt
	// (das müsste bei Predecessor-Analyse auffallen)

}
