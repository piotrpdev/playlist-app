package controllers

import com.jakewharton.picnic.renderText
import models.Artist
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import utils.SerializerUtils.ldp
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ArtistAPITest {
    private var michaelJackson: Artist? = null
    private var acDC: Artist? = null
    private var eminem: Artist? = null
    private var gunsNRoses: Artist? = null
    private var theBeatles: Artist? = null
    private var populatedArtists: ArtistAPI? = null
    private var emptyArtists: ArtistAPI? = null

    @BeforeEach
    fun buildUp() {
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
    inner class AddArtists {
        @Test
        fun `adding an Artist to a populated list adds to ArrayList`() {
            val newArtist = Artist("John Mayer", ldp("1985-03-15T13:05"), listOf("Rock", "Pop", "Blues"))
            assertEquals(5, populatedArtists!!.numberOfArtists())
            assertTrue(populatedArtists!!.add(newArtist))
            assertEquals(6, populatedArtists!!.numberOfArtists())
            assertEquals(newArtist, populatedArtists!!.findArtist(populatedArtists!!.numberOfArtists() - 1))
        }

        @Test
        fun `adding an Artist to an empty list adds to ArrayList`() {
            val newArtist = Artist("John Mayer", ldp("1985-03-15T13:05"), listOf("Rock", "Pop", "Blues"))
            assertEquals(0, emptyArtists!!.numberOfArtists())
            assertTrue(emptyArtists!!.add(newArtist))
            assertEquals(1, emptyArtists!!.numberOfArtists())
            assertEquals(newArtist, emptyArtists!!.findArtist(emptyArtists!!.numberOfArtists() - 1))
        }
    }

    @Nested
    inner class GenerateTableMethods {
        @Test
        fun `generateAllArtistsTable returns a table with the correct number of rows`() {
            val table = populatedArtists!!.generateAllArtistsTable()
            assertEquals(5, table.body.rows.size)
        }

        @Test
        fun `generateAllArtistsTable contains the correct artist names`() {
            val tableString = populatedArtists!!.generateAllArtistsTable().renderText()
            assertTrue(tableString.contains("Michael Jackson"))
            assertTrue(tableString.contains("AC/DC"))
            assertTrue(tableString.contains("Eminem"))
            assertTrue(tableString.contains("Guns N' Roses"))
            assertTrue(tableString.contains("The Beatles"))
        }

        @Test
        fun `generateArtistTable returns a table with the correct number of rows`() {
            val table = populatedArtists!!.generateArtistTable(michaelJackson!!)
            assertEquals(1, table.body.rows.size)
        }

        @Test
        fun `generateArtistTable contains the artist name`() {
            val tableString = populatedArtists!!.generateArtistTable(michaelJackson!!).renderText()
            assertTrue(tableString.contains("Michael Jackson"))
        }

        @Test
        fun `listAllArtists returns a table with the correct number of rows`() {
            val table = populatedArtists!!.generateAllArtistsTable()
            assertEquals(5, table.body.rows.size)
        }

        @Test
        fun `generateMultipleArtistsTable returns a table with the correct number of rows`() {
            val table = populatedArtists!!.generateMultipleArtistsTable(listOf(michaelJackson!!, acDC!!))
            assertEquals(2, table.body.rows.size)
        }
    }

    @Nested
    inner class FindingMethods {
        @Test
        fun `findAll returns all artists in the ArrayList`() {
            val artists = populatedArtists!!.findAll()
            assertEquals(5, artists.size)
            assertEquals(michaelJackson, artists[0])
            assertEquals(acDC, artists[1])
            assertEquals(eminem, artists[2])
            assertEquals(gunsNRoses, artists[3])
            assertEquals(theBeatles, artists[4])
        }

        @Test
        fun `findArtist returns the correct artist`() {
            assertEquals(michaelJackson, populatedArtists!!.findUsingArtist(michaelJackson!!))
            assertEquals(acDC, populatedArtists!!.findUsingArtist(acDC!!))
            assertEquals(eminem, populatedArtists!!.findUsingArtist(eminem!!))
            assertEquals(gunsNRoses, populatedArtists!!.findUsingArtist(gunsNRoses!!))
            assertEquals(theBeatles, populatedArtists!!.findUsingArtist(theBeatles!!))
        }

        @Test
        fun `findIndexUsingArtist returns the correct index`() {
            assertEquals(0, populatedArtists!!.findIndexUsingArtist(michaelJackson!!))
            assertEquals(1, populatedArtists!!.findIndexUsingArtist(acDC!!))
            assertEquals(2, populatedArtists!!.findIndexUsingArtist(eminem!!))
            assertEquals(3, populatedArtists!!.findIndexUsingArtist(gunsNRoses!!))
            assertEquals(4, populatedArtists!!.findIndexUsingArtist(theBeatles!!))
        }
    }

    @Nested
    inner class ValidIndex {
        @Test
        fun `validIndex returns true when index is within range of ArrayList`() {
            assertTrue(populatedArtists!!.isValidIndex(0))
            assertTrue(populatedArtists!!.isValidIndex(4))
        }

        @Test
        fun `validIndex returns false when index is out of range of ArrayList`() {
            assertFalse(populatedArtists!!.isValidIndex(-1))
            assertFalse(populatedArtists!!.isValidIndex(5))
        }

        @Test
        fun `validIndex returns false when index is not a number`() {
            assertFalse(populatedArtists!!.isValidIndex("a"))
        }

        @Test
        fun `validIndex returns false when index is a blank string`() {
            assertFalse(populatedArtists!!.isValidIndex(""))
        }

        @Test
        fun `validIndex returns false when index is null`() {
            assertFalse(populatedArtists!!.isValidIndex(null))
        }

        @Test
        fun `validIndex returns false when index is a decimal number`() {
            assertFalse(populatedArtists!!.isValidIndex("1.5"))
        }
    }

    @Nested
    inner class DeleteArtists {
        @Test
        fun `deleting an Artist that does not exist, returns null`() {
            assertNull(emptyArtists!!.deleteArtist(0))
            assertNull(populatedArtists!!.deleteArtist(-1))
            assertNull(populatedArtists!!.deleteArtist(5))
        }

        @Test
        fun `deleting an Artist that exists delete and returns deleted object`() {
            assertEquals(5, populatedArtists!!.numberOfArtists())
            assertEquals(theBeatles, populatedArtists!!.deleteArtist(4))
            assertEquals(4, populatedArtists!!.numberOfArtists())
            assertEquals(michaelJackson, populatedArtists!!.deleteArtist(0))
            assertEquals(3, populatedArtists!!.numberOfArtists())
        }
    }

    @Nested
    inner class UpdateArtists {
        @Test
        fun `updating an Artist that does not exist, returns false`() {
            assertFalse(
                populatedArtists!!.updateArtist(
                    6,
                    Artist("Updating Artist", ldp("1985-03-15T13:05"), listOf("Rock", "Pop", "Blues"))
                )
            )
            assertFalse(
                populatedArtists!!.updateArtist(
                    -1,
                    Artist("Updating Artist", ldp("1985-03-15T13:05"), listOf("Rock", "Pop", "Blues"))
                )
            )
            assertFalse(
                emptyArtists!!.updateArtist(
                    0,
                    Artist("Updating Artist", ldp("1985-03-15T13:05"), listOf("Rock", "Pop", "Blues"))
                )
            )
        }

        @Test
        fun `updating an Artist that exists returns true and updates`() {
            // check artist 5 exists and check the contents
            assertEquals(theBeatles, populatedArtists!!.findUsingArtist(theBeatles!!))
            assertEquals("The Beatles", populatedArtists!!.findUsingArtist(theBeatles!!)!!.name)
            assertEquals(ldp("1960-02-14T10:30"), populatedArtists!!.findUsingArtist(theBeatles!!)!!.foundedDate)
            assertEquals(listOf("Rock", "Pop"), populatedArtists!!.findUsingArtist(theBeatles!!)!!.genres)

            // update artist 5 with new information and ensure contents updated successfully
            assertTrue(
                populatedArtists!!.updateArtist(
                    4,
                    Artist("Updating Artist", ldp("1985-03-15T13:05"), listOf("Rock", "Pop", "Blues"))
                )
            )
            assertEquals("Updating Artist", populatedArtists!!.findUsingArtist(theBeatles!!)!!.name)
            assertEquals(ldp("1985-03-15T13:05"), populatedArtists!!.findUsingArtist(theBeatles!!)!!.foundedDate)
            assertEquals(listOf("Rock", "Pop", "Blues"), populatedArtists!!.findUsingArtist(theBeatles!!)!!.genres)
        }
    }
}
