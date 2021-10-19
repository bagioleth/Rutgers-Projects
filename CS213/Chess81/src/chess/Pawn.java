/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package chess;

public class Pawn extends ChessPiece 
{
	
	
	/**makes pawn
	 * @param row is starting row
	 * @param col is starting colomn
	 * @param player is player piece belongs to
	 */
	public Pawn(int row, int col, int player) {
		super(row, col, player);
	}
	
	/**
	 *
	 */
	public void movedYou(int fromrow, int fromcol, int torow, int tocol, int turn, ChessBoard board) {
		super.movedYou(fromrow, fromcol, torow, tocol, turn, board);
		

		if(player == 1) {
			if(torow == 0) {
				// BECOMES A QUEEN
				board.putPiece(torow, tocol, new Queen(torow, tocol, player));
			}
			
		}
		else if(player == 2) {
			if(torow == 7) {
				// BECOMES A QUEEN
				board.putPiece(torow, tocol, new Queen(torow, tocol, player));				
			}
		}
		
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
		if((player == 1))
		{
			if (torow < fromrow)
			{
				if((tocol == fromcol) && (fromrow - torow == 2) && (beenMoved == false) && (board.getPiece((torow + fromrow)/2, tocol) == null) && (board.getPiece((torow), tocol) == null))//move foward 2 spaces on first move
					{canEnPassant = false; twoStep = true; return true;}
				if((tocol == fromcol) && (fromrow - torow == 1) && (board.getPiece((torow), tocol) == null))//move foward 1 space on first move
					{canEnPassant = false; return true;}
				if((fromrow - torow == 1) && ((tocol - fromcol == 1) || (fromcol - tocol == 1)) && (board.getPiece((torow), tocol) != null))
					if(board.getPiece(torow, tocol).getPlayer() != player)
						{canEnPassant = false; return true;}
			}
		}
		else
		{//player 2
			if (torow > fromrow)
			{
				if((tocol == fromcol) && (fromrow - torow == -2) && (beenMoved == false) && (board.getPiece((torow + fromrow)/2, tocol) == null) && (board.getPiece((torow), tocol) == null))//move foward 2 spaces on first move
					{canEnPassant = false; twoStep = true; return true;}
				if((tocol == fromcol) && (fromrow - torow == -1) && (board.getPiece((torow), tocol) == null))//move foward 1 space on first move
					{canEnPassant = false; return true;}
				if((fromrow - torow == -1) && ((tocol - fromcol == 1) || (fromcol - tocol == 1)) && (board.getPiece((torow), tocol) != null))
					if(board.getPiece(torow, tocol).getPlayer() != player)
						{canEnPassant = false; return true;}
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
		// TODO Auto-generated method stub
		
		if((player == 1))
		{
			if (torow < fromrow)
			{
				if((tocol == fromcol) && (fromrow - torow == 2) && (beenMoved == false) && (board.getPiece((torow + fromrow)/2, tocol) == null) && (board.getPiece((torow), tocol) == null))//move foward 2 spaces on first move
					{canEnPassant = false; twoStep = true; return true;}
				if((tocol == fromcol) && (fromrow - torow == 1) && (board.getPiece((torow), tocol) == null))//move foward 1 space on first move
					{canEnPassant = false; return true;}
				if((fromrow - torow == 1) && ((tocol - fromcol == 1) || (fromcol - tocol == 1)) && (board.getPiece((torow), tocol) == null))
					if(board.getPiece((fromrow), tocol) != null)
						if(board.getPiece(fromrow, tocol).getPlayer() != player && (board.getPiece(fromrow, tocol).toString().charAt(1) == (this.toString().charAt(1))) && ((board.getPiece(fromrow, tocol)).twoStep))
							{canEnPassant = true; return true;}
				if((fromrow - torow == 1) && ((tocol - fromcol == 1) || (fromcol - tocol == 1)) && (board.getPiece((torow), tocol) != null))
					if(board.getPiece(torow, tocol).getPlayer() != player)
						{canEnPassant = false; return true;}
			}
		}
		else
		{//player 2
			if (torow > fromrow)
			{
				if((tocol == fromcol) && (fromrow - torow == -2) && (beenMoved == false) && (board.getPiece((torow + fromrow)/2, tocol) == null) && (board.getPiece((torow), tocol) == null))//move foward 2 spaces on first move
					{canEnPassant = false; twoStep = true; return true;}
				if((tocol == fromcol) && (fromrow - torow == -1) && (board.getPiece((torow), tocol) == null))//move foward 1 space on first move
					{canEnPassant = false; return true;}
				if((fromrow - torow == -1) && ((tocol - fromcol == 1) || (fromcol - tocol == 1)) && (board.getPiece((torow), tocol) == null))
					if(board.getPiece((fromrow), tocol) != null)
						if(board.getPiece(fromrow, tocol).getPlayer() != player && (board.getPiece(fromrow, tocol).toString().charAt(1) == (this.toString().charAt(1))) && ((board.getPiece(fromrow, tocol)).twoStep))
							{canEnPassant = true; return true;}
				if((fromrow - torow == -1) && ((tocol - fromcol == 1) || (fromcol - tocol == 1)) && (board.getPiece((torow), tocol) != null))
					if(board.getPiece(torow, tocol).getPlayer() != player)
						{canEnPassant = false; return true;}
			}
		}
		return false;
		
	}

	

	
	
	@Override
	public String toString() {
		//if(twoStep) {
		//	return super.toString() + "*";			
		//}
		return super.toString() + "P";
	}


}
