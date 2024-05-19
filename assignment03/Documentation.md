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

### External dependencies

In this exercise the `CatFactsRetriever` class depends on the `HTTPUtil` class, which relies on the `https://catfact.ninja/fact` API.
Since every call to a method of the `CatFactsRetriever` makes a call to this API via the `HTTPUtil` class, testing the `CatFactsRetriever`
could be flaky, as the API might not always answer. This is a common problem as soon as API calls are involved in the production code.
The `HTTPUtil` class therefore needs to be stubbed, to avoid making "real" calls to the API and depending on its availability. For the tests
we therefore need to configure a `mockResponse` which already consists of one or more returns from the API.
   
### Refactoring

When first stubbing the `HTTPUtil` class we get an MissingMethodInvocationException error. This stems from the static `get` method of the class. When removing
the static keyword, the `CatFactsRetriever` class throws a "Non-static method 'get(java.lang.String)' cannot be referenced from a static context" error. We therefore
introduce a constructor in the `CatFactsRetriever` class with the `HTTPUtil` class in it. The methods of the `CatFactsRetriever` class no longer call 
the `HTTPUtil` class itself but rather accesses the instance attribute `httpUtil`, responsible for calling the API. Now we are able to stub the `HTTPUtil` class
in the tests.

For the `retrieveRandom` method we only need one test and make sure the stub is called. For the `retrieveLongest` we also need to test the basic behavior first.
Since we have the attribute `limit` in this case we also test for null and edge cases (zero or negative).

The following tests are implemented:
1. basic behavior of `retrieveRandom`
2. basic behavior of `retrieveLongest`
3. `retrieveLongest` with limit = null
4. `retrieveLongest` with limit = 0 (0 is always an edge case as soon as integer are involved)
5. `retrieveLongest` with negative limit

With test 5 we noticed the strange behavior that the API still returns facts even when the limit is set to a negative number.
It seems like it returns the longest fact overall in this case. We still left this test in the test suite and used the originally
returned facts in our stub, since it qualifies as special behavior and should be covered by the test suite.
   
### Disadvantages of using test doubles

A general disadvantage when testing with test doubles is that if the API changes, we would not pick this up in our test suite,
as the tests would still pass. We would have to notice this somehow and then adapt our test suite to the new real returns
by the API. In terms of the `CatFactsRetriever` class another disadvantage is, that we have to instantiate the stubbed return
for every test, meaning we have a lot of Strings for our mocked responses. Especially the mocked response for the last
test is very long and makes the test suite look bloated. Configuring them also takes some time.

## e_shop

### A. Number of Invocations
To count the **number of invocations** the `Mockito` methods `never()` and `times(<int>)` in combination with `verify` are 
used whenever the `publishOrderToAllListeners` method is called. A first test verifies that if an null object is
passed as an order, the `onOrderPlaced` method of the `EmailNotificationService` and the `InventoryManager` are not
called. A second test proves that the methods are exactly called once whenever an order is placed through the `EventPublisher`.

Here, we do not test for empty Orders, as they cannot be instantiated to begin with. The order is either null or has content,
both cases being covered by the two tests.

The tests for this task are the following:
1. Null order
2. Order is placed and correct number of calls is made

### B. Content of invocations—`ArgumentCaptor`
To test the content of the orders we assert that the order which is published via the `EventPublisher` is the same one
as the order that is "ordered" by the `EmailNotificationService` and the `InventoryManager`. We use an ArgumentCaptor to capture
the order and then compare it to the original order.

The tests for this task are the following:
3. Single order is placed
4. Multiple orders are placed

Thinking about tests 2 and 3 we realized that it would be possible to combine them both into one test by copying the 
verify-statements from test 2 into the "middle" of test 3. For more clarity and also to follow the order of these tasks
we left them both as they stand, even though test 3 is only an extension of test 2.

### C. Content of invocations—Increasing observability

To increase the observability we introduced a new listener was implemented, similar to the one in the exercise about messages.
An interface was added which is responsible for logging orders and their content. The `EventPublisher` was adapted to account
for this addition. The orders are now logged in a list which can be accessed in the tests to verify their correctness.
In terms of the tests we didn't add anything new, but expanded the tests 3 and 4 with the flow `Observability`.

### D. Comparison

The advantage of using the ArgumentCaptor is that no additional code has to be written in the actual implementation of the 
classes. All the necessary code is placed in the test itself. Since we rely on mocking in this case, we are able to make use 
of all the advantages of mocking itself. We are able to isolate the unit under test from external dependencies and improve
readability for example.

When increasing the observability we were able to see that our tests are smaller, but the additional code was placed in
the classes instead. The additional code can be reused everywhere of course, which also has its benefits (Code is not
only for testing). Another advantage over using the ArgumentCaptor is that we don't have to learn about an additional
framework, potentially decreasing the amount of time we have to invest. Of course this argument doesn't hold once you 
are familiar with Mockito.

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
### Use of Doubles for NotificationService and LogService
#### Identify Dependencies
In the `createTicket` method, the following dependencies are external and should be mocked in the tests:

1. **NotificationService**: This service is responsible for notifying the customer, likely through email. Email notifications can be slow, unreliable, and dependent on external servers, making them unsuitable for unit testing.
2. **LogService**: This service logs ticket creation events. Logging operations can involve file I/O or network operations, which can also be slow and unreliable in a test environment.

Additionally, the `TicketRepository` should be mocked as it represents an interface to a database or persistent storage, which is another external dependency.

#### Test Doubles Usage
We use Mockito to mock these dependencies and verify that the services are called correctly during the execution of the `createTicket` method.
The tests ensure that the method interacts with the external services as expected without actually invoking them.

### Disadvantages of Using Doubles in Your Tests
1. **Lack of Realism**: Mocks and stubs do not execute actual code of the dependencies, which might miss certain integration issues.
2. **Maintenance Overhead**: Changes in the interfaces or behaviors of the actual dependencies require corresponding updates in the mocks.
3. **False Confidence**: Tests may pass with mocks but fail in a real environment due to unforeseen issues with the actual dependencies.

### Handling of Failures in Notification and Logging
We simulate failures in both the `NotificationService` and `LogService` to ensure the `createTicket` method handles these failures gracefully and continues its operation where appropriate.
