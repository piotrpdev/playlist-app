package persistence

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import models.Artist
import models.Song
import utils.SerializerUtils.isArrayList
import java.io.File

// https://www.mkammerer.de/blog/kotlin-and-yaml-part-2/
class YAMLSerializer(val file: File) : Serializer {

    fun getMapper(): ObjectMapper {
        val mapper: ObjectMapper = YAMLMapper()
        mapper.registerModule(
            KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build()
        ).registerModule(JavaTimeModule())

        return mapper
    }

    @Throws(Exception::class)
    inline fun <reified T> readGeneric(): ArrayList<T>? {
        val mapper: ObjectMapper = getMapper()

        val obj = mapper.readValue(file, object : TypeReference<ArrayList<T?>?>() {})!!

        return isArrayList(obj)
    }

    @Throws(Exception::class)
    fun <T> writeGeneric(obj: ArrayList<T>) {
        val mapper: ObjectMapper = getMapper()

        mapper.writeValue(file, obj)
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
