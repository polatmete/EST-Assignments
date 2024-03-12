# Documentation: Effective Software Testing Lab (Assignment 1)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file where you document your decisions and report test coverage requested nelow.
- Deadline: March 25, 2024 at 18:00 (Zurich, CH, time).


## atoi

## combination_sum

## frac2dec

## generate_parantheses

## maximum_subarray
The goal of the method is to find a subarray such that all its integer numbers added together output the highest possible value,
when compared to other subarrays. For this the method receives an integer array as single input.\
When testing arrays / lists it's common practice to test for null / undefined lists and empty ones. In addition lists with only one element, multiple elements as well as duplicates.\
The requirements fail to clarify the case of an undefined list. Since the desired output of an empty list is zero, this behavior is adapted for the case of `null`.\
Pursuing this idea and applying it to the given method, the following test cases can be derived as a first step:
1. Null array
2. Empty array
3. Array with one integer only
4. Array with more than one integer

When running the corresponding test cases, the ones for a null and an empty array fail. Looking at the method this is no surprise
since these edge cases are not handled before the array is accessed through indices. Therefore, an initial check for the corresponding cases
is added.\
Next, running JaCoCo reveals full coverage for the method `maxSubArray`. The class definition is not handled because the method is static but there is no class instance. However, given the context this can be ignored.
A possibility would be to declare the class `MaximumSubarray` as final, since it serves as a utility class providing the static method `maxSubArray`. A private constructor can then be added.
This would prevent the option to instantiate a variable of `MaximumSubarray`, therefore, yielding a 100% coverage with JaCoCo.\
Last, running Pitest reveals full mutation coverage. Solely the constructor `MaximumSubarray` is not included in the line coverage which again can be ignored for this context.\
In conclusion, the provided test suite catches all relevant bugs.

## median_of_arrays
The goal of the method `findMedianSortedArrays` is to compute the median of two sorted integer arrays (in ascending order). It receives the two arrays as input.\
When testing arrays / lists it's common practice to test for null / undefined lists and empty ones. In addition, lists with only one element, multiple elements as well as duplicates.\
The requirements fail to clarify the case of an empty list. Since the desired output is the median of both lists, the method is expected to return the median of the first array.
If both lists are empty the method is expected to return zero.\
Pursuing this idea and applying it to the given method, the following test cases can be derived as a first step:
1. Null array
2. Empty array
3. Array with one integer only
4. Array with more than one integer

Since we have two lists and the additional requirement of them being in ascending order the test cases are extended:
1. One null array (the other array has expected values)
2. One unsorted array (the other array has expected values)
3. Two empty arrays
4. One empty array (the other array has expected values)
5. Two arrays with one value only
6. Two arrays with multiple values
7. One array with one value only and the other array with multiple

The last test seems a bit redundant, but it helps to cover all cases and therefore is also considered. For the cases where the return value is expected to be zero one test each is sufficient.
Therefore, no tests are included where one array is null and the other is unsorted.\
To be able to call `findMedianSortedArrays` a private instance of the class `MedianOfArray` is necessary and instantiated. To guarantee a correct initialization a `@BeforeEach` is used and the instance is
re-initialized for every test.\
When running the test suite, the test for case 3. fails. To catch the edge case when both arrays are empty a simple if-statement is added.

## needle_in_hay

## palindrome