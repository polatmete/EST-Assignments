# Documentation: Effective Software Testing Lab (Assignment 2)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, you should create an Assets folder for each exercise which contains screenshots of your test results such as coverage reports and logs of your tests running successfully.
- Deadline: April 22, 2024, at 18:00 (Zurich, CH, time).

## climbing_stairs
### Task 1: Code Coverage

The goal of the method is to calculate all distinct combinations of steps to reach the top of a staircase with n steps. 
Each time you can either climb 1 or 2 steps. As constraints, it is given that n must be positive, and the return value 
must be non-negative.

For such tasks, where the input value is an integer it is common to test for negative and positive values as well as 
zero. When looking at the implementation of the method we can also see that n=2 acts as a boundary, since the return values
for the inputs 1 and 2 are their respective numbers as well. We therefore also want to test for n>2. This leads us 
to the following test suite.
1. n=-1 -> Should not be accepted as input, because of the constraint mentioned above. As no return value for such cases
    is specified, we will return 0 (return value must be non-negative).
2. n=0 -> Should not be accepted as input, because of the constraint mentioned above. As no return value for such cases
   is specified, we will return 0 (return value must be non-negative).
3. n=2 -> Should return 2.
4. n=4 -> Should return 5.

To clarify: In order for the test suite to run, we had to make the test class as well as the single test cases public, 
in order to be able to access them (I do recognize that this could have been a local issue, but I had to declare them
in this way in order to make them work).

In a first step, the method ```climbStairs``` is declared ```static```, because it's independent of any class instance.
Running the tests with Jacoco we can see that only test 1 fails. This is due to a missing precondition (not allowing non-negative
inputs). Test 2 passes but not in the correct way, as 0 is also forbidden as an input. These issues will be addressed
in Task 2 since we have to implement a pre-condition for these tests to both pass in the correct way. The class definition 
is not handled because there is no class instance. However, given the context this can be ignored. A possibility would be 
to declare the class `ClimbingStairs` as final, since it serves as a utility class. A private constructor can then be added. 
This would prevent the option to instantiate a variable of `ClimbingStairs`, therefore, yielding a true 100% coverage with JaCoCo.

It is important to notice that the 100% line coverage at the moment only comes from tests 3 and 4. Tests 1 and 2 seem
to be redundant for the moment, but it will get clear why they are still important in Task 2.

### Task 2: Designing Contracts
#### Pre-conditions
1. ```n``` must be positive.

#### Post-conditions
1. The return value must be non-negative

#### Invariants
The method does not deal with any state-changing operation for which an invariant could be added.

#### Test suite update
As we already declared tests for the constraints the whole test suite is now passing. However, Jacoco reveals that the 
line coverage dropped to 92%. This is due to the post condition, that states that the return value cannot be non-negative.
Looking at the code we can see that it is actually impossible to return a negative value. Either the input is:
1. 0 -> return value 0
2. Between 1 and 2 -> return value 1 and 2
3. Larger than 2 -> return value larger than 2, since the for loop is executed at least once, which means that the line
   ```allWays = oneStepBefore + twoStepsBefore;``` is executed at least once. Given that ```oneStepBefore``` and ```twoStepsBefore```
   are hardcoded positive values, ```allWays``` cannot be negative.
   
Edit after Task 4: The return value can be negative because of overflow. Since it is unstable to test for this as a post-
condition (some values are negative for certain large inputs, some values are negative), we changed this into a pre-condition.
More details in Task 4.
   
This means that we can delete the post-condition, as the code itself guarantees the non-negative return value. This bumps
the line coverage back to 100%.

Please notice that it is clear that test 1 and 2 executed the same block of code (pre-condition). We could therefore delete
one of these tests and still come to a 100% line coverage. However, we decided to keep them both in, since est 1 targets a set
of integers that conceptually should not be allowed as input and test 2 targets an integer that acts as a boundary.

### Task 3: Testing Contracts
In this task we updated the test once more, since now it is clear that approprate exceptions need to be thrown whenever
pre-conditions are violated. Up until now, we just returned 0, which didn't make the most sense. We implemented a 
```IllegalArgumentException``` to be thrown whenever the input is non-positive. We then adjusted tests 1 and 2 to check 
for that exceptions. In terms of post-condition and invariants we do not have to adjust anything. An updated jacoco report
is also added under ```assets/```.

### Task 4: Property-Based Testing
According to the principles of property-based testing, many of the test cases can be replaced by one single test which 
uses integer ranges for ```n```. For this to work we need a formula to calculate the expected outcome of method given
a specific input. Playing around with the method we quickly notice that the outcome follows the Fibonacci sequence.

We therefore implemented a property-based test that used the Fibonacci formula to validate the outcome of the climbing
stairs method. For all the negative inputs we implemented a second property-based test that assured we would always throw
the correct error. The first property-based test revealed that some outputs higher than the max value of a ```long``` would 
be returned as negative numbers. This is due to overflow. To counter this, we wanted to implement a post-condition in the 
method to assure that no negative values are returned. However, this got even more complicated, since not all return values
for inputs higher than 91 are negative. To counter this, we therefore changed the post-condition into a pre-condition, 
saying that the input values could not be higher than 91 (because otherwise we would enter overflow territory). 
A third property-based test was implemented to check for this.

To showcase the implementation process the other test cases are not deleted even though they are now redundant. Looking
at Jacoco, we can see that we still reach a 100% line coverage.

## course_schedule

## find_duplicate

## longest_increasing_subsequence

## merge_k_sorted_lists

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

