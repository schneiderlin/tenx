package com.example.linzihao97.plugindemo.tenx;

import clojure.lang.RT;
import clojure.lang.Var;

import java.util.function.Supplier;

public class ReplClient {
    public static Object evalClient(String code) {
        return eval("(eval-client \"" + code.replace("\"", "\\\"") + "\")");
    }

    private static Var var(String varName) {
        return RT.var("clojure.core", varName);
    }

    private static <T> T eval(String... code) {
        return using(() -> {
            Var EVAL = var("eval");
            return (T) EVAL.invoke(readString(String.join("\n", code)));
        });
    }

    private static <T> T readString(String s) {
        Var READ_STRING = var("read-string");
        return (T) READ_STRING.invoke(s);
    }

    public void start(int port) {
        using(() -> {
            Thread replThread = new Thread(() -> {
                eval("(require '[nrepl.core :as nrepl])");
                eval("(def conn (nrepl/connect :port " + port + "))");
                eval("(def timeout 3000)");
                eval("(def client (nrepl/client conn timeout))");
                eval("(defn eval-client [code]\n     (let [msg (nrepl/message client {:op \"eval\" :code code})\n           values (nrepl/response-values msg)]\n       (first values)))");
            });
            replThread.setName("Nrepl-Service-Client");
            replThread.start();
            return null;
        });
    }

    private static <T> T using(Supplier<T> f) {
        ClassLoader oldLoader = Thread.currentThread().getContextClassLoader();
        try {
            ClassLoader loader = ReplClient.class.getClassLoader();
            Thread.currentThread().setContextClassLoader(loader);
            return f.get();
        } finally {
            Thread.currentThread().setContextClassLoader(oldLoader);
        }
    }
}


