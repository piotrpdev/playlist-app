import controllers.ArtistAPI
import controllers.SongAPI
import models.Artist
import models.Song
import persistence.XMLSerializer
import utils.SerializerUtils.ldp
import java.io.File

object TestUtils {
    fun learnKotlin() = Song("Learning Kotlin", 5, "College", false, ldp("2023-03-10T10:00"), ldp("2023-03-10T10:00"))
    fun summerHoliday() = Song("Summer Holiday to France", 1, "Holiday", false, ldp("2023-03-10T20:00"), ldp("2023-03-10T19:30"))
    fun codeApp() = Song("Code App", 4, "Work", false, ldp("2023-03-08T12:15"), ldp("2023-03-08T12:00"))
    fun testApp() = Song("Test App", 4, "Work", false, ldp("2023-03-12T15:30"), ldp("2023-03-11T13:00"))
    fun swim() = Song("Swim - Pool", 3, "Hobby", false, ldp("2023-03-11T14:00"), ldp("2023-03-11T13:45"))
    fun populatedSongs() = SongAPI(XMLSerializer(File("songs.test.xml")))
    fun emptySongs() = SongAPI(XMLSerializer(File("songs.test.xml")))

    fun michaelJackson() = Artist("Michael Jackson", ldp("1964-02-14T10:30"), listOf("Pop", "Rock", "Funk"))
    fun acDC() = Artist("AC/DC", ldp("1973-02-14T10:30"), listOf("Rock", "Metal"))
    fun eminem() = Artist("Eminem", ldp("1988-02-14T10:30"), listOf("Hip Hop", "Rap"))
    fun gunsNRoses() = Artist("Guns N' Roses", ldp("1985-02-14T10:30"), listOf("Rock", "Metal"))
    fun theBeatles() = Artist("The Beatles", ldp("1960-02-14T10:30"), listOf("Rock", "Pop"))

    fun populatedArtists() = ArtistAPI(XMLSerializer(File("artists.test.xml")))
    fun emptyArtists() = ArtistAPI(XMLSerializer(File("artists.test.xml")))
}
