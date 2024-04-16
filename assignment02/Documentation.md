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

In a first step, the method ```getSum``` is declared ```static```, because it's independent of any class instance. 

When running the corresponding test cases, the ones for a null and an empty array fail. Looking at the method this is no surprise
since these edge cases are not handled before the array is accessed through indices. Therefore, an initial check for the corresponding cases
is added.\
Next, running JaCoCo reveals full coverage for the method `maxSubArray`. The class definition is not handled because the method is static but there is no class instance. However, given the context this can be ignored.
A possibility would be to declare the class `MaximumSubarray` as final, since it serves as a utility class providing the static method `maxSubArray`. A private constructor can then be added.
This would prevent the option to instantiate a variable of `MaximumSubarray`, therefore, yielding a 100% coverage with JaCoCo.\
Last, running Pitest reveals full mutation coverage. Solely the constructor `MaximumSubarray` is not included in the line coverage which again can be ignored for this context.\
In conclusion, the provided test suite catches all relevant bugs.

## unique_paths_grid

