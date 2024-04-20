# Documentation: Effective Software Testing Lab (Assignment 1)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, you should create an Assets folder for each exercise which contains screenshots of your test results such as coverage reports and logs of your tests running successfully.
- Deadline: April 22, 2024, at 18:00 (Zurich, CH, time).

## climbing_stairs

## course_schedule

## find_duplicate

### Task 1: Code Coverage
The method proves that at least one duplicate number must exist if an array containing n + 1 integers where each integer is between 1 and n (inclusive) is given. 

Assumptions:
- array of n + 1 integers
- each integer is between 1 and n (inclusive)
- there is only one duplicate number, but it could be repeated more than once

From the assumption and the restrictions of the definition of the input I derived the test cases where the same number occurs twice or more than twice. All other cases I then later tested when testing the contract (see below). 
To achieve 100% line and branch coverage of the method implementation one of this test cases already would have been enough. The assertions were not included in the line coverage as they are tested extra.

### Task 2: Designing Contracts
I then implemented pre- and post-conditions to ensure valid in- and output. The pre-conditions check the cases that the array input is not null, has at least two elements and that all elements are in the range [1,n].
The post-condition ensures that the output is also a valid number from the range [1,n].

### Task 3: Testing Contracts
Next, I then made sure to test all the pre-conditions in my test suite, including an array with only two elements, a null input or an array with numbers which were not in the range from [1,n]. The post-condition is already ensured by the pre-condition check as only numbers from the input array are returned which are in the range from [1,n].

### Task 4: Property-Based Testing
For property-based testing I created a test that inserts a random duplicate element at a random position and then checks whether the same duplicate integer is returned by the findDuplicate method.

## longest_increasing_subsequence

## merge_k_sorted_lists

## sorted_array2bst

## sum_of_two_integers

## unique_paths_grid

