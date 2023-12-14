package com.jicay.bookmanagement.domain.usecase

import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort

class BookUseCase(
        private val bookPort: BookPort
) {
    fun getAllBooks(): List<Book> {
        return bookPort.getAllBooks().sortedBy {
            it.name.lowercase()
        }
    }

    fun addBook(book: Book) {
        bookPort.createBook(book)
    }

    // Nouvelle fonction pour réserver un livre par titre
    fun reserveBook(bookTitle: String) {
        val book = bookPort.getBookByTitle(bookTitle)

        if (book != null && book.available) {
            // Vérifiez que le livre existe et est disponible
            bookPort.reserveBook(bookTitle) // Utilisez le titre comme identifiant
        } else {
            // Gérer la logique en cas de livre inexistant ou non disponible
            throw IllegalArgumentException("Le livre n'existe pas ou n'est pas disponible.")
        }
    }
}
