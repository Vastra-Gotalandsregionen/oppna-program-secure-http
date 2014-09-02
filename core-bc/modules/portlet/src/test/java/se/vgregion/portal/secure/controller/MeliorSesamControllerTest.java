package se.vgregion.portal.secure.controller;

import com.liferay.portal.util.Portal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.portlet.MockPortletRequest;
import org.springframework.mock.web.portlet.MockPortletResponse;
import org.springframework.ui.ExtendedModelMap;
import se.vgregion.crypto.xml.XmlSigner;
import se.vgregion.portal.secure.controller.util.RequestUtil;
import se.vgregion.portal.sesam.SesamTicketService;
import se.vgregion.secure.util.KeystoreHelper;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Patrik Bergström
 */
@RunWith(MockitoJUnitRunner.class)
public class MeliorSesamControllerTest {

    private XmlSigner xmlSigner = mock(XmlSigner.class);
    private KeystoreHelper keystoreHelper = mock(KeystoreHelper.class);
    private RequestUtil requestUtil = mock(RequestUtil.class);

    private SesamTicketService ticketService = new SesamTicketService(keystoreHelper, xmlSigner);

    private MeliorSesamController meliorSesamController = new MeliorSesamController(ticketService);

    @Before
    public void setup() {
        meliorSesamController.setRequestUtil(requestUtil);
    }

    @Test
    public void testShowForm() throws Exception {

        // Given
        String mySignedXml = "my signed xml...";

        Portal portal = mock(Portal.class);
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);

        when(servletRequest.getMethod()).thenReturn("POST");
        when(portal.getHttpServletRequest(any(PortletRequest.class))).thenReturn(servletRequest);
        when(requestUtil.getIvUserFromRequestHeader(any(PortletRequest.class))).thenReturn("vgrid1");
        when(requestUtil.getUserId(any(PortletRequest.class))).thenReturn("vgrid1");
        when(xmlSigner.sign(anyString(), any(KeyStore.PrivateKeyEntry.class))).thenReturn(mySignedXml);

        meliorSesamController.setPortal(portal);

        MockPortletRequest request = new MockPortletRequest();
        request.setParameter("usr", "vgrid1");
        request.setParameter("requestType", "order");

        ExtendedModelMap model = new ExtendedModelMap();

        // When
        meliorSesamController.showForm(request, new MockPortletResponse(), model);

        // Verify
        assertEquals(ticketService.toBase64(mySignedXml), model.get("base64SignedXml"));
    }

    @Test
    public void testLog() {
        Map<String, String[]> map = new HashMap<String, String[]>();

        map.put("key1", new String[]{"v1", "v2"});

        System.out.println(map);

        meliorSesamController.logRequest(map);
    }
}
