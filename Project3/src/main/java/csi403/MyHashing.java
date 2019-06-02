package csi403;

import java.util.*;

public class MyHashing {

    public MyHashing() {}

    public int hashAlgo(String inputString) {
        int asciiValue, asciiSum = 0;   // asciiValue stores the ASCII value of the lower case chars,
                                        // asciiSum stores the sum of the ASCII values of all the lower case char in String

        char c;                         // c stores a char value from string before ASCII value is found

        inputString = inputString.toLowerCase();

        for (int i = 0; i < inputString.length(); i++) {
            c = inputString.charAt(i);
            asciiValue = (int) c;
            asciiSum += asciiValue;
        }
        return asciiSum;
    }


}
