# Documentation: Effective Software Testing Lab (Assignment 2)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, you should create an Assets folder for each exercise which contains screenshots of your test results such as coverage reports and logs of your tests running successfully.
- Deadline: April 22, 2024, at 18:00 (Zurich, CH, time).

## climbing_stairs

## course_schedule

## find_duplicate

## longest_increasing_subsequence

## merge_k_sorted_lists
### Task 1: Code Coverage
After performing specification-based testing, the coverage was at 100% for the class `MergeKSortedLists`.
The test cases `testNull`,  `testEmptyList`, `testEmptyNode`, and `testExample` were enough to cover all the lines and branches of code.

### Task 2: Designing Contracts
The precondition, postcondition, and invariant can be abbreviated from the task description:
 - Precondition
   - 0 <= #nodes >= 10^4
   - all nodes provided should be sorted in ascending order within the list their in
 - Postcondition
   - A single sorted list should be returned
 - Invariant
   - The list should be sorted at any time during the runtime

### Task 3: Testing Contracts
To check the precondition I added the test case `testPrecondition`, which checks the above-mentioned conditions.
The postcondition and invariant are already checked by previous test cases (`testExample`) and do not need to be tested separately.
Unfortunately adding preconditions, postconditions, and invariants to the code lead to a decrease in branch coverage to 97%.
The one missing branch is due to the fact that during the runtime the invariant is always true. To cover the branch where the invariant is false the code must be manipulated in a way that it produces a wrong output.

### Task 4: Property-Based Testing
The following 3 properties have been identified:
1. Valid combinations in lists
2. Invalid combinations in lists
3. Lists in different lengths

To test the first property, an `Arbitrary<ListNode[]>` was created, which generates a list of nodes with a length between 0 and 10^4.
For the second property, tha same was done but with parameters that lead to an invalid list.
Lastly, to test the third property, lists with random lengths were generated.

## sorted_array2bst

## sum_of_two_integers
### Task 1
The goal of the method is to calculate the sum of two integers without using mathematical operations such as ```+``` or ```-```. It receives two inputs ```a``` and ```b```
which are both within the 32-bit signed integer range.\
When testing such inputs it's common practice to test for zero, positive and negative numbers. The 32-bit signed integer range includes all numbers in the interval [-2'147'483'648, 2'147'483'647].
Therefore, especially these values are candidates for boundary testing. Furthermore, since the implementation works with ```carry```, it's important to include corresponding tests.
Additionally, input validation could be applied to verify neither of the inputs are ```null```.
However, according to the description it can be assumed that they are both integers.\
Pursuing this idea and applying it to the given method, the following test cases can be derived as a first step:
1. Zero inputs
2. Zero input (```a``` or ```b```)
3. Negative input
4. Positive input
5. Negative & positive input

In a first step, the method ```getSum``` is declared ```static```, because it's independent of any class instance. Running JaCoCo reveals full coverage.
The class definition is not handled because there is no class instance. However, given the context this can be ignored.
A possibility would be to declare the class `SumOfTwoIntegers` as final, since it serves as a utility class. A private constructor can then be added.
This would prevent the option to instantiate a variable of `SumOfTwoIntegers`, therefore, yielding a true 100% coverage with JaCoCo.

### Task 2
#### Pre-conditions
1. ```a``` or ```b``` must be integers.
2. ```a``` or ```b``` must be within the 32-bit signed integer range ([-2'147'483'648, 2'147'483'647]).

#### Post-conditions
1. The sum of ```a``` or ```b``` must be an integer.
2. The sum of ```a``` or ```b``` must be within the 32-bit signed integer range ([-2'147'483'648, 2'147'483'647]).
3. If both ```a``` or ```b``` are positive the result must be positive (overflow otherwise).
4. If both ```a``` or ```b``` are negative the result must be negative (underflow otherwise).

#### Invariants
The method does not deal with any state-changing operation for which an invariant could be added. Instead of checking for over- and underflow after the loop,
the check could be added inside the loop, however the use of bitwise operations including the carry does not allow this.

### Task 3
Due to the method declaration the pre- and post-conditions concerning the given range are already guaranteed. The method cannot be called otherwise.
However, there is the issue of over- and underflow if both inputs added together are outside the integer range. Therefore, a check is added.

### Task 4
According to the principles of property-based testing, many of the test cases can be replaced by one single test which uses integer ranges for both inputs ```a``` and ```b```.
Three tests including the ```@Property``` tag are added.
1. The first one (for valid ranges) makes use of the ```@ForAll @IntRange(min = ..., max = ...)``` annotation to test various values for ```a``` and ```b```.
2. The second one checks all inputs that when summed up produce an overflow. This is achieved by using the ```@Provide``` tag and generating values that are larger than half
of the largest value ```Integer.MAX_VALUE```.
3. The last one works similar to the second but checks for underflow with ```Integer.MIN_VALUE```.

To showcase the implementation process the other test cases are not deleted even though they are now redundant.
## unique_paths_grid

