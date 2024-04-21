package zest;

public class SumOfTwoIntegers {
    public static int getSum(int a, int b) {
        int sum = add(a, b);
        verifyRange(a, b, sum);
        return sum;
    }

    private static int add(int a, int b) {
        while (b != 0) {
            int carry = (a & b) << 1;  // Carry now contains common set bits of a and b
            a = a ^ b;  // Sum of bits of a and b where at least one of the bits is not set
            b = carry;  // Carry is shifted by one so that adding it to a gives the required sum
        }
        return a;
    }

    private static void verifyRange(int a, int b, int sum) {
        if (a > 0 && b > 0 && sum <= a) {
            throw new ArithmeticException("overflow occurred");
        }
        if (a < 0 && b < 0 && sum >= a) {
            throw new ArithmeticException("underflow occurred");
        }
    }
}
