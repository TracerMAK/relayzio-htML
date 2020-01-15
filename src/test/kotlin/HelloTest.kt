
import kotlin.test.assertEquals
import org.junit.Test

class HelloTest {
    @Test fun testAssert() : Unit {
        val str: String = "Hello, world!"
        assertEquals("Hello, world!", str)
    }
}