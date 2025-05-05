package bumh3r.utils;

import java.util.Stack;

public abstract class Promiseld {

    static {
        stack = new Stack<>();
    }

    private static final Stack<String> stack;

    public static Boolean checkPromiseId(String key) {
        if (key == null) {
            throw new IllegalArgumentException("id must not null");
        }
        return stack.contains(key);
    }

    public static void commit(String key) {
        if (!stack.contains(key)) {
            stack.add(key);
        }
    }

    public static void terminate(String key) {
        stack.remove(key);
    }

}
