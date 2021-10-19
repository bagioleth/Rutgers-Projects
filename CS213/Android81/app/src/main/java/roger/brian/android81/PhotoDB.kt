package roger.brian.android81

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Album::class, Photo::class], version = 1, exportSchema = false)
@TypeConverters(MyTypeConverter::class)
abstract class PhotoDB : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun photoDao(): PhotoDao
    abstract fun albumWithPhotosDao(): AlbumWithPhotosDao

    fun addPhotoToAlbum(){

    }

}