package sk.tuke.gamestudio.database.security;

import org.jetbrains.annotations.NotNull;
import sk.tuke.gamestudio.annotations.security.Security;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackageScanner {

    private static final int SECURITY_CLASSES = 4;

    public static @NotNull List<Class<?>> scanClasses(@NotNull String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        File directory = new File(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource(path)).getFile());

        if (directory.exists()) {
            File[] files = directory.listFiles();
            assert files != null;
            for (File file : files) {
                if (file.isDirectory()) {
                    classes.addAll(scanClasses(packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                    Class<?> clazz = Class.forName(className);
                    classes.add(clazz);
                }
            }
        }

        return classes;
    }

    public static @NotNull List<Class<?>> scanForComponents(String packageName) {
        List<Class<?>> componentClasses = new ArrayList<>();
        try {
            List<Class<?>> classes = PackageScanner.scanClasses(packageName);

            for (Class<?> clazz : classes) {
                if (clazz.isAnnotationPresent(Security.class)) {
                    componentClasses.add(clazz);
                }
            }
        } catch (ClassNotFoundException e) {
            throw new ClassCastException("Class not found: " + e);
        }
        return componentClasses;
    }

    public static void scanAndCheckForUniqueClass(String packageName) {
        List<Class<?>> classes = scanForComponents(packageName);
        List<Class<?>> uniqueClasses = classes.stream()
                .filter(cls -> cls.isAnnotationPresent(Security.class))
                .toList();

        if (SECURITY_CLASSES < uniqueClasses.size()) {
            throw new SecurityException("Multiple instances of unique class found. Stopping execution.");
        }
    }

}