package HW1;

public class UtilMethod {
    
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static double getRandomProbability() {
        return Math.random();
    }
}