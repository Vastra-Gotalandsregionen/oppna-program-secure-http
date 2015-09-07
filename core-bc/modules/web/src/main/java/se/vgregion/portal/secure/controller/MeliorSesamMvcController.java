package se.vgregion.portal.secure.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import se.vgregion.portal.sesam.SesamTicket;
import se.vgregion.portal.sesam.SesamTicketService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Patrik Björk
 */
@Controller
@RequestMapping("/")
public class MeliorSesamMvcController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MeliorSesamMvcController.class);

    private SesamTicketService ticketService;

    @Value("${sesam.target.order.url}")
    private String targetOrderUrl;

    @Value("${sesam.target.view.url}")
    private String targetViewUrl;

    @Autowired
    public MeliorSesamMvcController(SesamTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @RequestMapping("/{requestType}")
    public String showForm(HttpServletRequest request, ServletResponse response,
                           @PathVariable("requestType") String requestType, Model model)
            throws Exception {

        Map<String, String[]> parameterMap = request.getParameterMap();

        logRequest(parameterMap);

        // Some validation
        if (!request.getMethod().equalsIgnoreCase("POST")) {
            LOGGER.error("Only POST requests are allowed.");
            model.addAttribute("errorMessage", "Anropet till denna vy var inte korrekt eller så har ett tekniskt fel uppstått.");
            return "form";
        }

        String ivUser = request.getHeader("iv-user");
        String userFromMeliorRequest = request.getParameter("usr");

        LOGGER.info("IV-user: " + ivUser + ", userFromMeliorRequest: " + userFromMeliorRequest);

        if (!userFromMeliorRequest.equals(ivUser)) {
            model.addAttribute("errorMessage", "Ogiltigt anrop.");
            LOGGER.error("Iv-user and user from Melior request did not match.");
            return "form";
        }

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

    public void setTicketService(SesamTicketService ticketService) {
        this.ticketService = ticketService;
    }
}
