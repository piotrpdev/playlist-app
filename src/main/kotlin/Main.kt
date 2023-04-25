import com.jakewharton.picnic.TextBorder
import com.jakewharton.picnic.renderText
import controllers.SongAPI
import models.Song
import mu.KotlinLogging
import persistence.JSONSerializer
import persistence.XMLSerializer
import persistence.YAMLSerializer
import utils.SerializerUtils
import utils.UITables
import utils.ValidatorUtils.getValidPropertyValue
import utils.ValidatorUtils.yesNoIsValid
import java.io.File
import java.time.LocalDateTime
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

private val songAPI = SongAPI(XMLSerializer(File("songs.xml")))
//private val songAPI = SongAPI(JSONSerializer(File("songs.json")))
//private val songAPI = SongAPI(YAMLSerializer(File("songs.yaml")))

/**
 * Prints all songs in a tabular format with rounded borders.
 */
fun printAllSongs() = println(songAPI.generateAllSongsTable().renderText(border = TextBorder.ROUNDED))

/**
 * Generates a new song instance using user input for song properties.
 * @param old An optional Song instance to update. The new song will use the old song's properties as default values.
 * @return A new Song instance with the provided properties.
 */
fun generateSong(old: Song? = null): Song {
    logger.debug { "Generating song" }

    val songTitle: String = getValidPropertyValue("songTitle", old?.songTitle)
    val songRating: Int = getValidPropertyValue("songRating", old?.songRating)
    val songGenre: String = getValidPropertyValue("songGenre", old?.songGenre)
    val isSongExplicit: Boolean = getValidPropertyValue("isSongExplicit", old?.isSongExplicit)

    return Song(songTitle, songRating, songGenre, isSongExplicit)
}

/**
 * Retrieves a Song instance based on its index.
 * @return The Song instance found, or null if not found.
 */
internal fun getSongByIndex(): Song? {
    logger.debug { "Trying to get song by index" }

    if (songAPI.numberOfSongs() == 0) {
        println("No songs found.")
        return null
    }

    printAllSongs()

    val allSongs = songAPI.findAll()

    val songIndex: Int = getValidPropertyValue("songIndex", customValidator = { songAPI.isValidIndex(it) })

    val song = allSongs[songIndex]

    logger.debug { "Song found: $song" }
    logger.debug { "Displaying song" }
    println("\nThe following song was found:")
    println(songAPI.generateSongTable(song).renderText(border = TextBorder.ROUNDED))

    return song
}

/**
 * Retrieves multiple Song instances based on their index.
 * @return A MutableList of Song instances found, or null if none found.
 */
internal fun getMultipleSongsByIndex(): MutableList<Song>? {
    logger.debug { "Trying to get multiple songs by index" }

    if (songAPI.numberOfSongs() == 0) {
        println("No songs found.")
        return null
    }

    printAllSongs()

    val searchMultiple: Boolean = getValidPropertyValue(
        "yesNo",
        customPrompt = "Do you want to search for multiple songs using their index? (y/n): "
    )

    if (!searchMultiple) {
        logger.debug { "User does not want to search for multiple songs using index" }
        return songAPI.findAll()
    }

    var searching = true
    val songList: MutableList<Song> = ArrayList()

    fun songInList(song: Song?): Boolean {
        if (song == null) return false
        return songList.any { p -> p == song }
    }

    while (searching) {
        val song = getSongByIndex()

        if (songInList(song)) {
            println("Song already added to list.")
        }

        if (song != null && !songInList(song)) {
            songList.add(song)
        }

        val searchAgain: Boolean = getValidPropertyValue(
            "yesNo",
            customPrompt = "Do you want to add another song to the list using their index? (y/n): "
        )

        if (!searchAgain) {
            searching = false
        }

        println()
    }

    logger.debug { "Returning song list (might be empty)." }
    return songList.ifEmpty { null }
}

/**
 * Filters a MutableList of Song instances based on user input.
 * @param songList The MutableList of Song instances to filter.
 * @return A MutableList of filtered Song instances, or null if none found.
 */
fun getFilteredSongs(songList: MutableList<Song>): MutableList<Song>? {
    logger.debug { "Trying to get filtered songs" }

    val input: Boolean = getValidPropertyValue(
        "yesNo",
        customPrompt = "Do you want to filter the songs? (y/n): "
    )

    if (!input) {
        logger.debug { "Not filtering songs" }
        return songList
    }

    var filtering = true
    while (filtering) {
        print("How do you want to filter the songs? (1 - Title, 2 - Rating, 3 - Genre, 4 - Explicit, 5 - Updated At, 6 - Created At): ")
        when (readln().toIntOrNull()) {
            1 -> {
                val songTitle: String = getValidPropertyValue("songTitle")
                songList.removeIf { !it.songTitle.lowercase().contains(songTitle.lowercase()) }
            }

            2 -> {
                val songRating: Int = getValidPropertyValue("songRating")
                songList.removeIf { it.songRating != songRating }
            }

            3 -> {
                val songGenre: String = getValidPropertyValue("songGenre")
                songList.removeIf { it.songGenre != songGenre }
            }

            4 -> {
                val isSongExplicit: Boolean = getValidPropertyValue("isSongExplicit")
                songList.removeIf { it.isSongExplicit != isSongExplicit }
            }

            5 -> {
                val updatedAt: LocalDateTime = getValidPropertyValue("updatedAt")
                songList.removeIf { it.updatedAt.compareTo(updatedAt) != 0 }
            }

            6 -> {
                val createdAt: LocalDateTime = getValidPropertyValue("createdAt")
                songList.removeIf { it.createdAt.compareTo(createdAt) != 0 }
            }

            else -> {
                println("Error: invalid option. Please enter a valid option.")
                continue
            }
        }

        val filterAgain: Boolean = getValidPropertyValue(
            "yesNo",
            customPrompt = "Do you want to filter the songs again? (y/n): "
        )

        if (!filterAgain) {
            filtering = false
        }

        println()
    }

    logger.debug { "Returning filtered song list (might be empty)." }
    return songList.ifEmpty { null }
}

/**
 * Sorts a MutableList of Song instances based on user input.
 * @param songList The MutableList of Song instances to sort. Defaults to all songs.
 * @return A MutableList of sorted Song instances.
 */
fun getSortedSongs(songList: MutableList<Song> = songAPI.findAll()): MutableList<Song> {
    logger.debug { "Trying to get sorted songs" }

    val input: Boolean = getValidPropertyValue(
        "yesNo",
        customPrompt = "Do you want to sort the songs? (y/n): "
    )

    if (!input) {
        logger.debug { "Not sorting songs" }
        return songList
    }

    print("How do you want to sort the songs? (1 - Title, 2 - Rating, 3 - Genre, 4 - Explicit, 5 - Updated At, 6 - Created At): ")

    when (readln().toIntOrNull()) {
        1 -> songList.sortBy { it.songTitle }
        2 -> songList.sortByDescending { it.songRating }
        3 -> songList.sortBy { it.songGenre }
        4 -> songList.sortBy { it.isSongExplicit }
        5 -> songList.sortBy { it.updatedAt }
        6 -> songList.sortBy { it.createdAt }
        else -> {
            println("Error: Invalid option. Please enter a valid option.")
            return getSortedSongs()
        }
    }

    logger.debug { "Returning sorted song list (might be empty)." }
    return songList
}

/**
 * Adds a new song to the SongAPI using user input for properties.
 */
fun addSong() {
    logger.debug { "addSong() function invoked" }

    val song = generateSong()

    logger.debug { "Adding song: $song" }
    songAPI.add(song)

    println("\nThe following song was added successfully:\n")
    println(songAPI.generateSongTable(songAPI.findUsingSong(song) ?: song).renderText(border = TextBorder.ROUNDED))
}

/**
 * Displays a selected song based on its index.
 */
fun viewSong() {
    logger.debug { "viewSong() function invoked" }

    getSongByIndex() ?: return
}

/**
 * Updates an existing song with new properties based on user input.
 */
fun updateSong() {
    logger.debug { "updateSong() function invoked" }

    val song = getSongByIndex() ?: return

    println("\nPlease enter the new details for the song (Enter nothing to keep previous value):")

    val updatedSong = generateSong(song)
    updatedSong.createdAt = song.createdAt

    logger.debug { "Song found, updating song" }
    songAPI.updateSong(songAPI.findIndexUsingSong(song), updatedSong)

    println("\nThe song was updated successfully:\n")
    println(songAPI.generateSongTable(updatedSong).renderText(border = TextBorder.ROUNDED))
}

/**
 * Deletes a song based on its index.
 */
fun deleteSong() {
    logger.debug { "deleteSong() function invoked" }

    if (songAPI.numberOfSongs() == 0) {
        println("No songs found.")
        return
    }

    printAllSongs()

    val songIndex: Int = getValidPropertyValue("songIndex", customPrompt = "Enter song index to delete: ", customValidator = { songAPI.isValidIndex(it) })

    //pass the index of the song to SongAPI for deleting and check for success.
    val songToDelete = songAPI.deleteSong(songIndex)
    if (songToDelete != null) {
        println("Delete Successful! Deleted song: ${songToDelete.songTitle}")
    } else {
        println("Delete NOT Successful")
    }
}

/**
 * Toggles the explicitify status of a song based on its index.
 */
fun explicitifySong() {
    logger.debug { "explicitifySong() function invoked" }

    if (songAPI.numberOfSongs() == 0) {
        println("No songs found.")
        return
    }

    printAllSongs()

    val songIndex: Int = getValidPropertyValue("songIndex", customPrompt = "Enter song index to explicitify: ", customValidator = { songAPI.isValidIndex(it) })

    //pass the index of the song and the new song details to SongAPI for updating and check for success.
    if (songAPI.explicitifySong(songIndex)) {
        println("Explicitify Successful")
    } else {
        println("Explicitify Failed")
    }
}

/**
 * Searches for songs based on index, filters, and sorts them based on user input.
 */
fun searchSongs() {
    logger.debug { "searchSongs() function invoked" }

    val songList = getMultipleSongsByIndex() ?: return
    val filteredSongList = getFilteredSongs(songList) ?: return
    val sortedSongList = getSortedSongs(filteredSongList)

    println("Here are the songs you wanted to view:")
    println(songAPI.generateMultipleSongsTable(sortedSongList).renderText(border = TextBorder.ROUNDED))
}

/**
 * Removes multiple songs based on their index.
 */
fun removeMultipleSongs() {
    logger.debug { "removeMultipleSongs() function invoked" }

    val songList = getMultipleSongsByIndex() ?: return

    println("Here are the songs you wanted to remove:")
    println(songAPI.generateMultipleSongsTable(songList).renderText(border = TextBorder.ROUNDED))

    val delete: Boolean = getValidPropertyValue(
        "yesNo",
        customPrompt = "Are you sure you want to remove these songs? (y/n): "
    )

    if (!delete) {
        println("Songs not deleted.")
        return
    }

    logger.debug { "Removing multiple songs" }
    songAPI.removeMultipleSongs(songList)
    println("Songs deleted.")
}

/**
 * Lists songs based on user-selected criteria.
 */
fun listSongs() {
    logger.debug { "listSongs() function invoked" }

    println(UITables.listSongsMenu)

    print("Enter option: ")

    when (readln().toIntOrNull()) {
        1 -> println(songAPI.listAllSongs())
        2 -> println(songAPI.listSafeSongs())
        3 -> println(songAPI.listExplicitSongs())
        4 -> listSongsByRating()
        5 -> listStaleSongs()
        6 -> listImportantSongs()
        0 -> {} // https://stackoverflow.com/questions/60755131/how-to-handle-empty-in-kotlins-when
        else -> println("Invalid choice")
    }
}

/**
 * Lists songs based on a specified number of days.
 */
fun listStaleSongs() {
    logger.debug { "listStaleSongs() function invoked" }

    val days: Int = getValidPropertyValue("staleDays")

    println(songAPI.listStaleSongs(days))
}

/**
 * Lists songs with a rating of 1.
 */
fun listImportantSongs() {
    logger.debug { "listImportantSongs() function invoked" }

    println(songAPI.listImportantSongs())
}

/**
 * Lists songs based on a specified rating.
 */
fun listSongsByRating() {
    logger.debug { "listSongsByRating() function invoked" }

    val songRating: Int = getValidPropertyValue("songRating")

    println(songAPI.listSongsBySelectedRating(songRating))
}

/**
 * Loads songs from an external file.
 */
fun load() {
    logger.debug { "load() function invoked" }

    try {
        if (songAPI.load()) {
            println("Songs loaded successfully:")

            printAllSongs()
        } else {
            println("Error loading songs, see debug log for more info")
        }
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}

/**
 * Saves songs to an external file.
 */
fun save() {
    logger.debug { "save() function invoked" }

    try {
        songAPI.store()
        println("Songs saved successfully:")

        printAllSongs()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

/**
 * Exits the application.
 */
fun exitApp() {
    logger.debug { "exitApp() function invoked" }

    logger.debug { "Exiting...bye" }
    exitProcess(0)
}

/**
 * Displays the main menu of the application and reads user input for the selected option.
 * @return The user's selected option as an Int, or null if an invalid option was entered.
 */
fun mainMenu(): Int? {
    logger.debug { "mainMenu() function invoked" }

    println(UITables.mainMenu)

    print("Enter option: ")

    return readln().toIntOrNull()
}

/**
 * Show menu and handle user choices.
 */
fun runMenu() {
    logger.debug { "runMenu() function invoked" }

    do {
        when (val option = mainMenu()) {
            1 -> addSong()
            2 -> viewSong()
            3 -> updateSong()
            4 -> deleteSong()
            5 -> explicitifySong()
            6 -> searchSongs()
            7 -> removeMultipleSongs()
            8 -> listSongs()
            9 -> load()
            10 -> save()
            -98 -> SerializerUtils.generateSeededFiles()
            -99 -> songAPI.seedSongs()
            0 -> exitApp()
            else -> println("Invalid option entered: $option")
        }
    } while (true)
}

/**
 * Start the Songs App.
 */
fun main() {
    logger.debug { "main() function invoked" }
    // https://patorjk.com/software/taag/
    println(
        """
        ███████╗ ██████╗ ███╗   ██╗ ██████╗ ███████╗     █████╗ ██████╗ ██████╗ 
        ██╔════╝██╔═══██╗████╗  ██║██╔════╝ ██╔════╝    ██╔══██╗██╔══██╗██╔══██╗
        ███████╗██║   ██║██╔██╗ ██║██║  ███╗███████╗    ███████║██████╔╝██████╔╝
        ╚════██║██║   ██║██║╚██╗██║██║   ██║╚════██║    ██╔══██║██╔═══╝ ██╔═══╝ 
        ███████║╚██████╔╝██║ ╚████║╚██████╔╝███████║    ██║  ██║██║     ██║     
        ╚══════╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝ ╚══════╝    ╚═╝  ╚═╝╚═╝     ╚═╝     
                                                                                  
    """.trimIndent()
    )

    load()

    runMenu()
}