package roger.brian.android81

import androidx.room.*

@Dao
interface AlbumDao {

    @Query("SELECT * from album")
    fun getAlbums(): List<Album>

    @Insert
    fun insert(album: Album): Long

    @Delete
    fun delete(album: Album)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(album: Album)

    @Query("SELECT * from album WHERE albumId like :id LIMIT 1")
    fun getAlbum(id: Long): Album
}