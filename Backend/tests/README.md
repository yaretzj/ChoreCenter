_Based off of Bootstrap's great testing documentation [here](https://github.com/twbs/bootstrap/tree/main/js/tests)._

## How does Chore Center's backend test suite work?

Chore Center uses [Pytest](https://docs.pytest.org/en/6.2.x/#). To run the backend test suite, run `pytest` in any directory.

We are using [Github Actions](https://github.com/features/actions) for our CI/CD. Currently, on a push request `pytest` will be automatically run to check the unit tests.

## How do I add a new test?

1. Make sure your system is properly configured to run the existing tests (`Backend/README.md`), as you'll need them to run the Flask tests.
2. Locate and open the `Backend/tests` directory.
3. Review the [Pytest Documentation](https://docs.pytest.org/en/6.2.x/contents.html#toc) and use the existing tests as references for how to structure your new tests.
4. Write the necessary test(s) for the new or revised functionality.
5. Run `pytest` to see the results of your newly-added test(s).

## What should a unit test look like?

- Each test should have a unique name clearly stating what unit is being tested.
- Each test should test only one unit per test, although one test can include several assertions. Create multiple tests for multiple units of functionality.
- Each test should use `assert` to ensure something is expected.
- Each test should follow the project's [Python Code Guidelines](https://pep8.org/), which can be checked for by running `pylint` and `black` on the test file.