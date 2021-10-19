package roger.brian.android81

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("albumId")])
data class Album(
        var name: String,
        @PrimaryKey(autoGenerate = true)
        var albumId: Long = 0)