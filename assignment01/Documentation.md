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
The goal of the method is to find the first occurrence of a substring (needle) in a string (haystack).\
The requirements were clear, but one case was missing. It was not specified what should happen if `needle` is an empty string.

I started with specification based testing. I went through the specification and wrote tests for each part of the specification. I also added some permutations, like what happens if the substring we are looking for is at the beginning, in the middle, or at the end.
After writing the last test case `needleEmpty()`, I realized that this test failed. I looked at the code and saw that the method does not handle the case of an empty string `needle`. I added a check for this case and the test passed.

After performing structural testing with JaCoCo, I realized that the coverage was already 100%. So I did not add any structural tests.


Running the mutation test, I saw that 18 out of 19 mutations were successfully killed. The one that survived was "changed conditional boundary" and concerns `for(int i = 0; i < (lenHay-lenNed + 1); i++)`.
Since this is not relevant to the method, I decided to ignore this mutation.



## palindrome