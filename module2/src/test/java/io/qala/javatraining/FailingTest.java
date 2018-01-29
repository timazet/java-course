package io.qala.javatraining;

import org.testng.annotations.Test;

import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class FailingTest {

    /*
     * Why test is always failed only when we try to execute `mvn package`, but not when `mvn test` or execute test by IDE:
     * 1. `myresource.txt` is a resource of another module, which is a dependency for the current module
     * 2. When we execute test by IDE or through `mvn test`, we have list of classpaths, where classpath which contains
     * required resource is a local directory (`module1/target/classes`), and there is no problem with getting
     * FileInputStream from the string representation of URL (.../module1/target/classes/myresource.txt)
     * 3. When we execute test during the `mvn package`, we have list of classpaths, where classpath which contains
     * required resource is a jar library (result artifact of `module1`), and there is a problem with getting
     * FileInputStream from the string representation of URL like '.../module1/target/module1-1.0-SNAPSHOT.jar!/myresource.txt'
     * 4. This happened because according to the maven reactor plan we execute the whole cycle for the `module1`,
     * where artifact is created at package phase, and after that for `module2`. During the `package` phase
     * classpath to locate compiled classes of `module1` is changing from local directory to result artifact library
     *
     * To solve this kind of problem we should ask ClassLoader to provide us with InputStream for the required resource
     * instead of just URL
     *
     * Use:
     *  `mvn test -X` and `mvn package -X` to get a detailed output;
     *  `mvn test -Dmaven.surefire.debug` and `mvn package -Dmaven.surefire.debug` to debug a test;
     *  `mvnDebug test` and `mvnDebug package` to debug `maven-surefire-plugin` source code
     * */
    @Test public void strangeFailure() throws Exception {
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream("myresource.txt")) {
            assertEquals(inputStream.read(), 49);
        }
    }
}
