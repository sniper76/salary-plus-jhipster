package com.salary.plus.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class SetComparator {

    public <T> Map<String, Set<T>> compareSets(Set<T> left, Set<T> right) {
        Set<T> onlyInLeft = new HashSet<>(left);
        Set<T> inBoth = new HashSet<>(left);
        Set<T> onlyInRight = new HashSet<>(right);

        onlyInLeft.removeAll(right); // 왼쪽에만 있는 값
        inBoth.retainAll(right); // 양쪽에 모두 있는 값
        onlyInRight.removeAll(left); // 오른쪽에만 있는 값

        Map<String, Set<T>> result = new HashMap<>();
        result.put("delete", onlyInLeft);
        result.put("update", inBoth);
        result.put("insert", onlyInRight);

        return result;
    }
    //    public static void main(String[] args) {
    //        Set<Long> left = Set.of(1L, 2L, 3L, 4L);
    //        Set<Long> right = Set.of(3L, 4L, 5L, 6L);
    //
    //        Map<String, Set<Long>> result = compareSets(left, right);
    //        System.out.println("onlyInLeft: " + result.get("onlyInLeft")); // [1, 2]
    //        System.out.println("inBoth: " + result.get("inBoth"));         // [3, 4]
    //        System.out.println("onlyInRight: " + result.get("onlyInRight"));// [5, 6]
    //    }
}
