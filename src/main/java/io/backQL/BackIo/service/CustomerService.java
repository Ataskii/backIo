package io.backQL.BackIo.service;

import io.backQL.BackIo.domain.Customer;
import io.backQL.BackIo.domain.Invoice;
import io.backQL.BackIo.domain.Statistics;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Customer customer);
    Page<Customer> getCustomers(int page, int size);
    Iterable<Customer> getCustomers();
    Customer getCustomer(long id);
    Page<Customer> searchCustomer(String name, int page, int size);


    Invoice createInvoice(Invoice invoice);
    Page<Invoice> getInvoices(int page, int size);
    Invoice getInvoice(Long id);
    void addInvoiceCustomer(Long id, Invoice invoice);
    Statistics getStatistics();

}
