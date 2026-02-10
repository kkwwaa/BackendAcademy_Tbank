package academy;

import java.lang.invoke.MethodType;

public final class BenchmarkConstants {

    private BenchmarkConstants() {}

    // Student
    public static final String STUDENT_NAME = "Басова Екатерина";
    public static final int STUDENT_AGE = 19;

    // Method names
    public static final String METHOD_NAME = "name";
    public static final String LAMBDA_METHOD_NAME = "get";

    // Method types
    public static final MethodType METHOD_TYPE_STRING = MethodType.methodType(String.class);

    public static final MethodType METHOD_TYPE_STRING_STUDENT = MethodType.methodType(String.class, Student.class);

    // Benchmark parameters
    public static final int WARMUP_ITERATIONS = 5;
    public static final int WARMUP_TIME_SECONDS = 1;

    public static final int MEASUREMENT_ITERATIONS = 10;
    public static final int MEASUREMENT_TIME_SECONDS = 1;

    public static final int FORK_COUNT = 3;

    // Output
    public static final String RESULT_FILE = "results.txt";
}
