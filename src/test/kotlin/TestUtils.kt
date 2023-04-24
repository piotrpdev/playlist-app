import controllers.SongAPI
import models.Song
import persistence.XMLSerializer
import utils.SerializerUtils
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
}