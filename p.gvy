import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AnsiColor {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";
    private static boolean consoleMode = true;

    public AnsiColor(boolean consoleMode) {
        this.consoleMode = consoleMode;
    }

    public AnsiColor() {
        this.consoleMode = true;
    }

    public String nativeTest2() {
        return "a1700b7c-c321-11e4-92f1-0021ccc95a85";
    }

    public String RESET() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_RESET;
        }
    }

    public String BLACK() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_BLACK;
        }
    }

    public String RED() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_RED;
        }
    }

    public String GREEN() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_GREEN;
        }
    }

    public String YELLOW() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_YELLOW;
        }
    }

    public String BLUE() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_BLUE;
        }
    }

    public String PURPLE() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_PURPLE;
        }
    }

    public String CYAN() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_CYAN;
        }
    }

    public String WHITE() {
        if (!consoleMode) {
            return "";
        } else {
            return ANSI_WHITE;
        }
    }
}

void match_buffer(Pattern p, BufferedReader br, String bufferName, Map<String, String> inList) {
    AnsiColor theColor = new AnsiColor();
    try {
        String line = null;
        int lineNo = 0;
        while ((line = br.readLine()) != null) {
            lineNo++;
            Matcher m = p.matcher(line);
            String txt_head = (false ? theColor.PURPLE() + bufferName + theColor.RESET() + " +" : "") + theColor.GREEN() + lineNo + theColor.RESET();
            String txt = "";
            int s1 = 0;
            while (m.find()) {//generate a user friendly output text
                //check if a file is binary data
                if (true) {
                    txt_head += "[" + m.start() + "," + m.end() + ")";
                }
                txt += line.substring(s1, m.start());
                txt += theColor.CYAN();
                txt += line.substring(m.start(), m.end());
                txt += theColor.RESET();
                s1 = m.end();

                if (true) {
                    for (int i = 0; i < m.groupCount(); i++) {
                        println("    [" + i + "]" + theColor.CYAN() + m.group(i + 1) + theColor.RESET());
                    }
                    if (null != inList) {
                        inList.put(m.group(1), m.group(2));
                    }
                }
            }

            //output
            if (!txt.isEmpty()) {
                txt += line.substring(s1);
                println(txt_head + ":" + txt);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

File src = new File("script");
Map<String, String> updateList = new HashMap<String, String>();
BufferedReader br = new BufferedReader(new FileReader(src.getPath()));
Pattern p;
String regex = "package_extract_file\\(\\s*\"(.+)\"\\s*,\\s*\"(/dev.+)\"\\s*\\)\\s*;";
p = Pattern.compile(regex);
match_buffer(p, br, src.getPath(), updateList);
for (Map.Entry<String, String> entry : updateList.entrySet()) {
    println(entry.getKey() + " -> " + entry.getValue());
}
