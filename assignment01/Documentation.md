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

The goal of the method is to find all unique combinations of integers from an array of integers where the sum of each 
combination adds up to a defined target integer. For this the method receives an integer array of distinct integers and 
a target integer.\

When testing arrays (candidates) it is common practice testing for null / undefined lists and empty ones. In terms of 
the target we don't have to test for null / undefined, since int cannot be null. Since we have two arguments, we also 
have to look at possible combinations of the two. The requirements fail to clarify the case of an undefined candidate 
list. Since the desired output of an empty list is zero, this behavior is adapted for the case of `null`. Therefore, 
we get the following test cases for null / undefined:
1. candidates: null; target: some number
2. candidates: empty; target: some number

Disclaimer: To ease testing we have decided to use 4 as target and {1, 2, 3} as candidates. It offers multiple 
combinations of candidates, but not so much that writing the test cases gets too confusing.

In addition, lists are often tested with only one element, multiple elements as well as duplicates. The case for the
duplicates can be left out, since the requirements state, that the candidates have to be distinct (Note: Testing the 
method with duplicates shows that the output differs heavily. However, this "bug" was not fixed due to the 
specifications). Here we can also test the case for when no combination of the candidates adds up to the target (test 5). 
We get the following additional test cases:
3. candidates: array with one integer; target: some multiple of the candidate
4. candidates: array with one integer; target: no multiple of the candidate
5. candidates: array with multiple integers; target: some number

Additionally, it is worth to look into different partitions of integers for the candidates. The partitions would be the 
following: negative, zero and positive (and subsequently the combination of those). The positive case can be combined 
with test 5, so we only want to look at the other two cases. The negative and zero case are worth looking into 
separately. In terms of combinations we generally want to avoid creating a bloated test suite. On the other hand, 
since we only have to look at 3 different cases we also only have to look at three different combinations. We therefore 
add three more tests: negative/positive combination and zero/positive combination and zero/negative combination.
6. candidates: all negative; target: some number
7. candidates: zero; target: some number
8. candidates: negative/positive; target: some number
9. candidates: zero/positive; target: some number
10. candidates: zero/negative; target: some number

At this stage we noticed that the target integer of course can also be negative or zero. Adapting this to the test cases
6.-10. would surely bloat the test suite too much. We therefore try to minimize the additional tests.\

For the negative candidates, a negative target integer should yield some result, a positive target should yield and 
empty list and zero as well. Since zero is the same as the positive case we leave out this case (overall one more test).\

For the zero candidate, a negative or positive target should yield an empty list (covered with test 7.) and zero should 
return 0. This case is potentially covered with test 4. but since 0 is such a special number we include is as well (overall one more test).\

For the positive candidates, a negative target should yield an empty list, a positive target is already covered (test 5.) 
and zero should yield an empty list. Since zero is the same as the positive case we leave out this case (overall one more test).\

One could argue that we have to adapt the target cases also for the test cass 8.-10. However, since we included three more
tests already, we think the possible results have been covered. Also we threw out test 10, since we covered negative numbers already 
and if it was proven that those work, then test 9 should cover this already.

We therefore update the tests 7.-11.:
6. candidates: all negative; target: some positive number
7. candidates: all negative; target: some negative number
8. candidates: positive + zero; target: some number
9. candidates: zero; target: zero
10. candidates: all positive; target: some negative number
11. candidates: negative/positive; target: some lower number (to avoid bloating the test)
12. candidates: zero/positive; target: some number

When running the corresponding test cases, 1. and 2. fail. Looking at the method this is no surprise
since these edge cases are not handled before the array is accessed through indices. Therefore, an initial check for the corresponding cases
is added.\

Test 5 yielded an interesting result. It didn't pass because the order of the lists were not the same. After going through
the requirements of the method we realised that this behavior is expected, but the test didn't reflect it. We therefore changed
the test to only assert the List without paying attention to the order.

This was noticed after revisiting the documentation. We also noticed the constraint of a maximum of 150 possible 
combinations out of the candidates summing to the target. To cover this we implemented an additional test:
13. candidates: positive; target: some very high number -> length of result should be <=150.

The next tests that failed revealed that the code was not able to handle negative candidates or targets, since tests 6/7/11 all failed.
We therefore extended the code such that it was able to handle negative targets and candidates accordingly.

Additionally, the code couldn't handle zero as a candidate. Since logically it doesn't make sense to count zero
as valid candidates and because the specification don't mentioned this, we decided to 'clean' the candidates of all possible
zeros before running the algorithm. The same goes for 'target == 0'. Since there were no specifics about this case, and 
logically it doesn't make sense to add different numbers to zero, we decided to return an empty list if the target is 0.
This made test 8 and 9 work again.

The biggest chunk of tests that failed were all tests that included negative numbers. The specifications of the method
do not provide any details about how to handle negative numbers. However, thinking about the candidates, negative numbers
should, logically thinking, break the code. **This is because if you insert negative candidates mixed with positive candidates
the possible combinations for a given target become endless!** To avoid this sort of behavior we decided to only allow negative 
candidates if and only if there are only negative numbers in the list of candidates (subsequently this means that positive
candidates are only allowed if and only if there are only positive numbers in the list of candidates).

After implementing a check to make sure the results list never exceeded 150 entries (A test with low candidates and 
a very high target), all tests passed, and we moved to JaCoco.

Jacoco revealed a method and line coverage of 100% and a branch coverage of 93%. One of the missing branches relates to the
maximum of 150 entries for the results in the negative candidates' method. This bumped the coverage to 95%. The two missing 
branches were the 'if else (target == 0)'. We changed the logic around detecting inputs with target = 0 to a more logic approach
and to generally allow them but just return a list of one empty list. This did not bump the coverage up, but we felt like the
test was still important. We left it at that, since the respective 'if else' branch still reported 1 hit and 1 false hit
(so technically, they were at least one time true and one time false).

PiTest reported a test strength of 83%, with one line missing and 9 mutations surviving.
- 3 of the surviving mutations regard the return statements, which were replaced by Collections.emptyList(). This is okay,
since, this is what we want to return (in the code we just return the initialized result list).
- The other 6 mutations are regarding 'changed conditional boundary → SURVIVED'. Most of them belong to the one big filter
which checks for 'forbidden' combinations of inputs. Since only one of the conditions is targeted in a test, the other ones 
can be replaced without the test failing. Therefore, this is also okay.

## frac2dec
The goal of this method is to convert a fraction into a decimal. To do this, the method is given two integers that represent the numerator and denominator of a fraction. The method should return the fraction in string format. If the fractional part is repeating, the repeating part should be enclosed in parentheses. If multiple answers are possible, the method should return one of them.

I started with specification-based testing. I went through the specification and wrote tests for each part of the specification. After writing the `zeroDominator()` test, I realized that this test failed. After a closer look at the code, I saw that the case where the dominator is zero is not handled. So I added this check at the beginning of the method: `if (denominator == 0) return null;`.

After that, I continued with the structural tests. I used JaCoCo to measure code coverage. The report showed that the coverage was 100%, so I continued with mutation testing.

The mutation testing with PITest showed that 18 / 20 mutants were killed. After taking a closer look at the report, I saw that the line `res.append(((numerator > 0) ^ (denominator > 0)) ? "-" : "");` caused it with the mutation `changed conditional boundary`. But since I already handled the case where the denominator or numerator is 0 on lines 8 and 9, I can ignore those mutations.

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
The goal of the method is to find the first occurrence of a substring (needle) in a string (haystack).\
The requirements were clear, but one case was missing. It was not specified what should happen if `needle` is an empty string.

I started with specification based testing. I went through the specification and wrote tests for each part of the specification. I also added some permutations, like what happens if the substring we are looking for is at the beginning, in the middle, or at the end.
After writing the last test case `needleEmpty()`, I realized that this test failed. I looked at the code and saw that the method does not handle the case of an empty string `needle`. I added a check for this case and the test passed.

After performing structural testing with JaCoCo, I realized that the coverage was already 100%. So I did not add any structural tests.


Running the mutation test, I saw that 18 out of 19 mutations were successfully killed. The one that survived was "changed conditional boundary" and concerns `for(int i = 0; i < (lenHay-lenNed + 1); i++)`.
I changed the for loop to `for(int i = 0; i <= lenHay-lenNed; i++)`. After that, all mutants were killed.



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
