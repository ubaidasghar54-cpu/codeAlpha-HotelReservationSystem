# CodeAlpha Hotel Reservation System (Task 4)

A professional, object-oriented desktop application built in **Java** utilizing **Java Swing** for a modern graphical user interface, custom **MVC** architecture, local file-based persistent storage via CSV, room categorization, and an interactive reservation management dashboard with animated UI elements.

---

## 🚀 Features

- **Room Management & Categorization:** Browse available rooms categorized by **Standard**, **Deluxe**, and **Suite** options with real-time pricing and availability tracking.
- **Booking & Payment Simulation:** Seamless booking workflow allowing guests to specify stay dates, view calculated pricing, and simulate payments via Credit Card, PayPal, or Apple Pay.
- **Reservation Management:** View active reservations with a detailed booking overview and cancel bookings to instantly restore room availability.
- **Persistent Data Storage:** Secure local data persistence using CSV files (`rooms.csv` and `bookings.csv`) so data remains saved across application launches.
- **Modern Animated UI:** Clean dashboard design featuring tabbed panes, custom hover-animated buttons, and a polished Look-and-Feel.

---

## 📂 Project Directory Structure

```text
codeAlpha-HotelReservationSystem/
│
├── data/
│   ├── rooms.csv                 # Room inventory and availability status
│   └── bookings.csv              # Active guest reservations records
├── src/
│   └── com/
│       └── codealpha/
│           └── hotel/
│               ├── Main.java     # Application entry point
│               ├── model/        # Data models (Room.java, Reservation.java)
│               ├── service/      # Business logic & File I/O (HotelService.java)
│               └── ui/           # GUI interface & components (HotelGUI.java, AnimatedButton.java)
└── README.md