/**
 * @author Roger Chan
 * @author Brian Angioletti
 * 
 * 
 */
package photos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class SceneChanger extends Application
{
	private Stage pStage;
	private static SessionInfo info;
	

	/**
	 * initializes the SessionInfo() (metadata) for the program and launches the javaFX GUI
	 * @param args that are used to launch program
	 */
	public static void main(String[] args) 
	{
		//picks up the program where it left off
		
		File f = new File("Obj.txt");
		try {
		FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);
		info = (SessionInfo) ois.readObject();
		ois.close();
		} catch (ClassNotFoundException  | IOException e) {info = new SessionInfo();}
		
		launch(args);
		
	}
	

	/**
	 * changes the GUI to the Login Screen where the user is prompted for their username. Type "admin" (case insensitive) to get to the admin page
	 */
	public void changeToLogin()
	{
		pStage.setTitle("Login to Photos");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 100));
		
		//makes components
		Label usernameLabel = new Label("Username:");
		TextField usernameField = new TextField();
		Button submitButton = new Button("Login");
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{
			if(usernameField.getText().equalsIgnoreCase("Admin")) 
				changeToAdmin();
			else 
			{
				User temp = new User(usernameField.getText());
				for(int i = 0; i < info.getUsers().size(); i++)
					if(info.getUsers().get(i).equals(temp) == 1) 
					{
						login(info.getUsers().get(i)); 
						changeToUser();
					}
					else if (i == info.getUsers().size() - 1) 
						usernameField.setText("Invalid username");
			}
		});
		
		//next three lines right aligns the login button
		HBox submitContainer = new HBox();
        submitContainer.getChildren().add(submitButton);
        submitContainer.setAlignment(Pos.BASELINE_RIGHT);
		
        
        //adds the components
		layout.getChildren().add(usernameLabel);
		layout.getChildren().add(usernameField);
		layout.getChildren().add(submitContainer);
	}
	
	/**
	 * changes the GUI to the Admin Screen where users can be added or deleted and are displayed in a list
	 */
	public void changeToAdmin()
	{
		pStage.setTitle("Administator Page");
		
		VBox layout = new VBox();
		pStage.setScene(new Scene(layout, 350, 400));
		
		//makes components
		Label welcomeLabel = new Label("Welcome: Admin");
		Label userListLabel = new Label("List of Users:");
		Button logoutButton = new Button("Logout");
		
		ObservableList<String> userList = FXCollections.<String>observableArrayList();
		ListView<String> userListView = new ListView<String>(userList);
		for(int i = 0; i < info.getUsers().size(); i ++)
		{
			userList.add(info.getUsers().get(i).toString());
		}
		
		Button addUserButton = new Button("Add User");
		Button deleteUserButton = new Button("Delete User");
		
		//sets actions for buttons
		
		logoutButton.setOnAction(e -> {logout(); changeToLogin();});
		addUserButton.setOnAction(e -> changeToAddUserPopup());
		deleteUserButton.setOnAction(e -> changeToDeleteUserPopup());
		
		//adds the components
		VBox labels = new VBox();
		labels.getChildren().add(welcomeLabel);
		labels.getChildren().add(userListLabel);
		labels.setSpacing(15);
		
		HBox top = new HBox();
		top.getChildren().add(labels);
		top.getChildren().add(logoutButton);
		top.setSpacing(200);
		
		HBox bottom = new HBox();
		bottom.getChildren().add(addUserButton);
		bottom.getChildren().add(deleteUserButton);
		bottom.setSpacing(200);
		
		layout.getChildren().add(top);
		layout.getChildren().add(userListView);
		layout.getChildren().add(bottom);
		layout.setSpacing(15);
	}
	
	/**
	 * changes the GUI to the Add User PopUp that prompts the admin for the name of the new user to add
	 */
	public void changeToAddUserPopup()
	{
		pStage.setTitle("Add User");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 100));
		
		//makes components
		Label addUserLabel = new Label("Add User:");
		TextField addUserField = new TextField();
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		
		
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{
			User temp = new User(addUserField.getText());
			for(int i = 0; i < info.getUsers().size(); i++)
			{
				if(info.getUsers().get(i).equals(temp) == 1) 
					addUserField.setText("User already exists");
				else if (i == info.getUsers().size() - 1) 
				{
					addUser(temp); 
					changeToAdmin();
				}
			}
			if (info.getUsers().size() == 0) 
			{
				addUser(temp); 
				changeToAdmin();
			}
		});
		
		cancelButton.setOnAction(e -> changeToAdmin());
		
		
        
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        
		layout.getChildren().add(addUserLabel);
		layout.getChildren().add(addUserField);
		layout.getChildren().add(buttons);
	}
	/**
	 * changes the GUI to the Delete User PopUp that prompts the admin for the name of the user to delete
	 */
	public void changeToDeleteUserPopup()
	{
		pStage.setTitle("Delete User");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 100));
		
		//makes components
		Label deleteUserLabel = new Label("Delete User:");
		TextField deleteUserField = new TextField();
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{
			User temp = new User(deleteUserField.getText());
			if (info.getUsers().size() == 0) 
			{
				deleteUserField.setText("User doesn't exists");
				return;
			}
			for(int i = 0; i < info.getUsers().size(); i++)
				if(info.getUsers().get(i).equals(temp) == 1) 
				{
					deleteUser(info.getUsers().get(i)); 
					changeToAdmin();
				}
				else if (i == info.getUsers().size() - 1) 
					deleteUserField.setText("User doesn't exists");
			
		});
		
		cancelButton.setOnAction(e -> changeToAdmin());
        
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        
		layout.getChildren().add(deleteUserLabel);
		layout.getChildren().add(deleteUserField);
		layout.getChildren().add(buttons);
	}
	/**
	 * changes the GUI to the Screen that displays the albums as well as has options to add/delete albums or search photos. Built based on information stored is SessionInfo
	 */
	public void changeToUser()
	{
		pStage.setTitle("Photos Application");

		VBox layout = new VBox();
		pStage.setScene(new Scene(layout, 350, 400));
		
		//makes components
		Label welcomeLabel = new Label("Welcome: " + info.getUser().getUsername());
		Label albumsLabel = new Label("Albums");
		Button logoutButton = new Button("Logout");
		
		
		ObservableList<String> albumList = FXCollections.<String>observableArrayList();
		ListView<String> albumListView = new ListView<String>(albumList);
		for(int i = 0; i < info.getUser().getAlbums().size(); i ++)
		{
			Album a = info.getUser().getAlbums().get(i);
			albumList.add(a.toString()
					 + "\n\tPictures: " + a.getPhotos().size()
					 + "\n\tFirst Date: " + a.getFirstDate()
					 + "\n\tLast Date: " + a.getLastDate());
		}
		
		albumListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>()				//changes current song when new song selected
			{public void changed(ObservableValue<? extends String> ov, final String oldValue, final String newValue) 
		        {
					int index = albumList.indexOf(newValue);
					info.setAlbum(info.getUser().getAlbums().get(index));
					changeToAlbumViewer();
		        }});
		
		Button searchAlbumsButton = new Button("Search Albums");
		Button addNewAlbumButton = new Button("Add New Album");
		Button deleteAlbumButton = new Button("Delete Album");

		//sets actions for buttons
		logoutButton.setOnAction(e -> {logout(); changeToLogin();});
		searchAlbumsButton.setOnAction(e -> changeToSearchTypePopup());
		addNewAlbumButton.setOnAction(e -> changeToAddAlbumPopup());
		deleteAlbumButton.setOnAction(e -> changeToDeleteAlbumPopup());
		
		//adds the components
		VBox labels = new VBox();
		labels.getChildren().add(welcomeLabel);
		labels.getChildren().add(albumsLabel);
		labels.setSpacing(15);
		
		HBox top = new HBox();
		top.getChildren().add(labels);
		top.getChildren().add(logoutButton);
		top.setSpacing(200);
		
		HBox bottom = new HBox();
		bottom.getChildren().add(searchAlbumsButton);
		bottom.getChildren().add(addNewAlbumButton);
		bottom.getChildren().add(deleteAlbumButton);
		bottom.setSpacing(30);
		
		layout.getChildren().add(top);
		layout.getChildren().add(albumListView);
		layout.getChildren().add(bottom);
		layout.setSpacing(15);
	}
	
	public void changeToSearchTypePopup()
	{
		pStage.setTitle("Search for Photos");

		HBox layout = new HBox();
		pStage.setScene(new Scene(layout, 350, 50));
		
		//makes components
		
		Button searchByDateButton = new Button("Search By Date");
		Button searchByTagsButton = new Button("Search By Tags");
		Button cancelButton = new Button("Cancel");
		
		//sets actions for buttons
		searchByDateButton.setOnAction(e -> changeToSearchByDate());
		searchByTagsButton.setOnAction(e -> changeToSearchByTags());
		cancelButton.setOnAction(e -> changeToUser());
		
		//adds the components
		HBox buttonBox = new HBox();
		buttonBox.getChildren().add(searchByDateButton);
		buttonBox.getChildren().add(searchByTagsButton);
		layout.getChildren().add(buttonBox);
		layout.getChildren().add(cancelButton);
		layout.setSpacing(50);
	}
	
	public void changeToSearchByDate()
	{
		pStage.setTitle("Search for Photos By Date");

		HBox layout = new HBox();
		pStage.setScene(new Scene(layout, 175, 175));
		
		//makes components
		Label startDateLabel = new Label("Start Date:");
		DatePicker startDatePicker = new DatePicker();
		startDatePicker.setValue(LocalDate.now());
		Label endDateLabel = new Label("End Date:");
		DatePicker endDatePicker = new DatePicker();
		endDatePicker.setValue(LocalDate.now());
		Button searchByDateButton = new Button("Search By Date");
		Button cancelButton = new Button("Cancel");

		//sets actions for buttons
		searchByDateButton.setOnAction(e -> {searchByDate(startDatePicker.getValue(), endDatePicker.getValue()); changeToSearchResultsViewer();});
		cancelButton.setOnAction(e -> changeToUser());
		
		//adds the components
		VBox left = new VBox();
		left.getChildren().add(startDateLabel);
		left.getChildren().add(startDatePicker);
		left.getChildren().add(endDateLabel);
		left.getChildren().add(endDatePicker);
		left.getChildren().add(searchByDateButton);
		left.getChildren().add(cancelButton);
		left.setSpacing(5);
		
		
		layout.getChildren().add(left);
		layout.setSpacing(50);
	}
	
	public void changeToSearchByTags()
	{
		pStage.setTitle("Search for Photos By Tags");

		HBox layout = new HBox();
		pStage.setScene(new Scene(layout, 275, 250));
		
		//makes components
		
		Label firstTagLabel = new Label("First Tag (Mandatory)");
		Label firstTagTypeLabel = new Label("Type: ");
		TextField firstTagTypeField = new TextField("");
		Label firstTagValueLabel = new Label("Value: ");
		TextField firstTagValueField = new TextField("");
		
		ToggleGroup rGroup = new ToggleGroup();
		RadioButton disjunctOption = new RadioButton("Disjunction");
		RadioButton conjunctOption = new RadioButton("Conjunction");
		disjunctOption.fire();// disjunct Option is the default option
		disjunctOption.setToggleGroup(rGroup);
		conjunctOption.setToggleGroup(rGroup);
		
		Label secondTagLabel = new Label("Second Tag (Optional)");
		Label secondTagTypeLabel = new Label("Type: ");
		TextField secondTagTypeField = new TextField("");
		Label secondTagValueLabel = new Label("Value: ");
		TextField secondTagValueField = new TextField("");
		Button searchByTagsButton = new Button("Search By Tags");
		
		Button cancelButton = new Button("Cancel");

		//sets actions for buttons
		searchByTagsButton.setOnAction(e -> 
		{
			searchByTags((firstTagTypeField.getText() + "=" + firstTagValueField.getText()), 
							(secondTagTypeField.getText() + "=" + secondTagValueField.getText()),
							disjunctOption.isSelected());
			changeToSearchResultsViewer();
														
		});
		cancelButton.setOnAction(e -> changeToUser());
		
		//adds the components
		
		VBox right = new VBox();
		right.getChildren().add(firstTagLabel);
		
		HBox firstTagLabelsBox = new HBox();
		firstTagLabelsBox.getChildren().add(firstTagTypeLabel);
		firstTagLabelsBox.getChildren().add(firstTagValueLabel);
		firstTagLabelsBox.setSpacing(110);
		right.getChildren().add(firstTagLabelsBox);
		
		HBox firstTagValuesBox = new HBox();
		firstTagValuesBox.getChildren().add(firstTagTypeField);
		firstTagValuesBox.getChildren().add(firstTagValueField);
		right.getChildren().add(firstTagValuesBox);
		
		right.getChildren().add(secondTagLabel);
		
		HBox secondTagLabelsBox = new HBox();
		secondTagLabelsBox.getChildren().add(secondTagTypeLabel);
		secondTagLabelsBox.getChildren().add(secondTagValueLabel);
		secondTagLabelsBox.setSpacing(110);
		right.getChildren().add(secondTagLabelsBox);
		
		HBox secondTagValuesBox = new HBox();
		secondTagValuesBox.getChildren().add(secondTagTypeField);
		secondTagValuesBox.getChildren().add(secondTagValueField);
		right.getChildren().add(secondTagValuesBox);
		
		right.getChildren().add(disjunctOption);
		right.getChildren().add(conjunctOption);
		right.getChildren().add(searchByTagsButton);
		right.getChildren().add(cancelButton);
		
		layout.getChildren().add(right);
	}
	
	
	/**
	 * changes the GUI to the Add Album Popup where the user is prompted for a name for the new album
	 */
	public void changeToAddAlbumPopup()
	{
		pStage.setTitle("Add Album");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 100));
		
		//makes components
		Label addAlbumLabel = new Label("Add Album:");
		TextField addAlbumField = new TextField();
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{
			Album temp = new Album(addAlbumField.getText());
			for(int i = 0; i < info.getUser().getAlbums().size(); i++)
			{
				if(info.getUser().getAlbums().get(i).equals(temp) == 1) 
					addAlbumField.setText("Album Name Already Exists");
				else if (i == info.getUser().getAlbums().size() - 1) 
				{
					addAlbum(temp); 
					changeToUser();
				}
			}
			
			if(info.getUser().getAlbums().size() == 0)
			{
				addAlbum(temp);
				changeToUser();
			}
		});
		
		cancelButton.setOnAction(e -> changeToUser());
		
		
        
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        
		layout.getChildren().add(addAlbumLabel);
		layout.getChildren().add(addAlbumField);
		layout.getChildren().add(buttons);
	}
	/**
	 * changes the GUI to the Delete Album Popup where the user is promted for the name of the album to be deleted
	 */
	public void changeToDeleteAlbumPopup()
	{
		pStage.setTitle("Delete Album");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 100));
		
		//makes components
		Label deleteAlbumLabel = new Label("Delete Album:");
		TextField deleteAlbumField = new TextField();
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{
			Album temp = new Album(deleteAlbumField.getText());
			if(info.getUser().getAlbums().size() == 0)
			{
				deleteAlbumField.setText("Folder not found");
			}
			for(int i = 0; i < info.getUser().getAlbums().size(); i++)
				if(info.getUser().getAlbums().get(i).equals(temp) == 1) 
				{
					deleteAlbum(info.getUser().getAlbums().get(i)); 
					changeToUser();
				}
				else if (i == info.getUser().getAlbums().size()) 
					deleteAlbumField.setText("Folder not found");
			
		});
		
		cancelButton.setOnAction(e -> changeToUser());
		
		
        
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        
		layout.getChildren().add(deleteAlbumLabel);
		layout.getChildren().add(deleteAlbumField);
		layout.getChildren().add(buttons);
	}
	/**
	 * changes the GUI to the Album Viewer that displays all of the photos that are contained in the album selected from the previous screen.
	 * User has options to add/delete/copy/move photos as well as add/delete tags
	 */
	public void changeToAlbumViewer() 
	{
		pStage.setTitle("Photo Viewer");
		
		HBox layout = new HBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 800, 600));
		
		//makes components
		Label albumLabel = new Label("Album: ");
		TextField albumNameField = new TextField(info.getAlbum().toString());
		Button backToAlbumsButton = new Button("Back to Albums");
		Button addPhotosButton = new Button("Add Photos");
		
		ObservableList<ImageView> thumbnailList = FXCollections.<ImageView>observableArrayList();
		ListView<ImageView> thumbnailListView = new ListView<ImageView>(thumbnailList);
		try {
		for(int i = 0; i < info.getAlbum().getPhotos().size(); i ++)
		{
			Image thumbnailImage = new Image(new FileInputStream(info.getAlbum().getPhotos().get(i).getFile().getAbsolutePath()));
			
			ImageView thumbnailPhoto = new ImageView();
			thumbnailPhoto.setFitWidth(105);
			thumbnailPhoto.setFitHeight(90);
			thumbnailPhoto.setImage(thumbnailImage);
			thumbnailList.add(thumbnailPhoto);
		}} catch(FileNotFoundException e) {}
		
		thumbnailListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImageView>()
			{public void changed(ObservableValue<? extends ImageView> ov, final ImageView oldValue, final ImageView newValue) 
		        {
					int index = thumbnailList.indexOf(newValue);
					info.setPhoto(info.getAlbum().getPhotos().get(index));
					changeToAlbumViewer();
		        }});
		
		Label photoNameLabel = new Label("Photo Name:");
		TextField photoNameField = new TextField("Photo Name goes here");//only changes when picture isn't null
		Button deletePhotoButton = new Button("Delete Photo");
		ImageView photo = new ImageView();
		
		Label captionLabel = new Label("Caption:");
		TextField captionField = new TextField("Caption goes here");//changes when picture isn't null
		
		Label dateLabel = new Label("Date: ");
		
		try {
			if(info.getPhoto() != null)
			{
				photoNameField.setText(info.getPhoto().getName());
				captionField.setText(info.getPhoto().getCaption());
				dateLabel.setText("Date: " + info.getPhoto().getDate());
				Image image = new Image(new FileInputStream(info.getPhoto().getFile().getAbsolutePath()));
				photo.setFitWidth(525);  //images bigger than this mess up the format of everything so everything will be this size
				photo.setFitHeight(450);
				photo.setImage(image);
			}
		}catch (FileNotFoundException e)//file should always be found unless it was deleted while the program is running
		{}
		
		Label tagsLabel = new Label("Tags:");
		ListView<String> tagsListView = new ListView<String>();
		if(info.getPhoto() != null)
		{
			ObservableList<String> tagsList = FXCollections.<String>observableArrayList();
			tagsListView = new ListView<String>(tagsList);
			for(int i = 0; i < info.getPhoto().getTags().size(); i ++)
			{
				tagsList.add(info.getPhoto().getTags().get(i).toString());
			}
		}
		
		Button addTagButton = new Button("Add Tag");
		Button deleteTagButton = new Button("Delete Tag");
		Button previousButton = new Button("Previous");
		Button movePhotoButton = new Button("Move Photo");
		Button copyPhotoButton = new Button("Copy Photo");
		Button nextButton = new Button("Next");
		
		
		
		//sets action for button
		backToAlbumsButton.setOnAction(e -> {changeToUser(); info.setPhoto(null);});
		addPhotosButton.setOnAction(e -> 
		{
			FileChooser fChooser = new FileChooser();
			addPhoto(fChooser.showOpenDialog(pStage));
			changeToAlbumViewer();
		});
		deletePhotoButton.setOnAction(e -> {changeToDeletePhotoPopup();});
		addTagButton.setOnAction(e -> {changeToAddTagPopup();});
		deleteTagButton.setOnAction(e -> {changeToDeleteTagPopup();});
		movePhotoButton.setOnAction(e -> {changeToMovePhotoPopup();});
		copyPhotoButton.setOnAction(e -> {changeToCopyPhotoPopup();});
		previousButton.setOnAction(e -> 
		{
			Photo temp = new Photo(info.getPhoto().getName());
			for(int i = 0; i < info.getAlbum().getPhotos().size(); i++)
			{
				if(info.getPhoto().equals(temp) == 1)
				{
					if(i == 0)
					{
						info.setPhoto(info.getAlbum().getPhotos().get(info.getAlbum().getPhotos().size() - 1));
						changeToAlbumViewer();
					}
					else
					{

						info.setPhoto(info.getAlbum().getPhotos().get(i - 1));
						changeToAlbumViewer();
					}
				}
			}
		});
		nextButton.setOnAction(e -> 
		{
			Photo temp = new Photo(info.getPhoto().getName());
			for(int i = 0; i < info.getAlbum().getPhotos().size(); i++)
			{
				if(info.getPhoto().equals(temp) == 1)
				{
					if(i == 0)
					{
						info.setPhoto(info.getAlbum().getPhotos().get(info.getAlbum().getPhotos().size() - 1));
						changeToAlbumViewer();
					}
					else
					{

						info.setPhoto(info.getAlbum().getPhotos().get(i - 1));
						changeToAlbumViewer();
					}
				}
			}
		});
		
		albumNameField.setOnAction(e -> {info.getAlbum().setName(albumNameField.getText()); changeToAlbumViewer();});
		photoNameField.setOnAction(e -> {info.getPhoto().setName(photoNameField.getText()); changeToAlbumViewer();});
		captionField.setOnAction(e -> info.getPhoto().setCaption(captionField.getText()));
        
        //adds the components
		VBox left = new VBox();
		HBox albumNameBox = new HBox();
		albumNameBox.getChildren().add(albumLabel);
		albumNameBox.getChildren().add(albumNameField);
		HBox leftButtons = new HBox();
		leftButtons.getChildren().add(backToAlbumsButton);
		leftButtons.getChildren().add(addPhotosButton);
		left.getChildren().add(albumNameBox);
		left.getChildren().add(leftButtons);
		left.getChildren().add(thumbnailListView);
		
		VBox right = new VBox();
		HBox top = new HBox();
		top.getChildren().add(photoNameLabel);
		top.getChildren().add(photoNameField);
		top.getChildren().add(deletePhotoButton);
		right.getChildren().add(top);
		right.getChildren().add(photo);
		
		VBox bottom = new VBox();
		HBox caption = new HBox();
		caption.getChildren().add(captionLabel);
		caption.getChildren().add(captionField);
		
		HBox bottomOfBottom = new HBox();
		bottomOfBottom.getChildren().add(tagsLabel);
		bottomOfBottom.getChildren().add(tagsListView);
		
		VBox buttonsLeft = new VBox();
		buttonsLeft.getChildren().add(addTagButton);
		buttonsLeft.getChildren().add(deleteTagButton);
		buttonsLeft.getChildren().add(previousButton);
		bottomOfBottom.getChildren().add(buttonsLeft);
		
		VBox buttonsRight = new VBox();
		buttonsRight.getChildren().add(movePhotoButton);
		buttonsRight.getChildren().add(copyPhotoButton);
		buttonsRight.getChildren().add(nextButton);
		bottomOfBottom.getChildren().add(buttonsRight);
		
		bottom.getChildren().add(caption);
		bottom.getChildren().add(dateLabel);
		bottom.getChildren().add(bottomOfBottom);
		
		right.getChildren().add(bottom);
		
		if(info.getPhoto() == null)//restricts access when no photo is selected
		{
			right.setVisible(false);
		}
		
		layout.getChildren().add(left);
		layout.getChildren().add(right);
	}
	
	/**
	 * changes the GUI to the Search Result Viewer that displays all of the photos that satisfy the conditions of the search.
	 * User has options to add/delete/copy/move photos as well as add/delete tags
	 */
	public void changeToSearchResultsViewer() 
	{
		pStage.setTitle("Search Results");
		
		HBox layout = new HBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 800, 600));
		
		//makes components
		Label albumLabel = new Label("Search Results");
		Button backToAlbumsButton = new Button("Back to Albums");
		Button saveResultsButton = new Button("Save as Album");
		
		ObservableList<ImageView> thumbnailList = FXCollections.<ImageView>observableArrayList();
		ListView<ImageView> thumbnailListView = new ListView<ImageView>(thumbnailList);
		try {
		for(int i = 0; i < info.getAlbum().getPhotos().size(); i ++)
		{
			Image thumbnailImage = new Image(new FileInputStream(info.getAlbum().getPhotos().get(i).getFile().getAbsolutePath()));
			
			ImageView thumbnailPhoto = new ImageView();
			thumbnailPhoto.setFitWidth(105);
			thumbnailPhoto.setFitHeight(90);
			thumbnailPhoto.setImage(thumbnailImage);
			thumbnailList.add(thumbnailPhoto);
		}} catch(FileNotFoundException e) {}
		
		thumbnailListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ImageView>()
			{public void changed(ObservableValue<? extends ImageView> ov, final ImageView oldValue, final ImageView newValue) 
		        {
					int index = thumbnailList.indexOf(newValue);
					info.setPhoto(info.getAlbum().getPhotos().get(index));
					changeToSearchResultsViewer();
		        }});
		
		Label photoNameLabel = new Label("Photo Name:");
		TextField photoNameField = new TextField("Photo Name goes here");//only changes when picture isn't null
		ImageView photo = new ImageView();
		
		Label captionLabel = new Label("Caption:");
		TextField captionField = new TextField("Caption goes here");//changes when picture isn't null
		
		Label dateLabel = new Label("Date: ");
		
		try {
			if(info.getPhoto() != null)
			{
				photoNameField.setText(info.getPhoto().getName());
				captionField.setText(info.getPhoto().getCaption());
				dateLabel.setText("Date: " + info.getPhoto().getDate());
				Image image = new Image(new FileInputStream(info.getPhoto().getFile().getAbsolutePath()));
				photo.setFitWidth(525);  //images bigger than this mess up the format of everything so everything will be this size
				photo.setFitHeight(450);
				photo.setImage(image);
				photoNameField.setEditable(false);
				captionField.setEditable(false);
			}
		}catch (FileNotFoundException e)//file should always be found unless it was deleted while the program is running
		{}
		
		Label tagsLabel = new Label("Tags:");
		ListView<String> tagsListView = new ListView<String>();
		if(info.getPhoto() != null)
		{
			ObservableList<String> tagsList = FXCollections.<String>observableArrayList();
			tagsListView = new ListView<String>(tagsList);
			for(int i = 0; i < info.getPhoto().getTags().size(); i ++)
			{
				tagsList.add(info.getPhoto().getTags().get(i).toString());
			}
		}
		
		Button previousButton = new Button("Previous");
		Button nextButton = new Button("Next");
		
		
		
		//sets action for button
		backToAlbumsButton.setOnAction(e -> {changeToUser(); info.setPhoto(null);});
		saveResultsButton.setOnAction(e -> {info.getUser().addAlbum(info.getAlbum()); changeToAlbumViewer();});
		previousButton.setOnAction(e -> 
		{
			Photo temp = new Photo(info.getPhoto().getName());
			for(int i = 0; i < info.getAlbum().getPhotos().size(); i++)
			{
				if(info.getPhoto().equals(temp) == 1)
				{
					if(i == 0)
					{
						info.setPhoto(info.getAlbum().getPhotos().get(info.getAlbum().getPhotos().size() - 1));
						changeToSearchResultsViewer();
					}
					else
					{

						info.setPhoto(info.getAlbum().getPhotos().get(i - 1));
						changeToSearchResultsViewer();
					}
				}
			}
		});
		nextButton.setOnAction(e -> 
		{
			Photo temp = new Photo(info.getPhoto().getName());
			for(int i = 0; i < info.getAlbum().getPhotos().size(); i++)
			{
				if(info.getPhoto().equals(temp) == 1)
				{
					if(i == 0)
					{
						info.setPhoto(info.getAlbum().getPhotos().get(info.getAlbum().getPhotos().size() - 1));
						changeToSearchResultsViewer();
					}
					else
					{

						info.setPhoto(info.getAlbum().getPhotos().get(i - 1));
						changeToSearchResultsViewer();
					}
				}
			}
		});
		
        
        //adds the components
		VBox left = new VBox();
		HBox albumNameBox = new HBox();
		albumNameBox.getChildren().add(albumLabel);
		HBox leftButtons = new HBox();
		leftButtons.getChildren().add(backToAlbumsButton);
		leftButtons.getChildren().add(saveResultsButton);
		left.getChildren().add(albumNameBox);
		left.getChildren().add(leftButtons);
		left.getChildren().add(thumbnailListView);
		
		VBox right = new VBox();
		HBox top = new HBox();
		top.getChildren().add(photoNameLabel);
		top.getChildren().add(photoNameField);
		right.getChildren().add(top);
		right.getChildren().add(photo);
		
		VBox bottom = new VBox();
		HBox caption = new HBox();
		caption.getChildren().add(captionLabel);
		caption.getChildren().add(captionField);
		
		HBox bottomOfBottom = new HBox();
		bottomOfBottom.getChildren().add(tagsLabel);
		bottomOfBottom.getChildren().add(tagsListView);
		
		VBox buttonsLeft = new VBox();
		buttonsLeft.getChildren().add(previousButton);
		bottomOfBottom.getChildren().add(buttonsLeft);
		
		VBox buttonsRight = new VBox();
		buttonsRight.getChildren().add(nextButton);
		bottomOfBottom.getChildren().add(buttonsRight);
		
		bottom.getChildren().add(caption);
		bottom.getChildren().add(dateLabel);
		bottom.getChildren().add(bottomOfBottom);
		
		right.getChildren().add(bottom);
		
		if(info.getPhoto() == null)//restricts access when no photo is selected
		{
			right.setVisible(false);
		}
		
		layout.getChildren().add(left);
		layout.getChildren().add(right);
	}
	
	/**
	 * changes the GUI to the Delete Photo popup where the user is prompted for the name of the photo to delete
	 */
	public void changeToDeletePhotoPopup()
	{
		pStage.setTitle("Delete Photo");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 100));
		
		//makes components
		Label deletePhotoLabel = new Label("Delete Photo?");
		TextField deletePhotoField = new TextField();
		Button submitButton = new Button("Continue");
		Button cancelButton = new Button("Cancel");
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{
			deletePhoto(info.getPhoto());
			changeToAlbumViewer();
		});
		
		cancelButton.setOnAction(e -> changeToAlbumViewer());
		
		
        
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        
		layout.getChildren().add(deletePhotoLabel);
		layout.getChildren().add(deletePhotoField);
		layout.getChildren().add(buttons);
	}
	/**
	 * changes the GUI to the Add Tag Popup where the user is prompted for the tag to be added to the photo
	 */
	public void changeToAddTagPopup()
	{
		pStage.setTitle("Add Tag");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 120));
		
		//makes components
		Label addTagLabel = new Label("Add Tag:");
		Label addTagTypeLabel = new Label("Type: ");
		TextField addTagTypeField = new TextField();
		Label addTagValueLabel = new Label("Value: ");
		TextField addTagValueField = new TextField();
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{if(info.getPhoto().getTags().indexOf(addTagTypeField.getText() + "=" + addTagValueField.getText()) == -1) 
		{
			addTag(addTagTypeField.getText() + "=" + addTagValueField.getText()); 
			changeToAlbumViewer();
		}
		else 
		{
			addTagTypeField.setText("Tag already exists");
			addTagValueField.setText("");
		}});
		
		cancelButton.setOnAction(e -> changeToAlbumViewer());
		
		
        
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        
        HBox labels = new HBox();
        labels.getChildren().add(addTagTypeLabel);
        labels.getChildren().add(addTagValueLabel);
        labels.setSpacing(110);
        
        HBox fields = new HBox();
        fields.getChildren().add(addTagTypeField);
        fields.getChildren().add(addTagValueField);
        
		layout.getChildren().add(addTagLabel);
		layout.getChildren().add(labels);
		layout.getChildren().add(fields);
		layout.getChildren().add(buttons);
	}
	/**
	 * changes the GUI to the Delete Tag Popup where the user is prompted for the tag to be deleted from the photo
	 */
	public void changeToDeleteTagPopup()
	{
		pStage.setTitle("Delete Tag");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 120));
		
		//makes components
		Label deleteTagLabel = new Label("Delete Tag:");
		Label deleteTagTypeLabel = new Label("Type: ");
		TextField deleteTagTypeField = new TextField();
		Label deleteTagValueLabel = new Label("Value: ");
		TextField deleteTagValueField = new TextField();
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{
		if(info.getPhoto().getTags().indexOf(deleteTagTypeField.getText() + "=" + deleteTagValueField.getText()) != -1) 
		{
			deleteTag(deleteTagTypeField.getText() + "=" + deleteTagValueField.getText()); 
			changeToAlbumViewer();
		}
		else 
		{
			deleteTagTypeField.setText("Tag not found");
			deleteTagValueField.setText("");
		}});
		
		cancelButton.setOnAction(e -> changeToAlbumViewer());
		
		
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        HBox labels = new HBox();
        labels.getChildren().add(deleteTagTypeLabel);
        labels.getChildren().add(deleteTagValueLabel);
        labels.setSpacing(110);
        
        HBox fields = new HBox();
        fields.getChildren().add(deleteTagTypeField);
        fields.getChildren().add(deleteTagValueField);
        
		layout.getChildren().add(deleteTagLabel);
		layout.getChildren().add(labels);
		layout.getChildren().add(fields);
		layout.getChildren().add(buttons);
	}
	/**
	 * changes the GUI to the Move Photo Popup where the user is prompted for the album to move the photo to
	 */
	public void changeToMovePhotoPopup()
	{
		pStage.setTitle("Add Album");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 100));
		
		//makes components
		Label movePhotoLabel = new Label("Move to Folder:");
		TextField movePhotoField = new TextField();
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		
		//sets action for button
		
		submitButton.setOnAction(e -> 
		{
			Album temp  = new Album(movePhotoField.getText());
			for(int i = 0; i < info.getUser().getAlbums().size(); i++)
				if(info.getUser().getAlbums().get(i).equals(temp) == 1) 
				{
					movePhoto(info.getUser().getAlbums().get(i)); 
					changeToAlbumViewer();
				}
				else if(i == info.getUser().getAlbums().size() - 1)
					movePhotoField.setText("Destination folder not found");});
		
		cancelButton.setOnAction(e -> changeToAlbumViewer());
		
		
        
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        
		layout.getChildren().add(movePhotoLabel);
		layout.getChildren().add(movePhotoField);
		layout.getChildren().add(buttons);
	}
	/**
	 * changes the GUI to the Copy Photo Popup where the user is prompted for the album to copy the photo to
	 */
	public void changeToCopyPhotoPopup()
	{
		pStage.setTitle("Copy Photo");
		
		VBox layout = new VBox();
		layout.setSpacing(10);
		pStage.setScene(new Scene(layout, 275, 100));
		
		//makes components
		Label copyPhotoLabel = new Label("Copy to Folder:");
		TextField copyPhotoField = new TextField();
		Button submitButton = new Button("Submit");
		Button cancelButton = new Button("Cancel");
		
		//sets action for button
		submitButton.setOnAction(e -> 
		{
			Album temp  = new Album(copyPhotoField.getText());
			for(int i = 0; i < info.getUser().getAlbums().size(); i++)
			if(info.getUser().getAlbums().get(i).equals(temp) == 1) 
			{
				copyPhoto(info.getUser().getAlbums().get(i)); 
				changeToAlbumViewer();
			}
			else if(i == info.getUser().getAlbums().size() - 1)
				copyPhotoField.setText("Destination folder not found");});
		
		cancelButton.setOnAction(e -> changeToAlbumViewer());
		
		
        
        //adds the components
		HBox buttons = new HBox();
        buttons.getChildren().add(cancelButton);
        buttons.getChildren().add(submitButton);
        
		layout.getChildren().add(copyPhotoLabel);
		layout.getChildren().add(copyPhotoField);
		layout.getChildren().add(buttons);
	}

	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		pStage = primaryStage;
		pStage.show();
		changeToLogin();
	}
	

	/**
	 * logs out the user and saves all the session info on exit
	 */
	@Override
	public void stop()
	{
		logout();
		File f = new File("Obj.txt");
		try {try {
		FileOutputStream fos = new FileOutputStream(f);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(info);
		oos.close();
		} catch (FileNotFoundException e) {}}	catch (IOException i) {}
	}

	/**
	 * logs out the user
	 */
	public void logout() {info.setUser(null);info.setAlbum(null);info.setPhoto(null);}
	/**
	 * updates the current user to the new user
	 * @param user is the user that logged in
	 */
	public void login(User user) {info.setUser(user);}
	/**
	 * adds a user to the list of valid users
	 * @param user is the user to be added
	 */
	public void addUser(User user) {info.addUser(user);}
	/**
	 * deletes a user from the list of valid users
	 * @param user is the user to be deleted
	 */
	public void deleteUser(User user) {info.deleteUser(user);}
	/**
	 * adds an album to the users list of albums
	 * @param album is the Album to be added
	 */
	public void addAlbum(Album album) {info.getUser().addAlbum(album);}
	/**
	 * deletes an album from the users list of albums
	 * @param album is the Album to be deleted
	 */
	public void deleteAlbum(Album album) {info.getUser().deleteAlbum(album);}
	/**
	 * adds a tag to the photo's list of tags
	 * @param tag is the String to be added
	 */
	public void addTag(String tag) {info.getPhoto().addTag(tag);}
	/**
	 * deletes a tag from the photo's list of tags
	 * @param tag is the String to be deleted
	 */
	public void deleteTag(String tag) {info.getPhoto().deleteTag(tag);}
	/**
	 * adds a photo to the album's list of Photos
	 * @param image is the file the added Photo will be based on
	 */
	public void addPhoto(File image) {info.getAlbum().addPhoto(new Photo(image));}
	/**
	 * adds a photo to the album's list of Photos
	 * @param image is the photo to be added
	 */
	public void addPhoto(Photo image) {info.getAlbum().addPhoto(image);}
	/**
	 * deletes a photo from the album's list of Photos
	 * @param photo is the photo to be deleted
	 */
	public void deletePhoto(Photo photo) {info.getAlbum().deletePhoto(photo);}
	/**
	 * deletes a photo from the album and adds it to another album
	 * @param album is the destination album
	 */
	public void movePhoto(Album album) {album.addPhoto(info.getPhoto()); info.getAlbum().deletePhoto(info.getPhoto());}
	/**
	 * copies a Photo to the target folder
	 * @param album is the destination album
	 */
	public void copyPhoto(Album album) {album.addPhoto(info.getPhoto());}
	

	/**
	 * sets the current Album to a temporary album(on that is not linked to a user) filled with Photos from a given time period
	 * @param start is the lower bound of valid Photo Dates
	 * @param end is the upper bound of valid Photo Dates
	 */
	public void searchByDate(LocalDate start, LocalDate end) 
	{
		Album searchAlbum = new Album("Search Result");
		info.setAlbum(searchAlbum);
		ZoneId zoneId = ZoneId.systemDefault();
		long startLong = start.atStartOfDay(zoneId).toEpochSecond();
		long endLong = end.atStartOfDay(zoneId).toEpochSecond();
		
		for(int i = 0; i < info.getUser().getAlbums().size(); i ++)
		{
			for(int j = 0; j < info.getUser().getAlbums().get(i).getPhotos().size(); j++)
			{
				long imageDate = info.getUser().getAlbums().get(i).getPhotos().get(j).getDateLong() / 1000;
				if(imageDate >= startLong && imageDate <= endLong)
				{
					Photo photo = new Photo(info.getUser().getAlbums().get(i).getPhotos().get(j).getFile());
					photo.setName(info.getUser().getAlbums().get(i).getPhotos().get(j).getName());
					photo.setCaption(info.getUser().getAlbums().get(i).getPhotos().get(j).getCaption());
					photo.setTags(info.getUser().getAlbums().get(i).getPhotos().get(j).getTags());
					addPhoto(photo);
				}
			}
		}
		
	}

	/**
	 * sets the current Album to a temporary album(on that is not linked to a user) filled with Photos from a given time period
	 * @param tag1 is the first tag
	 * @param tag2 is the second tag
	 * @param isDisjunct reflects how to interpret the tags
	 */
	public void searchByTags(String tag1, String tag2, boolean isDisjunct) 
	{
		Album searchAlbum = new Album("Search Result");
		info.setAlbum(searchAlbum);
		
		for(int i = 0; i < info.getUser().getAlbums().size(); i ++)
		{
			for(int j = 0; j < info.getUser().getAlbums().get(i).getPhotos().size(); j++)
			{
				ArrayList <String> tags = info.getUser().getAlbums().get(i).getPhotos().get(j).getTags();
				if(isDisjunct)
				{
					if(tags.indexOf(tag1) != -1 || tags.indexOf(tag2) != -1)
					{
						Photo photo = new Photo(info.getUser().getAlbums().get(i).getPhotos().get(j).getFile());
						photo.setName(info.getUser().getAlbums().get(i).getPhotos().get(j).getName());
						photo.setCaption(info.getUser().getAlbums().get(i).getPhotos().get(j).getCaption());
						photo.setTags(info.getUser().getAlbums().get(i).getPhotos().get(j).getTags());
						addPhoto(photo);
					}
				}
				else
				{
					if(tags.indexOf(tag1) != -1 && tags.indexOf(tag2) != -1)
					{
						Photo photo = new Photo(info.getUser().getAlbums().get(i).getPhotos().get(j).getFile());
						photo.setName(info.getUser().getAlbums().get(i).getPhotos().get(j).getName());
						photo.setCaption(info.getUser().getAlbums().get(i).getPhotos().get(j).getCaption());
						photo.setTags(info.getUser().getAlbums().get(i).getPhotos().get(j).getTags());
						addPhoto(photo);
					}
				}
			}
		}
	}
}
