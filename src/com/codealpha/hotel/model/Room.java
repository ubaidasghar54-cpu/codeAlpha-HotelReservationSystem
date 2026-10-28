package com.codealpha.hotel.model;

public class Room {
    private String roomId;
    private String category; // Standard, Deluxe, Suite
    private double pricePerNight;
    private boolean available;

    public Room(String roomId, String category, double pricePerNight, boolean available) {
        this.roomId = roomId;
        this.category = category;
        this.pricePerNight = pricePerNight;
        this.available = available;
    }

    public String getRoomId() { return roomId; }
    public String getCategory() { return category; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public String toCSV() {
        return roomId + "," + category + "," + pricePerNight + "," + available;
    }

    public static Room fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length == 4) {
            return new Room(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()), Boolean.parseBoolean(parts[3].trim()));
        }
        return null;
    }
}