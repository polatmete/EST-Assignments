# Documentation: Effective Software Testing Lab (Assignment 1)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file where you document your decisions and report test coverage requested nelow.
- Deadline: March 25, 2024 at 18:00 (Zurich, CH, time).

<!-- For reference when documenting -->
## What to document on (delete in the end)
- Perform specification-based testing, following the principles taught in the book and the lectures. Document each principle in the Documentation.md. If you find a bug, report the bug, the test that revealed the bug, as well as the bug fix.
- Enhance the previous test suite using structural testing. Specifically, aim for maximizing condition+branch coverage, which can be measured using the JaCoCo plugin. Document the process in the Documentation.md file, by reporting which conditions, branches, or lines did you miss with specification-based testing (if any), and what tests did you add to cover them.
- Now that you have a good testing suite, augment it further using mutation testing (you will need PItest and PItest plugin for JUnit 5). Report the mutation coverage in the Documentation.md. Explain whether the mutants that survive (if any) are worth writing tests for or not. If the surviving mutants are more than three (3), choose one from each mutation category (mutator in PITest terminology).


## atoi

### Specification-based Testing
- For Step 1 and Step 2 I carefully looked at the implementation and method description and then tested the method with a happy case until I understood what the method does.
- For Step 3 I explored possible inputs and outputs, and identified partitions
  - The string input can be: 
    - null 
    - character only 
    - positive number
    - negative number 
    - starting with whitespaces
    - starting with zeros
    - including characters at different positions
- For Step 4 I explored the boundaries
  - number can be out of range (+/-2^31)
- For Step 5 I devised test cases
  - as there was only one input, I could not test exceptional inputs only once, without combining with others
  - however, I decided that for negative as positive input testing with a certain number is enough and does not require testing with a bunch of other numbers in the same format
- I used Junit5 to help me automate testcases and then performed some final checks

### Structural Testing
With the assistance of JaCoCo, I performed structural testing. The code coverage tool revealed that I had overlooked a branch, specifically the one validating if the string is empty. I then created a new test `stringIsEmpty()`to test for this case, which then allowed me to reach 100% branch + condition coverage.

### Mutation Testing
When conducting mutation testing using PITest, 20 out of 21 mutants were successfully eliminated. The one mutant that survived is the change of the conditional boundary "if (num > (Integer.MAX_VALUE - digit) / 10)". Consequently, I expanded the test suite by testing on points as well as off points left and right from the boundary. This allowed me to kill all mutants.

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
7. One array with negative and one with positive numbers
8. Two arrays with negative numbers only
9. One array with one value only and the other array with multiple

The last test seems a bit redundant, but it helps to cover all cases and therefore is also considered. For the cases where the return value is expected to be zero one test each is sufficient.
Therefore, no tests are included where one array is null and the other is unsorted.\
To be able to call `findMedianSortedArrays` a private instance of the class `MedianOfArray` is necessary and instantiated. To guarantee a correct initialization a `@BeforeEach` is used and the instance is
re-initialized for every test.\
When running the test suite, the test for case 3. fails. To catch the edge case when both arrays are empty a simple if-statement is added.\
Next, running JaCoCo reveals a code coverage of 98%. Since we only have one test for the cases where the return value is expected to be zero (i.e., a null or empty array), not all branches are covered.
For example, the case where both arrays are null is not considered. However, including a test like this would be redundant and not necessary since testing for one null array is enough.
Similarly, this is true for the unsorted array, the empty array as well as case 9. The report shows that the only line not covered at all is line 14. This makes sense, since the variables `p1` and `p2`
are initialized with zero and then compared to the length of the arrays. Since the length of an array cannot be negative line 14 is never reached and can be considered dead code for this use case. For all of these
reasons it does not make sense to include more tests in the test suite.\
Last, running Pitest reveals a 88% mutation coverage. Following the control flow of the program the first mutant survived due to a changed conditional boundary on line 19. To tackle this mutant a test with an array
not only in descending order but rather with duplicates is required. The test `arrayWithDuplicates` is added and yields a coverage of 90%. This test additionally contributes to a better branch coverage once re-running JaCoCo.\
The second mutant survived due to a change of the math operator on line 33. Since the variables `m` and `n` stand for the length of the respective arrays, a case is necessary where the lengths added together return an odd number
and the if-statement still evaluates to true. The only way to get an odd number of the total length is by having an array with an odd length and the other with an even length. By definition, their difference will always be
odd and the sum as well as the difference if taken modulo two will give the same result. Therefore, this mutant can be skipped.\
The third mutant survived due to a changed conditional boundary on line 8. However, since it doesn't matter whether the middle between two numbers is considered from the left or the right boundary, this mutant can also be skipped.
The rest of the surviving mutants can be skipped as well since the test suite does not consider all the combinations of invalid inputs (such as null, empty or unordered arrays) intentionally.


## needle_in_hay

## palindrome