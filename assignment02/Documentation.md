# Documentation: Effective Software Testing Lab (Assignment 1)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, you should create an Assets folder for each exercise which contains screenshots of your test results such as coverage reports and logs of your tests running successfully.
- Deadline: April 22, 2024, at 18:00 (Zurich, CH, time).

## climbing_stairs

## course_schedule

## find_duplicate

Assumptions:
- array of n + 1 integers 
- each integer is between 1 and n (inclusive)
- there is only one duplicate number, but it could be repeated more than once

### Task 1: Code Coverage
To achieve 100% line and branch coverage one test case would have been enough. However, I decided to test with an examples containing duplicates twice and more than twice. Other edge cases were covered and tested by the pre-conditions (see below). The assertions were not included in the line coverage as they are tested extra.

### Task 2: Designing Contracts
I then implemented pre- and post-conditions to ensure valid in- and output. The pre-condition check the cases that the array input is not null, has at least two elements and that all elements are in the range [1,n].
The post-condition ensures that the output is also a valid number from the range [1,n].

### Task 3: Testing Contracts
Next, I then made sure to test all the conditions in my test suite, covering the edge cases like an array with only two elements or a invalid or null input.

### Task 4: Property-Based Testing
For property-based testing I create a test that inserts a random duplicate element at a random position and then checks whether the same duplicate integer is returned by the findDuplicate method.

## longest_increasing_subsequence

### Task 1: Code Coverage
My testsuite consists out of a happy case, of an array with unique numbers, decreasing numbers, a single element array and two edge case test a null and empty array test. With this test suite I was able to reach 100% branch and decision coverage.

TODO: zero input
### Task 2: Designing Contracts
The pre-condition is that the method returns 0 for null and empty arrays. 
The post-condition is that the number (if the array is not null or empty) returned is greater equal than 1, which is already ensured by the method. This is why an assert like 'assert maxLength >= 1...' is redundant.

### Task 3: Testing Contracts
I wrote two test to test the pre-conditions null and empty array. 
With the use of property-based testing I tested the post-condition that if the array is not null or empty the result hast to be greater equal than 1.

### Task 4: Property-Based Testing
As written above one of my property-based tests ensures that the post-condition result is greater equal than 1. The other creates a random sorted array with unique elements to check whether the result is equal the length of the generated array. 

## merge_k_sorted_lists

## sorted_array2bst

## sum_of_two_integers

## unique_paths_grid

