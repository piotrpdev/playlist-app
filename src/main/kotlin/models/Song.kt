package models

import java.time.LocalDateTime

/**
 * A data class representing a song with a title, rating, genre, and explicitify status.
 *
 * @property songTitle The title of the song.
 * @property songRating The rating of the song, where 1 is the lowest and 5 is the highest rating (inclusive).
 * @property songGenre The genre of the song.
 * @property isSongExplicit A flag indicating whether the song is explicit or not.
 * @property updatedAt The date and time the song was last updated, defaulting to the current date and time.
 * @property createdAt The date and time the song was created, defaulting to the current date and time.
 */
data class Song(
    var songTitle: String,
    var songRating: Int,
    var songGenre: String,
    var isSongExplicit: Boolean,
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var createdAt: LocalDateTime = LocalDateTime.now()
)
