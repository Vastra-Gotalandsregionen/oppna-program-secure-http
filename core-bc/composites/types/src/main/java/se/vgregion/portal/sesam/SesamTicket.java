package se.vgregion.portal.sesam;

import javax.xml.bind.annotation.XmlRootElement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Patrik Bergström
 */
@XmlRootElement
public class SesamTicket {

    private String pid;
    private String usr;
    private String logicaladdress;
    private String contactid;
    private String forskrivningsid;
    private String validTo;

    public SesamTicket() {
        this(new Date(System.currentTimeMillis() + 1000 * 60)); // Add 60 seconds
    }

    public SesamTicket(Date validTo) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); // ISO 8601
        df.setTimeZone(tz);

        String validToAsIso = df.format(validTo);

        this.validTo = validToAsIso;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        if (pid != null) {
            pid = pid.replace("-", "");
        }
        this.pid = pid;
    }

    public String getUsr() {
        return usr;
    }

    public void setUsr(String usr) {
        this.usr = usr;
    }

    public String getLogicaladdress() {
        return logicaladdress;
    }

    public void setLogicaladdress(String logicaladdress) {
        this.logicaladdress = logicaladdress;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public String getForskrivningsid() {
        return forskrivningsid;
    }

    public void setForskrivningsid(String forskrivningsid) {
        this.forskrivningsid = forskrivningsid;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }
}
