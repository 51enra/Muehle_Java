package muehletest;

/**
 * Byte[0]: bits 0-3 - which mover piece should be moved (0-8); bits 4-7 - if a mill is closed, which waiter piece to remove (0-8). 
 * Byte[1]: bits 0-4 - on which new field to place the mover piece (0-23); bit 7: 1 = waiter win position; 0 = mover win position. bit 6: If 1, a mill is closed.
 * Byte[2]: Number of moves till end of game (0-255). For end positions (Byte[2] = 0), Byte[0] is 0 and only bit 7 of Byte[1] is used.
 */

public class BestMove {
	
	private byte[] encoding;
	
	BestMove(byte[] encoding) {
		if (encoding.length == 3) {
			this.encoding = encoding;
		} else {
			this.encoding = new byte[3];
		}
	}

	public byte[] getEncoding() {
		return encoding;
	}

	public void setEncoding(byte[] encoding) {
		this.encoding = encoding;
	}

}
