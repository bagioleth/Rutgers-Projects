package roger.brian.android81

import androidx.room.*

@Dao
interface PhotoDao {

    @Insert
    fun insert(photo: Photo)

    @Delete
    fun delete(photo: Photo)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    fun update(photo: Photo)

    @Query("SELECT * FROM photo WHERE people LIKE :person")
    fun searchPeople(person: String): List<Photo>

    @Query("SELECT * FROM photo WHERE location LIKE :location")
    fun searchLocation(location: String): List<Photo>


}