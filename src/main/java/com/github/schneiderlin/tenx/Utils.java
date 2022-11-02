package com.github.schneiderlin.tenx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Utils {
    public static <A> void forEachWithIndex(List<A> l, BiConsumer<A, Integer> f) {
        for (int i = 0; i < l.size(); i++) {
            f.accept(l.get(i), i);
        }
    }

    public static <A, B> List<B> mapWithIndex(List<A> l, BiFunction<A, Integer, B> f) {
        ArrayList<B> result = new ArrayList<>();
        for (int i = 0; i < l.size(); i++) {
            B b = f.apply(l.get(i), i);
            result.add(b);
        }
        return result;
    }
}
