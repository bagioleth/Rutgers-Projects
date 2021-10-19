/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package photos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class SessionInfo implements Serializable
{
	private ArrayList <User> userList;
	private User user;
	private Album album;
	private Photo photo;
	

	/**
	 * creates a SessionInfo to store the current data of the session
	 */
	public SessionInfo()
	{
		initializeInfo();
	}
	

	/**
	 * creates the stock album on first time running program
	 */
	public void initializeInfo()
	{
		userList = new ArrayList <User> ();
		userList.add(new User("stock"));
		userList.get(0).addAlbum(new Album("stock"));
		Photo photo = new Photo(new File("Beach.jpeg"));
		photo.setName("Beach");
		photo.setCaption("A relaxing day at the beach");
		photo.addTag("Location=Beach");
		photo.addTag("Weather=Sunny");
		photo.addTag("Theme=Nature");
		userList.get(0).getAlbums().get(0).addPhoto(photo);
		
		photo = new Photo(new File("Fall Road.jpg"));
		photo.setName("Fall Road");
		photo.setCaption("The colors are lovely this time of year");
		photo.addTag("Location=Forrest");
		photo.addTag("Color=Red");
		photo.addTag("Theme=Nature");
		userList.get(0).getAlbums().get(0).addPhoto(photo);
		
		photo = new Photo(new File("Spider.jpeg"));
		photo.setName("Spider");
		photo.setCaption("Spiders are dumb");
		photo.addTag("Animal=Arachnid");
		photo.addTag("Number of Eyes=Eight");
		userList.get(0).getAlbums().get(0).addPhoto(photo);
		
		photo = new Photo(new File("Thumbs Up.jpg"));
		photo.setName("Thumbs Up");
		photo.setCaption("Charlie giving me two big thumbs up for a job well done!");
		photo.addTag("Person=Charlie");
		photo.addTag("Job=Office Worker");
		photo.addTag("Theme=Positivity");
		userList.get(0).getAlbums().get(0).addPhoto(photo);
		
		photo = new Photo(new File("Train Tracks.jpg"));
		photo.setName("Train Tracks");
		photo.setCaption("Looks so peacefull");
		photo.addTag("Location=Forrest");
		photo.addTag("Weather=Sunny");
		photo.addTag("Theme=Nature");
		userList.get(0).getAlbums().get(0).addPhoto(photo);
	}
	

	/**
	 * Returns the list of valid users
	 * @return the ArrayList representing all of the users
	 */
	public ArrayList <User> getUsers()
	{
		return userList;
	}
	

	/**
	 * returns the current user that is logged in
	 * @return the User that is logged in
	 */
	public User getUser()
	{
		return user;
	}

	/**
	 * sets the current user to a new user(logging in)
	 * @param newUser is the user to be logged in
	 */
	public void setUser(User newUser)
	{
		user = newUser;
	}

	/**
	 * returns the current Album that is being viewed
	 * @return the Album that is being viewed
	 */
	public Album getAlbum()
	{
		return album;
	}	
	
	/**
	 * sets the current Album to a new Album
	 * @param newAlbum to be switched to
	 */
	public void setAlbum(Album newAlbum)
	{
		album = newAlbum;
	}
	
	/**
	 * returns the current Photo that is being viewed
	 * @return the Photo that is being viewed
	 */
	public Photo getPhoto()
	{
		return photo;
	}
	
	/**
	 * sets the current Photo to a new Photo
	 * @param newPhoto to be switched to
	 */
	public void setPhoto(Photo newPhoto)
	{
		photo = newPhoto;
	}
	
	/**
	 * adds a User to the list of valid users
	 * @param user to be added
	 */
	public void addUser(User user)
	{
		userList.add(user);
	}

	/**
	 * deleted a User from the list of valid users
	 * @param user to be deleted
	 */
	public void deleteUser(User user)
	{
		userList.remove(user);
	}

}
