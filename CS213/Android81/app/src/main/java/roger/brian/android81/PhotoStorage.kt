package roger.brian.android81

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.room.Room
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

object PhotoStorage {

    var activeAlbum: AlbumWithPhotos? = null

    private var photoDb: PhotoDB? = null
    private const val DATABASE_NAME: String = "PHOTO_DB"

    var appContext: Context? = null

    fun attach(context: Context) {
        appContext = context.applicationContext
        photoDb = Room.databaseBuilder(context, PhotoDB::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
    }

    fun getAlbums(): List<AlbumWithPhotos> {
        return photoDb?.albumWithPhotosDao()?.getAlbumsWithPhotos() ?: listOf()
    }

    fun addAlbum(album: Album) {
        photoDb?.albumDao()?.insert(album)
    }

    fun deleteAlbum(album: Album) {
        photoDb?.albumDao()?.delete(album)
    }

    fun updateAlbum(album: Album) {
        photoDb?.albumDao()?.update(album)
    }

    fun getAlbum(albumId: Long): AlbumWithPhotos? {
       return photoDb?.albumWithPhotosDao()?.getAlbumWithId(albumId)
    }

    fun updateLocationOfPhoto(location: String, photo: Photo) {
        photo.location = location
        updatePhoto(photo)
    }

    fun removeLocationFromPhoto(location: String, photo: Photo) {
        if (photo.location.equals(location)) {
            photo.location = ""
            updatePhoto(photo)
        }
    }

    fun addPersonToPhoto(person: String, photo: Photo) {
        val people = ArrayList(photo.people)
        if (!people.contains(person)) {
            people.add(person)
            photo.people = people
            updatePhoto(photo)
        }
    }

    fun removePersonFromPhoto(person: String, photo: Photo) {
        val people = ArrayList(photo.people)
        people.remove(person)
        photo.people = people
        updatePhoto(photo)
    }

    fun deletePhotoFromAlbum(photo: Photo, album: Album) {
        photo.albumId = -1
        photoDb?.photoDao()?.update(photo)
    }

    fun updatePhoto(photo: Photo) {
        photoDb?.photoDao()?.update(photo)
    }

    fun addPhoto(photo: Photo) {
        photoDb?.photoDao()?.insert(photo)
    }

    fun movePhotoToAlbum(photo: Photo, album: Album) {
        photo.albumId = album.albumId
        photoDb?.photoDao()?.update(photo)
    }

    fun saveImage(fileUri: Uri, contentResolver: ContentResolver, outputFile: File): String {
        val inputStream = contentResolver.openInputStream(fileUri)

        // copy inputStream to file
        val outputStream = FileOutputStream(outputFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input?.copyTo(output)
            }
        }

        return outputFile.absolutePath
    }

    /**
     * Match both person and location if
     * specified, otherwise match the specified
     * field
     */
    fun searchTags(person: String, location: String): List<Photo> {
        val peopleSearch = if (person.isNotEmpty()) {
            photoDb?.photoDao()?.searchPeople("%$person%")
        } else {
            null
        }

        val locationSearch = if (location.isNotEmpty()) {
            photoDb?.photoDao()?.searchLocation("%$location%")
        } else {
            null
        }

        val searchResult = if (peopleSearch != null && locationSearch != null) {
            peopleSearch.intersect(locationSearch).toList()
        } else if (peopleSearch != null) {
            peopleSearch
        } else if (locationSearch != null) {
            locationSearch
        } else {
            listOf()
        }

        return searchResult
    }
}

fun Context.createOutputFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File.createTempFile(
            imageFileName,
            ".jpg",
            storageDir
    )

    return file
}