package muehletest;

import java.util.HashMap;

public class PositionMap extends HashMap<byte[], PositionMapEntry>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7015226108978751379L;
	private final int nrMoverPieces;
	private final int nrWaiterPieces;
	
	public PositionMap(int nrMoverPieces, int nrWaiterPieces) {
		
		super();
		this.nrMoverPieces = nrMoverPieces;
		this.nrWaiterPieces = nrWaiterPieces;
	}

	public int getNrMoverPieces() {
		return nrMoverPieces;
	}

	public int getNrWaiterPieces() {
		return nrWaiterPieces;
	}

}
