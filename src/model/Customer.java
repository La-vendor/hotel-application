package model;

import java.util.Objects;
import java.util.regex.Pattern;

public class Customer {
    private String firstName;
    private String lastName;
    private String email;


    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " " + email;
    }

    public Customer(String firstName, String lastName, String email) {

        Pattern pattern = Pattern.compile("^(.+)@(.+).(.+)$");
        if (!pattern.matcher(email).matches()){
            throw new IllegalArgumentException("Wrong email pattern.");
        }
        else{
            this.email = email;
        }
        this.firstName = firstName;
        this.lastName = lastName;

    }
}
