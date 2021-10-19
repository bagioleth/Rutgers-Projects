/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package chess;

public class ChessBoard {
	ChessPiece[][] board;
	int turnNumber;
	int player;
	King whiteKing;
	King blackKing;
	
	public ChessBoard() {
		board = new ChessPiece[8][8];
		turnNumber = 1;
		player = 0;
	}
	
	public ChessPiece[][] getBoard()
	{
		return board;
	}
	
	public void setupBoard() {
		player = 2;
		board[0][0] = new Rook(0, 0, player);
		board[0][1] = new Knight(0, 1, player);
		board[0][2] = new Bishop(0, 2, player);
		board[0][3] = new Queen(0, 3, player);
		board[0][4] = new King(0, 4, player);
		blackKing = (King)board[0][4];
		board[0][5] = new Bishop(0, 5, player);
		board[0][6] = new Knight(0, 6, player);
		board[0][7] = new Rook(0, 7, player);
		for(int i=0;i<8;i++) {
			board[1][i] = new Pawn(1, i, player);
		}
		
		player = 1;
		board[7][0] = new Rook(7, 0, player);
		board[7][1] = new Knight(7, 1, player);
		board[7][2] = new Bishop(7, 2, player);
		board[7][3] = new Queen(7, 3, player);
		board[7][4] = new King(7, 4, player);
		whiteKing = (King)board[7][4];
		board[7][5] = new Bishop(7, 5, player);
		board[7][6] = new Knight(7, 6, player);
		board[7][7] = new Rook(7, 7, player);
		for(int i=0;i<8;i++) {
			board[6][i] = new Pawn(6, i, player);
		}
	}
	
	public int getPlayer() {
		return player;
	}
	
	public King getKing()
	{
		if (player == 1)
			return whiteKing;
		else
			return blackKing;
	}
	
	public void putPiece(int row, int col, ChessPiece piece) {
		board[row][col] = piece;
	}
	
	public void printBoard() 
	{
		for(int row=0;row<8;row++) 
		{
			for(int col=0;col<8;col++) 
			{
				if(board[row][col] == null) 
				{
					if(row%2 != col%2) 
					{
						System.out.print("##");
					}
					else {
						System.out.print("  ");
					}
				}
				else {
					System.out.print(board[row][col]);
				}
				System.out.print(" ");
			}
			System.out.println(""+(8-row));
		}
		for(char spot = 'a'; spot <= 'h'; spot++) 
		{
			System.out.print(" " + spot + " ");
		}
		System.out.println("");
	}
	

	public boolean possibleMove()
	{
		int player = getPlayer();
		ChessPiece piece;
		for(int row = 0; row < 8 ; row++)
		{
			for(int col = 0; col < 8; col++)
			{
				if(getPiece(row, col) != null)
				{
					piece = getPiece(row, col);
					if(piece.getPlayer() == player)
					{
						for(int r = 0; r < 8; r++)
						{
							for (int c = 0; c < 8; c++)
							{
								if (piece.legalMove(row, col, r, c, turnNumber, this))
								{
									//System.out.println("" + row + col + r + c);
									return true;//at least 1 possible move exists
								}
							}
						}
					}
				}
			}
		}
							
		return false;
	}
	
	public ChessPiece getPiece(int row, int col) 
	{
		return board[row][col];
	}
	
	public void specialRemove(int row, int col) 
	{
		board[row][col] = null;
	}
	
	public void castle(int row, int col)
	{
		if(row == 0)
		{
			if (col == 6)
			{
				board[row][5] = board[row][7];
				board[row][7] = null;
				board[row][col] = board[row][4];
				board[row][4] = null;
			}
			else
			{
				board[row][2] = board[row][0];
				board[row][0] = null;
				board[row][col] = board[row][4];
				board[row][4] = null;
			}
		}
		else
		{
			if (col == 6)
			{
				board[row][5] = board[row][7];
				board[row][7] = null;
				board[row][col] = board[row][4];
				board[row][4] = null;
			}
			else
			{
				board[row][2] = board[row][0];
				board[row][0] = null;
				board[row][col] = board[row][4];
				board[row][4] = null;
			}
		}
	}
	
	public void tryMove(int fromrow, int fromcol, int torow, int tocol)
	{
		board[torow][tocol] = board[fromrow][fromcol];
		board[fromrow][fromcol] = null;
	}
	
	public void movePiece(int fromrow, int fromcol, int torow, int tocol) 
	{
		if(fromrow < 0 || torow < 0 || fromrow > 7 || torow > 7) 
		{
			System.out.println("Illegal Move");
			return;
		}//out of bounds
		else if (board[fromrow][fromcol] == null)
		{
			System.out.println("Illegal Move");
			return;
		}//no piece selected
		else if (board[fromrow][fromcol].getPlayer() != player) 									//piece selected is of wrong player
		{
			System.out.println("Illegal Move");
			return;
		}
		else if(board[fromrow][fromcol].legalMove(fromrow, fromcol, torow, tocol, turnNumber, this)) 
		{
			// System.out.println("OKAY");
			if(board[fromrow][fromcol].canCastle)
				castle(torow, tocol);
			else
			{
			if(board[fromrow][fromcol].canEnPassant)
				specialRemove(fromrow, tocol);
			board[torow][tocol] = board[fromrow][fromcol];
			board[fromrow][fromcol] = null;
			}
			board[torow][tocol].movedYou(fromrow, fromcol, torow, tocol, turnNumber, this);
			
			
			for(int r = 0; r < 8; r ++)
				for(int c = 0; c < 8; c++)
				{
					if(board[r][c] != null)
						if(board[r][c].getPlayer() != player)
							board[r][c].twoStep = false;
				}

			
			
			
			turnNumber++;
			player = 3 - player;
		}
		else 
		{
			System.out.println("Illegal Move");
		}
	}
	
	
}
