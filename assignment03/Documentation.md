# Documentation: Effective Software Testing Lab (Assignment 3)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with
  the
  tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where
  you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, create an
  Assets folder for each exercise which contains screenshots of your test results.
- Deadline: May 20, 2024, at 18:00 (Zurich, CH, time).

## book_reviews

### A. Get high-rated books

#### 1. What are the external dependencies? Which of these dependencies should be tested using doubles and which should not?

In this scenario, the ``BookManager`` class depends on the ``BookRatingsFetcher`` class, which in turn relies on an
external
dependency: the SQL database containing the book metadata. Being optimistic about external resources is a common
test smell. A database might not always be readily available, causing tests to fail
due to the external resource rather than the method under test. I therefore decided to use a stub to mock the
database. I stubbed the ``BookRatingsFetcher`` class and configured it to return a
list of books when ``all()`` is called. Using a stub also makes the test more cohesive, easier to understand and not
flaky.

#### 2. For the dependencies that should be tested using doubles, what refactoring should be done in the code? Do the refactoring and implement the tests.

Initially, to facilitate testing, I needed to modify the production code. ``BookRatingsFetcher`` is now passed into the
constructor of ``BookManager``. The method `highRatedBooks` no longer instantiates the database connection itself
but rather accesses the instance attribute ``bookRatingsFetcher``, responsible for establishing a database
connection. Consequently, the database is no longer handled within the test; instead, I configured a
``BookRatingsFetcher`` stub using ``Mockito``. This grants me full control over the stub. Whenever its all() method is
invoked, I simply return a list of books set up specifically for the test. I can then verify whether the method
returns the correct books. ``returnHighRatedBooksOnly`` tests the method and ensures that only books with a rating
higher or
equal to 4 are returned by the method. I also tested with an empty list ``emptyBookRatings``. Additionally, adhering to
best practices for comprehensive testing, I included the `ratingsOutOfRange` test to evaluate the behavior when rating
values fall outside the given range of [1,5]. Typically, I would rely on another component of the code to enforce
this range. However, with this test, I can now confirm that values beyond this range do not disrupt the 
functionality of the test or the code.

#### 3. What are the disadvantages of using test doubles in your test suite? Answer with examples from the BookManager class.

The disadvantage of using test doubles in my test suite is that the test becomes less realistic as it only mimicks
behaviour. Furthermore, the test code becomes more coupled with the implementation details of the production code.
This makes the test harder to maintain (e.g., if a new attribute for `Book` is added I have to add a new attribute
to all my `Book` contstructors)

### B. Get list of all authors

Next, I added a new method `uniqueAuthors`, which returns all authors of the books in the database. Additionally, I
added some new tests to my test suite to test my new method: `returnUniqueAuthors` to verify that the method returns
all names of all authors, and `returnDuplicateAuthors` to ensure that the method only returns duplicate author names
once, returning a list with unique names. Furthermore, I also tested for an empty book list with the
test `emptyAuthors`.

To minimize code duplication and enhance test readability, I decided to extract a
method `getBookManagerWithStubbedFetcher` that instantiates a stub for the `BookRatingsFetcher` and returns
the `BookManager` with the stub as a dependency.

## bus_tracking

A common issue in test suites is relying too heavily on external resources. In the case of `BusTracker`this would be
the ``GPSDeviceService``, the ``MapService`` and the ``NotificationService``. To address this concern, I've decided to
mock these dependencies using ``Mockito``. At the beginning of my test suite, I instantiate these dependencies as
mocks in a `@BeforeEach` method, which ensures that each test starts with fresh instances of these dependencies,
preventing interference between tests. Then, I set up a ``BusTracker`` using these mock service dependencies, which 
allows me to effectively test the ``updateBusLocation`` method without relying on the actual behaviour of the 
external services. Furthermore, this approach isolates the unit under test. 

### A. Accuracy of Location Updates

My test suite contains several tests to ensure that the `updateBusLocation` function accurately updates the bus's
location on the map when new data is received.

- `isKeyWayPoint`: This test confirms that when the location is a key waypoint, passengers are informed that the bus has
  arrived.
- `isNotKeyWayPoint`: This test verifies that when the location is not a key waypoint, the map is updated without
  notifying passengers.
- `correctLocation`: This test ensures that the correct location was used to update the map

These test cases verify that the `updateBusLocation` method correctly calls the corresponding services. For this
validation, I used Mockito's `verify` feature. To ensure that there are no interactions with the `notificationService`
in the `isNotKeyWayPoint` test, I utilized Mockito's `verifyNoInteractions` method.

### B. Notification of Key Events

To capture and assert specific objects passed to mocks, I utilized ``Mockito``'s argument captor feature in all of my
tests.
Specifically, I captured the string value passed to the `notifyPassengers` method of `NotificationService`, then
verified if the captured string meets the expected criteria. Furthermore, I also used it in `correctLocation`
to capture whether the right location was passed on.

### C. Response to GPS Signal Loss

Furthermore, I implemented the test `GPSSignalLost` to test that the passenger gets informed correctly if no signal is
available at the moment. For this I used Mockito's ``verifyNoInteractions`` method to ensure that nothing gets
updated on the mapService and again used the `ArgumentCaptor`.

### D. Comparison (direct method calls versus event-driven updates)

Direct method calls are simpler and make the code easier to understand than event-driven updates. However, they can
lead to tight coupling between the components, which also makes the method more difficult to extend. Event-driven
updates are more decoupled and scalable. However, they are also more complex. In the case of ``BusTracker`` it
would make sense to transition to event-driven updates if the system becomes more complex and requires the addition
of multiple event listeners. In respect to testing, implementing event-driven updates could complicate the testing
setups, especially with setting up a comprehensive test suite.

## cat_facts

## e_shop

## messages

Currently, the public class `MessageProcessor` instantiates an instance of the class `MessageService` which is further used to send all messages.
This architecture makes it difficult to work with test doubles and, therefore, the logic is split up. A constructor is added to the class `MessageProcessor`
which accepts an `MessageService` instance as a parameter to enable dependency injection. The passed instance is then assigned to a private final field that
is used throughout the class. Therefore, the method `processMessages` creates no new instance of the `MessageService` but always refers to the private variable.\
In the next step the mocking framework is implemented. Namely, the `MessageService` is mocked using `Mockito`. For this two private fields are instantiated, one `Service`
and one `Processor`. To guarantee independence between tests the annotation `@BeforeEach` is used to freshly initialize both fields before running each test.\
When testing lists, it's common practice to test for null / undefined as well as empty lists. In addition, lists can have only one element, multiple elements and duplicates.
Pursuing this idea and applying it to the method `processMessages`, the following test cases can be derived as a first step:
1. Null list
2. Empty list
3. List with one message only
4. List with more than one message
5. List with two identical messages

### A. Number of Invocations

To count the **number of invocations** the `Mockito` methods `never()` and `times(<int>)` in combination with `verify` are used whenever the `sendMessages` method is called.

### B. Content of invocations—`ArgumentCaptor`

To assert the **content of invocations** two new private fields are added, namely the `receiverCaptor` and the `contentCaptor` which are used to capture both parameters. Both the
receiver and the content can then be compared to verify the expected string. To reduce duplicate code, these two checks are combined in a method (one for `1` and another for `2+` messages).

### C. Content of invocations—Increasing observability

Finally, to **increase observability** of the `MessageProcessor` class, a special case of the observer pattern is implemented (single listener). For this an interface `MessageListener`
is added which includes a method for logging sent messages. Furthermore, an additional field and instance of the provided interface is added to the constructor in `MessageProcessor`.
Then, whenever `processMessages` is called, the listener is notified and logs all sent messages. To the test class a `StubMessageListener` subclass is added which implements the new interface with its method
and logs all messages in a list. This helps to verify the messages' receivers and contents when testing and removes the dependency on external tools.

### D. Comparison

Using tools like `ArgumentCaptor` has the following consequences:
- It's possible to directly capture the exact parameters
- Fast implementation
- Relies on mocking frameworks
- Only useful for testing

Increasing the observability has the following consequences:
- Independence from external tools
- Provides a framework which can be reused (such as the observer pattern)
- Requires a lot of additional code


## movie_streaming

In a first step the methods `updateMovieMetadata(String movieId, MovieMetadata metadata)` and `validateStreamingToken(String movieId, String token)` are implemented.\
The `movieId` is added as a parameter to `validateStreamingToken` since the token is generated with the help of a `movieId` and therefore required for validation too.
In addition, the method is made `private` since it will be only used by the `MovieStreamingManager` to validate the token when streaming a movie.
Next, the code is adjusted to make use of the newly added methods:
- The interface `FileStreamService` now includes `Boolean validateToken(String movieId, String token)` which return `true` if the token is validated successfully, `false` otherwise.
- The interface `CacheService` now includes `void refreshCache(String movieId, StreamingDetails details)` to enable refreshing the cache if the token could not be validated and a new one is generated.
- A method is added to the `MovieStreamingManager` to validate the provided input `movieId`. It throws an exception if the ID is invalid or the movie is not found.

For testing, to successfully mock the `FileStreamSercie` and the `CacheService` the framework provided by `Mockito` is used.  Two private fields are instantiated, one `FileStream` and one `Cache`.
To guarantee independence between tests the annotation `@BeforeEach` is used to freshly initialize both fields before running each test. Furthermore, two variables for `MovieMetadata` and `StreamingDetails`
are initialized to be used during the tests.\
When testing strings, it's common practice to also test for null / undefined as well as empty strings.
Calling the method `streamMovie`, the following test cases can be derived:
1. Null string `movieId`
2. Empty string `movieId`
3. Test string of non-existent movie
4. Test string with empty cache
5. Test string with cached details and invalid token
6. Test string with cached details and valid token

Calling the method `updateMovieMetadata`, the following test cases can be derived:
1. Null string `movieId`
2. Empty string `movieId`
3. Test string of non-existent movie
4. Test string and `null` metadata
5. Test string and invalid metadata: `null` in `title` or / and `description`
6. Test string and invalid metadata: empty `title` or / and `description`
7. Test string and valid metadata

To successfully compare the content of the created instances during testing (and not the objects themselves) `AssertJ` is used with its command `assertThat(<expected>).usingRecursiveComparison().isEqualTo(<actual>)`.

## payment_processing
### Setup
A `Transaction.java` class was created, with a `getId` method to access its ID.
It was assumed that transactions with `id > 0` are valid, and those with `id <= 0` are invalid, as manifested in the fraud detection service mocking.

## Test Scenarios

### A. Number of Invocations
Mockito was used to verify the number of times `onTransactionComplete` is called for valid, invalid, and mixed transactions.
Boundary cases of 0 and 1 were included and worked as expected.

### B. Content of Invocations—ArgumentCaptor
The `ArgumentCaptor` was used to capture and verify the transactions passed to `onTransactionComplete`.
By processing multiple transactions, the test ensured both the number of calls and the actual transaction IDs were correct.

### C. Content of Invocations—Increasing Observability
To increase observability, the `processPayment` method was modified to return the processed transaction if valid, or null if invalid.
This was tested by verifying the returned values matched the expected transactions or null for invalid ones.

### D. Comparison
#### ArgumentCaptor (B):
- **Advantages**: Captures exact arguments for detailed verification. Useful when methods do not return values.
- **Disadvantages**: Adds complexity to the test setup.

#### Increasing Observability (C):
- **Advantages**: Simplifies tests by directly verifying return values. Makes it easier to test method outputs.
- **Disadvantages**: May require modifying methods to return values, which could complicate the design.

Both techniques have their place, with `ArgumentCaptor` providing detailed insight into method interactions and increased observability offering a straightforward approach to output verification.


## ticket_system
