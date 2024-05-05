# Documentation: Effective Software Testing Lab (Assignment 3)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with
  the
  tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where
  you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, create an
  Assets folder for each exercise which contains screenshots of your test results.
- Deadline: May 20, 2024, at 18:00 (Zurich, CH, time).

## book_reviews

In this scenario the external dependency is the SQL database containing the book metadata. Being optimistic about
external resources is a common test smell. A database might not always be readily available, causing tests to fail
due to the external resource rather than the method under test. I therefore
decided to use a stub to mock the database. I stubbed the ``BookRatingsFetcher`` class and configured it to return a list
when ``all()`` is called. Using a stub also makes the test more cohesive, easier to understand and not flaky. 

Initially, to facilitate testing, I needed to modify the production code. ``BookRatingsFetcher`` is now passed into the
constructor of ``BookManager``. The method `highRatedBooks` no longer instantiates the database connection itself 
but rather 
accesses the
instance attribute ``ookRatingsFetcher``, responsible for establishing a database connection. Consequently, the database is
no longer handled within the test; instead, I configured a ``BookRatingsFetcher`` stub using Mockito. This grants me full
control over the stub. Whenever its all() method is invoked, I simply return a list of books set up specifically for
the test. I can then verify whether the method returns the correct books. I tested the method and made sure that only
books with a
rating higher equal 4 were returned by the method. I also
tested with an empty list. However, I did not test cases where the rating was outside the valid range, relying on the
database to enforce values within the range [1,5].

The disadvantage of using test doubles in my test suite is that the test becomes less realistic as it only mimicks
behaviour. Furthermore, the test code becomes more coupled with the implementation details of the production code.
This makes the test harder to maintain (e.g., if a new attribute for `Book` is added I have to add a new attribute
to all my `Book` contstructors)

Next, I then added a new method `uniqueAuthors`, which returns all authors of the books in the database. Additionally,
I added some new tests to my test suite to test my new method: `returnUniqueAuthors` and `emptyAuthors`.
To minimize code duplication and enhance test readability, I decided to
extract a method `getBookManagerWithStubbedFetcher` that instantiates a stub for the `BookRatingsFetcher` and 
returns the `BookManager` with the stub as a dependency.

## bus_tracking

## cat_facts

## e_shop

## messages

## movie_streaming

## payment_processing

## ticket_system