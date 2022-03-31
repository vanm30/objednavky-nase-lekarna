package cz.naseLekarna.system;

import javafx.scene.control.Label;

import java.util.ArrayList;

/**
 * @author Matěj Vaník
 * @created 06.03.2022
 */
public class Validator {


    /**
     * Method checks if String is number.
     *
     * @param string Any string.
     * @return True if is number, false if not.
     */
    public static boolean isNumeric(String string) {
        int intValue;

        if (string == null || string.equals("")) {
            return false;
        }

        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;
    }

    /**
     * Method checks if String is Alphanumeric.
     *
     * @param string Any string
     * @return True if is alphanumeric, false if not.
     */
    public static boolean isAlphaNumeric(String string) {
        return string != null && string.matches("^[a-žA-Ž0-9]*$");
    }

    /**
     * Method checks if String is Alphanumeric and can contain white space.
     *
     * @param string Any string
     * @return True if is alphanumeric, false if not.
     */
    public static boolean isAlphaNumericWithSpace(String string) {
        return string != null && string.matches("^[a-žA-Ž0-9 ]*$");
    }

}
