# Documentation: Effective Software Testing Lab (Assignment 3)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the
tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where
you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, create an
Assets folder for each exercise which contains screenshots of your test results.
- Deadline: May 20, 2024, at 18:00 (Zurich, CH, time).

## book_reviews

## bus_tracking

## cat_facts

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
Then, whenever `processMessages` is called, the listener is notified and logs all sent messages. To the test class a `TestMessageListener` subclass is added which implements the new interface with its method
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

## payment_processing

## ticket_system