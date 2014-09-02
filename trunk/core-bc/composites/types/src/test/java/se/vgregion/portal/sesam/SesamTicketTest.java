package se.vgregion.portal.sesam;

import org.junit.Test;

import static org.junit.Assert.*;

public class SesamTicketTest {

    @Test
    public void testSetPid() throws Exception {
        SesamTicket ticket = new SesamTicket();

        ticket.setPid("19121212-1212");

        assertEquals("191212121212", ticket.getPid());
    }
}