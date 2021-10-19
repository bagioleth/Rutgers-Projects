/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package photos;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Album implements Serializable
{
	private String albumName;
	private ArrayList<Photo> photos;
	

	/**
	 * Creates an Album based off of a Name
	 * @param name is what the Album will be reffered to as
	 */
	public Album(String name)
	{
		albumName = name;
		photos = new ArrayList<Photo>();
	}
	

	/**
	 * returns the name of the album
	 * @return String associated with the album's name
	 */
	public String getName()
	{
		return albumName;
	}
	

	/**
	 * sets the name of the album to a new value
	 * @param newName is the new name of the Album
	 */
	public void setName(String newName)
	{
		albumName = newName;
	}
	

	/**
	 * returns an arraylist of all the photos in the album
	 * @return the photos in the album
	 */
	public ArrayList<Photo> getPhotos()
	{
		return photos;
	}
	

	/**
	 * adds a Photo to the album
	 * @param photo is the Photo to be added
	 */
	public void addPhoto(Photo photo)
	{
		photos.add(photo);
	}
	
	/**
	 * deletes a Photo from the album
	 * @param photo is the Photo to be deleted
	 */
	public void deletePhoto(Photo photo)
	{
		photos.remove(photo);
	}
	
	/**
	 * returns the date of the earliest photo in the album
	 * @return the earliest date in the album as a string
	 */
	public String getFirstDate()
	{
		long l = Long.MAX_VALUE;
		for(int i = 0; i < photos.size(); i++)
		{
			if(photos.get(i).getDateLong() < l)
				l = photos.get(i).getDateLong();
		}
		Date d = new Date(l);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		if (l == Long.MAX_VALUE) return "";
		return df.format(d);
	}
	
	/**
	 * returns the date of the latest photo in the album
	 * @return the latest date in the album as a string
	 */
	public String getLastDate()
	{
		long l = 0;
		for(int i = 0; i < photos.size(); i++)
		{
			if(photos.get(i).getDateLong() > l)
				l = photos.get(i).getDateLong();
		}
		Date d = new Date(l);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		if (l == 0) return "";
		return df.format(d);
	}
	
	/**
	 * Compares the Album with another Album based off of the Albums' names
	 * @param album is the other Album to be compared with
	 * @return 0 or 1 based on if the Albums have the same name
	 */
	public int equals(Album album) 
	{
		if(album.getName().equals(albumName))
			return 1;
		return 0;
	}

	/**
	 * returns the name of the Album (Same as getName())
	 * @return the String associated with the name of the Album
	 */
	public String toString()
	{
		return albumName;
	}
}
