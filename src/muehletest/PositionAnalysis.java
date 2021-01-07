package muehletest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PositionAnalysis {

	public Map<Position, byte[]> moveMap;

	// Double mill rule: True - if one player piece closes 2 mills, two opponent
	// pieces can be taken.
	// False - only one piece can be taken.
	public static boolean doubleMillRule = false;

	// Double take out of mill rule (only effective if doubleMillRule == true): True
	// - both pieces can be taken out of
	// closed mills if no other possibility. False - at most one piece out of a
	// closed mill.
	public static boolean doubleTakeOutOfMillRule = false;

	// Take out of mill rule: True - if player closes a mill and all opponent pieces
	// are in closed mills, a mill piece can be taken.
	// False - only pieces which are not in closed mills can be taken
	public static boolean takeOutOfMillRule = true;

	public PositionAnalysis(int maxPieces) {
		// Perform retrograde analysis up to maxMoverPieces,
		// e.g. maxPieces = 4: Analyse 3-3, 3-4, 4-3
//		int[][] pieceIndices = position.getPieceIndices();
		moveMap = new HashMap<Position, byte[]>();

	}

	public List<Position> findGameEndPositions(int maxPieces) {
		// TODO
		// Also needs to write values to positionVAlueArray.
		// ok 3 waiter pieces & >= 1 mill closing field (mover) --> mover wins (+255).
		// Next position: Close mill; [remove 1 waiter piece]
		// ok else, 3 mover pieces, >= 2 mill closing fields (waiter) --> mover loses
		// (-255). No next position.
		// ok > 3 mover pieces, zero degrees of freedom --> mover loses (-255). No next
		// position
		// > 3 waiter pieces, mover 3 pieces, must remove piece from waiter mill -->
		// mover loses (?? Only if all three pieces block mills??)
		// ok > 3 waiter pieces, mover can block last degree of freedom --> mover wins
		// (+255). Next position: Close degree of freedom

		// if waiterpieces = 3: (for any number of mover pieces)
		// Loop: Go through mover positions until found open mill
		// if moverpieces > 3: if found open mill, check if closing is possible (i.e.
		// neighboring mover piece).
		// Go through all waiterpositions
		// Exclude those that block closing the mill (i.e. waiter piece on open mill
		// field).
		// All others -> put as wins in moveMap
		// if moverpieces = 3: (for any number of waiter pieces)
		// Loop: Go through waiter positions until found >=2 mill-closing fields
		// Go through all mover positions
		// if mover pieces don't block all or all but one open mill:
		// if position not already as win in moveMap: mark as loss in moveMap
		// for each mover piece position (4-9 pieces - for 3 can fly)
		// for all numbers of waiterpieces >= degrees of freedom of mover positions
		// find all waterpiece positions that close all degrees of freedom --> as loss
		// in movemap
		// check if position is already present in moveMap -> Exception!
		// for each waiter piece postion (4-9 pieces)
		// for all numbers of waiterpieces >= degrees of freedom of waiter position
		// find all mover positions that can close the last degree of freedom in the
		// next move --> as win in moveMap
		// check if position is already present in moveMap -> Exception!
		// note: loop through waiter position before mover by swapping mover/waiter role
		// in positiongenerator.
		// Need to swap back only before storing the result in the moveMap.

		List<Position> endPositions = new ArrayList<>();
		PositionIterator positionIterator = new PositionIterator();
		// 1. Player = Mover. Find all positions where player can close a mill.
		// Mover needs at least one open mill.
		positionIterator.init(3, 3);
		Position playerPosition = positionIterator.getNextPlayerPosition();
		do {
			Set<Integer> millClosingFields = playerPosition.getMillClosingFields('p');
			if (millClosingFields.size() >= 1) {
				List<Position> positions = positionIterator.findLosingOpponentPositions(millClosingFields, 1);
				for (Position position : positions) {
					moveMap.put(position, new byte[] { 0, 0, 0 });
					endPositions.add(position);
				}
			}
			playerPosition = positionIterator.getNextPlayerPosition();
		} while (playerPosition != null);

		// 2. Player = Waiter. Find all positions where waiter can close a mill after
		// mover has moved.
		// Waiter needs at least two open mills that can't be closed by one mover piece.
		// Mover must not have an open mill.
		positionIterator.init(3, 3);
		playerPosition = positionIterator.getNextPlayerPosition();
		do {
			Set<Integer> millClosingFields = playerPosition.getMillClosingFields('p');
			if (millClosingFields.size() >= 2) {
				List<Position> positions = positionIterator.findLosingOpponentPositions(millClosingFields, 2);
				for (Position position : positions) {
					// Waiter only wins if mover has no open mill
					if (position.getMillClosingFields('o').size() == 0) {
						// MoveMap is from Mover perspective, so exchange Mover and Waiter
						position = position.transpose();
						// Set flag for Waiter wins in byte[1]
						moveMap.put(position, new byte[] { 0, 80, 0 });
						endPositions.add(position);
					}
				}
			}
			playerPosition = positionIterator.getNextPlayerPosition();
		} while (playerPosition != null);

		return endPositions;
	}

	public List<Position> findAllPredecessors(Position position) {

		List<Position> predecessorPositions = new ArrayList<>();
		List<Integer> opponentPiecesInMills = position.getPiecesInMills('o');

		if (opponentPiecesInMills.size() > 0) {
			predecessorPositions.addAll(findPredecessorsMill(position, opponentPiecesInMills));
		}
		predecessorPositions.addAll(findPredeSuccessorsNotMill(position, opponentPiecesInMills));
		return predecessorPositions;
	}
	
	public List<Position> findAllSuccessors(Position position) {

		List<Position> successorPositions = new ArrayList<>();
		List<Integer> opponentPiecesInMills = position.getPiecesInMills('o');
		
		// TODO we need to exclude open mill positions as target fields first!

		if (opponentPiecesInMills.size() > 0) {
			successorPositions.addAll(findSuccessorsMill(position, opponentPiecesInMills));
		}
		successorPositions.addAll(findPredeSuccessorsNotMill(position, opponentPiecesInMills));
		return successorPositions;
	}

	

	public List<Position> findPredeSuccessorsNotMill(Position position, List<Integer> piecesToIgnore) {
		List<Position> predeSuccessorPositions = new ArrayList<>();
		List<Integer> playerPieceIndex = position.getPlayerPieceIndices();
		List<Integer> opponentPieceIndex = position.getOpponentPieceIndices();
		List<Integer> targetPieceIndex = new ArrayList<>(opponentPieceIndex);
		targetPieceIndex.removeAll(piecesToIgnore);

		List<Integer> freeFields = new ArrayList<>();
		if (opponentPieceIndex.size() == 3) {
			freeFields = position.getFreeFields();
		}

		for (Integer opponentPiece : targetPieceIndex) {

			// Distinguish between "shift" and "fly" phase by number of opponent pieces
			if (opponentPieceIndex.size() > 3) {
				freeFields = position.getFreeNeighborFields(opponentPiece);
			}

			opponentPieceIndex.remove(opponentPiece);
			predeSuccessorPositions
					.addAll(generateNewOpponentPositions(playerPieceIndex, opponentPieceIndex, freeFields));
			opponentPieceIndex.add(opponentPiece);
		}

		return predeSuccessorPositions;
	}

	
	public List<Position> findPredecessorsMill(Position position, List<Integer> piecesInMill) {

		List<Position> predecessorPositions = new ArrayList<>();
		List<Integer> playerPieceIndex = position.getPlayerPieceIndices();
		List<Integer> opponentPieceIndex = position.getOpponentPieceIndices();
		Set<Integer> piecesInMillSet;

		if (doubleMillRule == true) {
			Set<Integer> doubleMillPieceSet = new HashSet<>();
			piecesInMillSet = new HashSet<>();
			for (int field : piecesInMill) {
				if (piecesInMillSet.add(field) == false) {
					doubleMillPieceSet.add(field);
					piecesInMillSet.remove(field);
				}
			}
			if (doubleMillPieceSet.size() > 0) {
				predecessorPositions.addAll(findPredecessorsDoubleMill(position, doubleMillPieceSet));
			}
		} else {
			piecesInMillSet = new HashSet<>(piecesInMill);
		}

		if (playerPieceIndex.size() < 9) {

			// If other possibility exists, player piece can't have been taken out of a
			// closed mill
			List<Integer> extraPieceIndex = position.getFreeFields();
			extraPieceIndex.removeAll(position.getMillClosingFields('p'));

			if (takeOutOfMillRule = true && extraPieceIndex.size() == 0) {
				extraPieceIndex = position.getFreeFields();
			}

			for (Integer addPlayerPiece : extraPieceIndex) {
				playerPieceIndex.add(addPlayerPiece);
				Position newPosition = new Position(playerPieceIndex, opponentPieceIndex);

				List<Integer> freeFields = new ArrayList<>();
				if (opponentPieceIndex.size() == 3) {
					freeFields = position.getFreeFields();
				}

				for (Integer opponentPiece : piecesInMillSet) {
					// Distinguish between "shift" and "fly" phase by number of opponent pieces
					if (opponentPieceIndex.size() > 3) {
						freeFields = newPosition.getFreeNeighborFields(opponentPiece);
					}

					opponentPieceIndex.remove(opponentPiece);
					predecessorPositions
							.addAll(generateNewOpponentPositions(playerPieceIndex, opponentPieceIndex, freeFields));
					opponentPieceIndex.add(opponentPiece);
				}
				playerPieceIndex.remove(addPlayerPiece);
			}
		} else {
			throw new IllegalArgumentException("Illegal position - opponent can't have a mill at 9 player pieces");
		}
		return predecessorPositions;
	}
	
	
	public List<Position> findPredecessorsDoubleMill(Position position, Set<Integer> doubleMillPieceSet) {
		List<Position> predecessorPositions = new ArrayList<>();
		List<Integer> playerPieceIndex = position.getPlayerPieceIndices();
		List<Integer> opponentPieceIndex = position.getOpponentPieceIndices();
		
		if (playerPieceIndex.size() < 8) {
			
			// If other possibility exists, player piece can't have been taken out of a
			// closed mill
			List<Integer> extraPieceIndex = position.getFreeFields();
			extraPieceIndex.removeAll(position.getMillClosingFields('p'));

			if (doubleTakeOutOfMillRule == true && extraPieceIndex.size() == 1) {
				throw new IllegalArgumentException("Logic not yet implemented!!!");
				// TODO first non-mill piece MUST be taken; only second piece can come out of a closed mill! 
			}
			if ( takeOutOfMillRule == true  || doubleTakeOutOfMillRule == true && extraPieceIndex.size() == 0) {
				extraPieceIndex = position.getFreeFields();
			}
		    for (int i=0; i<extraPieceIndex.size(); i++) {
		    	
		    	playerPieceIndex.add(extraPieceIndex.get(i));
		    	for (int j=i+1; j<extraPieceIndex.size(); j++) {
		    		playerPieceIndex.add(extraPieceIndex.get(j));
					Position newPosition = new Position(playerPieceIndex, opponentPieceIndex);

					List<Integer> freeFields = new ArrayList<>();
					if (opponentPieceIndex.size() == 3) {
						freeFields = position.getFreeFields();
					}

					for (Integer opponentPiece : doubleMillPieceSet) {
						// Distinguish between "shift" and "fly" phase by number of opponent pieces
						if (opponentPieceIndex.size() > 3) {
							freeFields = newPosition.getFreeNeighborFields(opponentPiece);
						}

						opponentPieceIndex.remove(opponentPiece);
						predecessorPositions
								.addAll(generateNewOpponentPositions(playerPieceIndex, opponentPieceIndex, freeFields));
						opponentPieceIndex.add(opponentPiece);
					}
			    	playerPieceIndex.remove(extraPieceIndex.get(j));
		    	}
		    	playerPieceIndex.remove(extraPieceIndex.get(i));
		    }
		} else {
			throw new IllegalArgumentException("Illegal position - opponent can't have a double mill at 8 or more player pieces");			
		}
		return predecessorPositions;
	}

//	public List<Position> findAllPredecessors(Position position) {
//		// TODO
//		// distinguish jumping and shifting phase
//		// Note: reversal of mover and waiter to be considered!
//		List<Position> predecessorPositions = new ArrayList<>();
//		List<Integer> playerPieceIndex = position.getPlayerPieceIndices();
//		List<Integer> opponentPieceIndex = position.getOpponentPieceIndices();
//		List<Integer> opponentPiecesInMills = position.getPiecesInMills('o');
//		List<Integer> opponentPiecesNotInMills = new ArrayList<>(opponentPieceIndex);
//
//		if (opponentPiecesInMills.size() > 0) {
//
//			if (playerPieceIndex.size() < 9) {
//				opponentPiecesNotInMills.removeAll(opponentPiecesInMills);
//				// Player piece can't have been taken out of a closed mill
//				List<Integer> extraPieceIndex = position.getFreeFields();
//				extraPieceIndex.removeAll(position.getMillClosingFields('p'));
//
//				for (Integer addPlayerPiece : extraPieceIndex) {
//					playerPieceIndex.add(addPlayerPiece);
//					Position newPosition = new Position(playerPieceIndex, opponentPieceIndex);
//
//					for (Integer opponentPiece : opponentPiecesInMills) {
//						opponentPieceIndex.remove(opponentPiece);
//						List<Integer> freeNeighborFields = newPosition.getFreeNeighborFields(opponentPiece);
//						freeNeighborFields.remove(addPlayerPiece);
//						predecessorPositions.addAll(
//								generateNewOpponentPositions(playerPieceIndex, opponentPieceIndex, freeNeighborFields));
//						opponentPieceIndex.add(opponentPiece);
//					}
//					playerPieceIndex.remove(addPlayerPiece);
//				}
//			} else {
//				throw new IllegalArgumentException("Illegal position - opponent can't have a mill at 9 player pieces");
//			}
//		}
//		for (Integer opponentPiece : opponentPiecesNotInMills) {
//			opponentPieceIndex.remove(opponentPiece);
//			List<Integer> freeNeighborFields = position.getFreeNeighborFields(opponentPiece);
//			predecessorPositions
//					.addAll(generateNewOpponentPositions(playerPieceIndex, opponentPieceIndex, freeNeighborFields));
//			opponentPieceIndex.add(opponentPiece);
//		}
//
//		return predecessorPositions;
//	}
//
//	public List<Position> findAllPredecessorsJump(Position position) {
//		List<Position> predecessorPositions = new ArrayList<>();
//		List<Integer> playerPieceIndex = position.getPlayerPieceIndices();
//		List<Integer> opponentPieceIndex = position.getOpponentPieceIndices();
//		List<Integer> freeFieldList = position.getFreeFields();
//		List<Integer> opponentPiecesInMills = position.getPiecesInMills('o');
//
//		for (Integer opponentPiece : opponentPieceIndex) {
//			List<Integer> predOpponentPieceIndex = new ArrayList<>(opponentPieceIndex);
//			predOpponentPieceIndex.remove(opponentPiece);
//
//			if (!(opponentPiecesInMills.contains(opponentPiece))) {
//				predecessorPositions
//						.addAll(generateNewOpponentPositions(playerPieceIndex, predOpponentPieceIndex, freeFieldList));
//			} else { // add one player piece because of going from closed to open mill
//
//				if (playerPieceIndex.size() < 9) {
//					// Player piece can't have been taken out of a closed mill
//					List<Integer> extraPieceIndex = new ArrayList<>(freeFieldList);
//					extraPieceIndex.removeAll(position.getMillClosingFields('p'));
//
//					for (Integer addPlayerPiece : extraPieceIndex) {
//						playerPieceIndex.add(addPlayerPiece);
//						freeFieldList.remove(addPlayerPiece);
//						predecessorPositions.addAll(
//								generateNewOpponentPositions(playerPieceIndex, predOpponentPieceIndex, freeFieldList));
//						freeFieldList.add(addPlayerPiece);
//						playerPieceIndex.remove(addPlayerPiece);
//					}
//				} else {
//					throw new IllegalArgumentException(
//							"Illegal position - opponent can't have a mill at 9 player pieces");
//				}
//			}
//			predOpponentPieceIndex.add(opponentPiece);
//		}
//
//		return predecessorPositions;
//	}


	/**
	 * Generates a list of positions by adding one opponent piece on one of the
	 * fields in freeFieldIndex. As many positions are generated as there are
	 * elements in freeFieldIndex.
	 * 
	 * @param playerPieceIndex   the (unchanging) indices of player pieces [0..23]
	 * @param opponentPieceIndex the already existing opponent pieces.
	 * @param freeFieldIndex     the list of fields to be occupied by an opponent
	 *                           piece one by one
	 * @return the list of new positions
	 */

	private List<Position> generateNewOpponentPositions(List<Integer> playerPieceIndex,
			List<Integer> opponentPieceIndex, List<Integer> freeFieldIndex) {
		List<Position> positions = new ArrayList<>();
		for (Integer newPieceField : freeFieldIndex) {
			opponentPieceIndex.add(newPieceField);
			positions.add(new Position(playerPieceIndex, opponentPieceIndex));
			opponentPieceIndex.remove(newPieceField);
		}
		return positions;
	}

}
