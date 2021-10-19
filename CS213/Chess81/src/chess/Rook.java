/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package chess;

public class Rook extends ChessPiece {

	/**
	 * makes rook
	 * @param row is starting row
	 * @param col is starting colomn
	 * @param player is player piece belongs to
	 */
	public Rook(int row, int col, int player) {
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
			// must be arriving at either same row or same col.  diagonal NOT ALLOWED
			if(torow != fromrow && tocol != fromcol) {
				return false;
			}
			
			int dr = 0;
			int dc = 0;
			if(torow != fromrow) {
				dr = torow - fromrow;
				dr = dr/Math.abs(dr);
			}
			else {
				dc = tocol - fromcol;
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
			// must be arriving at either same row or same col.  diagonal NOT ALLOWED
			if(torow != fromrow && tocol != fromcol) {
				return false;
			}
			
			int dr = 0;
			int dc = 0;
			if(torow != fromrow) {
				dr = torow - fromrow;
				dr = dr/Math.abs(dr);
			}
			else {
				dc = tocol - fromcol;
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

	@Override
	public String toString() {
		return super.toString() + "R";
	}

}
