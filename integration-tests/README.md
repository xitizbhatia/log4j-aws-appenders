These tests run multiple scenarios against actual AWS resources.

**BEWARE:** you will be charged for these resources, and the tests do not delete them after running (intentionally, for debugging).
If you choose to run the tests, use the AWS Console or command line to delete all created resources. *I am not responsible for
your AWS bill.*

To run, you must have valid AWS credentials, with all permissions shown in the appender documentation. You must have also built
the appenders. To run the full suite of tests, execute `mvn clean test` from this directory. To run the tests for an individual
logging framework, run the same command from within the framework directory.