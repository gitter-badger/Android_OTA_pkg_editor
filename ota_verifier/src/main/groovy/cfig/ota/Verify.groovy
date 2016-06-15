package cfig.ota

import java.security.MessageDigest
import java.util.zip.ZipEntry
import java.util.zip.ZipFile

/**
 * Created by yzyu on 2016/6/15.
 */
class Verify {
    void Run(List<String> inCmd, String inWorkdir = null) {
        println("CMD:" + inCmd)
        if (inWorkdir == null) {
            inWorkdir = ".";
        }
        ProcessBuilder pb = new ProcessBuilder(inCmd)
                .directory(new File(inWorkdir))
                .redirectErrorStream(true);
        Process p = pb.start()
        p.inputStream.eachLine {println it}
        p.waitFor();
        assert 0 == p.exitValue()
    }

    void Run(String inCmd, String inWorkdir = null) {
        Run(Arrays.asList(inCmd.split()), inWorkdir);
    }

    public Object[] getImgInfo(File zipFIle, String name) {
        Object[] ret = new Object[3];
        MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
        ZipFile zf = new java.util.zip.ZipFile(zipFIle)
        ZipEntry entry = zf.getEntry(name)
        InputStream is = zf.getInputStream(entry)
        final byte[] buffer = new byte[1024];
        for (int read = 0; (read = is.read(buffer)) != -1;) {
            messageDigest.update(buffer, 0, read);
        }
        Formatter formatter = new Formatter();
        for (final byte b : messageDigest.digest()) {
            formatter.format("%02x", b);
        }
        ret[0] = name.replaceAll("\\.", "_");
        ret[1] = entry.getSize()
        ret[2] = (formatter.toString())
        return ret;
    }

    public void writeTestScript(String signedZip, String scriptName) {
        PrintWriter pw = new PrintWriter(new File(scriptName));
        pw.println("Good Day!");
        pw.close();
    }
    public void writeVerifyScript(String signedZip, String scriptName) {
        String scriptTemplate = "if ! applypatch -c EMMC:%s:%d:%s >/dev/null ; then\n\
\t%s=\" X \"\n\
\tresult=\"\${result}1\"\n\
else\n\
\t%s=\" - \"\n\
\tresult=\"\${result}0\"\n\
fi";

        PrintWriter pw = new PrintWriter(new File(scriptName));
        Object[] info2 = getImgInfo(new File(signedZip), "boot.img");
        pw.println("#!/system/bin/sh")
        pw.println("result=\"\"")
        pw.println(String.format(scriptTemplate,
                "/dev/block/by-name/boot",
                info2[1], info2[2], info2[0], info2[0]));

        Object[] info3 = getImgInfo(new File(signedZip), "fastlogo.img");
        pw.println(String.format(scriptTemplate,
                "/dev/block/by-name/fastlogo",
                info3[1], info3[2], info3[0], info3[0]));

        Object[] info1 = getImgInfo(new File(signedZip), "tzk_normal.img");
        pw.println(String.format(scriptTemplate,
                "/dev/block/by-name/tzk_normal",
                info1[1], info1[2], info1[0], info1[0]));

        Object[] info4 = getImgInfo(new File(signedZip), "bl_normal.img");
        pw.println(String.format(scriptTemplate,
                "/dev/block/by-name/bl_normal",
                info4[1], info4[2], info4[0], info4[0]));

        pw.println("echo")
        pw.println('print "boot.img       : ${boot_img}"')
        pw.println('print "fastlogo.img   : ${fastlogo_img}"')
        pw.println('print "tzk_normal.img : ${tzk_normal_img}"')
        pw.println('print "bl_normal.img  : ${bl_normal_img}"')
        pw.println('echo "\$result"');
        pw.println('echo "\$result" > /cache/ota_result')
        pw.close();
    }

    public void verify() {
        String signedZip = "signed.zip"
        String scriptName = "verify.sh"
        if (null != System.getProperty("zip")) {
            signedZip = System.getProperty("zip")
        }
        Run("adb shell getprop ro.build.fingerprint");
        writeTestScript(signedZip, scriptName)
        Run("adb push " + scriptName + " /cache")
        writeVerifyScript(signedZip, scriptName)
        Run("adb push " + scriptName + " /cache")
        Run("adb shell sh /cache/" + scriptName)
        Run("adb pull /cache/ota_result")
        Run("adb shell rm -f /cache/" + scriptName)
        Run("adb shell rm -f /cache/ota_result")
        new File(scriptName).delete();
    }
}
