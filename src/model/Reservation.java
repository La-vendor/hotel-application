package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    SimpleDateFormat dateFormat = new SimpleDateFormat("E dd MMMM yyyy", Locale.ENGLISH);

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(customer, that.customer) && Objects.equals(room, that.room) && Objects.equals(checkInDate, that.checkInDate) && Objects.equals(checkOutDate, that.checkOutDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, room, checkInDate, checkOutDate);
    }

    public Customer getCustomer() {
        return customer;
    }

    public IRoom getRoom() {
        return room;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    @Override
    public String toString() {
        return "Reservation for\n" +
                customer + "\n" +
                "Room: " + room.getRoomNumber() + " - " + room.getRoomType() + "\n" +
                "Price: $" + room.getRoomPrice() + " per night" + "\n" +
                "Checkin  Date: " + dateFormat.format(checkInDate) + "\n" +
                "Checkout Date: " + dateFormat.format(checkOutDate) + "\n";
    }
}
