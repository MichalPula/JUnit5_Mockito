package pulson.junit_mockito.junit_basics;

import java.util.List;
import java.util.stream.Collectors;

public class MathUtils {

    public int add (int a, int b) {
        return a + b;
    }

    public boolean isOdd(int number) {
        return number % 2 != 0;
    }

    public int getPower(int a) {
        return a * a;
    }

    public List<Integer> sortListASC(List<Integer> list) {
        return list.stream().sorted().collect(Collectors.toList());
    }

    public void taskWith20msSleep(){
        try {
            Thread.sleep(20);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int divide(int int1, int int2) {
        return int1/int2;
    }
}
