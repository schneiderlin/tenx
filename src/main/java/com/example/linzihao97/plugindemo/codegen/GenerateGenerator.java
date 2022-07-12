package com.example.linzihao97.plugindemo.codegen;

import com.intellij.psi.PsiClass;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateGenerator {
    private static class SomeClass {
        private String sField;
        private LocalDateTime ldtField1;
        private LocalDateTime ldtField2;
        private BigDecimal bigDecimal1;
        private Long longField;
    }

    private static class Field {
        public String name;
        public String type;

        public Field(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static void main(String[] args) {
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
    }

    public static String gen(PsiClass psiClass) {
        String className = psiClass.getName();
        List<Field> fields = Arrays.stream(psiClass.getAllFields())
                .map(psiField -> new Field(psiField.getName(), psiField.getType().getCanonicalText()))
                .collect(Collectors.toList());

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
