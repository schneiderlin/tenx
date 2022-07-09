package com.example.linzihao97.plugindemo.codegen;

import com.intellij.openapi.util.text.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
        public Class type;

        public Field(String name, Class type) {
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
                new Field("sField", String.class),
                new Field("ldtField1", LocalDateTime.class),
                new Field("ldtField2", LocalDateTime.class),
                new Field("bigDecimal1", BigDecimal.class),
                new Field("longField", Long.class)
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

    private static String clzToGenerator(Class clz) {
        switch (clz.getSimpleName()) {
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

    private static String toSetter(String fieldName) {
        return "set" + StringUtils.capitalize(fieldName);
    }

    private static String to_kebab_case(String camelCase) {
        return camelCase.replaceAll("(.)(\\p{Upper})", "$1-$2").toLowerCase();
    }
}
