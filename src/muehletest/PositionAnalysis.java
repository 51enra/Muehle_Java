package muehletest;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PositionAnalysis {
	

	public Map<Position, byte[]> moveMap;
		

	public PositionAnalysis(int maxPieces) {
		// Perform retrograde analysis up to maxMoverPieces,
		// e.g. maxPieces = 4: Analyse 3-3, 3-4, 4-3
//		int[][] pieceIndices = position.getPieceIndices();

	}
	
	//**
	
	public Set<Position> findGameEndPositions(int maxPieces) {
		//TODO
		// Also needs to write values to positionVAlueArray.
		// ok 3 waiter pieces & >= 1 mill closing field (mover) --> mover wins (+255). Next position: Close mill; [remove 1 waiter piece]
		// ok else, 3 mover pieces, >= 2 mill closing fields (waiter) --> mover loses (-255). No next position.
		// ok > 3 mover pieces, zero degrees of freedom --> mover loses (-255). No next position
		// > 3 waiter pieces, mover 3 pieces, must remove piece from waiter mill --> mover loses (?? Only if all three pieces block mills??)
		// ok > 3 waiter pieces, mover can block last degree of freedom --> mover wins (+255). Next position: Close degree of freedom 
		
		// if waiterpieces = 3: (for any number of mover pieces)
		//   Loop: Go through mover positions until found open mill
		//   if moverpieces > 3: if found open mill, check if closing is possible (i.e. neighboring mover piece).
		//     Go through all waiterpositions
		//       Exclude those that block closing the mill (i.e. waiter piece on open mill field).
		//       All others -> put as wins in moveMap
		//  if moverpieces = 3: (for any number of waiter pieces)
		//   Loop: Go through waiter positions until found >=2 mill-closing fields
		//     Go through all mover positions
		//        if mover pieces don't block all or all but one open mill:
		//          if position not already as win in moveMap: mark as loss in moveMap
		//  for each mover piece position (4-9 pieces - for 3 can fly)
		//    for all numbers of waiterpieces >= degrees of freedom of mover positions
		//      find all waterpiece positions that close all degrees of freedom --> as loss in movemap
		//         check if position is already present in moveMap -> Exception!
		//  for each waiter piece postion (4-9 pieces)
		//    for all numbers of waiterpieces >= degrees of freedom of waiter position
		//      find all mover positions that can close the last degree of freedom in the next move  --> as win in moveMap
		//         check if position is already present in moveMap -> Exception!
		// note: loop through waiter position before mover by swapping mover/waiter role in positiongenerator.
		// Need to swap back only before storing the result in the moveMap.
        
		PositionGenerator positionGenerator = new PositionGenerator();
		// 1. Player = Mover. Find all positions where player can close a mill.
		// Mover needs at least one open mill.
		positionGenerator.init(3,3);
		Position playerPosition = positionGenerator.getNextPlayerPosition();
		do {
			Set<Integer> millClosingFields = playerPosition.getMillClosingFields('m');
			if (millClosingFields.size()>=1) {
				List<Position> positions = positionGenerator.findLosingOpponentPositions(millClosingFields, 1);
				for (Position position : positions) {
					moveMap.put(position, new byte[] {0,0,0});
				}
			}
			playerPosition = positionGenerator.getNextPlayerPosition();
		} while (playerPosition != null);
		
		// 2. Player = Waiter. Find all positions where waiter can close a mill after mover has moved.
		// Waiter needs at least two open mills that can't be closed by one mover piece.
		positionGenerator.init(3,3);
		Position playerPosition = positionGenerator.getNextPlayerPosition();
		do {
			Set<Integer> millClosingFields = playerPosition.getMillClosingFields('m');
			if (millClosingFields.size()>=2) {
				List<Position> positions = positionGenerator.findLosingOpponentPositions(millClosingFields, 2);
				for (Position position : positions) {
					// MoveMap is from Mover perspective, so exchange Mover and Waiter
					position.transpose();
					// Set flag for Waiter wins in byte[1]
					moveMap.put(position, new byte[] {0,80,0});
				}
			}
			playerPosition = positionGenerator.getNextPlayerPosition();
		} while (playerPosition != null);
		
		
		return null;
	}
	
	public Set<Position> findAllPredecessors(Position position) {
		//TODO
		// distinguish jumping and shifting phase
		// Note: reversal of mover and waiter to be considered!
		return null;
	}
	
	public Set<Position> findAllSuccessors(Position position) {
		//TODO
		// distinguish jumping and shifting phase
		return null;
	}

	public Set<Position> findPredecessorsJump() {
		Set<Position> predecessorPositions = new HashSet<>();

		return predecessorPositions;
	}

}
