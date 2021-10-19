/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package chess;

import java.util.Scanner;

public class Chess {
	
	public static void main(String[] args) {
		ChessBoard cb = new ChessBoard();
		cb.setupBoard();
		
		Scanner sc = new Scanner(System.in);
		
		String[] colors = {"None", "White", "Black"};
		String move = "";
		String from = "";
		String to = "";
		Boolean drawOffered = false;
		
		while(true) 
		{
			cb.printBoard();
			if(cb.getKing().inCheck(cb))
			{
				if(cb.possibleMove() == false)
				{
					System.out.println("Check Mate");
					if(cb.getPlayer() == 1)
						System.out.println("White wins");
					else
						System.out.println("Black wins");
					break;
				}
				else
					System.out.println("Check");
			}
			else
			{
				if(cb.possibleMove() == false)
				{
					System.out.println("Stalemate");
					break;
				}
			}
			//cb.printBoard();
			
			System.out.print(colors[cb.getPlayer()] + "'s move: ");
			move = sc.nextLine();
			if(move.equals("resign"))
			{
				if(cb.getPlayer() == 2)
					System.out.println("White wins");
				else
					System.out.println("Black wins");
				break;
			}
			
			if(move.equals("draw"))
			{
				if(drawOffered)
				{
					break;
				}
				else
				{
					System.out.println("Invalid Command");
					continue;
				}
			}
			
			if(move.length() > 2)
				if(move.substring(0, move.indexOf(' ')).length() == 2)
				{
					from = move.substring(0, move.indexOf(' '));
					move = move.substring(move.indexOf(' ') + 1);
					if(move.length() > 2)
					{
						if(move.substring(0, move.indexOf(' ')).length() == 2)
						{
							to = move.substring(0, move.indexOf(' '));
							move = move.substring(move.indexOf(' ') + 1);
							if(move.equals("draw?"))
								drawOffered = true;
							else
							{
								System.out.println("Invalid Command");
								continue;
							}
						}
					}
					else if(move.length() == 2)
					{
						to = move;
						drawOffered = false;
					}
					else
					{
						System.out.println("Invalid Command");
						continue;
					}
				}
				else
				{
					System.out.println("Invalid Command");
					continue;
				}
			else
			{
				System.out.println("Invalid Command");
				continue;
			}
			
			
			int fromCol = from.charAt(0) - 'a';
			int fromRow = '8' - from.charAt(1);
			int toCol = to.charAt(0) - 'a';
			int toRow = '8' - to.charAt(1);
			System.out.println();
			cb.movePiece(fromRow, fromCol, toRow, toCol);
			
		}
		
		
		
		sc.close();
	}
	
}
