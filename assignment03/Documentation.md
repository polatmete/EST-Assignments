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
mock these
dependencies using ``Mockito``. At the beginning of my test suite, I instantiate these dependencies as mocks.
Then, I set up a ``BusTracker`` using these mock service dependencies, which allows me to effectively test the
``updateBusLocation`` method.

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

## movie_streaming

## payment_processing

## ticket_system