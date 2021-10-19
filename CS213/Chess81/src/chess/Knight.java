/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package chess;

public class Knight extends ChessPiece {
	
	/** makes knight
	 * @param row is starting row
	 * @param col is starting colomn
	 * @param player is player piece belongs to
	 */
	public Knight(int row, int col, int player) {
		super(row, col, player);
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
		// TODO Auto-generated method stub
		if(board.getPiece(torow, tocol) == null || board.getPiece(torow,  tocol).getPlayer() != player) {
			
			int dr = fromrow - torow;
			int dc = fromcol - tocol;
			// must be either, dr is 2 or -2 while dc is 1 or -1, or vice versa
			// there are 8 combinations taken like this, but if you sqare dr and dc, one must be 
			// four while the other is one, or vice versa (only 2 rules)
			if(dr*dr == 4 && dc*dc == 1 || dr*dr == 1 && dc*dc == 4) {
				return true;
			}
			
			return false;
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
		
		// TODO Auto-generated method stub
		if(board.getPiece(torow, tocol) == null || board.getPiece(torow,  tocol).getPlayer() != player) {
			
			int dr = fromrow - torow;
			int dc = fromcol - tocol;
			// must be either, dr is 2 or -2 while dc is 1 or -1, or vice versa
			// there are 8 combinations taken like this, but if you sqare dr and dc, one must be 
			// four while the other is one, or vice versa (only 2 rules)
			if(dr*dr == 4 && dc*dc == 1 || dr*dr == 1 && dc*dc == 4) {
				return true;
			}
			
			return false;
		}
		return false;
	}

	@Override
	public String toString() {
		return super.toString() + "N";
	}

}
