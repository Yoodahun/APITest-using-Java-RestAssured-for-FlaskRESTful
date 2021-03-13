# Created by yoodahun at 2021/02/21
Feature: User Register feature
  test user register feature

  @User
  Scenario: Check user information already exists.
    Given User "Register" API
    When I try create user with "jose2" and "asdf" in request body
    Then response status code is 400
    And message is "A User with that username already exists"

  @User
  Scenario: Check username attribute in Request is cannot be let blank
    Given User "Register" API
    When I try create user with "register_test2" and "null" in request body
    Then response status code is 400
    And "password" is cannot be let blank

  @User
  Scenario: Check password attribute in Request is cannot be let blank
    Given User "Register" API
    When I try create user with "null" and "asdf" in request body
    Then response status code is 400
    And "username" is cannot be let blank

