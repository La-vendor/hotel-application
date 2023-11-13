package service;

import model.Customer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class CustomerService {

    private static CustomerService INSTANCE;

    private CustomerService() {
    }

    public static synchronized CustomerService getInstance() {
        if (INSTANCE == null)
            INSTANCE=new CustomerService();
        return INSTANCE;
    }

    private static Map<String, Customer> customersList = new HashMap<String, Customer>();

    public void addCustomer(String email, String firstname, String lastName) {
        customersList.put(email, new Customer(firstname, lastName, email));
    }

    public Customer getCustomer(String customerEmail) {
        return customersList.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {

        return new ArrayList<>(customersList.values());
    }


}
