import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import utils.ValidatorUtils
import java.time.LocalDateTime

class ValidatorUtilsTest {

    @Test
    fun testStringIsValid() {
        assertTrue(ValidatorUtils.stringIsValid("valid string"))
        assertFalse(ValidatorUtils.stringIsValid(null))
        assertFalse(ValidatorUtils.stringIsValid(""))
        assertFalse(ValidatorUtils.stringIsValid("   "))
    }

    @Test
    fun testIntIsValid() {
        assertTrue(ValidatorUtils.intIsValid("1"))
        assertFalse(ValidatorUtils.intIsValid(null))
        assertFalse(ValidatorUtils.intIsValid(""))
        assertFalse(ValidatorUtils.intIsValid("a"))
        assertFalse(ValidatorUtils.intIsValid("1.1"))
    }

    @Test
    fun testSongRatingIsValid() {
        assertTrue(ValidatorUtils.songRatingIsValid("1"))
        assertTrue(ValidatorUtils.songRatingIsValid("5"))
        assertFalse(ValidatorUtils.songRatingIsValid("0"))
        assertFalse(ValidatorUtils.songRatingIsValid("6"))
        assertFalse(ValidatorUtils.songRatingIsValid(null))
    }

    @Test
    fun testYesNoIsValid() {
        assertTrue(ValidatorUtils.yesNoIsValid("y"))
        assertTrue(ValidatorUtils.yesNoIsValid("Y"))
        assertTrue(ValidatorUtils.yesNoIsValid("n"))
        assertTrue(ValidatorUtils.yesNoIsValid("N"))
        assertFalse(ValidatorUtils.yesNoIsValid(null))
        assertFalse(ValidatorUtils.yesNoIsValid(""))
        assertFalse(ValidatorUtils.yesNoIsValid("invalid"))
    }

    @Test
    fun testLocalDateTimeIsValid() {
        assertTrue(ValidatorUtils.localDateTimeIsValid("2023-03-09T11:30:00"))
        assertFalse(ValidatorUtils.localDateTimeIsValid(null))
        assertFalse(ValidatorUtils.localDateTimeIsValid(""))
        assertFalse(ValidatorUtils.localDateTimeIsValid("2023-03-09 11:30:00"))
    }

    @Test
    fun testStaleDaysIsValid() {
        assertTrue(ValidatorUtils.staleDaysIsValid("0"))
        assertTrue(ValidatorUtils.staleDaysIsValid("1"))
        assertFalse(ValidatorUtils.staleDaysIsValid("-1"))
        assertFalse(ValidatorUtils.staleDaysIsValid(null))
    }

    @Test
    fun testPropertyNameToPrompt() {
        assertEquals("Enter song title: ", ValidatorUtils.propertyNameToPrompt("songTitle", null))
        assertEquals(
            "Enter song rating (1-low, 2, 3, 4, 5-high): ",
            ValidatorUtils.propertyNameToPrompt("songRating", null)
        )
        assertThrows(IllegalArgumentException::class.java) {
            ValidatorUtils.propertyNameToPrompt(
                "invalidPropertyName",
                null
            )
        }
    }

    @Test
    fun testPropertyNameToError() {
        assertEquals(
            "Error: song title was invalid. Please enter a string",
            ValidatorUtils.propertyNameToError("songTitle")
        )
        assertEquals(
            "Error: invalid number of days. Please enter a valid positive integer.",
            ValidatorUtils.propertyNameToError("staleDays")
        )
        assertThrows(IllegalArgumentException::class.java) { ValidatorUtils.propertyNameToError("invalidPropertyName") }
    }

    @Test
    fun testPropertyNameToValidator() {
        assertEquals(ValidatorUtils::stringIsValid, ValidatorUtils.propertyNameToValidator("songTitle"))
        assertEquals(ValidatorUtils::localDateTimeIsValid, ValidatorUtils.propertyNameToValidator("updatedAt"))
        assertThrows(IllegalArgumentException::class.java) { ValidatorUtils.propertyNameToValidator("invalidPropertyName") }
    }
}
