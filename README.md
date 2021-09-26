# API Test using Cucumber-RestAssured on Java



Java의 `RestAssured` 를 사용하여 API Test를 구성해보았습니다.


테스트 대상인 API Server는 아래 repo를 확인해주세요.

- https://github.com/Yoodahun/Practice-Flask-RESTful

테스트 관점으로는 DB에서 발생하는 에러 등, **가능한 한 확인할 수 있는 모든 status code를 확인한다** 라는 관점으로 test scenario를 작성하였습니다.

---

Github action을 이용하여 Server의 repository에 Push가 발생할 경우, Triggering을 통한 자동 테스트를 구축해놓았습니다.

자세한 후기는 아래를 확인해주세요!

**Github Action을 이용한 CI Flow구축 후기**

- [https://velog.io/@dahunyoo/Github-Action의-Triggering을-이용한-간단한-CI-구성해보기](https://velog.io/@dahunyoo/Github-Action의-Triggering을-이용한-간단한-CI-구성해보기)



----

## pom.xml - dependencies
```xml
  <dependencies>
      <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-java -->
      <dependency>
          <groupId>io.cucumber</groupId>
          <artifactId>cucumber-java</artifactId>
          <version>6.1.2</version>
      </dependency>

      <!-- https://mvnrepository.com/artifact/io.cucumber/cucumber-testng -->
      <dependency>
          <groupId>io.cucumber</groupId>
          <artifactId>cucumber-testng</artifactId>
          <version>6.1.2</version>
      </dependency>

      <!-- https://mvnrepository.com/artifact/io.rest-assured/rest-assured -->
      <dependency>
          <groupId>io.rest-assured</groupId>
          <artifactId>rest-assured</artifactId>
          <version>4.3.0</version>
      </dependency>

      <!-- https://mvnrepository.com/artifact/com.fasterxml.jackson.core/jackson-databind -->
      <dependency>
          <groupId>com.fasterxml.jackson.core</groupId>
          <artifactId>jackson-databind</artifactId>
          <version>2.11.0</version>
      </dependency>

      <!-- https://mvnrepository.com/artifact/org.testng/testng -->
      <dependency>
          <groupId>org.testng</groupId>
          <artifactId>testng</artifactId>
          <version>7.3.0</version>
          <scope>test</scope>
      </dependency>


  </dependencies>

```

## Scenario_A01_User_Register
```gherkin
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
```


## Scenario_A02_User
```gherkin
Feature: User feature
  Check user feature which login, get, delete

  @User
  Scenario: Login which is not invalid or not registered user info
    Given User "Login" API
    When I try login user with "not_registered_user" and "password"
    Then response status code is 401
    And message is "Invalid credentials"

  @User
  Scenario: GET - User not found
    Given User "GET" API
    When I try get user information with user_id
    Then response status code is 404
    And message is "User not found"

  @User
  Scenario: DELETE - User not found
    Given User "DELETE" API
    When I try delete user information with user_id
    Then response status code is 404
    And message is "User not found"

```


## Scenario_B01_Authorization
```gherkin
Feature: Authorization feature
  check response about authorization

  @login
  Scenario: Request does not contain an access token
    When logout without access token
    Then response status code is 401
    And description is "Request does not contain an access token."

  @login
  Scenario: Signature verification failed
    When logout with wrong access token
    Then response status code is 401
    And description is "Signature verification failed."

  @login @BeforeLogin
  Scenario: Token has been revoked
    When logout user information
    And logout user information
    Then response status code is 401
    And description is "The token has been revoked"

```

## Scenario_C01_Store
```gherkin
Feature: Store feature
  GET stores, GET store / CREATE store / DELETE store

  @Store
  Scenario: Get stores
    Given Store "GET" API
    When I try get stores information
    Then response status code is 200
    And "name" value in stores response body is equal to "store1"

  @Store
  Scenario: Get store
    Given Store "GET" API
    When I try get store information with "store1"
    Then response status code is 200
    And "name" value in response body is equal to "store1"

  @Store
  Scenario: Store not found
    Given Store "GET" API
    When I try get store information with "not_registered_store"
    Then response status code is 404
    And message is "Store not found"

  @Store
  Scenario: Create store
    Given Store "create" API
    When I try create store information with "registered_store"
    Then response status code is 201
    And response body has key that "name" and "items"
    And "name" value in response body is equal to "registered_store"
    And create store object in context object

  @Store
  Scenario: Store with store_name already exists
    Given Store "create" API
    When I try create store information with "registered_store"
    Then response status code is 400
    And message is store with "registered_store" already exists

  @Store
  Scenario: Delete store
    Given Store "DELETE" API
    When I try delete store information with "registered_store"
    Then response status code is 200
    And store has successfully deleted

  @Store
  Scenario: Store with store_name is not exists
    Given Store "DELETE" API
    When I try delete store information with "registered_store"
    Then response status code is 400
    And message is store with "registered_store" is not exists


```


## Scenario_D01_Item
```gherkin
Feature: Item feature
  GET items, GET item / Create item / Update item / DELETE item

  @Item @item_feature_start
  Scenario: GET items without login
    Given Item "GET" API
    When I try get items information
    Then response status code is 200
    And item_name value in items response body is equal to "chair4"
    And message is "More data available if you log-in"

  @Item @BeforeLogin
  Scenario: GET items with login
    Given Item "GET" API
    When I try get items information
    Then response status code is 200
    And item_name value in items response body is equal to "chair4"


  @Item
  Scenario: Get item
    Given Item "GET" API
    And user has been logged in
    When I try get item information with "chair4"
    Then response status code is 200
    And "name" value in response body is equal to "chair4"
    And "store_id" value in response body is equal to store_id

  @Item
  Scenario: Create item
    Given Item "create" API
    And user has been logged in
    When I try create item information with "create_test_item1" and "18.01", store_id in request body
    Then response status code is 201
    And response body has key that "name" and "price"
    And "name" value in response body is equal to "create_test_item1"
    And "store_id" value in response body is equal to "52"
    And create item object in context object

  @Item
  Scenario: Check item is already exists
    Given Item "create" API
    And user has been logged in
    When I try create item information with "create_test_item1" and "18.01", store_id in request body
    Then response status code is 400
    And message is An item with "create_test_item1" already exists

  @Item
  Scenario: Check store is not registered in DB
    Given Item "create" API
    And user has been logged in
    When I try create item information with "no_store_item" and "18.01", store_id "402" in request body
    Then response status code is 400
    And message is "There is no Store. You should be add store first."

  @Item
  Scenario: Create item without price
    Given Item "create" API
    And user has been logged in
    When I try create item information with "create_test_item_without_price1" and "null", store_id in request body
    Then response status code is 400
    And "price" is cannot be let blank

  @Item
  Scenario: Create item without store_id
    Given Item "create" API
    And user has been logged in
    When I try create item information with "no_store_item" and "18.01", store_id "null" in request body
    Then response status code is 400
    And store_id is Every item needs a store id

  @Item @create_item_model
  Scenario: Update item
    Given Item "PUT" API
    And user has been logged in
    When I try put item information with item_name and "20.25", store_id in request body
    Then response status code is 200
    And "name" value in response body is equal to "create_test_item1"
    And "price" value in response body is equal to "20.25"

  @Item
  Scenario: Update item without price
    Given Item "PUT" API
    And user has been logged in
    When I try put item information with item_name and "null", store_id in request body
    Then response status code is 400
    And "price" is cannot be let blank

  @Item @update_item_without_store_id_in_DB
  Scenario: Update item with store_id is not registered in DB
    Given Item "PUT" API
    And user has been logged in
    When I try put item information with item_name and "20.25", store_id in request body
    Then response status code is 400
    And message is "There is no Store. You should be add store first."

  @Item @update_item_without_store_id
  Scenario: Update item without store_id
    Given Item "PUT" API
    And user has been logged in
    When I try put item information with item_name and "20.25", store_id in request body
    Then response status code is 400
    And store_id is Every item needs a store id


  @Item @logout
  Scenario: Delete item
    Given Item "DELETE" API
    When I try delete item information with "create_test_item1"
    Then response status code is 200
    And item has successfully deleted
```


## Scenario_Z01_HappyScenario

모든 end-point의 Happy case만을 모아 작성하였으며, Regression Testcase이기도 합니다.

```gherkin
Feature: Execute Test that Happy Scenario from user create, store and item create, update delete, and user delete
  See https://github.com/Yoodahun/Practice-Flask-RESTful

  @RegressionTest @User
  Scenario: Register user
    Given User "Register" API
    When I try create user with "happy_test1" and "asdf" in request body
    Then response status code is 201
    And user has successfully "created"

  @RegressionTest @User
  Scenario: Get user
    Given User "GET" API
    When I try get user information with user_id
    Then response status code is 200
    And "username" value in response body is equal to "happy_test1"

  @RegressionTest @User
  Scenario: Login user
    Given User "Login" API
    When I try login user with "happy_test1" and "asdf"
    Then response status code is 200
    And response body has key that "access_token" and "refresh_token"
    And save access_token and refresh_token in response object

  @RegressionTest @Store
  Scenario: Create store
    Given Store "create" API
    When I try create store information with "test_store2"
    Then response status code is 201
    And response body has key that "name" and "items"
    And "name" value in response body is equal to "test_store2"
    And create store object in response object

  @RegressionTest @Store
  Scenario: Get store
    Given Store "GET" API
    When I try get store information with "test_store2"
    Then response status code is 200
    And "name" value in response body is equal to "test_store2"

  @RegressionTest @Item
  Scenario: Create item
    Given Item "create" API
    And user has been logged in
    When I try create item information with "test_item1" and "18.01", store_id in request body
    Then response status code is 201
    And response body has key that "name" and "price"
    And "name" value in response body is equal to "test_item1"
    And "store_id" value in response body is equal to store_id
    And create item object in context object

  @RegressionTest @Item
  Scenario: Get item
    Given Item "GET" API
    And user has been logged in
    When I try get item information with "test_item1"
    Then response status code is 200
    And "name" value in response body is equal to "test_item1"
    And "store_id" value in response body is equal to store_id

  @RegressionTest @Item
  Scenario: Update item info
    Given Item "PUT" API
    And user has been logged in
    When I try put item information with item_name and "20.25", store_id in request body
    Then response status code is 200
    And "name" value in response body is equal to "test_item1"
    And "price" value in response body is equal to "20.25"
##
  @RegressionTest @Item
  Scenario: Delete item
    Given Item "DELETE" API
    When I try delete item information with "test_item1"
    Then response status code is 200
    And item has successfully deleted
#
  @RegressionTest @Store
  Scenario: Delete store
    Given Store "DELETE" API
    And user has been logged in
    When I try delete store information with "test_store2"
    Then response status code is 200
    And store has successfully deleted

  @RegressionTest @User
  Scenario: Delete user
    Given User "DELETE" API
    When I try delete user information with user_id
    Then response status code is 200
    And user has successfully "deleted"

  @RegressionTest @User
  Scenario: Check user has deleted
    Given User "GET" API
    When I try get user information with user_id
    Then response status code is 404
    And user is not found
```