/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */

package chess;

public class King extends ChessPiece {

	
	/**
	 * makes king
	 * @param row is starting row
	 * @param col is starting colomn
	 * @param player is player piece belongs to
	 */
	public King(int row, int col, int player) {
		super(row, col, player);
	}
	
	/**checks if player is in check
	 * @param board is the chessboard
	 * @return a boolean
	 */
	public boolean inCheck(ChessBoard board)
	{
		
		for(int r = 0; r < 8; r++) 
		{
			for(int c = 0; c < 8; c++)
			{
				if(board.getPiece(r,  c) != null) 
				{ 
					// found a piece
					ChessPiece piece = board.getPiece(r, c);
					// only check enemy pieces
					if(piece.getPlayer() != board.getKing().getPlayer()) 
						if(piece.singleMove(piece.getRow(), piece.getCol(), row, col, piece.getPlayer(), board)) //checks if valid move to take king
						{
							return true;
						}
				}
			}
		}
		
		
		return false;
	}
	
	
	/**checks if player is can have their king taken in a single move
	 * @param fromrow is where the attacking piece is
	 * @param fromcol is where the attacking piece is
	 * @param torow is where the players king is
	 * @param tocol is where the players king is
	 * @param turn was used for en passant but no longer needed
	 * @param board is the current chess board
	 * @return a boolean
	 */
	public boolean singleMove(int fromrow, int fromcol, int torow, int tocol, int turn, ChessBoard board) {
		
		if((fromrow == torow) && (tocol == fromcol))
			return false;
		
		if((Math.abs(torow - fromrow) <= 1) && (Math.abs(tocol - fromcol) <= 1))
		{
			if(board.getPiece(torow, tocol) != null)
			{
				if(board.getPiece(torow, tocol).getPlayer() != player)
					return true;
			}
			else
			{
				return true;
			}
		}
	
	
	return false;
	}
	
	/**checks if player is can make the move
	 * @param fromrow is where the piece was
	 * @param fromcol is where the piece was
	 * @param torow is where the piece is
	 * @param tocol is where the piece is
	 * @param turn was used for en passant but no longer needed
	 * @param board is the current chess board
	 * @return a boolean
	 */
	@Override
	public boolean legalMove(int fromrow, int fromcol, int torow, int tocol, int turn, ChessBoard board) {
		
		if (willBeInCheck(fromrow, fromcol, torow, tocol, board))
		{
			return false;
		}
		// NOTE: legalMove currently is MOVING THE ROOK during a castle.  This may cause unintended beahvior,
		//   Need to rewrite this section at some point (can just ensure no castling if in check)
		
		canCastle = false;
		
		if((fromrow == torow) && (tocol == fromcol))
			return false;
		
		if((beenMoved == false) && (inCheck(board) == false))
		{
			if(player == 1)
			{
				if((tocol == 1) && (board.getPiece(7, 3) == null) && (board.getPiece(7, 2) == null) && (board.getPiece(7, 1) == null) && (board.getPiece(7, 0) != null))
				{
					if(board.getPiece(7, 0).beenMoved = false)
						{canCastle = true; return true;}
				}
				if((tocol == 6) && (board.getPiece(7, 5) == null) && (board.getPiece(7, 6) == null) && (board.getPiece(7, 7) != null))
				{
					if(board.getPiece(7, 7).beenMoved == false)
						{canCastle = true; return true;}
				}
			}
			else
			{
				if((tocol == 1) && (board.getPiece(0, 3) == null) && (board.getPiece(0, 2) == null) && (board.getPiece(0, 1) == null) && (board.getPiece(0, 0) != null))
				{
					if(board.getPiece(0, 0).beenMoved = false)
						{canCastle = true; return true;}
				}
				if((tocol == 6) && (board.getPiece(0, 5) == null) && (board.getPiece(0, 6) == null) && (board.getPiece(0, 7) != null))
				{
					if(board.getPiece(0, 7).beenMoved == false)
						{canCastle = true; return true;}
				}
			}
		}
		
			if((Math.abs(torow - fromrow) <= 1) && (Math.abs(tocol - fromcol) <= 1))
			{
				if(board.getPiece(torow, tocol) != null)
				{
					if(board.getPiece(torow, tocol).getPlayer() != player)
						return true;
				}
				else
				{
					return true;
				}
			}
		
		
		return false;
		
		
	}

	@Override
	public String toString() {
		return super.toString() + "K";
	}

}
