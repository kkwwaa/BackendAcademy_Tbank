package academy.models;

import java.util.*;

public record ClassInfo(
        String className,
        String superclass,
        List<String> interfaces,
        List<FieldInfo> fields,
        List<MethodInfo> methods,
        List<String> annotations,
        Map<String, Object> hierarchy) {}
