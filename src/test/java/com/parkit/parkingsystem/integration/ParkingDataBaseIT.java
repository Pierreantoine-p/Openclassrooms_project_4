package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    @BeforeAll
    public static void setUp() throws Exception{
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    @BeforeEach
    public void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @AfterAll
    public static void tearDown(){

    }

    @Test
    public void testParkingACar(){
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        //TODO: check that a ticket is actualy saved in DB and Parking table is updated with availability
        Ticket ticket = new Ticket();
        Date inTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);

        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCD");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setRecurrent(true);
        ticketDAO.saveTicket(ticket);


        Ticket ticket1 = ticketDAO.getTicket("ABCD");
        LocalDateTime ldt = LocalDateTime.ofInstant(inTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime ldt1 = LocalDateTime.ofInstant(ticket1.getInTime().toInstant(), ZoneId.systemDefault());

        assertEquals(ticket.getParkingSpot().getId(), ticket1.getParkingSpot().getId());
        assertEquals(ticket.getVehicleRegNumber(), ticket1.getVehicleRegNumber());
        assertEquals(ticket.getPrice(), ticket1.getPrice() );
        assertEquals(ldt.getYear(), ldt1.getYear());
        assertEquals(ldt.getMonth(), ldt1.getMonth());
        assertEquals(ldt.getDayOfMonth(), ldt1.getDayOfMonth());
        assertEquals(ldt.getHour(), ldt1.getHour());
        assertEquals(ldt.getMinute(), ldt1.getMinute());
        assertEquals(ticket.getOutTime(), ticket1.getOutTime() );
        assertEquals(ticket.getRecurrent(), ticket1.getRecurrent() );

    }

    @Test
    public void testParkingLotExit(){
        testParkingACar();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        //TODO: check that the fare generated and out time are populated correctly in the database

        Ticket ticket = new Ticket();
        Date inTime = new Date();
        ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE,false);


        ticket.setParkingSpot(parkingSpot);
        ticket.setVehicleRegNumber("ABCD");
        ticket.setPrice(0);
        ticket.setInTime(inTime);
        ticket.setOutTime(null);
        ticket.setRecurrent(true);
        ticketDAO.saveTicket(ticket);

        Ticket ticket1 = ticketDAO.getTicket("ABCD");
        Date outTime = new Date();
        ticket1.setOutTime(outTime);
        ticket1.setPrice(1.5);
        ticketDAO.updateTicket(ticket1);

        Ticket ticket2 = ticketDAO.getTicket("ABCD");
        LocalDateTime ldt = LocalDateTime.ofInstant(outTime.toInstant(), ZoneId.systemDefault());
        LocalDateTime ldt1 = LocalDateTime.ofInstant(ticket2.getOutTime().toInstant(), ZoneId.systemDefault());

        assertEquals(ticket1.getPrice(), ticket2.getPrice() );
        assertEquals(ldt.getYear(), ldt1.getYear());
        assertEquals(ldt.getMonth(), ldt1.getMonth());
        assertEquals(ldt.getDayOfMonth(), ldt1.getDayOfMonth());
        assertEquals(ldt.getHour(), ldt1.getHour());
        assertEquals(ldt.getMinute(), ldt1.getMinute());

    }


}

