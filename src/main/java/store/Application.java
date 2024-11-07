package store;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현
        long my = 0;
        double your = 0;
        int count = 2_000_000_000;
        for (int i = 0; i < count; i++) {
            my += 2_000_000_000;
            your += 2_000_000_000;
        }
        double myA = my / (count * 1000) * 100;
        your = your / (count * 1000) * 100;
        System.out.println(myA);
        System.out.println(your);
    }
}
