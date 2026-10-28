package com.codealpha.hotel.ui;

import com.codealpha.hotel.model.Reservation;
import com.codealpha.hotel.model.Room;
import com.codealpha.hotel.service.HotelService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HotelGUI extends JFrame {
    private HotelService hotelService;
    private JTable roomTable, bookingTable;
    private DefaultTableModel roomTableModel, bookingTableModel;

    public HotelGUI() {
        hotelService = new HotelService();

        setTitle("LuxeStay - Hotel Reservation & Management System");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initComponents();
        refreshData();
    }

    private void initComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // 1. Rooms & Booking Tab
        JPanel bookingPanel = new JPanel(new BorderLayout(10, 10));
        bookingPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        bookingPanel.setBackground(new Color(245, 247, 250));

        String[] roomColumns = {"Room ID", "Category", "Price / Night ($)", "Availability"};
        roomTableModel = new DefaultTableModel(roomColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        roomTable = new JTable(roomTableModel);
        roomTable.setRowHeight(25);
        bookingPanel.add(new JScrollPane(roomTable), BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        actionPanel.setBackground(new Color(245, 247, 250));

        AnimatedButton bookBtn = new AnimatedButton("Book Selected Room", new Color(37, 99, 235), new Color(29, 78, 216));
        bookBtn.addActionListener(e -> openBookingDialog());
        actionPanel.add(bookBtn);

        bookingPanel.add(actionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Available Rooms & Booking", bookingPanel);

        // 2. Manage / View Reservations Tab
        JPanel managePanel = new JPanel(new BorderLayout(10, 10));
        managePanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        managePanel.setBackground(new Color(245, 247, 250));

        String[] bookingColumns = {"Booking ID", "Room ID", "Guest Name", "Check-In", "Check-Out", "Total ($)"};
        bookingTableModel = new DefaultTableModel(bookingColumns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        bookingTable = new JTable(bookingTableModel);
        bookingTable.setRowHeight(25);
        managePanel.add(new JScrollPane(bookingTable), BorderLayout.CENTER);

        JPanel manageActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        manageActionPanel.setBackground(new Color(245, 247, 250));

        AnimatedButton cancelBtn = new AnimatedButton("Cancel Reservation", new Color(220, 38, 38), new Color(185, 28, 28));
        cancelBtn.addActionListener(e -> cancelBooking());
        manageActionPanel.add(cancelBtn);

        managePanel.add(manageActionPanel, BorderLayout.SOUTH);
        tabbedPane.addTab("Manage Bookings", managePanel);

        add(tabbedPane);
    }

    private void refreshData() {
        hotelService.loadRooms();
        hotelService.loadReservations();

        roomTableModel.setRowCount(0);
        for (Room r : hotelService.getRooms()) {
            roomTableModel.addRow(new Object[]{
                r.getRoomId(),
                r.getCategory(),
                r.getPricePerNight(),
                r.isAvailable() ? "Available" : "Occupied"
            });
        }

        bookingTableModel.setRowCount(0);
        for (Reservation res : hotelService.getReservations()) {
            bookingTableModel.addRow(new Object[]{
                res.getBookingId(),
                res.getRoomId(),
                res.getGuestName(),
                res.getCheckIn(),
                res.getCheckOut(),
                res.getTotalAmount()
            });
        }
    }

    private void openBookingDialog() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room from the table first!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String roomId = (String) roomTableModel.getValueAt(selectedRow, 0);
        String status = (String) roomTableModel.getValueAt(selectedRow, 3);
        double price = (double) roomTableModel.getValueAt(selectedRow, 2);

        if (!status.equals("Available")) {
            JOptionPane.showMessageDialog(this, "The selected room is already occupied!", "Unavailable", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(this, "Room Booking & Payment Simulation", true);
        dialog.setSize(400, 420);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(6, 2, 10, 10));
        ((JComponent) dialog.getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        JTextField nameField = new JTextField();
        JTextField checkInField = new JTextField("2026-11-01");
        JTextField checkOutField = new JTextField("2026-11-05");
        JComboBox<String> paymentMethodBox = new JComboBox<>(new String[]{"Credit Card", "PayPal", "Apple Pay"});

        dialog.add(new JLabel("Guest Full Name:"));
        dialog.add(nameField);
        dialog.add(new JLabel("Check-In Date (YYYY-MM-DD):"));
        dialog.add(checkInField);
        dialog.add(new JLabel("Check-Out Date (YYYY-MM-DD):"));
        dialog.add(checkOutField);
        dialog.add(new JLabel("Payment Simulation:"));
        dialog.add(paymentMethodBox);

        JLabel totalLabel = new JLabel("Total Amount: $" + (price * 4) + " (4 Nights)");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dialog.add(totalLabel);

        AnimatedButton confirmBtn = new AnimatedButton("Confirm & Pay", new Color(16, 185, 129), new Color(5, 150, 105));
        confirmBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter guest name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = hotelService.bookRoom(roomId, name, checkInField.getText(), checkOutField.getText(), price * 4);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "Payment Successful via " + paymentMethodBox.getSelectedItem() + "!\nRoom " + roomId + " booked successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Booking failed. Room might be taken.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.add(confirmBtn);

        dialog.setVisible(true);
    }

    private void cancelBooking() {
        int selectedRow = bookingTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a reservation to cancel!", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String bookingId = (String) bookingTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel booking " + bookingId + "?", "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = hotelService.cancelReservation(bookingId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Reservation cancelled successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Cancellation failed.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}