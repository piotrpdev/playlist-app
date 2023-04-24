package models

import java.time.LocalDateTime

/**
 * A data class representing a song with a title, priority, category, and archive status.
 *
 * @property songTitle The title of the song.
 * @property songPriority The priority of the song, where 1 is the lowest and 5 is the highest priority (inclusive).
 * @property songCategory The category of the song.
 * @property isSongArchived A flag indicating whether the song is archived or not.
 * @property updatedAt The date and time the song was last updated, defaulting to the current date and time.
 * @property createdAt The date and time the song was created, defaulting to the current date and time.
 */
data class Song(
    var songTitle: String,
    var songPriority: Int,
    var songCategory: String,
    var isSongArchived: Boolean,
    var updatedAt: LocalDateTime = LocalDateTime.now(),
    var createdAt: LocalDateTime = LocalDateTime.now()
)