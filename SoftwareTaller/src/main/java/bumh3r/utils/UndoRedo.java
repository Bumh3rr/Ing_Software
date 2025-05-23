
package bumh3r.utils;

import java.util.Iterator;
import java.util.Stack;

public class UndoRedo<T> implements Iterable<T> {
    private final Stack<T> stack1;
    private final Stack<T> stack2;

    public UndoRedo() {
        stack1 = new Stack<>();
        stack2 = new Stack<>();
    }

    public void add(T item) {
        stack1.push(item);
        stack2.clear();
    }

    public T undo() {
        if (stack1.size() > 1) {
            stack2.push(stack1.pop());
            return stack1.get(stack1.size() - 1);
        } else {
            return null;
        }
    }

    public T redo() {
        if (!stack2.isEmpty()) {
            T item = stack2.pop();
            stack1.push(item);
            return item;
        } else {
            return null;
        }
    }

    public T getCurrent() {
        if (stack1.isEmpty()) {
            return null;
        } else {
            return stack1.get(stack1.size() - 1);
        }
    }

    public boolean isUndoAble() {
        return stack1.size() > 1;
    }

    public boolean isRedoAble() {
        return !stack2.empty();
    }

    public void clear() {
        stack1.clear();
        stack2.clear();
    }

    public void clearRedo() {
        stack2.clear();
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<T> {

        private int index = 0;

        @Override
        public boolean hasNext() {
            if (index < stack1.size()) {
                return true;
            } else if (index < stack1.size() + stack2.size()) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public T next() {
            if (index < stack1.size()) {
                return stack1.elementAt(index++);
            } else {
                return stack2.elementAt((index++) - stack1.size());
            }
        }
    }
}