package academy.services;

import academy.models.ClassInfo;
import academy.models.FieldInfo;
import academy.models.MethodInfo;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class InspectorService {

    public ClassInfo inspect(Class<?> clazz) {
        return new ClassInfo(
                clazz.getSimpleName(),
                getSuperclassName(clazz),
                getInterfaces(clazz),
                getFields(clazz),
                getMethods(clazz),
                getClassAnnotations(clazz),
                buildFullHierarchy(clazz));
    }

    private String getSuperclassName(Class<?> clazz) {
        Class<?> superclass = clazz.getSuperclass();
        return superclass != null ? superclass.getSimpleName() : null;
    }

    private List<String> getInterfaces(Class<?> clazz) {
        return Arrays.stream(clazz.getInterfaces()).map(Class::getSimpleName).toList();
    }

    private List<FieldInfo> getFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> !field.isSynthetic())
                .map(field -> new FieldInfo(
                        getAccessModifier(field.getModifiers()),
                        field.getName(),
                        field.getType().getSimpleName()))
                .toList();
    }

    private List<MethodInfo> getMethods(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> !method.isSynthetic())
                .map(method -> new MethodInfo(
                        getAccessModifier(method.getModifiers()),
                        method.getName(),
                        getParameterTypes(method),
                        method.getReturnType().getSimpleName()))
                .sorted(Comparator.comparing(MethodInfo::name))
                .toList();
    }

    private List<String> getParameterTypes(Method method) {
        return Arrays.stream(method.getParameterTypes())
                .map(Class::getSimpleName)
                .toList();
    }

    private List<String> getClassAnnotations(Class<?> clazz) {
        return getAnnotations(clazz.getAnnotations());
    }

    private List<String> getAnnotations(Annotation[] annotations) {
        return Arrays.stream(annotations)
                .map(annotation -> "@" + annotation.annotationType().getSimpleName())
                .toList();
    }

    private String getAccessModifier(int modifiers) {
        if (Modifier.isPublic(modifiers)) return "public";
        if (Modifier.isProtected(modifiers)) return "protected";
        if (Modifier.isPrivate(modifiers)) return "private";
        return "package-private";
    }

    private Map<String, Object> buildFullHierarchy(Class<?> clazz) {
        List<Class<?>> parents = new ArrayList<>();
        Class<?> current = clazz;

        while (current != null && current != Object.class) {
            parents.addFirst(current);
            current = current.getSuperclass();
        }

        Map<String, Object> root = new LinkedHashMap<>();
        Map<String, Object> cursor = root;

        for (Class<?> parent : parents) {
            Map<String, Object> next = new LinkedHashMap<>();
            cursor.put(parent.getSimpleName(), next);
            cursor = next;
        }

        assert clazz != null;
        addChildren(cursor, clazz);

        return root;
    }

    private void addChildren(Map<String, Object> node, Class<?> clazz) {
        Class<?>[] children = clazz.getPermittedSubclasses();
        if (children == null) {
            return;
        }

        for (Class<?> child : children) {
            Map<String, Object> childNode = new LinkedHashMap<>();
            node.put(child.getSimpleName(), childNode);
            addChildren(childNode, child);
        }
    }
}
