package muehletest;

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
	private int[] opponentPieceIndex;
	private int firstPlayerPieceStepCounter;

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
		// Initialize playerPieceIndex to 'Reset' position
		this.firstPlayerPieceStepCounter = -1;
		for (int i = 1; i < nrPlayerPieces; i++) {
			this.playerPieceIndex[i] = FIELDNR - nrPlayerPieces + i;
		}
	}

	// TODO:
	// Needs to be tested!!!

	public Position getNextPlayerPosition() {

		int i = nrPlayerPieces - 1;
		while (playerPieceIndex[i] == FIELDNR - nrPlayerPieces + i && i > 0) {
			i--;
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
//		for (int j=0; j<nrPlayerPieces; j++) {
//			System.out.print(playerPieceIndex[j] + ", ");
//		}
//		System.out.println();
		return new Position(this.playerPieceIndex, null);
	}

}
