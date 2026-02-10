package academy;

import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

public class Application {

    public static void main(String[] args) {
        int exitCode = 0;

        try {
            Options options = new OptionsBuilder()
                    .include(MethodInvocationBenchmark.class.getName())
                    .warmupIterations(BenchmarkConstants.WARMUP_ITERATIONS)
                    .warmupTime(TimeValue.seconds(BenchmarkConstants.WARMUP_TIME_SECONDS))
                    .measurementIterations(BenchmarkConstants.MEASUREMENT_ITERATIONS)
                    .measurementTime(TimeValue.seconds(BenchmarkConstants.MEASUREMENT_TIME_SECONDS))
                    .forks(BenchmarkConstants.FORK_COUNT)
                    .resultFormat(ResultFormatType.TEXT)
                    .result(BenchmarkConstants.RESULT_FILE)
                    .build();

            new Runner(options).run();

        } catch (IllegalArgumentException configError) {
            System.err.println("Configuration error: " + configError.getMessage());
            configError.printStackTrace(System.err);
            exitCode = 2;

        } catch (RunnerException executionError) {
            System.err.println("Benchmark execution failed: " + executionError.getMessage());
            executionError.printStackTrace(System.err);
            exitCode = 1;

        } catch (Exception unexpected) {
            System.err.println("Unexpected error: " + unexpected.getMessage());
            unexpected.printStackTrace(System.err);
            exitCode = 1;
        }

        System.exit(exitCode);
    }
}
