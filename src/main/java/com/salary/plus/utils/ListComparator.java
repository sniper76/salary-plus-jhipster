package com.salary.plus.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class ListComparator {

    public Map<String, List<Long>> compareLists(List<Long> left, List<Long> right) {
        Set<Long> leftSet = new HashSet<>(left);
        Set<Long> rightSet = new HashSet<>(right);

        Set<Long> onlyInLeft = new HashSet<>(leftSet);
        Set<Long> inBoth = new HashSet<>(leftSet);
        Set<Long> onlyInRight = new HashSet<>(rightSet);

        onlyInLeft.removeAll(rightSet); // 왼쪽에만 있는 값
        inBoth.retainAll(rightSet); // 양쪽에 모두 있는 값
        onlyInRight.removeAll(leftSet); // 오른쪽에만 있는 값

        Map<String, List<Long>> result = new HashMap<>();
        result.put("delete", new ArrayList<>(onlyInLeft));
        result.put("update", new ArrayList<>(inBoth));
        result.put("insert", new ArrayList<>(onlyInRight));

        return result;
    }
    //    public static void main(String[] args) {
    //        List<Long> left = List.of(1L, 2L, 3L, 4L);
    //        List<Long> right = List.of(3L, 4L, 5L, 6L);
    //
    //        Map<String, List<Long>> result = compareLists(left, right);
    //        System.out.println("onlyInLeft: " + result.get("onlyInLeft")); // [1, 2]
    //        System.out.println("inBoth: " + result.get("inBoth"));         // [3, 4]
    //        System.out.println("onlyInRight: " + result.get("onlyInRight"));// [5, 6]
    //    }
}
