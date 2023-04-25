package utils

import controllers.SongAPI
import models.Song
import persistence.JSONSerializer
import persistence.XMLSerializer
import persistence.YAMLSerializer
import java.io.File
import java.time.LocalDateTime

/**
 * An object containing utility functions for serialization and deserialization of songs, as well as generating seed data.
 */
object SerializerUtils {
    /**
     * Parses a string into a LocalDateTime object.
     *
     * @param s The string to be parsed.
     * @return The LocalDateTime object parsed from the string.
     */
    fun ldp(s: String): LocalDateTime = LocalDateTime.parse(s)

    /**
     * Checks if the given object is an ArrayList of Song objects.
     *
     * @param obj The object to be checked.
     * @return The ArrayList of Song objects if the given object is of the correct type, null otherwise.
     */
    @JvmStatic
    fun isArrayList(obj: Any): ArrayList<Song>? = if (obj is ArrayList<*> && obj.all { it is Song }) {
        @Suppress("UNCHECKED_CAST")
        obj as ArrayList<Song>
    } else {
        null
    }

    /**
     * Returns an ArrayList of Song objects containing seeded data.
     *
     * @return The ArrayList of seeded Song objects.
     */
    @JvmStatic
    fun getSeededSongs(): ArrayList<Song> {
        val songs = ArrayList<Song>()

        songs.add(Song("Smooth", 3, "Latin", false, ldp("2023-03-10T10:00"), ldp("2023-03-10T10:00")))
        songs.add(Song("Lose Yourself", 1, "Hip Hop", false, ldp("2023-03-12T15:30"), ldp("2023-03-11T13:00")))
        songs.add(Song("Ain't No Mountain High Enough", 2, "Soul", false, ldp("2023-03-14T18:45"), ldp("2023-03-14T17:30")))
        songs.add(Song("Highway to Hell", 4, "Rock", false, ldp("2023-03-09T11:30"), ldp("2023-03-09T11:00")))
        songs.add(Song("Sweet Child o' Mine", 5, "Rock", true, ldp("2023-03-08T12:15"), ldp("2023-03-08T12:00")))
        songs.add(Song("Livin' on a Prayer", 1, "Rock", false, ldp("2023-03-10T20:00"), ldp("2023-03-10T19:30")))
        songs.add(Song("Billie Jean", 3, "Pop", true, ldp("2023-03-11T14:00"), ldp("2023-03-11T13:45")))
        songs.add(Song("The Power of Love", 1, "Pop", false, ldp("2023-03-15T16:00"), ldp("2023-03-15T14:30")))
        songs.add(Song("Sweet Home Alabama", 2, "Country", false, ldp("2023-03-12T17:15"), ldp("2023-03-12T16:45")))
        songs.add(Song("Enter Sandman", 4, "Metal", false, ldp("2023-03-14T10:30"), ldp("2023-03-14T10:00")))
        songs.add(Song("Beat It", 3, "Rock", false, ldp("2023-02-14T10:30"), ldp("2023-02-14T10:00")))

        return songs
    }

    /**
     * Generates and stores seed data as XML, JSON, and YAML files.
     */
    @JvmStatic
    fun generateSeededFiles() {
        val songAPIs = ArrayList<SongAPI>()

        songAPIs.add(SongAPI(XMLSerializer(File("songs.xml"))))
        songAPIs.add(SongAPI(JSONSerializer(File("songs.json"))))
        songAPIs.add(SongAPI(YAMLSerializer(File("songs.yaml"))))

        songAPIs.forEach { it.seedSongs(); it.store() }
    }
}