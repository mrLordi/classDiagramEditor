package ClassDiagramsEditor;

public class ReservedNames {

    private static String[] reservedName = {"if", "else", "switch", "case", "default", "while",
                                            "do", "break", "continue", "for", "try", "catch", "finally",
                                            "throw", "throws", "private", "protected", "public", "package",
                                            "import", "class", "interface", "extends", "implements", "static",
                                            "final", "abstract", "native", "new", "return",
                                            "this", "super", "synchronized", "volatile", "const", "goto",
                                            "instanceof", "enum", "assert", "transient", "strictfp"};

    public static boolean check(String str) {
        for (String s : reservedName) {
            if (s.equals(str)) {
                return false;
            }
        }
        return true;
    }
}
