package persistence

import controllers.SongAPI
import models.Song
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class PersistenceTest {
    private var learnKotlin: Song? = null
    private var summerHoliday: Song? = null
    private var codeApp: Song? = null
    private var testApp: Song? = null
    private var swim: Song? = null
    private var populatedSongs: SongAPI? = null
    private var emptySongs: SongAPI? = null

    @BeforeEach
    fun buildUp() {
        learnKotlin = TestUtils.learnKotlin()
        summerHoliday = TestUtils.summerHoliday()
        codeApp = TestUtils.codeApp()
        testApp = TestUtils.testApp()
        swim = TestUtils.swim()
        populatedSongs = TestUtils.populatedSongs()
        emptySongs = TestUtils.emptySongs()

        //adding 5 Song to the songs api
        populatedSongs!!.add(learnKotlin!!)
        populatedSongs!!.add(summerHoliday!!)
        populatedSongs!!.add(codeApp!!)
        populatedSongs!!.add(testApp!!)
        populatedSongs!!.add(swim!!)
    }

    @AfterEach
    fun tearDown() {
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedSongs = null
        emptySongs = null

        File("songs.test.xml").delete()
        File("songs.test.json").delete()
        File("songs.test.yaml").delete()
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty songs.XML file.
            val storingSongs = SongAPI(XMLSerializer(File("songs.test.xml")))
            storingSongs.store()

            //Loading the empty songs.test.xml file into a new object
            val loadedSongs = SongAPI(XMLSerializer(File("songs.test.xml")))
            loadedSongs.load()

            //Comparing the source of the songs (storingSongs) with the XML loaded songs (loadedSongs)
            assertEquals(0, storingSongs.numberOfSongs())
            assertEquals(0, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 songs to the songs.XML file.
            val storingSongs = SongAPI(XMLSerializer(File("songs.test.xml")))
            storingSongs.add(testApp!!)
            storingSongs.add(swim!!)
            storingSongs.add(summerHoliday!!)
            storingSongs.store()

            //Loading songs.test.xml into a different collection
            val loadedSongs = SongAPI(XMLSerializer(File("songs.test.xml")))
            loadedSongs.load()

            //Comparing the source of the songs (storingSongs) with the XML loaded songs (loadedSongs)
            assertEquals(3, storingSongs.numberOfSongs())
            assertEquals(3, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
            assertEquals(storingSongs.findSong(0), loadedSongs.findSong(0))
            assertEquals(storingSongs.findSong(1), loadedSongs.findSong(1))
            assertEquals(storingSongs.findSong(2), loadedSongs.findSong(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty songs.test.json file.
            val storingSongs = SongAPI(JSONSerializer(File("songs.test.json")))
            storingSongs.store()

            //Loading the empty songs.test.json file into a new object
            val loadedSongs = SongAPI(JSONSerializer(File("songs.test.json")))
            loadedSongs.load()

            //Comparing the source of the songs (storingSongs) with the json loaded songs (loadedSongs)
            assertEquals(0, storingSongs.numberOfSongs())
            assertEquals(0, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 songs to the songs.test.json file.
            val storingSongs = SongAPI(JSONSerializer(File("songs.test.json")))
            storingSongs.add(testApp!!)
            storingSongs.add(swim!!)
            storingSongs.add(summerHoliday!!)
            storingSongs.store()

            //Loading songs.test.json into a different collection
            val loadedSongs = SongAPI(JSONSerializer(File("songs.test.json")))
            loadedSongs.load()

            //Comparing the source of the songs (storingSongs) with the json loaded songs (loadedSongs)
            assertEquals(3, storingSongs.numberOfSongs())
            assertEquals(3, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
            assertEquals(storingSongs.findSong(0), loadedSongs.findSong(0))
            assertEquals(storingSongs.findSong(1), loadedSongs.findSong(1))
            assertEquals(storingSongs.findSong(2), loadedSongs.findSong(2))
        }

        @Test
        fun `saving and loading an empty collection in YAML doesn't crash app`() {
            // Saving an empty songs.test.yaml file.
            val storingSongs = SongAPI(YAMLSerializer(File("songs.test.yaml")))
            storingSongs.store()

            //Loading the empty songs.test.yaml file into a new object
            val loadedSongs = SongAPI(YAMLSerializer(File("songs.test.yaml")))
            loadedSongs.load()

            //Comparing the source of the songs (storingSongs) with the yaml loaded songs (loadedSongs)
            assertEquals(0, storingSongs.numberOfSongs())
            assertEquals(0, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
        }

        @Test
        fun `saving and loading an loaded collection in YAML doesn't loose data`() {
            // Storing 3 songs to the songs.test.yaml file.
            val storingSongs = SongAPI(YAMLSerializer(File("songs.test.yaml")))
            storingSongs.add(testApp!!)
            storingSongs.add(swim!!)
            storingSongs.add(summerHoliday!!)
            storingSongs.store()

            //Loading songs.test.yaml into a different collection
            val loadedSongs = SongAPI(YAMLSerializer(File("songs.test.yaml")))
            loadedSongs.load()

            //Comparing the source of the songs (storingSongs) with the yaml loaded songs (loadedSongs)
            assertEquals(3, storingSongs.numberOfSongs())
            assertEquals(3, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
            assertEquals(storingSongs.findSong(0), loadedSongs.findSong(0))
            assertEquals(storingSongs.findSong(1), loadedSongs.findSong(1))
            assertEquals(storingSongs.findSong(2), loadedSongs.findSong(2))
        }
    }
}