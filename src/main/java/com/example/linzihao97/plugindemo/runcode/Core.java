package com.example.linzihao97.plugindemo.runcode;

import com.example.linzihao97.plugindemo.Utils;
import com.example.linzihao97.plugindemo.utils.PsiUtils;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.jvm.JvmNamedElement;
import com.intellij.lang.jvm.JvmParameter;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Core {
    public static String getBeanCode(PsiClass psiClass, String varName) {
        return "(def " + varName + " (R/getBean " + psiClass.getQualifiedName() + "))";
    }

    public static String methodCallCode(PsiMethod method, Optional<String> varName, Map<String, Object> params) {
        if (PsiUtils.isStatic(method)) {
            return staticMethodCall(method, params);
        } else {
            return instanceMethodCall(method, varName.orElseThrow(), params);
        }
    }

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

    private static String staticMethodCall(PsiMethod method, Map<String, Object> params) {
        PsiClass psiClass = PsiTreeUtil.getParentOfType(method, PsiClass.class);
        String methodName = psiClass.getName() + "/" + method.getName();

        Stream<JvmParameter> parameters = Arrays.stream(method.getParameters());
        List<String> parameterNames = parameters.map(p -> p.getName()).collect(Collectors.toList());
        StringBuilder codeBuilder = new StringBuilder();
        codeBuilder.append("(" + methodName + " ");
        Utils.forEachWithIndex(parameterNames, (arg, i) -> {
            Object paramValue = params.get(arg);
            codeBuilder.append(paramValue);
            if (i != parameterNames.size() - 1) {
                codeBuilder.append(" ");
            }
        });
        codeBuilder.append(")");
        return codeBuilder.toString();
    }

    private static String methodCall(String fName, String varName, Stream<JvmParameter> parameters) {
        List<String> parameterNames = parameters.map(p -> p.getName()).collect(Collectors.toList());

        StringBuilder codeBuilder = new StringBuilder();
        codeBuilder.append("(" + fName + " ");
        Utils.forEachWithIndex(parameterNames, (arg, i) -> {
            codeBuilder.append(arg);
            if (i != parameterNames.size() - 1) {
                codeBuilder.append(" ");
            }
        });
        codeBuilder.append(")");
        return codeBuilder.toString();
    }

    private static String instanceMethodCall(PsiMethod method, String varName, Map<String, Object> params) {
        String methodName = "." + method.getName();
        Stream<JvmParameter> parameters = Arrays.stream(method.getParameters());
        List<String> parameterNames = parameters.map(JvmNamedElement::getName).collect(Collectors.toList());

        StringBuilder codeBuilder = new StringBuilder();
        codeBuilder.append("(" + methodName + " ");
        codeBuilder.append(varName + " ");
        Utils.forEachWithIndex(parameterNames, (arg, i) -> {
            Object paramValue = params.get(arg);
            codeBuilder.append(paramValue);
            if (i != parameterNames.size() - 1) {
                codeBuilder.append(" ");
            }
        });
        codeBuilder.append(")");
        return codeBuilder.toString();
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

    private static class SomeClass {
        private String sField;
        private LocalDateTime ldtField1;
        private LocalDateTime ldtField2;
        private BigDecimal bigDecimal1;
        private Long longField;
    }

    public static void test() {
        // (def gen-some-class
        //   (gen/let [s-field gen/string-alphanumeric
        //             ldt-field1 gen-local-date-time
        //             ldt-field2 gen-local-date-time
        //             big-decimal1 gen-big-decimal
        //             long-field gen/large-integer]
        //     (doto (SomeClass.)
        //       (.setSField s-field)
        //       (.setLdtField1 ldt-field1)
        //       (.setLdtField2 ldt-field2)
        //       (.setBigDecimal1 big-decimal1)
        //       (.setLongField long-field))))

        String className = "SomeClass";
        List<Field> fields = List.of(
                new Field("sField", "String"),
                new Field("ldtField1", "LocalDateTime"),
                new Field("ldtField2", "LocalDateTime"),
                new Field("bigDecimal1", "BigDecimal"),
                new Field("longField", "Long")
        );

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(def ");
        stringBuilder.append("gen-" + className + "\n");
        stringBuilder.append("  (gen/let [");
        fields.forEach(field -> {
            stringBuilder.append(to_kebab_case(field.name) + " " + clzToGenerator(field.type) + "\n");
        });
        stringBuilder.append("]");
        stringBuilder.append("(doto (" + className + ".) \n");
        fields.forEach(field -> {
            stringBuilder.append("(." + toSetter(field.name) + " " + to_kebab_case(field.name) + ") \n");
        });
        stringBuilder.append(")))");
        System.out.println(stringBuilder);

        ProblemsHolder holder = null;
        PsiReference reference = null;
    }

}
