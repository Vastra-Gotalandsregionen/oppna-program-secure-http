package se.vgregion.secure.util;

import se.vgregion.certificate.PkixUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStore;

/**
 * @author Patrik Bergström
 */
public class KeystoreHelper {

    private String storetype;

    private File keystoreFile;

    private String alias;

    private String password;

    public KeystoreHelper(String storetype, File keystoreFile, String alias, String password) {
        this.storetype = storetype;
        this.keystoreFile = keystoreFile;
        this.alias = alias;
        this.password = password;
    }

    public KeyStore.PrivateKeyEntry getPrivateKeyEntry() {
        FileInputStream keystoreInput = null;
        try {
            keystoreInput = new FileInputStream(keystoreFile);
            return PkixUtil.getPrivateKeyEntry(keystoreInput, storetype, alias, password);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (keystoreInput != null) {
                    keystoreInput.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
