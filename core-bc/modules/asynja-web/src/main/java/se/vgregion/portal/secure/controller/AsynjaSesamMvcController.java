package se.vgregion.portal.secure.controller;

import org.apache.log4j.lf5.util.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import se.vgregion.portal.sesam.SesamTicket;
import se.vgregion.portal.sesam.SesamTicketService;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;

import static se.vgregion.secure.util.HmacUtil.calculateHmac;

/**
 * @author Patrik Björk
 */
@Controller
@RequestMapping("/")
public class AsynjaSesamMvcController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AsynjaSesamMvcController.class);

    private SesamTicketService ticketService;

    @Value("${sesam.target.order.url}")
    private String targetOrderUrl;

    @Value("${sesam.target.view.url}")
    private String targetViewUrl;

    @Value("${asynja.hmac.key}")
    private String hmacKey;

    private static final SimpleDateFormat SDF_TIMESTAMP = new SimpleDateFormat("yyyyMMddHHmmss");
//    private static final SimpleDateFormat SDF_DATE = new SimpleDateFormat("yyyyMMdd");

    static {
        SDF_TIMESTAMP.setTimeZone(TimeZone.getTimeZone("Europe/Stockholm"));
        SDF_TIMESTAMP.setLenient(false);
//        SDF_DATE.setLenient(false);
    }

    @Autowired
    public AsynjaSesamMvcController(SesamTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @RequestMapping("/{requestType}")
    public String showForm(HttpServletRequest request, ServletResponse response,
                           @PathVariable("requestType") String requestType, Model model)
            throws Exception {

        if (requestType.toLowerCase().contains("favicon")) {
            return "form";
        }

        Map<String, String[]> parameterMap = request.getParameterMap();

        logRequest(parameterMap);

        if (!validate(request, requestType, model)) {
            return "form";
        }

        String hmacFromParameter = request.getParameter("hmac");

        String parameterStringWithoutHmac;
        String method = request.getMethod();

        if (method.equalsIgnoreCase("POST")) {
            byte[] postBodyArray = ((ContentCachingRequestWrapper) request).getContentAsByteArray();
            String postBody = new String(postBodyArray, StandardCharsets.UTF_8);
            parameterStringWithoutHmac = postBody.replaceAll(hmacFromParameter, "");
        } else if (method.equalsIgnoreCase("GET")) {
            parameterStringWithoutHmac = request.getQueryString().replaceAll(hmacFromParameter, "");
        } else {
            LOGGER.error("Only POST and GET requests are allowed.");
            model.addAttribute("errorMessage", "Ogiltigt anrop.");
            return "form";
        }

        String calculatedHmac = calculateHmac(parameterStringWithoutHmac, hmacKey);

        if (calculatedHmac.equals(hmacFromParameter)) {
            SesamTicket sesamTicket = ticketService.toSesamTicket(parameterMap);

            String base64SignedXml = ticketService.toBase64SignedXml(sesamTicket);

            model.addAttribute("base64SignedXml", base64SignedXml);
        } else {
            model.addAttribute("errorMessage", "Ogiltigt anrop.");
            LOGGER.error("Invalid HMAC");
        }

        return "form";
    }

    private boolean validate(HttpServletRequest request, @PathVariable("requestType") String requestType, Model model) {
        String ivUser = request.getHeader("iv-user");
        String userFromRequest = request.getParameter("usr");
        String date = request.getParameter("time");

        String hmac = request.getParameter("hmac");
        if (hmac == null) {
            model.addAttribute("errorMessage", "Ogiltigt anrop.");
            LOGGER.error("Missing HMAC.");
            return false;
        }

        if (ivUser == null || userFromRequest == null || date == null) {
            model.addAttribute("errorMessage", "Ogiltigt anrop.");
            LOGGER.error("All of iv-user, usr and time parameters must be set.");
            return false;
        }

        try {
            Date parsedDate = SDF_TIMESTAMP.parse(date);

            Instant instantFromParameter = parsedDate.toInstant();
            Instant sixtySecondsAgo = Instant.now().minusSeconds(60);
            Instant tenSecondsAfterNow = Instant.now().plusSeconds(10);
            if (instantFromParameter.isBefore(sixtySecondsAgo)) {
                model.addAttribute("errorMessage", "Ogiltigt anrop.");
                LOGGER.error("Request is too old.");
                return false;
            }
            if (instantFromParameter.isAfter(tenSecondsAfterNow)) {
                model.addAttribute("errorMessage", "Ogiltigt anrop.");
                LOGGER.error("Request time is too much after the current time..");
                return false;
            }
        } catch (ParseException e) {
            throw new IllegalArgumentException("Illegal date format.");
        }

        LOGGER.info("IV-user: " + ivUser + ", userFromMeliorRequest: " + userFromRequest);

        if (!userFromRequest.equalsIgnoreCase(ivUser)) {
            model.addAttribute("errorMessage", "Ogiltigt anrop.");
            LOGGER.error("Iv-user and user from Melior request did not match.");
            return false;
        }

        if (requestType == null) {
            LOGGER.error("requestType parameter was not provided.");
            model.addAttribute("errorMessage", "Anropet till denna vy var inte korrekt eller så har ett tekniskt fel uppstått.");
            return false;
        }

        if (requestType.equals("order")) {
            model.addAttribute("targetUrl", targetOrderUrl);
        } else if (requestType.equals("view")) {
            model.addAttribute("targetUrl", targetViewUrl);
        } else {
            LOGGER.error("requestType parameter was not provided correctly.");
            model.addAttribute("errorMessage", "Anropet till denna vy var inte korrekt eller så har ett tekniskt fel uppstått.");
            return false;
        }
        return true;
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
