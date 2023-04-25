package persistence

import controllers.ArtistAPI
import controllers.SongAPI
import models.Artist
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

    private var michaelJackson: Artist? = null
    private var acDC: Artist? = null
    private var eminem: Artist? = null
    private var gunsNRoses: Artist? = null
    private var theBeatles: Artist? = null
    private var populatedArtists: ArtistAPI? = null
    private var emptyArtists: ArtistAPI? = null

    @BeforeEach
    fun buildUp() {
        learnKotlin = TestUtils.learnKotlin()
        summerHoliday = TestUtils.summerHoliday()
        codeApp = TestUtils.codeApp()
        testApp = TestUtils.testApp()
        swim = TestUtils.swim()
        populatedSongs = TestUtils.populatedSongs()
        emptySongs = TestUtils.emptySongs()

        // adding 5 Song to the songs api
        populatedSongs!!.add(learnKotlin!!)
        populatedSongs!!.add(summerHoliday!!)
        populatedSongs!!.add(codeApp!!)
        populatedSongs!!.add(testApp!!)
        populatedSongs!!.add(swim!!)

        michaelJackson = TestUtils.michaelJackson()
        acDC = TestUtils.acDC()
        eminem = TestUtils.eminem()
        gunsNRoses = TestUtils.gunsNRoses()
        theBeatles = TestUtils.theBeatles()
        populatedArtists = TestUtils.populatedArtists()
        emptyArtists = TestUtils.emptyArtists()

        // adding 5 Artists to the artists api
        populatedArtists!!.add(michaelJackson!!)
        populatedArtists!!.add(acDC!!)
        populatedArtists!!.add(eminem!!)
        populatedArtists!!.add(gunsNRoses!!)
        populatedArtists!!.add(theBeatles!!)
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

        michaelJackson = null
        acDC = null
        eminem = null
        gunsNRoses = null
        theBeatles = null
        populatedArtists = null
        emptyArtists = null

        File("artists.test.xml").delete()
        File("artists.test.json").delete()
        File("artists.test.yaml").delete()
    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty songs.XML file.
            val storingSongs = SongAPI(XMLSerializer(File("songs.test.xml")))
            storingSongs.storeSongs()

            // Loading the empty songs.test.xml file into a new object
            val loadedSongs = SongAPI(XMLSerializer(File("songs.test.xml")))
            loadedSongs.loadSongs()

            // Comparing the source of the songs (storingSongs) with the XML loaded songs (loadedSongs)
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
            storingSongs.storeSongs()

            // Loading songs.test.xml into a different collection
            val loadedSongs = SongAPI(XMLSerializer(File("songs.test.xml")))
            loadedSongs.loadSongs()

            // Comparing the source of the songs (storingSongs) with the XML loaded songs (loadedSongs)
            assertEquals(3, storingSongs.numberOfSongs())
            assertEquals(3, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
            assertEquals(storingSongs.findSong(0), loadedSongs.findSong(0))
            assertEquals(storingSongs.findSong(1), loadedSongs.findSong(1))
            assertEquals(storingSongs.findSong(2), loadedSongs.findSong(2))
        }

        @Test
        fun `readArtists in XML`() {
            // Storing 3 artists to the artists.test.xml file.
            val storingArtists = ArtistAPI(XMLSerializer(File("artists.test.xml")))
            storingArtists.add(michaelJackson!!)
            storingArtists.add(acDC!!)
            storingArtists.add(eminem!!)
            storingArtists.storeArtists()

            // Loading artists.test.xml into a different collection
            val loadedArtists = ArtistAPI(XMLSerializer(File("artists.test.xml")))
            loadedArtists.loadArtists()

            // Comparing the source of the artists (storingArtists) with the XML loaded artists (loadedArtists)
            assertEquals(3, storingArtists.numberOfArtists())
            assertEquals(3, loadedArtists.numberOfArtists())
            assertEquals(storingArtists.numberOfArtists(), loadedArtists.numberOfArtists())
            assertEquals(storingArtists.findArtist(0), loadedArtists.findArtist(0))
            assertEquals(storingArtists.findArtist(1), loadedArtists.findArtist(1))
            assertEquals(storingArtists.findArtist(2), loadedArtists.findArtist(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty songs.test.json file.
            val storingSongs = SongAPI(JSONSerializer(File("songs.test.json")))
            storingSongs.storeSongs()

            // Loading the empty songs.test.json file into a new object
            val loadedSongs = SongAPI(JSONSerializer(File("songs.test.json")))
            loadedSongs.loadSongs()

            // Comparing the source of the songs (storingSongs) with the json loaded songs (loadedSongs)
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
            storingSongs.storeSongs()

            // Loading songs.test.json into a different collection
            val loadedSongs = SongAPI(JSONSerializer(File("songs.test.json")))
            loadedSongs.loadSongs()

            // Comparing the source of the songs (storingSongs) with the json loaded songs (loadedSongs)
            assertEquals(3, storingSongs.numberOfSongs())
            assertEquals(3, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
            assertEquals(storingSongs.findSong(0), loadedSongs.findSong(0))
            assertEquals(storingSongs.findSong(1), loadedSongs.findSong(1))
            assertEquals(storingSongs.findSong(2), loadedSongs.findSong(2))
        }

        @Test
        fun `readArtists in JSON`() {
            // Storing 3 artists to the artists.test.json file.
            val storingArtists2 = ArtistAPI(JSONSerializer(File("artists.test.json")))
            storingArtists2.add(michaelJackson!!)
            storingArtists2.add(acDC!!)
            storingArtists2.add(eminem!!)
            storingArtists2.storeArtists()

            // Loading artists.test.json into a different collection
            val loadedArtists2 = ArtistAPI(JSONSerializer(File("artists.test.json")))
            loadedArtists2.loadArtists()

            // Comparing the source of the artists (storingArtists) with the JSON loaded artists (loadedArtists)
            assertEquals(3, storingArtists2.numberOfArtists())
            assertEquals(3, loadedArtists2.numberOfArtists())
            assertEquals(storingArtists2.numberOfArtists(), loadedArtists2.numberOfArtists())
            assertEquals(storingArtists2.findArtist(0), loadedArtists2.findArtist(0))
            assertEquals(storingArtists2.findArtist(1), loadedArtists2.findArtist(1))
            assertEquals(storingArtists2.findArtist(2), loadedArtists2.findArtist(2))
        }

        @Test
        fun `saving and loading an empty collection in YAML doesn't crash app`() {
            // Saving an empty songs.test.yaml file.
            val storingSongs = SongAPI(YAMLSerializer(File("songs.test.yaml")))
            storingSongs.storeSongs()

            // Loading the empty songs.test.yaml file into a new object
            val loadedSongs = SongAPI(YAMLSerializer(File("songs.test.yaml")))
            loadedSongs.loadSongs()

            // Comparing the source of the songs (storingSongs) with the yaml loaded songs (loadedSongs)
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
            storingSongs.storeSongs()

            // Loading songs.test.yaml into a different collection
            val loadedSongs = SongAPI(YAMLSerializer(File("songs.test.yaml")))
            loadedSongs.loadSongs()

            // Comparing the source of the songs (storingSongs) with the yaml loaded songs (loadedSongs)
            assertEquals(3, storingSongs.numberOfSongs())
            assertEquals(3, loadedSongs.numberOfSongs())
            assertEquals(storingSongs.numberOfSongs(), loadedSongs.numberOfSongs())
            assertEquals(storingSongs.findSong(0), loadedSongs.findSong(0))
            assertEquals(storingSongs.findSong(1), loadedSongs.findSong(1))
            assertEquals(storingSongs.findSong(2), loadedSongs.findSong(2))
        }

        @Test
        fun `readArtists in YAML`() {
            // Storing 3 artists to the artists.test.yaml file.
            val storingArtists3 = ArtistAPI(YAMLSerializer(File("artists.test.yaml")))
            storingArtists3.add(michaelJackson!!)
            storingArtists3.add(acDC!!)
            storingArtists3.add(eminem!!)
            storingArtists3.storeArtists()

            // Loading artists.test.yaml into a different collection
            val loadedArtists3 = ArtistAPI(YAMLSerializer(File("artists.test.yaml")))
            loadedArtists3.loadArtists()

            // Comparing the source of the artists (storingArtists) with the YAML loaded artists (loadedArtists)
            assertEquals(3, storingArtists3.numberOfArtists())
            assertEquals(3, loadedArtists3.numberOfArtists())
            assertEquals(storingArtists3.numberOfArtists(), loadedArtists3.numberOfArtists())
            assertEquals(storingArtists3.findArtist(0), loadedArtists3.findArtist(0))
            assertEquals(storingArtists3.findArtist(1), loadedArtists3.findArtist(1))
            assertEquals(storingArtists3.findArtist(2), loadedArtists3.findArtist(2))
        }
    }
}
