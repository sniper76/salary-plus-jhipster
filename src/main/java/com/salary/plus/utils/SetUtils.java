package com.salary.plus.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SetUtils {

    private SetUtils() {
        // restrict instantiation
    }

    @SafeVarargs
    public static <T> Set<T> immutableSet(T... elements) {
        Set<T> set = new HashSet<>();
        Collections.addAll(set, elements); // 배열의 요소를 추가
        return Collections.unmodifiableSet(set); // 불변으로 반환
    }
}
