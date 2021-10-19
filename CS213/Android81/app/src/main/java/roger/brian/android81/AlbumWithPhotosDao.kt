package roger.brian.android81

import androidx.room.Dao
import androidx.room.Query

@Dao
interface AlbumWithPhotosDao {

    @Query("SELECT * from album")
    fun getAlbumsWithPhotos(): List<AlbumWithPhotos>

    @Query("SELECT * from album WHERE albumId LIKE :id LIMIT 1")
    fun getAlbumWithId(id: Long): AlbumWithPhotos

}