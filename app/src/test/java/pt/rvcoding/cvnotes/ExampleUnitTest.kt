package pt.rvcoding.cvnotes

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @BeforeEach
    fun setUp() {}

    @AfterEach
    fun tearDown() {}

    @Test
    fun addition_isCorrect() {
        assertThat(2 + 2).isEqualTo(4)
    }
}