package academy;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(java.util.concurrent.TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
public class MethodInvocationBenchmark {

    private Student student;
    private MethodHandle methodHandle;
    private java.lang.reflect.Method reflectionMethod;
    private NameGetter lambdaGetter;

    @FunctionalInterface
    interface NameGetter {
        String get(Student student);
    }

    @Setup(Level.Trial)
    public void setup() throws Throwable {
        student = new Student(BenchmarkConstants.STUDENT_NAME, BenchmarkConstants.STUDENT_AGE);

        // Reflection
        reflectionMethod = Student.class.getMethod(BenchmarkConstants.METHOD_NAME);

        // MethodHandle
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        methodHandle = lookup.findVirtual(
                Student.class, BenchmarkConstants.METHOD_NAME, BenchmarkConstants.METHOD_TYPE_STRING);

        // LambdaMetafactory
        CallSite callSite = java.lang.invoke.LambdaMetafactory.metafactory(
                lookup,
                BenchmarkConstants.LAMBDA_METHOD_NAME,
                MethodType.methodType(NameGetter.class),
                BenchmarkConstants.METHOD_TYPE_STRING_STUDENT,
                methodHandle,
                BenchmarkConstants.METHOD_TYPE_STRING_STUDENT);

        lambdaGetter = (NameGetter) callSite.getTarget().invokeExact();
    }

    @Benchmark
    public void directInvocation(Blackhole blackhole) {
        blackhole.consume(student.name());
    }

    @Benchmark
    public void reflectionInvocation(Blackhole blackhole) throws Exception {
        blackhole.consume(reflectionMethod.invoke(student));
    }

    @Benchmark
    public void methodHandleInvocation(Blackhole blackhole) throws Throwable {
        blackhole.consume(methodHandle.invoke(student));
    }

    @Benchmark
    public void lambdaMetafactoryInvocation(Blackhole blackhole) {
        blackhole.consume(lambdaGetter.get(student));
    }
}
