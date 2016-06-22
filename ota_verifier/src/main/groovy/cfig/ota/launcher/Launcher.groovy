package cfig.ota.launcher

import cfig.ota.Verify

/**
 * Created by yzyu on 2016/6/15.
 */
public class Launcher {
    private static final boolean bDebug = false;
    public static void main(String[] argv) {
        String device = null;
        String pkg = null;
        if (1 == argv.length) {
            pkg = argv[0];
        } else if (2 == argv.length) {
            device = argv[0];
            pkg = argv[1];
        } else {
            printHelp();
        }
        System.setProperty("line.separator", "\n");
        if (bDebug) println(String.format("[%02x]", (int) System.getProperty("line.separator").charAt(0)));
        new Verify().run(device, pkg);
    }
    public static void printHelp() {
        println("Usage: verify <device_id> <ota_package_path>");
        System.exit(1);
    }
}
