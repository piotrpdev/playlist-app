package models

import java.time.LocalDateTime

/**
 * Represents a music artist or band.
 *
 * @property name The name of the artist or band.
 * @property foundedDate The date when the artist or band was founded or formed.
 * @property genres A list of genres that the artist or band is associated with.
 */
data class Artist(
    var name: String,
    var foundedDate: LocalDateTime,
    var genres: List<String>
)