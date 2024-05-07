# Documentation: Effective Software Testing Lab (Assignment 3)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with
  the
  tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where
  you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, create an
  Assets folder for each exercise which contains screenshots of your test results.
- Deadline: May 20, 2024, at 18:00 (Zurich, CH, time).

## book_reviews

## bus_tracking
A common smell is being optimistic about external resources In my test suite a concern is the reliance on external
resources such as GPSDeviceService, which may not always be available. To
mitigate this, I mock these external resources to ensure consistent test behavior. Alternatively, I could skip the test
if the external resource is unavailable.

I extracted the setup of the mock interfaces and classes at the beginning of the
parent test class to minimize code duplication. The test cases x, x, x verify that the ``updateBusLocation`` method
correctly calls the corresponding services, confirmed using Mockito's ``verify`` feature.

To capture and assert specific objects passed to mocks, I utilized Mockito's argument captor feature. Specifically, I
captured the string value passed to the ``notifyPassengers`` method of ``NotificationService``, then verified if the
captured string meets the expected criteria.


NOTES
//You should stub the queries, as you now know that queries return values and do not change the object’s state; and you
should mock commands, as you know they change the world outside the object under test.
//TODO: testing with exact strings is not good

## cat_facts

## e_shop

## messages

## movie_streaming

## payment_processing

## ticket_system