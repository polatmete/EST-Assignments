# Documentation: Effective Software Testing Lab (Assignment 2)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file per exercise where you document your decisions, testing strategy, choices, and any assumptions made. Furthermore, you should create an Assets folder for each exercise which contains screenshots of your test results such as coverage reports and logs of your tests running successfully.
- Deadline: April 22, 2024, at 18:00 (Zurich, CH, time).

## climbing_stairs

## course_schedule

## find_duplicate

## longest_increasing_subsequence

## merge_k_sorted_lists

## sorted_array2bst

## sum_of_two_integers
### Task 1
The goal of the method is to calculate the sum of two integers without using mathematical operations such as ```+``` or ```-```. It receives two inputs ```a``` and ```b```
which are both within the 32-bit signed integer range.\
When testing such inputs it's common practice to test for zero, positive and negative numbers. The 32-bit signed integer range includes all numbers in the interval ```[-2'147'483'648, 2'147'483'647]```.
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
2. ```a``` or ```b``` must be within the 32-bit signed integer range ```[-2'147'483'648, 2'147'483'647]```.

#### Post-conditions
1. The sum of ```a``` or ```b``` must be an integer.
2. The sum of ```a``` or ```b``` must be within the 32-bit signed integer range ```[-2'147'483'648, 2'147'483'647]```.
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
### Task 1
The goal of the method is to compute the total number of possible paths for a robot to reach the bottom right corner of a grid
starting at the top lef corner. The robot can only move ```down``` or ```right```. It receives two inputs ```m``` and ```n```
which represent the number of rows and columns respectively. Both inputs are positive integers in the range ```[1, 100]```.
Given this specification the normal 32-bit integer range quickly will overflow. Therefore, the implementation is changed to using ```BigIntegers```.\
The requirements fail to clarify the return value for integers not in the specified range. So, for these cases a soft value of -1 is returned.\
To effectively test this method the following cases are implemented:
1. Example case
2. Max case (```m = 100```, ```n = 100```)
3. Special case (```m = 1```, ```n = 100```)
4. Zero input (```m``` or ```n```)
5. Negative input
6. 100+ input

In a first step, the method ```uniquePaths``` is declared ```static```, because it's independent of any class instance.\
For test cases 4. and 5. the execution crashes. When using zero input there is an IndexOutOfBoundsException. For negative input
there is a NegativeArraySizeException. To fix both of them a input check at the beginning of the method is included.\
For test case 6. an additional check is included, since otherwise the code does in fact return the correct computation, but the requirements
specify that the input should be in range ```[1, 100]``` and therefore a soft value of ```-1``` is expected.\
Running JaCoCo reveals full coverage. The class definition is not handled because there is no class instance. However, given the context this can be ignored.
A possibility would be to declare the class `UniquePaths` as final, since it serves as a utility class. A private constructor can then be added.
This would prevent the option to instantiate a variable of `UniquePaths`, therefore, yielding a true 100% coverage with JaCoCo.

### Task 2
#### Pre-conditions
1. ```m``` or ```n``` must be integers.
2. ```m``` or ```n``` must be within the range ```[1, 100]```.

#### Post-conditions
1. The total number of possible paths has to be a positive integer.
2. The maximum number of possible paths is roughly ```2.28 x power(10, 58)``` and is achieved with ```m = n = 100```.
3. If ```m > 100``` or ```n > 100``` the method returns the soft value ```-1```.
4. If ```m < 1``` or ```n < 1``` the method returns the soft value ```-1```.

#### Invariants
The updating of the grid ```dp``` is a state-changing operation, however, since the values are only added together and nothing is subtracted, they will always
be positive. This guarantees a correct behavior by definition.

### Task 3
To guarantee the pre-conditions a check has been added. Test cases 4.-6. verify the correctness of the conditions. Similarly, the check guarantees the post-conditions
concerning the soft value. To guarantee that the maximum number of possible paths doesn't overflow the method is changed to use ```BigIntegers```. This guarantees the
correctness of all conditions.

### Task 4
According to the principles of property-based testing, many of the test cases can be replaced by one single test which uses integer ranges for both inputs ```m``` and ```n```.
Two tests including the ```@Property``` tag are added.
1. The first one (for valid ranges) makes use of the ```@ForAll @IntRange(min = ..., max = ...)``` annotation to test various values for ```m``` and ```n```.
2. The second one checks all inputs which are lower than ```1``` or larger than ```100```. For this the ```@Provide``` tag is used in which integers outside the range are chosen
using the ```Arbitrary<Integer>``` class.

To showcase the implementation process the other test cases are not deleted even though they are now redundant.
