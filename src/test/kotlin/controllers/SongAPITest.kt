package controllers

import com.jakewharton.picnic.renderText
import models.Song
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.io.File
import kotlin.test.assertEquals

class SongAPITest {

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
    inner class AddSongs {
        @Test
        fun `adding a Song to a populated list adds to ArrayList`() {
            val newSong = Song("Study Lambdas", 1, "College", false)
            assertEquals(5, populatedSongs!!.numberOfSongs())
            assertTrue(populatedSongs!!.add(newSong))
            assertEquals(6, populatedSongs!!.numberOfSongs())
            assertEquals(newSong, populatedSongs!!.findSong(populatedSongs!!.numberOfSongs() - 1))
        }

        @Test
        fun `adding a Song to an empty list adds to ArrayList`() {
            val newSong = Song("Study Lambdas", 1, "College", false)
            assertEquals(0, emptySongs!!.numberOfSongs())
            assertTrue(emptySongs!!.add(newSong))
            assertEquals(1, emptySongs!!.numberOfSongs())
            assertEquals(newSong, emptySongs!!.findSong(emptySongs!!.numberOfSongs() - 1))
        }
    }

    @Nested
    inner class GenerateTableMethods {
        @Test
        fun `generateAllSongsTable returns a table with the correct number of rows`() {
            val table = populatedSongs!!.generateAllSongsTable()
            assertEquals(5, table.body.rows.size)
        }

        @Test
        fun `generateAllSongsTable contains the correct song titles`() {
            val tableString = populatedSongs!!.generateAllSongsTable().renderText()
            assertTrue(tableString.contains("Learning Kotlin"))
            assertTrue(tableString.contains("Summer Holiday to France"))
            assertTrue(tableString.contains("Code App"))
            assertTrue(tableString.contains("Test App"))
            assertTrue(tableString.contains("Swim - Pool"))
        }

        @Test
        fun `generateSongTable returns a table with the correct number of rows`() {
            val table = populatedSongs!!.generateSongTable(learnKotlin!!)
            assertEquals(1, table.body.rows.size)
        }

        @Test
        fun `generateSongTable contains the song title`() {
            val tableString = populatedSongs!!.generateSongTable(learnKotlin!!).renderText()
            assertTrue(tableString.contains("Learning Kotlin"))
        }
    }

    @Nested
    inner class FindingMethods {
        @Test
        fun `findAll returns all songs in the ArrayList`() {
            val songs = populatedSongs!!.findAll()
            assertEquals(5, songs.size)
            assertEquals(learnKotlin, songs[0])
            assertEquals(summerHoliday, songs[1])
            assertEquals(codeApp, songs[2])
            assertEquals(testApp, songs[3])
            assertEquals(swim, songs[4])
        }

        @Test
        fun `findUsingSong returns the correct song`() {
            assertEquals(learnKotlin, populatedSongs!!.findUsingSong(learnKotlin!!))
            assertEquals(summerHoliday, populatedSongs!!.findUsingSong(summerHoliday!!))
            assertEquals(codeApp, populatedSongs!!.findUsingSong(codeApp!!))
            assertEquals(testApp, populatedSongs!!.findUsingSong(testApp!!))
            assertEquals(swim, populatedSongs!!.findUsingSong(swim!!))
        }

        @Test
        fun `findIndexUsingSong returns the correct index`() {
            assertEquals(0, populatedSongs!!.findIndexUsingSong(learnKotlin!!))
            assertEquals(1, populatedSongs!!.findIndexUsingSong(summerHoliday!!))
            assertEquals(2, populatedSongs!!.findIndexUsingSong(codeApp!!))
            assertEquals(3, populatedSongs!!.findIndexUsingSong(testApp!!))
            assertEquals(4, populatedSongs!!.findIndexUsingSong(swim!!))
        }
    }

    @Nested
    inner class ValidIndex {
        @Test
        fun `validIndex returns true when index is within range of ArrayList`() {
            assertTrue(populatedSongs!!.isValidIndex(0))
            assertTrue(populatedSongs!!.isValidIndex(4))
        }

        @Test
        fun `validIndex returns false when index is out of range of ArrayList`() {
            assertFalse(populatedSongs!!.isValidIndex(-1))
            assertFalse(populatedSongs!!.isValidIndex(5))
        }

        @Test
        fun `validIndex returns false when index is not a number`() {
            assertFalse(populatedSongs!!.isValidIndex("a"))
        }

        @Test
        fun `validIndex returns false when index is a blank string`() {
            assertFalse(populatedSongs!!.isValidIndex(""))
        }

        @Test
        fun `validIndex returns false when index is null`() {
            assertFalse(populatedSongs!!.isValidIndex(null))
        }

        @Test
        fun `validIndex returns false when index is a decimal number`() {
            assertFalse(populatedSongs!!.isValidIndex("1.5"))
        }
    }

    @Nested
    inner class ListingMethods {

        @Test
        fun `listAllSongs returns No Songs Stored message when ArrayList is empty`() {
            assertEquals(0, emptySongs!!.numberOfSongs())
            assertTrue(emptySongs!!.listAllSongs().lowercase().contains("no songs"))
        }

        @Test
        fun `listAllSongs returns Songs when ArrayList has songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            val songsString = populatedSongs!!.listAllSongs().lowercase()
            assertTrue(songsString.contains("learning kotlin"))
            assertTrue(songsString.contains("code app"))
            assertTrue(songsString.contains("test app"))
            assertTrue(songsString.contains("swim"))
            assertTrue(songsString.contains("summer holiday"))
        }

        @Test
        fun `listSafeSongs returns No Safe Songs Stored message when ArrayList is empty`() {
            assertEquals(0, emptySongs!!.numberOfSongs())
            assertTrue(emptySongs!!.listSafeSongs().lowercase().contains("no safe songs"))
        }

        @Test
        fun `listSafeSongs returns No Safe Songs Stored message when ArrayList has no safe songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            populatedSongs!!.findSong(0)!!.isSongExplicit = true
            populatedSongs!!.findSong(1)!!.isSongExplicit = true
            populatedSongs!!.findSong(2)!!.isSongExplicit = true
            populatedSongs!!.findSong(3)!!.isSongExplicit = true
            populatedSongs!!.findSong(4)!!.isSongExplicit = true
            assertTrue(populatedSongs!!.listSafeSongs().lowercase().contains("no safe songs"))
        }

        @Test
        fun `listSafeSongs returns Safe Songs when ArrayList has safe songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            val songsString = populatedSongs!!.listSafeSongs().lowercase()
            assertTrue(songsString.contains("learning kotlin"))
            assertTrue(songsString.contains("code app"))
            assertTrue(songsString.contains("test app"))
            assertTrue(songsString.contains("swim"))
            assertTrue(songsString.contains("summer holiday"))
        }

        @Test
        fun `listExplicitSongs returns No Explicit Songs Stored message when ArrayList is empty`() {
            assertEquals(0, emptySongs!!.numberOfSongs())
            assertTrue(emptySongs!!.listExplicitSongs().lowercase().contains("no explicit songs"))
        }

        @Test
        fun `listExplicitSongs returns No Explicit Songs Stored message when ArrayList has no explicit songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            assertTrue(populatedSongs!!.listExplicitSongs().lowercase().contains("no explicit songs"))
        }

        @Test
        fun `listExplicitSongs returns Explicit Songs when ArrayList has explicit songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            populatedSongs!!.findSong(0)!!.isSongExplicit = true
            populatedSongs!!.findSong(1)!!.isSongExplicit = true
            populatedSongs!!.findSong(2)!!.isSongExplicit = true
            populatedSongs!!.findSong(3)!!.isSongExplicit = true
            populatedSongs!!.findSong(4)!!.isSongExplicit = true
            val songsString = populatedSongs!!.listExplicitSongs().lowercase()
            assertTrue(songsString.contains("learning kotlin"))
            assertTrue(songsString.contains("code app"))
            assertTrue(songsString.contains("test app"))
            assertTrue(songsString.contains("swim"))
            assertTrue(songsString.contains("summer holiday"))
        }

        @Test
        fun `listSongsBySelectedRating returns No Songs with Rating Stored message when ArrayList is empty`() {
            assertEquals(0, emptySongs!!.numberOfSongs())
            assertTrue(emptySongs!!.listSongsBySelectedRating(1).lowercase().contains("no songs with rating"))
        }

        @Test
        fun `listSongsBySelectedRating returns No Songs with Rating Stored message when ArrayList has no songs with rating stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            assertTrue(populatedSongs!!.listSongsBySelectedRating(2).lowercase().contains("no songs with rating"))
        }

        @Test
        fun `listSongsBySelectedRating returns Songs with Rating when ArrayList has songs with rating stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            val songsString = populatedSongs!!.listSongsBySelectedRating(4).lowercase()
            assertTrue(songsString.contains("code app"))
            assertTrue(songsString.contains("test app"))
        }

        @Test
        fun `listStaleSongs returns No Stale Songs Stored message when ArrayList is empty`() {
            assertEquals(0, emptySongs!!.numberOfSongs())
            assertTrue(emptySongs!!.listStaleSongs(1).lowercase().contains("no stale songs"))
        }

        @Test
        fun `listStaleSongs returns No Stale Songs Stored message when ArrayList has no stale songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            assertTrue(populatedSongs!!.listStaleSongs(365*100).lowercase().contains("no stale songs"))
        }

        @Test
        fun `listStaleSongs returns Stale Songs when ArrayList has stale songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            val songsString = populatedSongs!!.listStaleSongs(1).lowercase()
            assertTrue(songsString.contains("learning kotlin"))
            assertTrue(songsString.contains("code app"))
            assertTrue(songsString.contains("test app"))
            assertTrue(songsString.contains("swim"))
            assertTrue(songsString.contains("summer holiday"))
        }

        @Test
        fun `listImportantSongs returns No Important Songs Stored message when ArrayList is empty`() {
            assertEquals(0, emptySongs!!.numberOfSongs())
            assertTrue(emptySongs!!.listImportantSongs().lowercase().contains("no important songs"))
        }

        @Test
        fun `listImportantSongs returns No Important Songs Stored message when ArrayList has no important songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            populatedSongs!!.deleteSong(populatedSongs!!.findIndexUsingSong(summerHoliday!!))
            assertTrue(populatedSongs!!.listImportantSongs().lowercase().contains("no important songs"))
        }

        @Test
        fun `listImportantSongs returns Important Songs when ArrayList has important songs stored`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            val songsString = populatedSongs!!.listImportantSongs().lowercase()
            assertTrue(songsString.contains("summer holiday"))
        }
    }

    @Nested
    inner class DeleteSongs {

        @Test
        fun `deleting a Song that does not exist, returns null`() {
            assertNull(emptySongs!!.deleteSong(0))
            assertNull(populatedSongs!!.deleteSong(-1))
            assertNull(populatedSongs!!.deleteSong(5))
        }

        @Test
        fun `deleting a song that exists delete and returns deleted object`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            assertEquals(swim, populatedSongs!!.deleteSong(4))
            assertEquals(4, populatedSongs!!.numberOfSongs())
            assertEquals(learnKotlin, populatedSongs!!.deleteSong(0))
            assertEquals(3, populatedSongs!!.numberOfSongs())
        }

        // Test for `removeMultipleSongs(songList: List<Song>)`
        @Test
        fun `removeMultipleSongs returns false when songList is empty`() {
            assertFalse(emptySongs!!.removeMultipleSongs(emptyList()))
        }

        @Test
        fun `removeMultipleSongs removes songs from the ArrayList`() {
            populatedSongs!!.removeMultipleSongs(listOf(swim!!, codeApp!!))
            assertEquals(3, populatedSongs!!.numberOfSongs())
            val songsString = populatedSongs!!.listAllSongs().lowercase()
            assertFalse(songsString.contains("swim"))
            assertFalse(songsString.contains("code app"))
            assertTrue(songsString.contains("learning kotlin"))
            assertTrue(songsString.contains("test app"))
            assertTrue(songsString.contains("summer holiday"))
        }
    }

    @Nested
    inner class UpdateSongs {
        @Test
        fun `updating a song that does not exist returns false`() {
            assertFalse(populatedSongs!!.updateSong(6, Song("Updating Song", 2, "Work", false)))
            assertFalse(populatedSongs!!.updateSong(-1, Song("Updating Song", 2, "Work", false)))
            assertFalse(emptySongs!!.updateSong(0, Song("Updating Song", 2, "Work", false)))
        }

        @Test
        fun `updating a song that exists returns true and updates`() {
            //check song 5 exists and check the contents
            assertEquals(swim, populatedSongs!!.findSong(4))
            assertEquals("Swim - Pool", populatedSongs!!.findSong(4)!!.songTitle)
            assertEquals(3, populatedSongs!!.findSong(4)!!.songRating)
            assertEquals("Hobby", populatedSongs!!.findSong(4)!!.songGenre)

            //update song 5 with new information and ensure contents updated successfully
            assertTrue(populatedSongs!!.updateSong(4, Song("Updating Song", 2, "College", false)))
            assertEquals("Updating Song", populatedSongs!!.findSong(4)!!.songTitle)
            assertEquals(2, populatedSongs!!.findSong(4)!!.songRating)
            assertEquals("College", populatedSongs!!.findSong(4)!!.songGenre)
        }
    }

    @Nested
    inner class ArchiveTests {

        @Test
        fun `archiving a song that does not exist returns false`() {
            assertFalse(emptySongs!!.archiveSong(0))
            assertFalse(populatedSongs!!.archiveSong(-1))
            assertFalse(populatedSongs!!.archiveSong(5))
        }

        @Test
        fun `archiving a song that exists returns true and archives`() {
            //check song 5 exists and check the contents
            assertEquals(swim, populatedSongs!!.findSong(4))
            assertEquals("Swim - Pool", populatedSongs!!.findSong(4)!!.songTitle)
            assertEquals(3, populatedSongs!!.findSong(4)!!.songRating)
            assertEquals("Hobby", populatedSongs!!.findSong(4)!!.songGenre)
            assertFalse(populatedSongs!!.findSong(4)!!.isSongExplicit)

            //archive song 5 and ensure contents updated successfully
            assertTrue(populatedSongs!!.archiveSong(4))
            assertEquals("Swim - Pool", populatedSongs!!.findSong(4)!!.songTitle)
            assertEquals(3, populatedSongs!!.findSong(4)!!.songRating)
            assertEquals("Hobby", populatedSongs!!.findSong(4)!!.songGenre)
            assertTrue(populatedSongs!!.findSong(4)!!.isSongExplicit)
        }
    }

    @Nested
    inner class CountingMethods {

        @Test
        fun numberOfSongsCalculatedCorrectly() {
            assertEquals(5, populatedSongs!!.numberOfSongs())
            assertEquals(0, emptySongs!!.numberOfSongs())
        }

        @Test
        fun numberOfExplicitSongsCalculatedCorrectly() {
            assertEquals(0, populatedSongs!!.numberOfExplicitSongs())
            assertEquals(0, emptySongs!!.numberOfExplicitSongs())
        }

        @Test
        fun numberOfSafeSongsCalculatedCorrectly() {
            assertEquals(5, populatedSongs!!.numberOfSafeSongs())
            assertEquals(0, emptySongs!!.numberOfSafeSongs())
        }

        @Test
        fun numberOfSongsByRatingCalculatedCorrectly() {
            assertEquals(1, populatedSongs!!.numberOfSongsByRating(1))
            assertEquals(0, populatedSongs!!.numberOfSongsByRating(2))
            assertEquals(1, populatedSongs!!.numberOfSongsByRating(3))
            assertEquals(2, populatedSongs!!.numberOfSongsByRating(4))
            assertEquals(1, populatedSongs!!.numberOfSongsByRating(5))
            assertEquals(0, emptySongs!!.numberOfSongsByRating(1))
        }

        @Test
        fun numberOfStaleSongsCalculatedCorrectly() {
            assertEquals(5, populatedSongs!!.numberOfStaleSongs(0))
            assertEquals(5, populatedSongs!!.numberOfStaleSongs(1))
            assertEquals(0, populatedSongs!!.numberOfStaleSongs(365*100))
            assertEquals(0, populatedSongs!!.numberOfStaleSongs(365*50))
        }

        @Test
        fun numberOfImportantSongsCalculatedCorrectly() {
            assertEquals(1, populatedSongs!!.numberOfImportantSongs())
            assertEquals(0, emptySongs!!.numberOfImportantSongs())
        }
    }

    @Nested
    inner class SearchMethods {

        @Test
        fun `search songs by title returns no songs when no songs with that title exist`() {
            //Searching a populated collection for a title that doesn't exist.
            assertEquals(5, populatedSongs!!.numberOfSongs())
            val searchResults = populatedSongs!!.searchByTitle("no results expected")
            assertTrue(searchResults.isEmpty())

            //Searching an empty collection
            assertEquals(0, emptySongs!!.numberOfSongs())
            assertTrue(emptySongs!!.searchByTitle("").isEmpty())
        }

        @Test
        fun `search songs by title returns songs when songs with that title exist`() {
            assertEquals(5, populatedSongs!!.numberOfSongs())

            //Searching a populated collection for a full title that exists (case matches exactly)
            var searchResults = populatedSongs!!.searchByTitle("Code App")
            assertTrue(searchResults.contains("Code App"))
            assertFalse(searchResults.contains("Test App"))

            //Searching a populated collection for a partial title that exists (case matches exactly)
            searchResults = populatedSongs!!.searchByTitle("App")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))

            //Searching a populated collection for a partial title that exists (case doesn't match)
            searchResults = populatedSongs!!.searchByTitle("aPp")
            assertTrue(searchResults.contains("Code App"))
            assertTrue(searchResults.contains("Test App"))
            assertFalse(searchResults.contains("Swim - Pool"))
        }
    }
}
