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
Next, running JaCoCo reveals full coverage for the method `maxSubArray`, however the class definition is not handled. For correctness the class `MaximumSubarray` is declared as final,
since it serves as a utility class providing the static method `maxSubArray`. In addition, a private constructor is added. This prevents the option to instantiate a variable of `MaximumSubarray`,
therefore, now yielding a 100% coverage with JaCoCo.\
Last, running Pitest reveals full mutation coverage. Solely the constructor `MaximumSubarray` is not included in the line coverage.\
In conclusion, the provided test suite catches all relevant bugs.
## median_of_arrays

## needle_in_hay

## palindrome