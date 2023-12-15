package com.jicay.bookmanagement.infrastructure.driver.web

import com.jicay.bookmanagement.domain.usecase.BookUseCase
import com.jicay.bookmanagement.infrastructure.driver.web.dto.BookDTO
import com.jicay.bookmanagement.infrastructure.driver.web.dto.toDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(
    private val bookUseCase: BookUseCase
) {
    @CrossOrigin
    @GetMapping
    fun getAllBooks(): List<BookDTO> {
        return bookUseCase.getAllBooks()
            .map { it.toDto() }
    }

    @CrossOrigin
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addBook(@RequestBody bookDTO: BookDTO) {
        bookUseCase.addBook(bookDTO.toDomain())
    }


    @CrossOrigin
    @PatchMapping("/reserve")
    fun reserveBook(@RequestBody requestBody: Map<String, String>) {
        val bookTitle = requestBody["bookTitle"]
        if (bookTitle != null) {
            bookUseCase.reserveBook(bookTitle)
            ResponseEntity.ok("Book reserved successfully.")
        } else {
            ResponseEntity.badRequest().body("Invalid or missing 'bookTitle' in the request.")
        }
    }

//    @CrossOrigin
//    @PostMapping("/coucou")
//    @ResponseStatus(HttpStatus.OK)
//    fun test(@RequestBody json: ): String {
//        JSONObject
//        return
//    }
}