package persistence

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.DomDriver
import models.Song
import utils.SerializerUtils.isArrayList
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class XMLSerializer(private val file: File) : Serializer {

    @Throws(Exception::class)
    override fun read(): ArrayList<Song>? {
        val xStream = XStream(DomDriver())
        xStream.allowTypes(arrayOf(Song::class.java))
        val obj = xStream.createObjectInputStream(FileReader(file)).use {
            it.readObject() as Any
        }

        return isArrayList(obj)
    }


    @Throws(Exception::class)
    override fun write(obj: ArrayList<Song>) {
        val xStream = XStream(DomDriver())

        xStream.createObjectOutputStream(FileWriter(file)).use {
            it.writeObject(obj)
        }
    }
}
