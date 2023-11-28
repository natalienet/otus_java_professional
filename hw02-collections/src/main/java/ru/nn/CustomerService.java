package ru.nn;

import java.util.*;

public class CustomerService {
    private final NavigableMap<Customer, String> customersData;

    public CustomerService() {
        customersData = new TreeMap<>(Comparator.comparingLong(o -> o.getScores()));
    }

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> nextEntry = customersData.firstEntry();
        return nextEntry == null ? null : Map.entry(new Customer(nextEntry.getKey()), nextEntry.getValue());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> nextEntry = customersData.higherEntry(customer);
        return nextEntry == null ? null : Map.entry(new Customer(nextEntry.getKey()), nextEntry.getValue());
    }

    public void add(Customer customer, String data) {
        customersData.put(customer, data);
    }
}
