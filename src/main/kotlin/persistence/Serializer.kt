package persistence

import models.Song

interface Serializer {
    @Throws(Exception::class)
    fun write(obj: ArrayList<Song>)

    @Throws(Exception::class)
    fun read(): ArrayList<Song>?
}
