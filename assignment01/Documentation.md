# Documentation: Effective Software Testing Lab (Assignment 1)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file where you document your decisions and report test coverage requested nelow.
- Deadline: March 25, 2024 at 18:00 (Zurich, CH, time).


## atoi

## combination_sum

## frac2dec

## generate_parantheses
The goal of the method is to generate all combinations of well-formed parentheses, given n pairs of parentheses. If n is
zero or negative, the method returns an empty array. Another constraint is that n shall not be smaller than 1 or higher than
eight. The return of the method, should n be higher than 8 is not given. We assume it should also be handled with an empty
array as return (similar to n < 1).

Looking at this rather small set of possible inputs, we could just write a test for every possible number for n plus a
test for n < 1 and n > 8, which would give us a total of 10 tests. For a method of this size we deem this too much. Therefore,
we rather look at the on and off points. The on points are 1 and 8 and the off points are 0 and 9. Writing tests for these 
numbers should suffice and shrink the number of tests to 4. Since negative inputs are specifically mentioned in the
description we would also like a test that checks this. Normally we would also add a test for null input, but Java doesn't
even allow this as an argument for the method and flags it immediately, which is why we can leave that out.

After more thoughts, the test with n = 8 seems to be too complicated since there are 1'430 different possibilities and 
writing them down for the assertion is totally out of scope. Another idea would be that we could test if the size of the 
output with n = 8 is actually 1'430. For this to work we would need another way to calculate this number, since taking it
for granted just by inserting it into the method is actually not a good practice (since the method could be implement
incorrectly).

At this stage we turned to ChatGpt with the following prompt: 'Given n pairs of parentheses, write a function to generate 
all combinations of well-formed parentheses. If n is zero or negative, return empty array.' It then implemented a method,
similar to the one given in this code. We then followed up: 'Is there any correlation between the number of possible 
combinations and the input n?'. It now answered with the term 'Catalan number sequence'. A quick google search confirmed
this suggestion. The Catalan number sequence is a sequence of numbers based on 'C = 1/(n+1)*(2n n)'. The parentheses problem
is a known application of this sequence. We can therefore confirm that the size of the output for n = 8 should indeed be
1'430.

Following this, we would like to have a test that actually asserts the combinations of parentheses and decided on n = 3
since the effort to write down the output in a test is low enough to make it worth it. For this test we have to ake sure that 
the order of the output does not matter.

This leads us to the following 5 test cases:
1. n = -1 -> empty array
2. n = 0 -> empty array
3. n = 1 -> [()]
4. n = 3 -> [((())), (()()), (())(), ()(()), ()()()]
5. size of n = 8 -> 1'430
6. n = 9 -> empty Array

Running the tests revealed that the last test failed, because the input of n = 9 was allowed in the method even though
the constraint forbids it. We therefore implement a precondition in the method that checks that the input is not greater
than 8.

Next, running JaCoCo reveals full coverage for the method `generateParentheses`. The only line not covered is the class definition,
which is okay in our context. We could add the keyword 'final' to it, but that seems to be a bit overkill.

Last, running Pitest reveals 95% mutation coverage. One mutant survived due to the new check we implemented earlier. There,
we just added a '|| n >= 9' to the already existing check for n <= 0. This means that one of the checks can be altered, 
with test still working. This behavior is okay and cannot be changed by e.g. separating the two checks to two lines. 
In conclusion, the provided test suite catches all relevant bugs.

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