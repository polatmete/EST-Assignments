# Documentation: Effective Software Testing Lab (Assignment 3)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with
  the
  tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where
  you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, create an
  Assets folder for each exercise which contains screenshots of your test results.
- Deadline: May 20, 2024, at 18:00 (Zurich, CH, time).

## book_reviews

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

Currently, the public class `MessageProcessor` instantiates an instance of the class `MessageService` which is further
used to send all messages.
This architecture makes it difficult to work with test doubles and, therefore, the logic is split up. A constructor is
added to the class `MessageProcessor`
which accepts an `MessageService` instance as a parameter to enable dependency injection. The passed instance is then
assigned to a private final field that
is used throughout the class. Therefore, the method `processMessages` creates no new instance of the `MessageService`
but always refers to the private variable.\
In the next step the mocking framework is implemented. Namely, the `MessageService` is mocked using `Mockito`. For this
two private fields are instantiated, one `Service`
and one `Processor`. To guarantee independence between tests the annotation `@BeforeEach` is used to freshly initialize
both fields before running each test.\
When testing lists, it's common practice to test for null / undefined as well as empty lists. In addition, lists can
have only one element, multiple elements and duplicates.
Pursuing this idea and applying it to the method `processMessages`, the following test cases can be derived as a first
step:

1. Null list
2. Empty list
3. List with one message only
4. List with more than one message
5. List with two identical messages

### A. Number of Invocations

To count the **number of invocations** the `Mockito` methods `never()` and `times(<int>)` in combination with `verify`
are used whenever the `sendMessages` method is called.

### B. Content of invocations—`ArgumentCaptor`

To assert the **content of invocations** two new private fields are added, namely the `receiverCaptor` and
the `contentCaptor` which are used to capture both parameters. Both the
receiver and the content can then be compared to verify the expected string. To reduce duplicate code, these two checks
are combined in a method (one for `1` and another for `2+` messages).

### C. Content of invocations—Increasing observability

Finally, to **increase observability** of the `MessageProcessor` class, a special case of the observer pattern is
implemented (single listener). For this an interface `MessageListener`
is added which includes a method for logging sent messages. Furthermore, an additional field and instance of the
provided interface is added to the constructor in `MessageProcessor`.
Then, whenever `processMessages` is called, the listener is notified and logs all sent messages. To the test class
a `TestMessageListener` subclass is added which implements the new interface with its method
and logs all messages in a list. This helps to verify the messages' receivers and contents when testing and removes the
dependency on external tools.

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