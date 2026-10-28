package com.codealpha.hotel.model;

public class Reservation {
    private String bookingId;
    private String roomId;
    private String guestName;
    private String checkIn;
    private String checkOut;
    private double totalAmount;

    public Reservation(String bookingId, String roomId, String guestName, String checkIn, String checkOut, double totalAmount) {
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.guestName = guestName;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.totalAmount = totalAmount;
    }

    public String getBookingId() { return bookingId; }
    public String getRoomId() { return roomId; }
    public String getGuestName() { return guestName; }
    public String getCheckIn() { return checkIn; }
    public String getCheckOut() { return checkOut; }
    public double getTotalAmount() { return totalAmount; }

    public String toCSV() {
        return bookingId + "," + roomId + "," + guestName + "," + checkIn + "," + checkOut + "," + totalAmount;
    }

    public static Reservation fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 6) {
            return new Reservation(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim(), parts[4].trim(), Double.parseDouble(parts[5].trim()));
        }
        return null;
    }
}