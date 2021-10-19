/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package photos;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class Photo implements Serializable
{
	private String name;
	private String caption;
	private ArrayList<String> tags;
	private File photo;
	private long date;
	
	/**
	 * makes the photo using a File. Default name is the absolute path of the file
	 * @param path is the file that will be the basis of the Photo
	 */
	public Photo(File path)
	{
		photo = path;
		caption = "";
		name = path.getPath();
		tags = new ArrayList<String>();
		date = path.lastModified();
	}
	
	/**
	 * makes the photo using a String. Only used internally to test if a photo is in an album or not using equals(Photo photo) method
	 * @param name is the String that will be the name of the Photo
	 */
	public Photo(String name)
	{
		this.name = name;
	}
	
	/**
	 * returns a String of the name of the Photo(the absolute path of the photo by default)
	 * @return name is the name associated with the photo
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * changes the name of the Photo
	 * @param newName is what the name will be updated to
	 */
	public void setName(String newName)
	{
		name = newName;
	}
	
	/**
	 * returns a String of the caption of the Photo
	 * @return caption is the caption associated with the photo
	 */
	public String getCaption()
	{
		return caption;
	}
	
	/**
	 * changes the caption of the Photo
	 * @param newCaption is what the caption will be updated to
	 */
	public void setCaption(String newCaption)
	{
		caption = newCaption;
	}
	
	/**
	 * returns all the tags associated with the photo
	 * @return tags is the arraylist with all the tags
	 */
	public ArrayList<String> getTags()
	{
		return tags;
	}
	
	/**
	 * sets the tags of the photo using an arrayList
	 * @param newTags is the new set of tags
	 */
	public void setTags(ArrayList<String> newTags)
	{
		tags = newTags;
	}
	
	/**
	 * adds a tag to the Photo
	 * @param tag is the tag to be added
	 */
	public void addTag(String tag)
	{
		tags.add(tag);
	}

	/**
	 * deletes a tag from the Photo
	 * @param tag is the tag to be deleted
	 */
	public void deleteTag(String tag)
	{
		tags.remove(tag);
	}
	

	/**
	 * returns the File where the image is located
	 * @return the File associated with the Photo
	 */
	public File getFile()
	{
		return photo;
	}
	
	/**
	 * returns the string representation of the date in format: "yyyy-MM-dd hh:mm:ss"
	 * @return the last modified date of the file
	 */
	public String getDate()
	{
		Date d = new Date(date);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		
		return df.format(d);
	}
	
	/**
	 * returns the long representation of the date
	 * @return the last modified date of the file
	 */
	public long getDateLong()
	{
		return date;
	}


	/**
	 * Compares the Photo with another Photo based off of the Photos' names
	 * @param photo is the other Photo to be compared with
	 * @return 0 or 1 based on if the photos have the same name
	 */
	public int equals(Photo photo) 
	{
		if(photo.getName().equals(name))
			return 1;
		return 0;
	}
	

	/**
	 * returns the name of the Photo (Same as getName())
	 * @return the String associated with the name of the Photo
	 */
	public String toString()
	{
		return name;
	}
}
