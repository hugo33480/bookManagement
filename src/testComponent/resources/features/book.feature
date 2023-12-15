Feature: the user can create and retrieve the books and can reserve books
  Scenario: user creates two books and retrieve both of them
    When the user creates the book "Les Misérables" written by "Victor Hugo" and available "true"
    And the user creates the book "L'avare" written by "Molière" and available "true"
    And the user get all books
    Then the list should contains the following books in the same order
      | name | author | available |
      | L'avare | Molière | true |
      | Les Misérables | Victor Hugo | true |

  Scenario: user creates two books and reserve one of them
    When the user creates the book "Les Misérables" written by "Victor Hugo" and available "true"
    And the user creates the book "L'avare" written by "Molière" and available "true"
    And the user reserves the book "L'avare"
    Then the book "L'avare" should be reserved
      | name | author | available |
      | L'avare | Molière | false |
      | Les Misérables | Victor Hugo | true |
