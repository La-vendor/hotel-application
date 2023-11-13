package model;

public interface IRoom extends Comparable<IRoom>
{
    public String getRoomNumber();
    public Double getRoomPrice();
    public RoomType getRoomType();
    public boolean isFree();
}
