package io.backQL.BackIo.controller;

import io.backQL.BackIo.domain.Customer;
import io.backQL.BackIo.domain.HttpResponse;
import io.backQL.BackIo.domain.Invoice;
import io.backQL.BackIo.dto.userDto;
import io.backQL.BackIo.service.CustomerService;
import io.backQL.BackIo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<HttpResponse> getCustomers(@AuthenticationPrincipal userDto user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                    "customers", customerService.getCustomers(page.orElse(0), size.orElse(10)),
                                    "statistics", customerService.getStatistics()))
                        .message("Customers retrieved.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createCustomer(@AuthenticationPrincipal userDto user, @RequestBody Customer customer) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                     "customer", customerService.createCustomer(customer)))
                        .message("Customers created.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PostMapping("/get/{id}")
    public ResponseEntity<HttpResponse> getCustomer(@AuthenticationPrincipal userDto user, @PathVariable("id") Long id ) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                     "customer", customerService.getCustomer(id)))
                        .message("Customer retrieved.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PostMapping("/search")
    public ResponseEntity<HttpResponse> searchCustomer(@AuthenticationPrincipal userDto user,Optional<String>name, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                     "customers", customerService.searchCustomer(name.orElse(""), page.orElse(0), size.orElse(10))))
                        .message("Customers retrieved.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse> updateCustomer(@AuthenticationPrincipal userDto user, @RequestBody Customer customer) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                     "customer", customerService.updateCustomer(customer)))
                        .message("Customers updated.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

        // Invoice Controller
    @GetMapping("/invoice/list")
    public ResponseEntity<HttpResponse> getInvoice(@AuthenticationPrincipal userDto user, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                    "invoices", customerService.getInvoices(page.orElse(0), size.orElse(10))))
                        .message("Invoice retrieved.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

    @PostMapping("/invoice/created")
    public ResponseEntity<HttpResponse> createInvoice(@AuthenticationPrincipal userDto user, @RequestBody Invoice invoice) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                "customer", customerService.createInvoice(invoice)))
                        .message("Invoice created.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @PostMapping("/invoice/new")
    public ResponseEntity<HttpResponse> newInvoice(@AuthenticationPrincipal userDto user) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                     "customer", customerService.getCustomers()))
                        .message("Customer retrieved.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
    @PostMapping("/invoice/get/{id}")
    public ResponseEntity<HttpResponse> getInvoice(@AuthenticationPrincipal userDto user, @PathVariable("id") Long id ) {
        Invoice invoice = customerService.getInvoice(id);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail()),
                                     "invoice", invoice,
                                     "customer", invoice.getCustomer()))
                        .message("invoice retrieved.")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }
    @PostMapping("/invoice/addToCustomer/{id}")
    public ResponseEntity<HttpResponse> addInvoiceCustomer(@AuthenticationPrincipal userDto user,@PathVariable("id") Long id, @RequestBody Invoice invoice) {
        customerService.addInvoiceCustomer(id, invoice);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("user", userService.getUserByEmail(user.getEmail())))
                        .message("Invoice added.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
