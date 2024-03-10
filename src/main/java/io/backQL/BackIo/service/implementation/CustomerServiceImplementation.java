package io.backQL.BackIo.service.implementation;

import io.backQL.BackIo.domain.Customer;
import io.backQL.BackIo.domain.Invoice;
import io.backQL.BackIo.domain.Statistics;
import io.backQL.BackIo.query.EventQuery;
import io.backQL.BackIo.repository.CustomerRepository;
import io.backQL.BackIo.repository.InvoiceRepository;
import io.backQL.BackIo.rowmapper.StatsRowMapper;
import io.backQL.BackIo.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

import static io.backQL.BackIo.query.EventQuery.GET_STATISTICS_QUERY;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImplementation implements CustomerService {
    private final CustomerRepository customerRepository;
    private final InvoiceRepository invoiceRepository;
    private final NamedParameterJdbcTemplate jdbc;
    @Override
    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(new Date());
        return customerRepository.save(customer);
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Page<Customer> getCustomers(int page, int size) {
        return customerRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Iterable<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer getCustomer(long id) {
        return customerRepository.findById(id).get();
    }

    @Override
    public Page<Customer> searchCustomer(String name, int page, int size) {
        return customerRepository.findByNameContaining(name, PageRequest.of(page, size));
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        invoice.setInvoiceNumber(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
        return invoiceRepository.save(invoice);
    }

    @Override
    public Page<Invoice> getInvoices(int page, int size) {
        return invoiceRepository.findAll(PageRequest.of(page, size));
    }


    @Override
    public Invoice getInvoice(Long id) {
        return invoiceRepository.findById(id).get();
    }

    @Override
    public void addInvoiceCustomer(Long id, Invoice invoice) {
        invoice.setInvoiceNumber(RandomStringUtils.randomAlphanumeric(8).toUpperCase());
        Customer customer = customerRepository.findById(id).get();
        invoice.setCustomer(customer);
        invoiceRepository.save(invoice);
    }
    @Override
    public Statistics getStatistics() {
        return jdbc.queryForObject(GET_STATISTICS_QUERY, Map.of(), new StatsRowMapper());

    }
}








