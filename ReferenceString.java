package HW1;

import java.util.ArrayList;
import java.util.List;

/**
 * ReferenceString
 */
public class ReferenceString {

    /**
     * Generating random reference strings
     * @return List of reference strings
     */
    public static List<Integer> random() {
        List<Integer> referenceStrings = new ArrayList<>();
        while(referenceStrings.size() < Constant.MAXIMAL_MEM_REF) {
            int randomNum = UtilMethod.getRandomNumber(1, 20);
            int start = UtilMethod.getRandomNumber(Constant.MIN_REF_STR, Constant.MAX_REF_STR);
            for (int i = 0; i < randomNum; i++) {
                referenceStrings.add(start);
                start++;
                if (start > Constant.MAX_REF_STR) {
                    start = Constant.MIN_REF_STR;
                }
                if (referenceStrings.size() == Constant.MAXIMAL_MEM_REF) { break; }
            }
        }
        return referenceStrings;
    }

    /**
     * Generating locality reference strings
     * @return List of reference strings
     */
    public static List<Integer> locality() {
        int[] startIndexs = {1, 101, 201, 301, 401, 501, 601, 701, 801, 901, 1001, 1101};
        List<Integer> referenceStrings = new ArrayList<>();

        while(referenceStrings.size() < Constant.MAXIMAL_MEM_REF) {
            int numberOfReferencesForEachProcedure = UtilMethod.getRandomNumber(300000/120, 300000/300);

            int randomStart = startIndexs[UtilMethod.getRandomNumber(0, 11)];
            int randomEnd = randomStart + 100 - 1;

            for (int i = 0; i < numberOfReferencesForEachProcedure; i++) {
                int randomRefString = UtilMethod.getRandomNumber(randomStart, randomEnd);
                referenceStrings.add(randomRefString);
                if (referenceStrings.size() == Constant.MAXIMAL_MEM_REF) { break; }
            }
        }
        return referenceStrings;
    }

    /**
     * Generating my reference strings
     * @return List of reference strings
     */
    public static List<Integer> myRef() {
        List<Integer> referenceStrings = new ArrayList<>();

        while(referenceStrings.size() < Constant.MAXIMAL_MEM_REF) {
            double randomProb = UtilMethod.getRandomProbability();
            if (randomProb < 0.4) {
                // randomly pick one number between 1 to 600 
                referenceStrings.add(UtilMethod.getRandomNumber(1, 600));
            } else {
                // continuously pick 6 numbers
                int randomNum = UtilMethod.getRandomNumber(Constant.MIN_REF_STR, Constant.MAX_REF_STR);
                for (int i = 0; i < 6; i++) {
                    referenceStrings.add(randomNum);
                    randomNum++;
                    if (randomNum > Constant.MAX_REF_STR) {
                        randomNum = Constant.MIN_REF_STR;
                    }
                }
            }
        }
        return referenceStrings;
    }
}