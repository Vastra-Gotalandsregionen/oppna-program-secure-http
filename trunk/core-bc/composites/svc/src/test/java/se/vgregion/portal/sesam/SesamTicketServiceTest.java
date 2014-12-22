package se.vgregion.portal.sesam;

import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Patrik Bergström
 */

public class SesamTicketServiceTest {

    private SesamTicketService sesamTicketService = new SesamTicketService(null, null);

    @Test
    public void testToBase64() throws Exception {

        String string = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><sesamTicket><contactid>asdf</contactid><logicaladdress>3010|jort|ÖOMOTT</logicaladdress><pid>20101010-1010</pid><usr> eeeas3</usr><validTo>2014-03-25T10:19:51+0000</validTo><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"\"><Transforms><Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\"/></Transforms><DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\"/><DigestValue>Xrm1Q/BfACFQTvwAV6fVL5bME3s=</DigestValue></Reference></SignedInfo><SignatureValue>i7XUtZ74lKC9E0mVcKfRhTlcTgznr2z5Gb768AyBrYhTZYWwBlsE5M4XsViDhUTYv0pGSpWavqPh5T3A9Co6cYGlDxWFG1A7SXV8/PxS4uIezOgGuwj17g411ZkjFWC97BrFkBUVmPgNjLA+Au8Rk7TLZxFQf9K7YSRpN6EQ5+5xZ4QDUGB5dX1O6RmDb5WCRIXzEEPZWyu9RC/8kegyOgEzSFUatqjgZxFQf9K7YSRpN6EQ5+elCS0Gufcsyt2A52ouNnwr8bWQlfSZh6PLzPt+ibscsgUaeplbdoEJVjdU97BK2BmzwSLZx9fEV/0Gufcsyt2A52ouNnwr8bWQlfSZh6PLzPt+i7BI751/BrBAwgBUkqQKe6ju3wa8YYPpYBms9jbRlw==</SignatureValue><KeyInfo><X509Data><X509SubjectName>CN=test,OU=test,O=test,L=test,ST=test,C=SE</X509SubjectName><X509Certificate>MIIDLDCCAhSgAwIBAgIEUyL8YDANBgkqhkiG9w0BAQUFADBYMQswCQYDVQQGEwJTRTENMAsGA1UECBMEdGVzdDENMAsGA1UEBxMEdGVzdDENMAsGA1UEChMEdGVzdDENMAsGA1UECxMEdGVzdDENMAsGA1UEAxMEdGVzdDAeFw0xNDAzMTQxMjU2MDBaFw0yNDAzMTExMjU2MDBaMFgxCzAJBgNVBAYTAlNFMQ0wCwYDVQQIEwR0ZXN0MQ0wCwYDVQQHEwR0ZXN0MQ0wCwYDVQQKEwR0ZXN0MQ0wCwYDVQQLEwR0ZXN0MQ0wCwYDVQQDEwR0ZXN0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoFzs303o3dBpdp8GkgBVHEbwlWSp15VT0jMVgGqCaoy/NeOUYxNkCR5iLtoqspwDulNnJCW6tLqY+h4dq0Ez2AxRb3Sqf5Nt7uPCgpibrwfO3+5wwSIur4cbc9TUFJDfdfxBgFV5PQPc/bMW8laHkYFqJ9gU2AxRb3Sqf5Nt7uPCgpibrwfO3+zHuJRQtcLO6csBQnjxtl650DCg3NSSHjgq+wRCnBImBTUvR2PqPTTMR9hUd+nYEtHOGBrXwqdUb7RQtcLO6csBQnjxtl650DCg3NSSHjgq+wRCnBImBTUvR2PqPTTMR9hUd+0VECdOGer94xsPDzkWho4Q6NHtOCSjz0gMkx/Q6MchtV39lk/rzXwuIptcMro4or/PRu+nae26lUeliw2jWPashCkZOiYuB25jzuFK+LS/yp6wIDAQABMA0GCSqGSIb3DQEBBQUAA4IBAQBb3mhLaD/O2jWPashCkZOiYuB25jzuFK+sIj5P/1DtH2M088p60v8O8IGlFKr1NGVWWvXKabiQuqNhlzzGHuCRtiwfwdsCdd5E2MEiG4LEax8DuUW6Jx2pXyQgtsn/DUGRQp26+NIq/H6yCnG+scxKMdya8e826M9WFHY6PWmRjeBQjNVsjpWlw978/h7k6cwT8xFm8742DRs/pWFNyX9C+4mYTU/62PUu5CfYyKsDH4dLTZ8p9iPLOTIPvFND6iCitkgLrnk1aMZZEmjt6gZ/ExMKHSxa/JVVOXY+i+v9CQatnIXQOMx8ARa9szdSX49L0Gy587TbdmcmpA3oM3ut+TofTfRVngeRw/sdnULZd47</X509Certificate></X509Data></KeyInfo></Signatut+re></sesamTicket>";
        String base64 = sesamTicketService.toBase64(string);

        System.out.println(base64);

        byte[] decoded = Base64.decode(base64);

        System.out.println(new String(decoded, "UTF-8"));

        assertEquals(string, new String(decoded, "UTF-8"));
    }

    @Test
    public void testToSesamTicket() throws Exception {
        HashMap<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("contactid", new String[]{"theContactId"});
        parameterMap.put("logicaladdress", new String[]{"theLogicalAddress"});
        parameterMap.put("pid", new String[]{"19121212-1212"});
        parameterMap.put("usr", new String[]{"theUsr"});
        parameterMap.put("forskrivningsid", new String[]{"theForskrivningsid"});

        SesamTicket sesamTicket = sesamTicketService.toSesamTicket(parameterMap);

        assertEquals("theContactId", sesamTicket.getContactid());
        assertEquals("theLogicalAddress", sesamTicket.getLogicaladdress());
        assertEquals("191212121212", sesamTicket.getPid()); // Notice the removal of the dash
        assertEquals("theUsr", sesamTicket.getUsr());
        assertEquals("theForskrivningsid", sesamTicket.getForskrivningsid());
        assertNotNull(sesamTicket.getValidTo());
    }
}
