package model;

public enum RoomType {

    SINGLE("Single bed"), DOUBLE("Double bed");

    private String roomType;

    RoomType(String type) {
        this.roomType = type;
    }

    @Override
    public String toString() {
        return roomType;
    }
}
