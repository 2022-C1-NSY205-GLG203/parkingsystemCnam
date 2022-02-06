package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long in = ticket.getInTime().getTime();
        long out = ticket.getOutTime().getTime();

        //TODO: Some tests are failing here. Need to check if this logic is correct
        long duration =  (out - in) / 60000;
        int durationInHours = (int)duration/60;

        double extraFare = 1.0;

        if (duration < 30) {
            extraFare = 0.0;
        } else if (duration < 60) {
            extraFare = 0.75;
            durationInHours = 1;
        }

        double reccurentReduction = ticket.isRecurrent() ? 0.95 : 1.0;


        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {
                ticket.setPrice(reccurentReduction * extraFare * durationInHours * Fare.CAR_RATE_PER_HOUR);
                break;
            }
            case BIKE: {
                ticket.setPrice(reccurentReduction * extraFare * durationInHours * Fare.BIKE_RATE_PER_HOUR);
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }
}
