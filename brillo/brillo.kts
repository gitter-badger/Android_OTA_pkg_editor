// Generate an Android OTA package that has A/B update payload
import org.apache.commons.exec.*
import java.io.BufferedWriter
import java.io.FileWriter
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun Run(cmd: String) {
    println("> " + cmd)
    println(">")
    DefaultExecutor().execute(CommandLine.parse(cmd.replace("\n", " ").replace("    ","")))
}

fun genPayload() {
    val unsigned_payload = "generated_payload.bin"
    val signed_payload = "signed_payload.bin"
    val tgt_file = "target.zip"
    val meta_hash = "meta_hash.bin"
    val meta_hash_sig = "meta_hash_sig.bin"
    val payload_hash = "payload_hash.bin"
    val payload_hash_sig = "payload_hash_sig.bin"
    val testKey = "build/target/product/security/testkey.pk8"
    val pem_key = "testkey.pem.key"
    val payload_prop = "payload_properties.txt"

    // A/B updater expects a signing key in RSA format. Gets the key ready for
    // later use in step 3, unless a payload_signer has been specified.
    Run("openssl pkcs8 -in $testKey -inform DER -nocrypt -out $pem_key")

    //1. Generate payload.
    Run("""brillo_update_payload generate
    --payload $unsigned_payload
    --target_image $tgt_file
    --max_timestamp 1521096611""")

    //2. Generate hashes of the payload and metadata files.
    Run("""brillo_update_payload hash
    --unsigned_payload $unsigned_payload
    --signature_size 256
    --metadata_hash_file $meta_hash
    --payload_hash_file $payload_hash""")

    //3. Sign the hashes and insert them back into the payload file.
    //3a. Sign the payload hash.
    Run("""openssl pkeyutl -sign
    -inkey $pem_key
    -pkeyopt digest:sha256
    -in $payload_hash
    -out $payload_hash_sig""")

    //3b. Sign the metadata hash.
    Run("""openssl pkeyutl -sign
    -inkey $pem_key
    -pkeyopt digest:sha256
    -in $meta_hash
    -out $meta_hash_sig""")

    //3c. Insert the signatures back into the payload file.
    Run("""brillo_update_payload sign
    --unsigned_payload $unsigned_payload
    --payload $signed_payload
    --signature_size 256
    --metadata_signature_file $meta_hash_sig
    --payload_signature_file $payload_hash_sig""")

    //4. Dump the signed payload properties.
    Run("""brillo_update_payload properties
    --payload $signed_payload
    --properties_file $payload_prop""")
    //openssl pkcs8 -in build/target/product/security/testkey.pk8 -inform DER -nocrypt
}

fun ZipIt(src: String, sink: String, name: String) {
    val zos: ZipOutputStream = ZipOutputStream(FileOutputStream(File(sink)))
    zos.putNextEntry(ZipEntry(name))
    val data = File(src).readBytes()
    zos.write(data, 0, data.size)
    zos.closeEntry()
    zos.close()
}

//genPayload()
ZipIt("1.mk", "1.zip", "good/day/file")
