package util;

import org.apache.commons.collections4.ListUtils;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class ListUtilTest {
    @Test
    public void testInsertInAscOrderedList() {
        String[] strArr = {"2", "4", "6", "8"};
        List<String> list = Arrays.asList(strArr);
        list = new ArrayList<>(list);
        list.sort(String::compareTo);
        ListUtil.insertInAscOrderedList(list, "1");
        list.forEach(System.out::println);
        if (ListUtil.countCheck(list, 6)) {
            System.out.println("足够");
        }else {
            System.out.println("不够");
        }
    }

    @Test
    public void testIsEmpty() {
        List<String> list = new ArrayList<>();
        System.out.println(ListUtil.isEmpty(list));
    }
}