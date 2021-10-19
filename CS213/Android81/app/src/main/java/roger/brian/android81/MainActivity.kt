package roger.brian.android81

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_search.*

class MainActivity : AppCompatActivity() {
   // private var info = SessionInfo(ArrayList<Album>(), Album("", ArrayList<Photo>()), Photo("", ArrayList<String>(), null))
    private var info = PhotoStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        PhotoStorage.attach(applicationContext)
        switchToMain()
    }

    fun switchToMain(){
        setContentView(R.layout.activity_main)

        val createAlbumButton = findViewById<Button>(R.id.createAlbumButton)
        val searchPhotosButton = findViewById<Button>(R.id.searchPhotosButton)
        val albumScrollViewLayout = findViewById<LinearLayout>(R.id.albumScrollViewLayout)


        val albumsWithPhotos = PhotoStorage.getAlbums()

        for (albumWithPhotos in albumsWithPhotos) {
            val albumListText = TextView(this)
            albumListText.setText(albumWithPhotos.album.name)
            albumListText.textSize = 24.0f
            albumListText.setOnClickListener{
                switchToAlbumView(albumWithPhotos.album.albumId)
            }

            albumScrollViewLayout.addView(albumListText)
        }

        createAlbumButton.setOnClickListener{
            switchToCreateAlbum()
        }
        searchPhotosButton.setOnClickListener{
            switchToSearch()
        }
    }

    fun switchToSearch(){
        setContentView(R.layout.activity_search)

        val personValue = findViewById<EditText>(R.id.personEditText)
        val locationValue = findViewById<EditText>(R.id.locationEditText)
        val searchButton = findViewById<Button>(R.id.searchPhotosButton)
        val cancelButton = findViewById<Button>(R.id.cancelSearchButton)


        searchButton.setOnClickListener{
            val photos = PhotoStorage.searchTags(personValue.text.toString(), locationValue.text.toString())
            switchToSearchResults(photos)
        }
        cancelButton.setOnClickListener{
            switchToMain()
        }
    }

    private fun switchToSearchResults(photos: List<Photo>) {
        setContentView(R.layout.activity_search_results)
        val searchResultsLayout = findViewById<LinearLayout>(R.id.searchResults)
        val backToSearchButton = findViewById<Button>(R.id.backToSearchButton)

        backToSearchButton.setOnClickListener {
            switchToSearch()
        }

        for (photo in photos) {
            val photoListImage = ImageView(this)
            //photoListImage.setBackgroundColor(Color.GREEN)
            photoListImage.setImageURI(photo.getUri())
            val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            )
            photoListImage.layoutParams = params
            photoListImage.layoutParams.height = 500
            photoListImage.layoutParams.width = 500
            photoListImage.setOnClickListener {
                val album = PhotoStorage.getAlbum(photo.albumId) ?: return@setOnClickListener
                switchToPhotoView(album.album, photo)
            }
            searchResultsLayout.addView(photoListImage)
        }
    }

    fun switchToCreateAlbum(){
        setContentView(R.layout.activity_create_album)

        val nameEditText = findViewById<EditText>(R.id.albumNameEditText)
        val createButton = findViewById<Button>(R.id.searchPhotosButton)
        val cancelButton = findViewById<Button>(R.id.cancelNewAlbumButton)

        createButton.setOnClickListener{
            PhotoStorage.addAlbum(Album(nameEditText.text.toString()))
            switchToMain()
        }
        cancelButton.setOnClickListener{
            switchToMain()
        }
    }

    fun switchToAlbumView(albumId: Long) {
        setContentView(R.layout.activity_album)

        val titleTextView = findViewById<TextView>(R.id.albumNameTextView)
        val addPhotoButton = findViewById<Button>(R.id.addPhotoButton)
        val renameAlbumButton = findViewById<Button>(R.id.renameAlbumButton)
        val deleteAlbumButton = findViewById<Button>(R.id.deleteAlbumButton)
        val backToAlbumsButton = findViewById<Button>(R.id.backToAlbumsButton)

        val photoScrollViewLayout = findViewById<LinearLayout>(R.id.photoScrollViewLayout)

        val albumWithPhotos = PhotoStorage.getAlbum(albumId)
        PhotoStorage.activeAlbum = albumWithPhotos

        if (albumWithPhotos == null) return

        for (photo in albumWithPhotos.photos) {
            val photoListImage = ImageView(this)
            //photoListImage.setBackgroundColor(Color.GREEN)
            photoListImage.setImageURI(photo.getUri())
            val params = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            photoListImage.layoutParams = params
            photoListImage.layoutParams.height = 500
            photoListImage.layoutParams.width = 500
            photoListImage.setOnClickListener {
                switchToPhotoView(albumWithPhotos.album, photo)
            }
            photoScrollViewLayout.addView(photoListImage)
        }

        //adds function to buttons

        titleTextView.text = albumWithPhotos.album.name

        renameAlbumButton.setOnClickListener{
            switchToChangeAlbumName(albumWithPhotos.album)
        }

        //may want to add a confirm notice
        deleteAlbumButton.setOnClickListener{
            PhotoStorage.deleteAlbum(albumWithPhotos.album)
            switchToMain()
        }

        backToAlbumsButton.setOnClickListener{
            PhotoStorage.activeAlbum = null
            switchToMain()
        }

        //image picker
        addPhotoButton.setOnClickListener{
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                //permission denied
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                //permission already granted
                pickImageFromGallery()
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object{
        //image pick code
        private val IMAGE_PICK_CODE = 1000
        //permission code
        private val PERMISSION_CODE = 1001

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode){
            PERMISSION_CODE -> {
                if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                }
                else{
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE && data != null){
            val activeAlbum = PhotoStorage.activeAlbum
            if (activeAlbum == null) return

            val path = PhotoStorage.saveImage(data.data, contentResolver, createOutputFile())
            PhotoStorage.addPhoto(Photo(activeAlbum.album.albumId,"New Photo", ArrayList(), "", path))
            switchToAlbumView(activeAlbum.album.albumId)
        }
    }

    fun switchToPhotoView(album: Album, photo: Photo) {
        setContentView(R.layout.activity_photo)
        val photoImageView = findViewById<ImageView>(R.id.photoImageView)
        val photoNameTextView = findViewById<TextView>(R.id.photoNameTextView)
        val changeNameButton = findViewById<Button>(R.id.changeNameButton)
        val addTagButton = findViewById<Button>(R.id.addTagButton)
        val removeTagButton = findViewById<Button>(R.id.removeTagButton)
        val deletePhotoButton = findViewById<Button>(R.id.deletePhotoButton)
        val movePhotoButton = findViewById<Button>(R.id.movePhotoButton)
        val backToAlbumButton = findViewById<Button>(R.id.backToAlbumButton)
        val albumListSpinner = findViewById<Spinner>(R.id.albumListSpinner)
        val locationTag = findViewById<TextView>(R.id.locationTag)
        val tagScrollView = findViewById<LinearLayout>(R.id.tagsLayoutScrollView)
        val nextPhotoButton = findViewById<Button>(R.id.nextPhotoButton)
        val previousPhotoButton = findViewById<Button>(R.id.previousPhotoButton)

        if (photo.location.isNotEmpty()) {
            locationTag.setText(photo.location)
        } else {
            locationTag.visibility = View.INVISIBLE
        }

        if (photo.people.isEmpty()) {
            tagScrollView.visibility = View.INVISIBLE
        }

        for (person in photo.people) {
            val tagView = TextView(this)
            tagView.setText(person)
            tagScrollView.addView(tagView)
        }

        val albums = PhotoStorage.getAlbums()
        val albumsStr = albums.map {
            it.album.name
        }

        albumListSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, albumsStr)

        photoImageView.setImageURI(photo.getUri())
        photoNameTextView.text = photo.name

        //button functionality

        backToAlbumButton.setOnClickListener{
            switchToAlbumView(album.albumId)
        }
        deletePhotoButton.setOnClickListener{
            PhotoStorage.deletePhotoFromAlbum(photo, album)
            switchToAlbumView(album.albumId)
        }
        changeNameButton.setOnClickListener{
            switchToChangePhotoName(photo, album)
        }

        addTagButton.setOnClickListener {
            switchToAddTagView(album, photo)
        }

        removeTagButton.setOnClickListener {
            switchToRemoveTagView(album, photo)
        }

        movePhotoButton.setOnClickListener {
            val selectedItemPosition = albumListSpinner.selectedItemPosition
            PhotoStorage.movePhotoToAlbum(photo, albums[selectedItemPosition].album)
            switchToMain()
        }

        nextPhotoButton.setOnClickListener {
            val albumWithPhotos = PhotoStorage.getAlbum(photo.albumId)
            if (albumWithPhotos == null) return@setOnClickListener
            val photos = albumWithPhotos.photos
            val indexOfNextPhoto = photos.indexOf(photo) + 1
            if (indexOfNextPhoto < photos.size) {
                switchToPhotoView(album, photos[indexOfNextPhoto])
            } else {
                switchToPhotoView(album, photos[0])
            }
        }

        previousPhotoButton.setOnClickListener {
            val albumWithPhotos = PhotoStorage.getAlbum(photo.albumId)
            if (albumWithPhotos == null) return@setOnClickListener
            val photos = albumWithPhotos.photos
            val indexOfPrevPhoto = photos.indexOf(photo) - 1
            if (indexOfPrevPhoto >= 0) {
                switchToPhotoView(album, photos[indexOfPrevPhoto])
            } else {
                switchToPhotoView(album, photos[photos.size - 1])
            }
        }
    }

    private fun switchToRemoveTagView(album: Album, photo: Photo) {
        setContentView(R.layout.activity_remove_tag)
        val removeTagButton = findViewById<Button>(R.id.saveTagButton)
        val tagValue = findViewById<EditText>(R.id.tagValueEditText)
        val tagTypeSpinner = findViewById<Spinner>(R.id.tagTypeSpinner)
        val cancelTagButton = findViewById<Button>(R.id.cancelTagButton)

        removeTagButton.setOnClickListener {
            val newTagValue = tagValue.text
            if (newTagValue != null && newTagValue.isNotEmpty()) {
                val selectedItem = tagTypeSpinner.selectedItem as String
                when (selectedItem) {
                    "Person" -> PhotoStorage.removePersonFromPhoto(newTagValue.toString(), photo)
                    "Location" -> PhotoStorage.removeLocationFromPhoto(newTagValue.toString(), photo)
                }
                switchToPhotoView(album, photo)
            }
        }

        cancelTagButton.setOnClickListener {
            switchToPhotoView(album, photo)
        }
    }

    private fun switchToAddTagView(album: Album, photo: Photo) {
        setContentView(R.layout.activity_add_tag)
        val saveTagButton = findViewById<Button>(R.id.saveTagButton)
        val tagValue = findViewById<EditText>(R.id.tagValueEditText)
        val tagTypeSpinner = findViewById<Spinner>(R.id.tagTypeSpinner)
        val cancelTagButton = findViewById<Button>(R.id.cancelTagButton)

        saveTagButton.setOnClickListener {
            val newTagValue = tagValue.text
            if (newTagValue != null && newTagValue.isNotEmpty()) {
                val selectedItem = tagTypeSpinner.selectedItem as String
                when (selectedItem) {
                    "Person" -> PhotoStorage.addPersonToPhoto(newTagValue.toString(), photo)
                    "Location" -> PhotoStorage.updateLocationOfPhoto(newTagValue.toString(), photo)
                }
                switchToPhotoView(album, photo)
            }
        }

        cancelTagButton.setOnClickListener {
            switchToPhotoView(album, photo)
        }
    }

    private fun switchToChangeAlbumName(album: Album) {
        setContentView(R.layout.activity_rename_album)
        val renameButton = findViewById<Button>(R.id.renameAlbumButton)
        val cancelButton = findViewById<Button>(R.id.cancelRenameAlbumButton)
        val newNameEditText = findViewById<EditText>(R.id.albumNewNameEditText)

        //Add button functionality

        renameButton.setOnClickListener{
            if((newNameEditText.text.toString()) != "")
            {
                album.name = newNameEditText.text.toString()
                PhotoStorage.updateAlbum(album)
                switchToAlbumView(album.albumId)
            }
            else
                Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show()
        }
        cancelButton.setOnClickListener{
            switchToAlbumView(album.albumId)
        }

    }

    private fun switchToChangePhotoName(photo: Photo, album: Album) {
        setContentView(R.layout.activity_rename_photo)
        val renameButton = findViewById<Button>(R.id.renamePhotoButton)
        val cancelButton = findViewById<Button>(R.id.cancelRenamePhotoButton)
        val newNameEditText = findViewById<EditText>(R.id.photoNewNameEditText)

        //Add button functionality

        renameButton.setOnClickListener{
            if((newNameEditText.text.toString()) != "")
            {
                photo.name = newNameEditText.text.toString()
                PhotoStorage.updatePhoto(photo)
                switchToPhotoView(album, photo)
            }
            else
                Toast.makeText(this, "Invalid name", Toast.LENGTH_SHORT).show()
        }
        cancelButton.setOnClickListener{
            switchToPhotoView(album, photo)
        }

    }
}
