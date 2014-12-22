package se.vgregion.portal.secure.controller;

import com.liferay.portal.util.Portal;
import com.liferay.portal.util.PortalUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import se.vgregion.portal.secure.controller.util.RequestUtil;
import se.vgregion.portal.sesam.SesamTicket;
import se.vgregion.portal.sesam.SesamTicketService;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Map;

/**
 * @author Patrik Bergström
 */
@Controller
@RequestMapping(value = "VIEW")
public class MeliorSesamController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeliorSesamController.class);

    private SesamTicketService ticketService;
    private Portal portal;
    private RequestUtil requestUtil = new RequestUtil();

    @Value("${sesam.target.order.url}")
    private String targetOrderUrl;

    @Value("${sesam.target.view.url}")
    private String targetViewUrl;

    @Autowired
    public MeliorSesamController(SesamTicketService ticketService) {
        this.ticketService = ticketService;
        this.portal = PortalUtil.getPortal();
    }

    @RenderMapping
    public String showForm(PortletRequest request, PortletResponse response, Model model) throws CertificateException, NoSuchAlgorithmException, IOException, KeyStoreException, UnrecoverableEntryException, IllegalAccessException {

        Map<String, String[]> parameterMap = request.getParameterMap();

        logRequest(parameterMap);

        // Some validation
        HttpServletRequest httpServletRequest = portal.getHttpServletRequest(request);
        if (!httpServletRequest.getMethod().equalsIgnoreCase("POST")) {
            LOGGER.error("Only POST requests are allowed.");
            model.addAttribute("errorMessage", "Anropet till denna vy var inte korrekt eller så har ett tekniskt fel uppstått.");
            return "form";
        }

        String ivUser = requestUtil.getIvUserFromRequestHeader(request);
        String userId = requestUtil.getUserId(request);
        String userFromMeliorRequest = request.getParameter("usr");
        LOGGER.info("IV-user: " + ivUser + ", userId: " + userId + ", userFromMeliorRequest: " + userFromMeliorRequest);
        if (!userId.equals(ivUser) || !userId.equals(userFromMeliorRequest)) {
            model.addAttribute("errorMessage", "Ogiltigt anrop.");
            LOGGER.error("Iv-user, logged in user and user from Melior request did not match.");
            return "form";
        }

        String requestType = request.getParameter("requestType");
        if (requestType == null) {
            LOGGER.error("requestType parameter was not provided.");
            model.addAttribute("errorMessage", "Anropet till denna vy var inte korrekt eller så har ett tekniskt fel uppstått.");
            return "form";
        }

        if (requestType.equals("order")) {
            model.addAttribute("targetUrl", targetOrderUrl);
        } else if (requestType.equals("view")) {
            model.addAttribute("targetUrl", targetViewUrl);
        } else {
            LOGGER.error("requestType parameter was not provided correctly.");
            model.addAttribute("errorMessage", "Anropet till denna vy var inte korrekt eller så har ett tekniskt fel uppstått.");
            return "form";
        }

        SesamTicket sesamTicket = ticketService.toSesamTicket(parameterMap);

        String base64SignedXml = ticketService.toBase64SignedXml(sesamTicket);

        model.addAttribute("base64SignedXml", base64SignedXml);

        return "form";
    }

    void logRequest(Map<String, String[]> parameterMap) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String[]> stringEntry : parameterMap.entrySet()) {
            sb.append(stringEntry.getKey() + "\r\n");
            for (String value : stringEntry.getValue()) {
                sb.append("\t" + value);
            }
            sb.append("\r\n");
        }
        LOGGER.info("Incoming parameters: \r\n" + sb.toString());
    }

    public void setPortal(Portal portal) {
        this.portal = portal;
    }

    public void setRequestUtil(RequestUtil requestUtil) {
        this.requestUtil = requestUtil;
    }

    public void setTicketService(SesamTicketService ticketService) {
        this.ticketService = ticketService;
    }
}

