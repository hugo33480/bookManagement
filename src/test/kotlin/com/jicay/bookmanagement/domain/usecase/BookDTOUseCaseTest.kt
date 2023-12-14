package com.jicay.bookmanagement.domain.usecase

import assertk.assertThat
import assertk.assertions.containsExactly
import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.justRun
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class BookDTOUseCaseTest {

    @InjectMockKs
    private lateinit var bookUseCase: BookUseCase

    @MockK
    private lateinit var bookPort: BookPort

    // ... (existing tests)

    @Test
    fun `get all books should returns all books sorted by name`() {
        every { bookPort.getAllBooks() } returns listOf(
                Book("Les Misérables", "Victor Hugo"),
                Book("Hamlet", "William Shakespeare")
        )

        val res = bookUseCase.getAllBooks()

        assertThat(res).containsExactly(
                Book("Hamlet", "William Shakespeare"),
                Book("Les Misérables", "Victor Hugo")
        )
    }

    @Test
    fun `add book`() {
        justRun { bookPort.createBook(any()) }

        val book = Book("Les Misérables", "Victor Hugo")

        bookUseCase.addBook(book)

        verify(exactly = 1) { bookPort.createBook(book) }
    }

    @Test
    fun `reserve book success`() {
        // Mocking
        val bookToReserve = Book("ReserveBook", "ReserveAuthor", true)
        every { bookPort.getBookByTitle("ReserveBook") } returns bookToReserve

        // Testing
        bookUseCase.reserveBook("ReserveBook")

        // Verification
        verify(exactly = 1) { bookPort.reserveBook("ReserveBook") }
    }

    @Test
    fun `reserve book failure`() {
        // Mocking
        val bookToReserve = Book("ReserveBook", "ReserveAuthor", false)
        every { bookPort.getBookByTitle("ReserveBook") } returns bookToReserve

        // Testing and Verification
        assertThrows(IllegalArgumentException::class.java) {
            bookUseCase.reserveBook("ReserveBook")
        }
    }
}
