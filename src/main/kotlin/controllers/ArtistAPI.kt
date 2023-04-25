package controllers

import com.jakewharton.picnic.Table
import com.jakewharton.picnic.TextBorder
import com.jakewharton.picnic.renderText
import models.Artist
import persistence.Serializer
import utils.SerializerUtils
import utils.UITables

/**
 * A class representing an Artist API that allows for managing artists using a provided serializer.
 *
 * @param serializerType The serializer used for storing and retrieving artists.
 */
class ArtistAPI(serializerType: Serializer) {

    private var serializer: Serializer = serializerType

    private var artists = ArrayList<Artist>()

    /**
     * Adds an artist to the list of artists.
     *
     * @param artist The artist to add.
     * @return True if the artist was added successfully, false otherwise.
     */
    fun add(artist: Artist): Boolean = artists.add(artist)

    /**
     * Deletes an artist from the list of artists by index.
     *
     * @param indexToDelete The index of the artist to delete.
     * @return The deleted artist if the index was valid, null otherwise.
     */
    fun deleteArtist(indexToDelete: Int): Artist? =
        if (isValidListIndex(indexToDelete, artists)) artists.removeAt(indexToDelete) else null

    /**
     * Updates an artist at a given index with new artist data.
     *
     * @param indexToUpdate The index of the artist to update.
     * @param artist The new artist data.
     * @return True if the artist was updated successfully, false otherwise.
     */
    fun updateArtist(indexToUpdate: Int, artist: Artist): Boolean = findArtist(indexToUpdate)?.apply {
        name = artist.name
        foundedDate = artist.foundedDate
        genres = artist.genres
    } != null

    /**
     * Retrieves all artists as a mutable list.
     *
     * @return A mutable list containing all artists.
     */
    fun findAll(): MutableList<Artist> = artists.toMutableList()

    /**
     * Lists all artists in a formatted string.
     *
     * @return A formatted string with all artists or a message indicating no artists stored.
     */
    fun listAllArtists(): String = if (artists.isEmpty()) "No artists stored" else
        generateAllArtistsTable().renderText(border = TextBorder.ROUNDED)

    /**
     * Lists all artists with a given rating in a formatted string.
     *
     * @param rating The rating to filter artists by.
     * @return A formatted string with all artists of the selected rating or a message indicating no artists with the specified rating.
     */
//    fun listArtistsBySelectedRating(rating: Int): String =
//        if (artists.isEmpty() || numberOfArtistsByRating(rating) == 0) "No artists with rating"
//        else
//            generateMultipleArtistsTable(artists.filter { artist -> artist.artistRating == rating }).renderText(border = TextBorder.ROUNDED)

    /**
     * Retrieves the number of artists with a given rating.
     *
     * @param rating The rating to filter artists by.
     * @return The number of artists with the specified rating.
     */
    // fun numberOfArtistsByRating(rating: Int): Int = artists.count { it.artistRating == rating }

    /**
     * Retrieves the total number of artists.
     *
     * @return The number of artists.
     */
    fun numberOfArtists(): Int = artists.size

    /**
     * Retrieves an artist at a given index.
     *
     * @param index The index of the artist to retrieve.
     * @return The artist at the given index if the index is valid, null otherwise.
     */
    fun findArtist(index: Int): Artist? = if (isValidListIndex(index, artists))
        artists[index]
    else null

    /**
     * Finds an artist in the list of artists.
     *
     * @param artist The artist to search for.
     * @return The artist if found, null otherwise.
     */
    fun findUsingArtist(artist: Artist): Artist? = artists.find { it == artist }

    /**
     * Finds the index of an artist in the list of artists.
     *
     * @param artist The artist to search for.
     * @return The index of the artist if found, -1 otherwise.
     */
    fun findIndexUsingArtist(artist: Artist): Int = artists.indexOf(artist)

    /**
     * Determines if a given index is valid in a list.
     *
     * @param index The index to validate.
     * @param list The list in which to check the index.
     * @return True if the index is valid, false otherwise.
     */
    private fun isValidListIndex(index: Int, list: List<Any>): Boolean = (index >= 0 && index < list.size)

    /**
     * Determines if a given index is valid in the list of artists.
     *
     * @param index The index to validate.
     * @return True if the index is valid, false otherwise.
     */
    fun isValidIndex(index: Int): Boolean = isValidListIndex(index, artists)

    fun isValidIndex(index: String?): Boolean = !index.isNullOrBlank() && index.toIntOrNull() != null && isValidIndex(index.toInt())

    /**
     * Generates a table containing artist information, using a predefined template.
     *
     * @param title The title to display in the table.
     * @param data The list of artists to display in the table.
     * @param allArtists An optional parameter to indicate whether to display all artists (default is false).
     * @return A table containing the artist information.
     */
    private fun artistInfoTemplate(title: String, data: List<Artist>, allArtists: Boolean = false): Table =
        UITables.artistInfoTemplate(title, data, allArtists)

    /**
     * Generates a table containing a single artist's information.
     *
     * @param artist The artist to display.
     * @return A table containing the artist's information.
     */
    fun generateArtistTable(artist: Artist): Table = artistInfoTemplate("Artist Information", listOf(artist))

    /**
     * Generates a table containing multiple artists' information.
     *
     * @param artists The list of artists to display.
     * @return A table containing the artists' information.
     */
    fun generateMultipleArtistsTable(artists: List<Artist>): Table = artistInfoTemplate("Multiple Artist Information", artists)

    /**
     * Generates a table containing all artists' information.
     *
     * @return A table containing all artists' information.
     */
    fun generateAllArtistsTable(): Table = artistInfoTemplate("All Artist Information", artists, true)

    /**
     * Seeds the artists with a predefined set of artists.
     */
    fun seedArtists() {
        artists = SerializerUtils.getSeededArtists()
    }

    /**
     * Loads the artists from the serializer.
     *
     * @return True if the artists were loaded successfully, false otherwise.
     * @throws Exception if an error occurs while loading the artists.
     */
    @Throws(Exception::class)
    fun loadArtists(): Boolean =
        serializer.readArtists()?.also {
            artists = it
        } != null

    /**
     * Stores the artists using the serializer.
     *
     * @throws Exception if an error occurs while storing the artists.
     */
    @Throws(Exception::class)
    fun storeArtists() = serializer.writeArtists(artists)
}
