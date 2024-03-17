# Documentation: Effective Software Testing Lab (Assignment 1)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file where you document your decisions and report test coverage requested nelow.
- Deadline: March 25, 2024 at 18:00 (Zurich, CH, time).

<!-- For reference when documenting -->
## What to document on (delete later)
- Perform specification-based testing, following the principles taught in the book and the lectures. Document each principle in the Documentation.md. If you find a bug, report the bug, the test that revealed the bug, as well as the bug fix.
- Enhance the previous test suite using structural testing. Specifically, aim for maximizing condition+branch coverage, which can be measured using the JaCoCo plugin. Document the process in the Documentation.md file, by reporting which conditions, branches, or lines did you miss with specification-based testing (if any), and what tests did you add to cover them.
- Now that you have a good testing suite, augment it further using mutation testing (you will need PItest and PItest plugin for JUnit 5). Report the mutation coverage in the Documentation.md. Explain whether the mutants that survive (if any) are worth writing tests for or not. If the surviving mutants are more than three (3), choose one from each mutation category (mutator in PITest terminology).


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
### Specification-based Testing
- For Step 1 and Step 2 I carefully looked at the implementation and method description and then tested the method with happy cases `isPalindrome()` until I understood what the method does.
- For Step 3 I explored possible inputs and outputs, and identified partitions
    - The integer input can be:
        - a negative number
        - a palindrome with length equals 1
          - I decided that single digits are considered a palindrome as well (including zero)
        - a palindrome with length longer than 1
        - not a palindrome
        - leading zeros
            - numbers with leading zeros will be automatically detected as octal integers and will be transformed to its decimal. I decided to calculate isPalindrome for its decimal representation. 
- For Step 4 I explored the boundaries
    - number can be out of range `-2^20 <= x <= 2^20 - 1`
- For Step 5 I devised test cases
- I used Junit5 to help me automate testcases and then performed some final checks.
- I found some things to fix:
  - The implementation of both methods did not handle the boundary constraint of `-2^20 <= x <= 2^20 - 1`, so I decided to implement an IllegalArgumentException for this case. I did not simple want to return false because this could be confusing for user, if they enter a number out of range which is clearly a palindrome like 1111111 but then get false as a result. 
  - Furthermore, PalindromeTwo did not consider 0 as a palindrome, so I changed the implementation to fix this.

### Structural Testing
Structural testing revealed to me that I missed a branch in palindromeTwo, because the expression `if (x % 10 == 0) return false;` never evaluated to true with my tests. So I augmented the test suites with an additional test: `assertFalse(PalindromeTwo.isPalindrome(1100));`.
In the end I had full branch coverage for both palindrome methods (leaving out the constructor as it is not relevant). 

### Mutation Testing
- For PalindromeOne there is one mutant that survived for the conditional boundary `while (start < end)`. However, if the condition would be <= instead of < that would always be fine, because if the index of the numbers array is the same, the value will also be the same and won't return false for the expression `if (numbers[start] != numbers[end])` anyway. Therefore, this mutant can survive. 
- For PalindromeTwo there were many mutants that survived for the lines 16 and 17. However, these lines are just to quicker check whether the first and last digits for a two or three digits number is divisible by 11. This means even if these lines are altered the rest of the code will still be able to catch the palindrome. Therefore, it is reasonable that these mutant survive. 

## ??
- what to do with leading zeros???