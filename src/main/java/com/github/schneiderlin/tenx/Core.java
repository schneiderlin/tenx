package com.github.schneiderlin.tenx;

import com.intellij.psi.PsiClass;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Core {

    public static String callWithJsonCode(String className,
                                          String methodName,
                                          List<String> argNames,
                                          List<String> argClassNames,
                                          String json) {
        StringBuilder sb = new StringBuilder();
        sb.append("(call-with-json ");
        sb.append("\"");
        sb.append(className);
        sb.append("\" ");

        sb.append("\"");
        sb.append(methodName);
        sb.append("\" ");

        sb.append("[");
        if (!argNames.isEmpty()) {
            argNames.forEach(argName -> {
                sb.append("\"");
                sb.append(argName);
                sb.append("\"");
                sb.append(" ");
            });
        }
        sb.append("] ");

        sb.append("[");
        if (!argClassNames.isEmpty()) {
            argClassNames.forEach(argClassName -> {
                sb.append("\"");
                sb.append(argClassName);
                sb.append("\"");
                sb.append(" ");
            });
        }
        sb.append("] ");

        sb.append("\"");
        sb.append(json.replace("\"", "\\\""));
        sb.append("\"");

        sb.append(")");
        return sb.toString();
    }

    private static class Field {
        public String name;
        public String type;

        public Field(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static String importCode(PsiClass psiClass) {
        return "(import " + psiClass.getQualifiedName() + ")";
    }

    public static String createCode(PsiClass psiClass) {
        String className = psiClass.getName();
        List<Field> fields = Arrays.stream(psiClass.getAllFields())
                .map(psiField -> new Field(psiField.getName(), psiField.getType().getCanonicalText()))
                .collect(Collectors.toList());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(def ");
        stringBuilder.append("gen-" + className + "\n");
        stringBuilder.append("  (gen/let [");
        Utils.forEachWithIndex(fields, (field, i) -> {
            if (i != 0) {
                stringBuilder.append("            ");
            }
            stringBuilder.append(to_kebab_case(field.name) + " " + clzToGenerator(field.type));
            if (i < fields.size() - 1) {
                stringBuilder.append("\n");
            } else {
                stringBuilder.append("]\n");
            }
        });
        stringBuilder.append("    (doto (" + className + ".) \n");
        Utils.forEachWithIndex(fields, (field, i) -> {
            stringBuilder.append("      (." + toSetter(field.name) + " " + to_kebab_case(field.name) + ")");
            if (i < fields.size() - 1) {
                stringBuilder.append("\n");
            } else {
                stringBuilder.append(")))");
            }
        });

        return stringBuilder.toString();
    }

    public static String clzToGenerator(String clzName) {
        switch (clzName) {
            case "String":
                return "gen/string-alphanumeric";
            case "LocalDateTime":
                return "gen-local-date-time";
            case "BigDecimal":
                return "gen-big-decimal";
            case "Long":
                return "gen/large-integer";
        }
        return "unknown";
    }

    public static String toSetter(String fieldName) {
        return "set" + StringUtils.capitalize(fieldName);
    }

    public static String to_kebab_case(String camelCase) {
        return camelCase.replaceAll("(.)(\\p{Upper})", "$1-$2").toLowerCase();
    }

}
