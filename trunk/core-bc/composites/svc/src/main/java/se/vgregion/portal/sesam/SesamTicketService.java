package se.vgregion.portal.sesam;

import org.bouncycastle.util.encoders.Base64Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.vgregion.crypto.xml.XmlSigner;
import se.vgregion.secure.util.KeystoreHelper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.util.Map;

/**
 * @author Patrik Bergström
 */
@Service
public class SesamTicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SesamTicketService.class);

    private KeystoreHelper keystoreHelper;
    private XmlSigner xmlSigner;

    @Autowired
    public SesamTicketService(KeystoreHelper keystoreHelper, XmlSigner xmlSigner) {
        this.keystoreHelper = keystoreHelper;
        this.xmlSigner = xmlSigner;
    }

    public SesamTicket toSesamTicket(Map<String, String[]> parameterMap) throws IllegalAccessException {
        SesamTicket sesamTicket = new SesamTicket();

        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            try {
                String key = stringEntry.getKey();
                String value = stringEntry.getValue()[0];
                String methodName = "set" + key.substring(0, 1).toUpperCase() + key.substring(1);
                Method field = SesamTicket.class.getDeclaredMethod(methodName, value.getClass());
                field.invoke(sesamTicket, value);
            } catch (NoSuchMethodException e) {
                // Ok, we only want fields which are declared in the class
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return sesamTicket;
    }

    public String toBase64SignedXml(SesamTicket sesamTicket) throws UnsupportedEncodingException {
        String xml;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(SesamTicket.class);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            jaxbContext.createMarshaller().marshal(sesamTicket, outputStream);
            xml = outputStream.toString("UTF-8");
            LOGGER.info("XML Sesam Ticket:\r\n" + xml);

            // Load the KeyStore and get the signing key and certificate.
            KeyStore.PrivateKeyEntry privateKeyEntry = keystoreHelper.getPrivateKeyEntry();

            String signedXml = xmlSigner.sign(xml, privateKeyEntry);

            LOGGER.info("Signed XML Sesam Ticket:\r\n" + signedXml);

            String base64SignedXml = toBase64(signedXml);

            return base64SignedXml;
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String toBase64(String string) throws IOException {
        Base64Encoder base64Encoder = new Base64Encoder();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        // Sesam requires ISO-8859-1.
        base64Encoder.encode(string.getBytes("ISO-8859-1"), 0, string.length(), out);

        return out.toString("UTF-8");
    }

    public void setKeystoreHelper(KeystoreHelper keystoreHelper) {
        this.keystoreHelper = keystoreHelper;
    }

    public void setXmlSigner(XmlSigner xmlSigner) {
        this.xmlSigner = xmlSigner;
    }
}
