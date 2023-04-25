package persistence

import models.Artist
import models.Song

interface Serializer {
    @Throws(Exception::class)
    fun writeSongs(obj: ArrayList<Song>)

    @Throws(Exception::class)
    fun readSongs(): ArrayList<Song>?

    @Throws(Exception::class)
    fun writeArtists(obj: ArrayList<Artist>)

    @Throws(Exception::class)
    fun readArtists(): ArrayList<Artist>?
}
