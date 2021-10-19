/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package photos;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
	private String username;
	private ArrayList<Album> Albums;
	

	/**
	 * creates a User based off of a String of the new users name
	 * @param name is the String that will become the name of the User
	 */
	public User(String name)
	{
		username = name;
		Albums = new ArrayList<Album>();
	}
	

	/**
	 * returns the username of the User
	 * @return the String associated with the name of the User
	 */
	public String getUsername()
	{
		return username;
	}
	

	/**
	 * returns the Albums that belong to the User)
	 * @return the Albums associated with the name of the User as an ArrayList
	 */
	public ArrayList<Album> getAlbums()
	{
		return Albums;
	}
	
	/**
	 * adds an Album to the user
	 * @param a is the Album to be added
	 */
	public void addAlbum(Album a)
	{
		Albums.add(a);
	}
	
	/**
	 * deletes an Album from the User
	 * @param a is the Album to be deleted
	 */
	public void deleteAlbum(Album a)
	{
		Albums.remove(a);
	}
	
	/**
	 * Compares the User with another User based off of the Users' usernames
	 * @param user is the other User to be compared with
	 * @return 0 or 1 based on if the Users have the same name
	 */
	public int equals(User user) 
	{
		if(user.getUsername().equals(username))
			return 1;
		return 0;
	}

	/**
	 * returns the username of the User (Same as getName())
	 * @return the String associated with the name of the User
	 */
	public String toString()
	{
		return username;
	}
}
