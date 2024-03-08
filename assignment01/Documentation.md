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
Next, running JaCoCo reveals full coverage for the method `maxSubArray`, however the class definition is not handled. For correctness the class `MaximumSubarray` is declared as final,
since it serves as a utility class providing the static method `maxSubArray`. In addition, a private constructor is added. This prevents the option to instantiate a variable of `MaximumSubarray`,
therefore, now yielding a 100% coverage with JaCoCo.\
Last, running Pitest reveals full mutation coverage. Solely the constructor `MaximumSubarray` is not included in the line coverage.\
In conclusion, the provided test suite catches all relevant bugs.
## median_of_arrays

## needle_in_hay

## palindrome