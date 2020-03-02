package muehletest;

public class PositionMapEntry {
	
	byte maxValue;
	byte minValue;
	Position maxMinSuccessor;
	// Stores maxSuccessor as mover position and minSuccessor as waiter position????
	// could be optimized by just storing the change to the current position???
	// Better: different data structure!
	// May need to store which piece is removed if mill

}
