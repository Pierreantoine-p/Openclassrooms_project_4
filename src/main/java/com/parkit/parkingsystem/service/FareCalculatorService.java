package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

import java.util.Date;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        //TODO: Some tests are failing here. Need to check if this logic is correct

        Date inHour = ticket.getInTime();
        Date outHour = ticket.getOutTime();
        double discount = 0.95;


        long inTimeInMilliseconds = inHour.getTime();
        long outTimeInMilliseconds = outHour.getTime();

        double durationInMilliseconds = outTimeInMilliseconds - inTimeInMilliseconds;
        double durationInHours =  (durationInMilliseconds / (1000d * 60d * 60d));

        if(durationInHours <= 0.5 ){
            durationInHours = 0;
        }

        boolean userRecurrent = ticket.getRecurrent();

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(durationInHours * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(durationInHours * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
        if (userRecurrent) {
            double price = Math.round(ticket.getPrice() * discount * 100.0) / 100.0;
            ticket.setPrice(price);
        }
    }
}