package cfig.ota.launcher

import cfig.ota.Verify

/**
 * Created by yzyu on 2016/6/15.
 */
public class Launcher {
    private static final boolean bDebug = false;
    public static void main(String[] argv) {
        System.setProperty("line.separator", "\n");
        if (bDebug) println(String.format("[%02x]", (int) System.getProperty("line.separator").charAt(0)));
        Verify v = new Verify();
        v.verify();
    }
}
