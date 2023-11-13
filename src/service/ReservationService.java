package service;

import model.Customer;
import model.IRoom;
import model.Reservation;
import model.Room;

import java.util.*;

public class ReservationService {

    private static ReservationService INSTANCE;

    private ReservationService() {
    }

    public static synchronized ReservationService getInstance() {
        if (INSTANCE == null)
            INSTANCE=new ReservationService();
        return INSTANCE;
    }

    private static Collection<IRoom> roomList = new HashSet<>();
    private static Collection<Reservation> reservations = new HashSet<Reservation>();

    public void addRoom(IRoom room) {
        roomList.add(room);
    }

    public IRoom getARoom(String roomId) {
        IRoom returnRoom = new Room();

        for (IRoom room : roomList) {
            if (room.getRoomNumber().equals(roomId)) {
                returnRoom = room;
            }
        }
        return returnRoom;
    }

    public List<IRoom> getAllRooms() {

        List<IRoom> rooms = new ArrayList<>(roomList);
        Collections.sort(rooms);

        return rooms;
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate) {

        Collection<IRoom> availableRooms = new HashSet<>(roomList);

        for (Reservation reservation : reservations) {
            if (!(checkOutDate.compareTo(reservation.getCheckInDate()) < 0 || checkInDate.compareTo(reservation.getCheckOutDate()) > 0)) {
                availableRooms.remove(reservation.getRoom());
            }
        }
        return availableRooms;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer) {

        Collection<Reservation> customerReservations = new HashSet<>();
        for (Reservation reservation : reservations) {
            if (reservation.getCustomer() == customer) {
                customerReservations.add(reservation);
            }
        }
        return customerReservations;
    }

    public void printAllReservation() {
        for (Reservation reservation : reservations) {
            System.out.println(reservation);
        }
    }


}
