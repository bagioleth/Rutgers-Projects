/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package chess;
public class Queen extends ChessPiece {
	/**
	 * makes queen
	 * @param row is starting row
	 * @param col is starting colomn
	 * @param player is player piece belongs to
	 */
	public Queen(int row, int col, int player) {
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
			int dr = 0;
			int dc = 0;

			dr = torow - fromrow;
			dc = tocol - fromcol;

			if(Math.abs(dr) != Math.abs(dc) && Math.abs(dr) != 0 && Math.abs(dc) != 0) {
				return false;
			}

			
			if(dr != 0) {
				dr = dr/Math.abs(dr);
			}
			if(dc != 0) {
				dc = dc/Math.abs(dc);
			}
			
			
			fromrow += dr;
			fromcol += dc;
			while(fromrow != torow || fromcol != tocol) {
				if(board.getPiece(fromrow, fromcol) != null) {
					return false;
				}
				fromrow += dr;
				fromcol += dc;
			}
			
			
			return true;
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
			int dr = 0;
			int dc = 0;

			dr = torow - fromrow;
			dc = tocol - fromcol;

			if(Math.abs(dr) != Math.abs(dc) && Math.abs(dr) != 0 && Math.abs(dc) != 0) {
				return false;
			}

			
			if(dr != 0) {
				dr = dr/Math.abs(dr);
			}
			if(dc != 0) {
				dc = dc/Math.abs(dc);
			}
			
			
			fromrow += dr;
			fromcol += dc;
			while(fromrow != torow - dr || fromcol != tocol - dc) {
				if(board.getPiece(fromrow, fromcol) != null) {
					return false;
				}
				fromrow += dr;
				fromcol += dc;
			}
			
			
			return true;
		}
		return false;
	}
	
	
	@Override
	public String toString() {
		return super.toString() + "Q";
	}

}
