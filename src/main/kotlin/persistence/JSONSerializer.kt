package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver
import models.Artist
import models.Song
import utils.SerializerUtils.isArrayList
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class JSONSerializer(val file: File) : Serializer {
    @Throws(Exception::class)
    inline fun <reified T> readGeneric(): ArrayList<T>? {
        val xStream = XStream(JettisonMappedXmlDriver())
        xStream.allowTypes(arrayOf(T::class.java))
        val obj = xStream.createObjectInputStream(FileReader(file)).use {
            it.readObject() as Any
        }

        return isArrayList(obj)
    }

    @Throws(Exception::class)
    fun <T> writeGeneric(obj: ArrayList<T>) {
        val xStream = XStream(JettisonMappedXmlDriver())

        xStream.createObjectOutputStream(FileWriter(file)).use {
            it.writeObject(obj)
        }
    }

    @Throws(Exception::class)
    override fun readSongs(): ArrayList<Song>? = readGeneric()

    @Throws(Exception::class)
    override fun writeSongs(obj: ArrayList<Song>) = writeGeneric(obj)

    @Throws(Exception::class)
    override fun readArtists(): ArrayList<Artist>? = readGeneric()

    @Throws(Exception::class)
    override fun writeArtists(obj: ArrayList<Artist>) = writeGeneric(obj)
}
