package se.vgregion.secure.util;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.security.KeyStore;

/**
 * @author Patrik Bergström
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:keystore-helper-context.xml")
public class KeystoreHelperTest extends TestCase {

    @Autowired
    private KeystoreHelper keystoreHelper;

    @Test
    public void testGetPrivateKey() throws Exception {
        KeyStore.PrivateKeyEntry privateKey = keystoreHelper.getPrivateKeyEntry();

        assertNotNull(privateKey);
    }

}
