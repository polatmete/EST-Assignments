# Documentation: Effective Software Testing Lab (Assignment 2)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, you should create an Assets folder for each exercise which contains screenshots of your test results such as coverage reports and logs of your tests running successfully.
- Deadline: April 22, 2024, at 18:00 (Zurich, CH, time).

## climbing_stairs
### Task 1: Code Coverage

The goal of the method is to calculate all distinct combinations of steps to reach the top of a staircase with n steps. 
Each time you can either climb `1` or `2` steps. As constraints, it is given that n must be positive, and the return value 
must be non-negative.

For such tasks, where the input value is an integer it is common to test for negative and positive values as well as 
zero. When looking at the implementation of the method we can also see that `n = 2` acts as a boundary, since the return values
for the inputs `1` and `2` are their respective numbers as well. We therefore also want to test for `n > 2`. This leads us 
to the following test suite.
1. `n = -1`: Should not be accepted as input, because of the constraint mentioned above. As no return value for such cases
    is specified, we will return `0` (return value must be non-negative).
2. `n = 0`: Should not be accepted as input, because of the constraint mentioned above. As no return value for such cases
   is specified, we will return `0` (return value must be non-negative).
3. `n = 2`: Should return `2`.
4. `n = 4`: Should return `5`.

To clarify: In order for the test suite to run, we had to make the test class as well as the single test cases public, 
in order to be able to access them (I do recognize that this could have been a local issue, but I had to declare them
in this way in order to make them work).

In a first step, the method `climbStairs` is declared `static`, because it's independent of any class instance.
Running the tests with Jacoco we can see that only test 1. fails. This is due to a missing pre-condition (not allowing non-negative
inputs). Test 2. passes but not in the correct way, as `0` is also forbidden as an input. These issues will be addressed
in Task 2 since we have to implement a pre-condition for these tests to both pass in the correct way. The class definition 
is not handled because there is no class instance. However, given the context this can be ignored. A possibility would be 
to declare the class `ClimbingStairs` as final, since it serves as a utility class. A private constructor can then be added. 
This would prevent the option to instantiate a variable of `ClimbingStairs`, therefore, yielding a true 100% coverage with JaCoCo.

It is important to notice that the 100% line coverage at the moment only comes from tests 3. and 4. Tests 1. and 2. seem
to be redundant for the moment, but it will get clear why they are still important in Task 2.

### Task 2: Designing Contracts
#### Pre-conditions
`n` must be positive.

#### Post-conditions
The return value must be non-negative

#### Invariants
The method does not deal with any state-changing operation for which an invariant could be added.

#### Test suite update
As we already declared tests for the constraints the whole test suite is now passing. However, Jacoco reveals that the 
line coverage dropped to 92%. This is due to the post condition, that states that the return value cannot be non-negative.
Looking at the code we can see that it is actually impossible to return a negative value. Either the input is:
1. `0`: return value `0`
2. Between `1` and `2`: return value `1` and `2`
3. Larger than `2`: return value larger than `2`, since the for loop is executed at least once, which means that the line
   `allWays = oneStepBefore + twoStepsBefore;` is executed at least once. Given that `oneStepBefore` and `twoStepsBefore`
   are hardcoded positive values, `allWays` cannot be negative.
   
Edit after Task 4: The return value can be negative because of overflow. Since it is unstable to test for this as a post-
condition (some values are negative for certain large inputs, some values are negative), we changed this into a pre-condition.
More details in Task 4.
   
This means that we can delete the post-condition, as the code itself guarantees the non-negative return value. This bumps
the line coverage back to 100%.

Please notice that it is clear that tests 1. and 2. executed the same block of code (pre-condition). We could therefore delete
one of these tests and still come to a 100% line coverage. However, we decided to keep them both in, since est 1 targets a set
of integers that conceptually should not be allowed as input and test 2 targets an integer that acts as a boundary.

### Task 3: Testing Contracts
In this task we updated the test once more, since now it is clear that approprate exceptions need to be thrown whenever
pre-conditions are violated. Up until now, we just returned `0`, which didn't make the most sense. We implemented a 
`IllegalArgumentException` to be thrown whenever the input is non-positive. We then adjusted tests 1. and 2. to check 
for that exceptions. In terms of post-condition and invariants we do not have to adjust anything. An updated jacoco report
is also added under `assets/`.

### Task 4: Property-Based Testing
According to the principles of property-based testing, many of the test cases can be replaced by one single test which 
uses integer ranges for `n`. For this to work we need a formula to calculate the expected outcome of method given
a specific input. Playing around with the method we quickly notice that the outcome follows the Fibonacci sequence.

We therefore implemented a property-based test that used the Fibonacci formula to validate the outcome of the climbing
stairs method. For all the negative inputs we implemented a second property-based test that assured we would always throw
the correct error. The first property-based test revealed that some outputs higher than the max value of a `long` would 
be returned as negative numbers. This is due to overflow. To counter this, we wanted to implement a post-condition in the 
method to assure that no negative values are returned. However, this got even more complicated, since not all return values
for inputs higher than `91` are negative. To counter this, we therefore changed the post-condition into a pre-condition, 
saying that the input values could not be higher than `91` (because otherwise we would enter overflow territory). 
A third property-based test was implemented to check for this.

To showcase the implementation process the other test cases are not deleted even though they are now redundant. Looking
at Jacoco, we can see that we still reach a 100% line coverage.

## course_schedule
### Task 1: Code Coverage

The method gets a list of courses numbered from `0` to `numCourses - 1` as well as a list of prerequisite requirements that tell
if one of the courses is a prerequisite of one of the other courses. It then checks if it is possible for a student to 
enroll in all the courses in a certain order or if there are any circular prerequisites.

As input values we therefore have a positive integer (this is a constraint and will be adhered to in Task 2), and a list
of lists that is transformed into a directed graph and then searched for circles. To test integers we usually start
with negative, `0` and positive values. However, this will be checked in detail in Task 2. For now, we will only test positive 
integers.

In terms of the directed graph we would usually test for null/empty inputs (For empty we decided to return true, since in that
case it makes sense to say that there are no circles), inputs with circles and inputs without circles. The 
two pre-conditions (no courses have itself as a prerequisite and each prerequisite must refer to a valid course) will be adhered
to in Task 2. For now, we implemented the following test cases:

1. `withoutCircles`: `numCourses = 2`, `prerequisites = [[1,0]]` returns `true`
2. `withCircles`: `numCourses = 2`, `prerequisites = [[1,0],[0,1]]` returns `false`
3. `emptyPrerequisites`: `numCourses = 2`, `prerequisites = []` returns `true`
4. `nullPrerequisites`: `numCourses = 2`, `prerequisites = []` throws `error`

With these the first two tests we already get to a 100% line coverage in Jacoco. However, since we only settled for the simplest
solution, we want to add two more tests with higher numbers to really stress the method and especially the for-loops. This
should also test larger circles than only with two prerequisites.

5. `largeWithoutCircles`: `numCourses = 6`, `prerequisites = [[1,0], [4,0], [5,1], [3,2], [2,5], [3,0]]` returns `true`
6. `withLargeCircles`: `numCourses = 6`, `prerequisites = [[1,0], [0,4], [4,2], [2,3], [3,5], [5,1]]` returns `false`

Tests 3. and 4. fail currently which is why we implement null and empty checks for the prerequisites. After this, we reach
100% line coverage on Jacoco again.

### Task 2: Designing Contracts

The pre-condition, post-condition, and invariant can be abbreviated from the task description:

#### Pre-conditions:
1. Prerequisites are depicted as a pair `[a, b]` indicating a one-way requirement from `b` to `a`. Since this is a logical pre-condition,  it does not have to be tested. 
2. numCourses must be a positive integer. 
3. No courses can require itself directly as a prerequisite. 
4. Each prerequisite must refer to a valid course (`0 ≤ a`, `b < numCourses`).

#### Post-condition:
Method returns a boolean value. This is already given through method declaration.

#### Invariant:
The method does not deal with any state-changing operation for which an invariant could be added.

#### Test suite update
The following tests are added to target these conditions:
7. `invalidNumCourses`: `numCourses = 0`, `prerequisites = [[1,0],[0,1]]` throws `error`
8. `circleTooSmall`: `numCourses = 2`, `prerequisites = [[1,0],[0,0]]` throws `error`
9. `invalidPrerequisite`: `numCourses = 2`, `prerequisites = [[1,0],[2,0]]` throws `error`
10. `negativePrerequisite`: `numCourses = 2`, `prerequisites = [[1,0],[2,-1]]` throws `error`

### Task 3: Testing Contracts

Tests 7. and 9. already fail but should throw and catch and error and test 8. passes, since the self-reference is not checked.
We implemented the according checks throwing errors with descriptive messages, updated the tests to catch those errors
and after re-running the suite Jacoco revealed a 100% line coverage again (Line 31 is marked yellow, which means it is 
only partially executed, which is fine. Adding another test just to have the other integer being to high is an overkill).

### Task 4: Property-Based Testing

The following properties have been identified:
1. `validRangeWithoutCircles`: Valid inputs for both arguments without circles.
2. `validRangeWithCircles`: Valid inputs for both arguments with circles.
3. `invalidNoOfCourses`: Number of courses is negative.
4. `referencingItself`: Any prerequisite (`a`, `b`) existing where `a = b` should return an error.
5. `invalidPrerequisite`: A prerequisite references a course that doesn't exist or contains a negative number.

Please note that we capped the number of courses and prerequisites to 100. This has to do with `outOfMemoryErrors`
that we got if we didn't set maximums for those inputs. In this context we decided it should be sufficient to cap those
inputs at much higher numbers than we used in the tests before.

## find_duplicate

### Task 1: Code Coverage
The method proves that at least one duplicate number must exist if an array containing `n + 1` integers where each integer is between `1` and `n` (inclusive) is given. 

Assumptions:
1. array of `n + 1` integers
2. each integer is between `1` and `n` (inclusive)
3. there is only one duplicate number, but it could be repeated more than once

From the assumption and the restrictions of the definition of the input I derived the test cases where the same number occurs twice or more than twice. All other cases I then later tested when testing the contract (see below). 
To achieve 100% line and branch coverage of the method implementation one of this test cases already would have been enough. So far my test suite contained the following tests:

1. `oneDuplicate`: Find the only existing duplicate
2. `hasSameNumberMoreThanTwice`: The duplicate number appears more than twice 
3. `multipleDuplicates`: Find the first duplicate when multiple duplicates exist


### Task 2: Designing Contracts
I then implemented pre- and post-conditions to ensure valid in- and output. The pre-conditions check the cases that the array input is not null, has at least two elements and that all elements are in the range `[1, n]`. It can be mathematically proven that under the given constraints there always has to be a duplicate. Implementing a post-condition like `assert hare >= 1 && hare <=n: "Not a valid output";` would never be false, which is why I left it out. 

#### Pre-conditions
1. The input array `nums` is not null.
2. The input array `nums` must have at least two elements.
3. Each element in the array must adhere to the range `[1, n]`.

#### Post-conditions
The method returns an integer that is the duplicate number found in `nums` and therefore ranges from `[1, n]`.

#### Invariants
The method does not deal with any state-changing operation for which an invariant could be added.


### Task 3: Testing Contracts
Next, I then made sure to test all the pre-conditions in my test suite, including an array with only one element, a null input or an array with numbers which were not in the range from `[1, n]`. The post-condition is already ensured by the pre-condition check as only numbers from the input array are returned which are in the range from `[1, n]`. As  my post-condition is never reached (see the screenshot in the assets file), I decided to remove it.
My test suites was therefore extended by the following tests: 

1. `nullArray`: input array is null
2. `oneElementArray`: the input array only contains one element
3. `elementOutOfRange`: the array contains at least one element which is not in the range `[1, n]`

### Task 4: Property-Based Testing
For property-based testing I created tests that inserts a random duplicate element at a random position and then checks whether the same duplicate integer is returned by the findDuplicate method. I did this with one duplicate element and multiple duplicates:

1. `propertyBasedTestMultipleDuplicates`
2. `propertyBasedTestOneDuplicate`

## longest_increasing_subsequence

### Task 1: Code Coverage
My testsuite consists out of a `happyCase` test to test the behaviour for a sequence with increasing numbers, a test with an array with unique numbers `uniqueNumbers`, decreasing numbers `decreasingNumbers` for both positive and negative numbers and a single element array test `singleElementArray`. Without the pre-conditions checks at the beginning this already covers 100% branch and decision coverage of the actual implementation.

### Task 2: Designing Contracts
The pre-conditions are implemented at the very beginning of the method and specify that if the array is null or empty the method returns zero.
The post-condition is that the number (if the array is not null or empty) returned is greater equal than `1`, which is already ensured by the method and therefore was marked as redundant when I explicitly specified it in the code.

#### Pre-conditions
1. the input `nums` is an array of integers.
2. `nums` is not null.
3. `nums` is not empty. 
4. Each element in the array can be negative, positive or zero.

#### Post-conditions
The method returns a non-negative integer

#### Invariants
The method does not deal with any state-changing operation for which an invariant could be added.

### Task 3: Testing Contracts
I wrote two test to test the pre-conditions null and empty array: `nullArray` and `emptyArray`.  
With the use of property-based testing I tested the post-condition that if the array is not null or empty the result hast to be greater equal than 1: `resultGreaterEqualOne`.

### Task 4: Property-Based Testing
As written above one of my property-based tests ensures that the post-condition result is greater equal than `1`: `resultGreaterEqualOne`.
The other creates a random sorted array with unique elements to check whether the result is equal the length of the generated array: `propertyBasedTest`. 

## merge_k_sorted_lists
### Task 1: Code Coverage
After performing specification-based testing, the coverage was at 100% for the class `MergeKSortedLists`.
The test cases `testNull`,  `testEmptyList`, `testEmptyNode`, and `testExample` were enough to cover all the lines and branches of code.

### Task 2: Designing Contracts
The pre-condition, post-condition, and invariant can be abbreviated from the task description:
#### Pre-condition
1. `0 <= #nodes >= 10^4`
2. all nodes provided should be sorted in ascending order within the list their in

#### Post-condition
A single sorted list should be returned 

#### Invariant
The list should be sorted at any time during the runtime

### Task 3: Testing Contracts
To check the pre-condition I added the test case `testPrecondition`, which checks the above-mentioned conditions.
The post-condition and invariant are already checked by previous test cases (`testExample`) and do not need to be tested separately.
Unfortunately adding pre-conditions, post-conditions, and invariants to the code lead to a decrease in branch coverage to 97%.
The one missing branch is due to the fact that during the runtime the invariant is always true. To cover the branch where the invariant is false the code must be manipulated in a way that it produces a wrong output.

### Task 4: Property-Based Testing
The following 3 properties have been identified:
1. Valid combinations in lists
2. Invalid combinations in lists
3. Lists in different lengths

To test the first property, an `Arbitrary<ListNode[]>` was created, which generates a list of nodes with a length between `0` and `10^4`.
For the second property, tha same was done but with parameters that lead to an invalid list.
Lastly, to test the third property, lists with random lengths were generated.

## sorted_array2bst
### Task 1: Code Coverage
After performing specification-based testing, the coverage was at 100% for the class `SortedArrayToBST`.

The test cases
1. `testNull`: Tests for null as input
2. `testEmptyList`: Tests for an empty list as input
3. `testOne`: Tests for a list with one element
4. `testExample`: Tests for an arbitrary example
5. `testMaxSize`: Tests for the maximum allowed size of the list `10^4`
6. `testTooLong`: Tests for a list that is too long `10^4 + 1`
7. `testNotSorted`: Tests for a list that is not sorted in ascending order
8. `testNotUnique`: Tests for a list that contains duplicates

were enough to cover all the lines and branches of code.

### Task 2: Designing Contracts
The pre-condition, post-condition, and invariant can be abbreviated from the task description:
#### Pre-condition
1. Array contains unique integers
2. `0 <= array.length() >= 10^4`
3. Input array is sorted in ascending order 
4. Input array is not null

#### Post-condition
1. Valid height-balanced binary search tree 
2. BST has the same size as the input array 

#### Invariant
1. BST must be height-balanced at all times 
2. Left subtree must be less or equal to root 
3. Right subtree must be greater than root

### Task 3: Testing Contracts
The pre-condition is already checked by the other test cases. 
To check the post-condition and the invariant, the test case `testPostcondition` respectively `testInvariant` was added.
Those test cases check the above-mentioned conditions.

### Task 4: Property-Based Testing
The following 2 properties have been identified:
1. Valid array that has not more than `10^4` elements
2. Array with more than `10^4` elements

To test the first property, an array with unique values is created and sorted afterward. The size is between `0` and `10^4`.
For the second property, an array with the size of `10^4 + 1` is created. Since the array is too large, it does not matter if the other properties hold.

## sum_of_two_integers
### Task 1
The goal of the method is to calculate the sum of two integers without using mathematical operations such as `+` or `-`. It receives two inputs `a` and `b`
which are both within the 32-bit signed integer range.\
When testing such inputs it's common practice to test for zero, positive and negative numbers. The 32-bit signed integer range includes all numbers in the interval `[-2'147'483'648, 2'147'483'647]`.
Therefore, especially these values are candidates for boundary testing. Furthermore, since the implementation works with `carry`, it's important to include corresponding tests.
Additionally, input validation could be applied to verify neither of the inputs are `null`.
However, according to the description it can be assumed that they are both integers.\
Pursuing this idea and applying it to the given method, the following test cases can be derived as a first step:
1. Zero inputs
2. Zero input (`a` or `b`)
3. Negative input
4. Positive input
5. Negative & positive input

In a first step, the method `getSum` is declared `static`, because it's independent of any class instance. Running JaCoCo reveals full coverage.
The class definition is not handled because there is no class instance. However, given the context this can be ignored.
A possibility would be to declare the class `SumOfTwoIntegers` as final, since it serves as a utility class. A private constructor can then be added.
This would prevent the option to instantiate a variable of `SumOfTwoIntegers`, therefore, yielding a true 100% coverage with JaCoCo.

### Task 2
#### Pre-conditions
1. `a` or `b` must be integers.
2. `a` or `b` must be within the 32-bit signed integer range `[-2'147'483'648, 2'147'483'647]`.

#### Post-conditions
1. The sum of `a` or `b` must be an integer.
2. The sum of `a` or `b` must be within the 32-bit signed integer range `[-2'147'483'648, 2'147'483'647]`.
3. If both `a` or `b` are positive the result must be positive (overflow otherwise).
4. If both `a` or `b` are negative the result must be negative (underflow otherwise).

#### Invariants
The method does not deal with any state-changing operation for which an invariant could be added. Instead of checking for over- and underflow after the loop,
the check could be added inside the loop, however the use of bitwise operations including the carry does not allow this.

### Task 3
Due to the method declaration the pre- and post-conditions concerning the given range are already guaranteed. The method cannot be called otherwise.
However, there is the issue of over- and underflow if both inputs added together are outside the integer range. Therefore, a check is added.

### Task 4
According to the principles of property-based testing, many of the test cases can be replaced by one single test which uses integer ranges for both inputs `a` and `b`.
Three tests including the `@Property` tag are added.
1. The first one (for valid ranges) makes use of the `@ForAll @IntRange(min = ..., max = ...)` annotation to test various values for `a` and `b`.
2. The second one checks all inputs that when summed up produce an overflow. This is achieved by using the `@Provide` tag and generating values that are larger than half
of the largest value `Integer.MAX_VALUE`.
3. The last one works similar to the second but checks for underflow with `Integer.MIN_VALUE`.

To showcase the implementation process the other test cases are not deleted even though they are now redundant.

## unique_paths_grid
### Task 1
The goal of the method is to compute the total number of possible paths for a robot to reach the bottom right corner of a grid
starting at the top lef corner. The robot can only move `down` or `right`. It receives two inputs `m` and `n`
which represent the number of rows and columns respectively. Both inputs are positive integers in the range `[1, 100]`.
Given this specification the normal 32-bit integer range quickly will overflow. Therefore, the implementation is changed to using `BigIntegers`.\
The requirements fail to clarify the return value for integers not in the specified range. So, for these cases a soft value of `-1` is returned.\
To effectively test this method the following cases are implemented:
1. Example case
2. Max case (`m = 100`, `n = 100`)
3. Special case (`m = 1`, `n = 100`)
4. Zero input (`m` or `n`)
5. Negative input
6. 100+ input

In a first step, the method `uniquePaths` is declared `static`, because it's independent of any class instance.\
For test cases 4. and 5. the execution crashes. When using zero input there is an IndexOutOfBoundsException. For negative input
there is a NegativeArraySizeException. To fix both of them a input check at the beginning of the method is included.\
For test case 6. an additional check is included, since otherwise the code does in fact return the correct computation, but the requirements
specify that the input should be in range `[1, 100]` and therefore a soft value of `-1` is expected.\
Running JaCoCo reveals full coverage. The class definition is not handled because there is no class instance. However, given the context this can be ignored.
A possibility would be to declare the class `UniquePaths` as final, since it serves as a utility class. A private constructor can then be added.
This would prevent the option to instantiate a variable of `UniquePaths`, therefore, yielding a true 100% coverage with JaCoCo.

### Task 2
#### Pre-conditions
1. `m` or `n` must be integers.
2. `m` or `n` must be within the range `[1, 100]`.

#### Post-conditions
1. The total number of possible paths has to be a positive integer.
2. The maximum number of possible paths is roughly `2.28 x 10^58` and is achieved with `m = n = 100`.
3. If `m > 100` or `n > 100` the method returns the soft value `-1`.
4. If `m < 1` or `n < 1` the method returns the soft value `-1`.

#### Invariants
The updating of the grid `dp` is a state-changing operation, however, since the values are only added together and nothing is subtracted, they will always
be positive. This guarantees a correct output behavior by definition.

### Task 3
To guarantee the pre-conditions a check has been added. Test cases 4.-6. verify the correctness of the conditions. Similarly, the check guarantees the post-conditions
concerning the soft value. To guarantee that the maximum number of possible paths doesn't overflow the method is changed to use `BigIntegers`. This guarantees the
correctness of all conditions.

### Task 4
According to the principles of property-based testing, many of the test cases can be replaced by one single test which uses integer ranges for both inputs `m` and `n`.
Two tests including the `@Property` tag are added.
1. The first one (for valid ranges) makes use of the `@ForAll @IntRange(min = ..., max = ...)` annotation to test various values for `m` and `n`.
2. The second one checks all inputs which are lower than `1` or larger than `100`. For this the `@Provide` tag is used in which integers outside the range are chosen
using the `Arbitrary<Integer>` class.

To showcase the implementation process the other test cases are not deleted even though they are now redundant.
