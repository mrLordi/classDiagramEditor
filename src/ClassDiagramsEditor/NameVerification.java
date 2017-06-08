package ClassDiagramsEditor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameVerification {
    private static Pattern pattern = Pattern.compile("^[a-zA-Z_][([a-zA-Z_]+)(\\d?)]*$");;
    public static boolean checkWithRegExp(String str){
        Matcher m = pattern.matcher(str);
        return m.matches();
    }
}
