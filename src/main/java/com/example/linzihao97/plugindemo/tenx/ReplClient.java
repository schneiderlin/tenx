package com.example.linzihao97.plugindemo.tenx;

import clojure.lang.RT;
import clojure.lang.Var;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Supplier;

public class ReplClient {
    public static Object evalClient(String code) {
        return eval("(eval-client \"" + Base64.getEncoder().encodeToString(code.getBytes(StandardCharsets.UTF_8)) + "\")");
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

    private Thread.UncaughtExceptionHandler h = (th, ex) -> {
        String msg = "start repl client Exception: " + ex.getMessage();
        MyNotifier.notifyError(null, msg);
    };

    public void start(int port) {
        using(() -> {
            Thread replThread = new Thread(() -> {
                eval("(require '[nrepl.core :as nrepl])");
                eval("(def conn (nrepl/connect :port " + port + "))");
                eval("(def timeout 3000)");
                eval("(def client (nrepl/client conn timeout))");
                eval("(import java.util.Base64)");
                eval("(defn decode [to-decode]\n" +
                        "  (String. (.decode (Base64/getDecoder) to-decode)))");
                eval("(defn eval-client [code]\n     " +
                        "(let [msg (nrepl/message client {:op \"eval\" :code (decode code)})\n" +
                        "      values (nrepl/response-values msg)]\n       " +
                        "  (first values)))");
            });
            replThread.setName("Nrepl-Service-Client");
            replThread.setUncaughtExceptionHandler(h);
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


