package roger.brian.android81

class SessionInfo constructor(var albums: ArrayList<Album>, var album: Album, var photo: Photo) {
    fun addAlbum(newAlbum: Album){
        albums.add(newAlbum)
    }
    fun deleteAlbum(newAlbum: Album){
        albums.remove(newAlbum)
    }
    //fun setAlbum(n: Int){
    //    album = albums[n]
    //}
    //fun setPhoto(n: Int){
    //    photo = album.photos[n]
    //}

}