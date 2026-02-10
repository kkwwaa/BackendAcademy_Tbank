package academy.models;

import java.util.List;

public record MethodInfo(String access, String name, List<String> params, String returnType) {}
