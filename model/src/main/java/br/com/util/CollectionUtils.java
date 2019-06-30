package br.com.util;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {
	
	 /**
     * Splits the list.
     * 
     * @param <T>
     *            the type of list element
     * @param list
     *            the list
     * @param size
     *            the piece size
     * @return the split lists.
     * @throws NullPointerException
     *             if the list parameter is null
     * @throws IllegalArgumentException
     *             if the size parameter is less than 1
     */
    public static <T> List<List<T>> split(List<T> list, int size)
            throws NullPointerException, IllegalArgumentException {
        if (list == null) {
            throw new NullPointerException("The list parameter is null.");
        }
        if (size <= 0) {
            throw new IllegalArgumentException(
                "The size parameter must be more than 0.");
        }
        int num = list.size() / size;
        int mod = list.size() % size;
        List<List<T>> ret = new ArrayList<List<T>>(mod > 0 ? num + 1 : num);
        for (int i = 0; i < num; i++) {
            ret.add(list.subList(i * size, (i + 1) * size));
        }
        if (mod > 0) {
            ret.add(list.subList(num * size, list.size()));
        }
        return ret;
    }
 }
