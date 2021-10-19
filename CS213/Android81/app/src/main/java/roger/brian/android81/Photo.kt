package roger.brian.android81

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.File
import java.io.FileInputStream

@Entity(indices = [Index("photoId")])
data class Photo (
    var albumId: Long = 0,
    var name: String,
    var people: List<String> = ArrayList(),
    var location: String,
    var path: String?,
    @PrimaryKey (autoGenerate = true)
    var photoId: Long = 0
){

    fun getImageFromInternalStorage(context: Context, imageFileName: String): Bitmap? {
        val directory = context.filesDir
        val file = File(directory, imageFileName)
        return BitmapFactory.decodeStream(FileInputStream(file))
    }

    fun getUri(): Uri? {
        return Uri.parse("file://$path")
    }
}