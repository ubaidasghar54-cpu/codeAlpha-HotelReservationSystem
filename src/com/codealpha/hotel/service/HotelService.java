package com.codealpha.hotel.service;

import com.codealpha.hotel.model.Reservation;
import com.codealpha.hotel.model.Room;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HotelService {
    private List<Room> rooms;
    private List<Reservation> reservations;
    private final String ROOMS_FILE = "data/rooms.csv";
    private final String BOOKINGS_FILE = "data/bookings.csv";

    public HotelService() {
        rooms = new ArrayList<>();
        reservations = new ArrayList<>();
        initializeDataFiles();
        loadRooms();
        loadReservations();
    }

    private void initializeDataFiles() {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();

        File rFile = new File(ROOMS_FILE);
        if (!rFile.exists()) {
            try (PrintWriter pw = new PrintWriter(rFile)) {
                pw.println("101,Standard,80.0,true");
                pw.println("102,Standard,80.0,true");
                pw.println("201,Deluxe,150.0,true");
                pw.println("202,Deluxe,150.0,true");
                pw.println("301,Suite,250.0,true");
                pw.println("302,Suite,250.0,true");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File bFile = new File(BOOKINGS_FILE);
        if (!bFile.exists()) {
            try { bFile.createNewFile(); } catch (IOException e) { e.printStackTrace(); }
        }
    }

    public void loadRooms() {
        rooms.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(ROOMS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Room r = Room.fromCSV(line);
                if (r != null) rooms.add(r);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRooms() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ROOMS_FILE))) {
            for (Room r : rooms) {
                pw.println(r.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadReservations() {
        reservations.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(BOOKINGS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                Reservation res = Reservation.fromCSV(line);
                if (res != null) reservations.add(res);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveReservations() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(BOOKINGS_FILE))) {
            for (Reservation res : reservations) {
                pw.println(res.toCSV());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Room> getRooms() { return rooms; }
    public List<Reservation> getReservations() { return reservations; }

    public boolean bookRoom(String roomId, String guestName, String checkIn, String checkOut, double amount) {
        for (Room r : rooms) {
            if (r.getRoomId().equals(roomId) && r.isAvailable()) {
                r.setAvailable(false);
                saveRooms();
                String bookingId = "BK-" + System.currentTimeMillis();
                Reservation res = new Reservation(bookingId, roomId, guestName, checkIn, checkOut, amount);
                reservations.add(res);
                saveReservations();
                return true;
            }
        }
        return false;
    }

    public boolean cancelReservation(String bookingId) {
        Reservation target = null;
        for (Reservation res : reservations) {
            if (res.getBookingId().equals(bookingId)) {
                target = res;
                break;
            }
        }

        if (target != null) {
            reservations.remove(target);
            saveReservations();
            for (Room r : rooms) {
                if (r.getRoomId().equals(target.getRoomId())) {
                    r.setAvailable(true);
                    saveRooms();
                    break;
                }
            }
            return true;
        }
        return false;
    }
}