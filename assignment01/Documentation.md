# Documentation: Effective Software Testing Lab (Assignment 1)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file where you document your decisions and report test coverage requested nelow.
- Deadline: March 25, 2024 at 18:00 (Zurich, CH, time).


## atoi

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

Additionally, the code couldn't handle candidates that equalled zero. Since logically it doesn't make sense to count them
as valid candidates and because the specification don't mentioned this, we decided to 'clean' the candidates of all possible
zeros before running the algorithm. The same goes for 'target == 0'. Since there were no specifics about this case, and 
logically it doesn't make sense to add different numbers to zero, we decided to return an empty list if the target is 0.
This made test 8 and 9 work again.

The biggest chunk of tests that failed were all tests that included negative numbers. To not overcomplicate the code (since there
are no restrictions or specifications regarding the matter) we decided to make the code work if a list of negative candidates with
a negative target was inserted but restrict the insertion of negative AND positive candidates or negative candidates with positive targets.
These combinations make logically no sense since there is either no output, or an infinite amount (both of which we don't need to show).

After implementing a check to make sure the results list never exceeded 150 entries, all tests passed, and we moved to JaCoco.

Jacoco revealed a method and line coverage of 100% and a branch coverage of 93%. One of the missing branches relates to the
maximum of 150 entries for the results in the negative candidates' method. This bumped the coverage to 95%. The two missing 
branches were the 'if else (target == 0)'. We changed the logic around detecting inputs with target = 0 to a more logic approach
and to generally allow them but just return a list of one empty list. This did not bump the coverage up, but we felt like the
test was still important. We left it at that, since the respective 'if else' branch still reported 1 hit and 1 false hit
(so technically, they were at least one time true and one time false).

PiTest reported a test strength of 83%, with one line missing and 9 mutations surviving. The one line was the class definition
which was set to final since it serves as a utility class providing a static method. 
- 3 of the surviving mutations regard the return statements, which were replaced by Collections.emptyList(). This is okay,
since, this is what we want to return (in the code we just return the initialized result list).
- The other 6 mutations are regarding 'changed conditional boundary → SURVIVED'. Most of them belong to the one big filter
which checks for 'forbidden' combinations of inputs. Since only one of the conditions is targeted in a test, the other ones 
can be replaced without the test failing. Therefore, this is also okay.

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