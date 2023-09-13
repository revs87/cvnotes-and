package pt.rvcoding.cvnotes

import assertk.assertThat
import assertk.assertions.isGreaterThan
import assertk.assertions.isLessThan
import assertk.assertions.isTrue
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class Coroutines {

    @BeforeEach
    fun setUp() {}

    @AfterEach
    fun tearDown() {}


    @Test
    fun `Concurrent coroutine return the sum of each execution time`() {
        runBlocking {
            val start = System.currentTimeMillis()
            delay(1000)
            delay(500)
            val end = System.currentTimeMillis()
            assertThat(end - start).isGreaterThan(1500)
        }
    }

    @Test
    fun `Concurrent coroutine return the sum of each execution time - launch - when result doesn't matter`() {
        runBlocking {
            val c1 = launch { delay(1000) }
            val c2 = launch { delay(500) }
            val start = System.currentTimeMillis()
            c1.join()
            c2.join()
            val end = System.currentTimeMillis()
            assertThat(end - start).isLessThan(1500)
        }
    }
    
    @Test
    fun `Asynchronous coroutines return an execution time lesser than the sum of each execution time - async - result matters!`() {
        runBlocking {
            val c1 = async { delay(1000); true }
            val c2 = async { delay(500); true }
            val start = System.currentTimeMillis()
            val res1 = c1.await()
            val res2 = c2.await()
            val end = System.currentTimeMillis()
            assertThat(res1 && res2).isTrue()
            assertThat(end - start).isLessThan(1500)

            listOf(c1, c2).awaitAll().map { assertThat(it).isTrue() }
        }
    }
}