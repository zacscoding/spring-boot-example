package demo.util;

import java.util.Random;

/**
 * Random extraction from items
 *
 * @author zacconding
 * @Date 2018-12-05
 * @GitHub : https://github.com/zacscoding
 */
public class RandomExtractor<T> {

    private T[] item;
    private int offset;

    public static RandomExtractor<Integer> createIntegerItems(int size) {
        return createIntegerItems(0, size - 1);
    }

    public static RandomExtractor<Integer> createIntegerItems(int start, int last) {
        if (start > last) {
            throw new IllegalArgumentException("start must be smaller than last");
        }

        int size = last - start + 1;

        Integer[] item = new Integer[size];
        for (int i = 0; i < size; i++) {
            item[i] = start + i;
        }

        return new RandomExtractor<>(item);
    }

    public RandomExtractor(T[] item) {
        this.item = item;
        reset();
    }

    public T nextItem() throws NoRemainItemException {
        if (!isRemain()) {
            throw new NoRemainItemException();
        }

        return item[offset++];
    }

    public boolean isRemain() {
        return offset < item.length;
    }

    public void reset() {
        if (item == null || item.length == 0) {
            throw new IllegalArgumentException("Invalid item");
        }

        Random rnd = new Random();
        for (int i = item.length; i > 1; i--) {
            swap(i - 1, rnd.nextInt(i));
        }
    }

    private void swap(int idx1, int idx2) {
        T helper = item[idx1];
        item[idx1] = item[idx2];
        item[idx2] = helper;
    }

    public static class NoRemainItemException extends RuntimeException {

        public NoRemainItemException() {
            super();
        }

        public NoRemainItemException(String message) {
            super(message);
        }
    }
}
