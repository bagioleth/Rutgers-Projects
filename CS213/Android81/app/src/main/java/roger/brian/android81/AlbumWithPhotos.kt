package roger.brian.android81

import androidx.room.Embedded
import androidx.room.Relation

data class AlbumWithPhotos(
    @Embedded
    var album: Album,

    @Relation(
        parentColumn = "albumId",
        entityColumn = "albumId"
    )
    var photos: List<Photo> = ArrayList()
)