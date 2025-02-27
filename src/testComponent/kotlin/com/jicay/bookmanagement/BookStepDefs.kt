package com.jicay.bookmanagement

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import io.cucumber.datatable.DataTable
import io.cucumber.java.Before
import io.cucumber.java.Scenario
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import io.restassured.path.json.JsonPath
import io.restassured.response.ValidatableResponse
import org.springframework.boot.test.web.server.LocalServerPort

class BookStepDefs {
    @LocalServerPort
    private var port: Int? = 0

    @Before
    fun setup(scenario: Scenario) {
        RestAssured.baseURI = "http://localhost:$port"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @When("the user creates the book {string} written by {string} and available {string}")
    fun createBook(title: String, author: String, available: String) {
        given()
            .contentType(ContentType.JSON)
            .and()
            .body(
                """
                    {
                      "name": "$title",
                      "author": "$author",
                      "available": ${available.toBoolean()}
                    }
                """.trimIndent()
            )
            .`when`()
            .post("/books")
            .then()
            .statusCode(201)
    }


    @When("the user reserves the book {string}")
    fun reserveBook(title: String) {
        given()
                .contentType(ContentType.JSON)
                .and()
                .body(
                        """
                    {
                      "name": "$title"
                    }
                """.trimIndent()
                )
                .`when`()
                .patch("/books/reserve")
                .then()
                .statusCode(200)
    }

    @When("the user get all books")
    fun getAllBooks() {
        lastBookResult = given()
            .`when`()
            .get("/books")
            .then()
            .statusCode(200)
    }

    @Then("the list should contains the following books in the same order")
    fun shouldHaveListOfBooks(payload: List<Map<String, Any>>) {
        val expectedResponse = payload.joinToString(separator = ",", prefix = "[", postfix = "]") { line ->
            """
            ${
                line.entries.joinToString(separator = ",", prefix = "{", postfix = "}") {
                    """"${it.key}": ${if (it.key == "available") it.value else "\"${it.value}\""}"""
                }
            }
        """.trimIndent()

        }
        assertThat(lastBookResult.extract().body().jsonPath().prettify())
                .isEqualTo(JsonPath(expectedResponse).prettify())
    }

    @Then("the book {string} should be reserved")
    fun shouldHaveReservedBook(title: String, dataTable: DataTable) {
        // Convertir la DataTable en une liste de maps
        val books = dataTable.asMaps<String, String>(String::class.java, String::class.java)

        // Trouver le livre spécifié par le titre
        val reservedBook = books.find { it["name"] == title }

        // Vérifier si le livre a été trouvé et s'il est réservé
        assertThat(reservedBook).isNotNull()
        assertThat(reservedBook?.get("available")).isEqualTo("false")
    }

    companion object {
        lateinit var lastBookResult: ValidatableResponse
    }
}