package calculator;

public class Rational {

    private int num, denom;

    public Rational(double d) {
        String s = String.valueOf(d);
        int digitsDec = s.length() - 1 - s.indexOf('.');
        int denom = 1;
        for (int i = 0; i < digitsDec; i++) {
            d *= 10;
            denom *= 10;
        }

        int num = (int) Math.round(d);
        int g = gcd(num, denom);
        this.num = num / g;
        this.denom = denom /g;
    }

    public Rational(int num, int denom) {
        this.num = num;
        this.denom = denom;
    }

    public String toString() {
        return String.valueOf(num) + "/" + String.valueOf(denom);
    }

    public static int gcd(int num1, int denom1) {
        if(denom1 == 0){
            return num1;
        }
        return gcd(denom1, num1%denom1);
    }

    public static void main(String[] args) {
        System.out.println(new Rational(1.5));
    }
}