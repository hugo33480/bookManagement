package com.jicay.bookmanagement.infrastructure.driven.adapter

import com.jicay.bookmanagement.domain.model.Book
import com.jicay.bookmanagement.domain.port.BookPort
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import org.springframework.stereotype.Service

@Service
class BookDAO(private val namedParameterJdbcTemplate: NamedParameterJdbcTemplate): BookPort {
    override fun getAllBooks(): List<Book> {
        return namedParameterJdbcTemplate
                .query("SELECT * FROM BOOK", MapSqlParameterSource()) { rs, _ ->
                    Book(
                            name = rs.getString("title"),
                            author = rs.getString("author"),
                            available = rs.getBoolean("available")
                    )
                }
    }


    override fun createBook(book: Book) {
        namedParameterJdbcTemplate
                .update("INSERT INTO BOOK (title, author) values (:title, :author)", mapOf(
                        "title" to book.name,
                        "author" to book.author
                ))
    }

    override fun getBookByTitle(title: String): Book? {
        val query = "SELECT * FROM BOOK WHERE title = :title"
        val params = mapOf("title" to title)

        return try {
            namedParameterJdbcTemplate.queryForObject(query, params) { rs, _ ->
                Book(
                        name = rs.getString("title"),
                        author = rs.getString("author"),
                        available = rs.getBoolean("available")
                )
            }
        } catch (ex: Exception) {
            null
        }
    }

    override fun reserveBook(title: String) {
        val updateQuery = "UPDATE BOOK SET available = false WHERE title = :title"
        val params = mapOf("title" to title)

        namedParameterJdbcTemplate.update(updateQuery, params)
    }
}