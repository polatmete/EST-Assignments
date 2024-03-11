# Documentation: Effective Software Testing Lab (Assignment 1)

- Deliverable: One single zip file containing all the folders that you find attached to this assignment, augmented with the tests you will write for each problem, as described below. Additionally, a `Documentation.md` file where you document your decisions and report test coverage requested nelow.
- Deadline: March 25, 2024 at 18:00 (Zurich, CH, time).

<!-- For reference when documenting -->
## What to document on (delete later)
- Perform specification-based testing, following the principles taught in the book and the lectures. Document each principle in the Documentation.md. If you find a bug, report the bug, the test that revealed the bug, as well as the bug fix.
- Enhance the previous test suite using structural testing. Specifically, aim for maximizing condition+branch coverage, which can be measured using the JaCoCo plugin. Document the process in the Documentation.md file, by reporting which conditions, branches, or lines did you miss with specification-based testing (if any), and what tests did you add to cover them.
- Now that you have a good testing suite, augment it further using mutation testing (you will need PItest and PItest plugin for JUnit 5). Report the mutation coverage in the Documentation.md. Explain whether the mutants that survive (if any) are worth writing tests for or not. If the surviving mutants are more than three (3), choose one from each mutation category (mutator in PITest terminology).


## atoi

<!-- I am happy to receive feedback, I am unsure if I understood it correctly -->
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

### TODOs
- [ ] Mutation Testing

## combination_sum

## frac2dec

## generate_parantheses

## maximum_subarray

## median_of_arrays

## needle_in_hay

## palindrome