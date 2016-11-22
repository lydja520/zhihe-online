package cn.com.zhihetech.util.hibernate;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Date;
import java.util.Random;

/**
 * Created by ShenYunjie on 2015/11/16.
 */
public class GeneralQueryParamsTest extends TestCase {

    @Test
    public void testGetTargetHQL() throws Exception {
        IQueryParams queryParams = new GeneralQueryParams();
        queryParams.andEqual("name", "king").orBetween("birthday", new Date(), new Date())
                .andMoreThan("age", 28).sort("age", Order.ASC).sort("name", Order.DESC)
                .andNotEq("user.code", "12301345");
        SubQueryParams subQueryParams = new SubQueryParams();
        subQueryParams.andNotEq("age", 28).orAllLike("name", "tom")
                .orEqual("add.name", "asfasdf");
        queryParams.andSubParams(subQueryParams);
        System.out.println(queryParams.getTargetHQL());
    }

    @Test
    public void testString() throws Exception {
        String tmp = "hell.name";
        System.out.println(tmp.replaceAll("\\.", "_"));
    }

    @Test
    public void testRandom() throws Exception {
        int n = 100;
        int L = 10;
        Random rand = new Random();
        int temp = L;
        float j = 0;
        for (int i = 0; i < n - 1; i++) {
            j = rand.nextFloat() + 1;
            temp -= j;
            System.out.println(j);
        }
        System.out.println(temp);
    }
}