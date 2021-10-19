/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */

package chess;


public abstract class ChessPiece {
	
	protected int player;
	protected int turn;
	protected boolean beenMoved;
	protected int row;
	protected int col;
	protected boolean isInCheck;
	public boolean twoStep;
	public boolean canEnPassant;
	public boolean canCastle;

	
	/**
	 * @param row is starting row
	 * @param col is starting colomn
	 * @param player is player piece belongs to
	 */
	public ChessPiece(int row, int col, int player) {
		this.row = row;
		this.col = col;
		this.player = player;
		beenMoved = false;
		isInCheck = false;
		twoStep = false;
		canEnPassant = false;
		canCastle = false;
	}
	/**
	 * gets the piece's row
	 * @return an int
	 *
	 */
	public int getRow()
	{
		return row;
	}
	/**
	 * gets the piece's column
	 * @return an int
	 *
	 */
	public int getCol()
	{
		return col;
	}
	/**
	 * returns the piece's player
	 * @return an int
	 *
	 */
	public int getPlayer() { 
		return player;
	}
	/**
	 * sets a piece to a new location. updates variables
	 * @param fromrow is where the piece was
	 * @param fromcol is where the piece was
	 * @param torow is where the piece is
	 * @param tocol is where the piece is
	 * @param turn was used for en passant but no longer needed
	 * @param board is the current chess board
	 */
	public void movedYou(int fromrow, int fromcol, int torow, int tocol, int turn, ChessBoard board) {
		this.turn = turn;
		beenMoved = true;
		row = torow;
		col = tocol;
	}
	/**
	 * returns if piece has been moved or not (used for castleing)
	 * @return a boolean
	 *
	 */
	public boolean hasBeenMoved() {
		return beenMoved;
	}
	/**
	 * @deprecated
	 * @param turn is the current turn of chess
	 * @return boolean
	 */
	public boolean canBeEnpassanted(int turn) {
		// by default, chess pieces can not be enpassant'ed, except a pawn who moved 2 spaces 1 turn ago (yeesh)
		return false;
	}
	
	

	/**returns if the move will put the player in check
	 * @param fromrow is where the piece was
	 * @param fromcol is where the piece was
	 * @param torow is where the piece is
	 * @param tocol is where the piece is
	 * @param board is the current chess board
	 * @return a boolean
	 */
	//checks if the board state would be in check if move is made
	public boolean willBeInCheck(int fromrow, int fromcol, int torow, int tocol, ChessBoard board)
	{
		ChessPiece piece = board.getPiece(torow, tocol);
		board.tryMove(fromrow, fromcol, torow, tocol);
		
		if(board.getKing().inCheck(board))
		{
			board.tryMove(torow, tocol, fromrow, fromcol);
			if (piece != null)//return piece that was taken
			{
				board.putPiece(torow, tocol, piece);
			}
			return true;
		}
		else
		{
			board.tryMove(torow, tocol, fromrow, fromcol);
			if (piece != null)//return piece that was taken
			{
				board.putPiece(torow, tocol, piece);
			}
			return false;
		}
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
	public abstract boolean legalMove(int fromrow, int fromcol, int torow, int tocol, int turn, ChessBoard board);
	/**checks if player is can have their king taken in a single move
	 * @param fromrow is where the attacking piece is
	 * @param fromcol is where the attacking piece is
	 * @param torow is where the players king is
	 * @param tocol is where the players king is
	 * @param turn was used for en passant but no longer needed
	 * @param board is the current chess board
	 * @return a boolean
	 */
	public abstract boolean singleMove(int fromrow, int fromcol, int torow, int tocol, int turn, ChessBoard board);
	
	@Override
	public String toString() {
		if(player == 1) {
			return "w";
		}
		else {
			return "b";
		}
	}

	
}
