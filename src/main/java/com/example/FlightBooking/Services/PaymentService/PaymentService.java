package com.example.FlightBooking.Services.PaymentService;

import com.example.FlightBooking.Models.Statistics;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.PaymentMethodRepository;
import com.example.FlightBooking.Repositories.StatisticsRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.AuthJWT.JwtService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentMethodAttachParams;
import com.stripe.param.SetupIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${stripe.api.secretKey}")
    private String stripeSecretKey;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String getStripeClientSecret(String token) throws StripeException
    {
        String username = jwtService.getUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        SetupIntent setupIntent = SetupIntent.retrieve(users.getSetupIntentId());
        return setupIntent.getClientSecret();
    }

    public String createStripeCustomer(String email) throws StripeException {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .build();
        Customer customer = Customer.create(params);
        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        user.setStripeCustomerId(customer.getId());
        userRepository.save(user);
        return customer.getId();
    }

    public String getStripeCustomerId(String token) {
        String username = jwtService.getUsername(token);
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        return user.getStripeCustomerId();
    }

    public String getStripeSetupIntentId (String token) throws StripeException
    {
        String username = jwtService.getUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        return users.getSetupIntentId();
    }

    public SetupIntent createSetupIntent(String customerId) throws StripeException {
        SetupIntentCreateParams params = SetupIntentCreateParams.builder()
                .setCustomer(customerId)
                .addPaymentMethodType("card")
                .build();
        return SetupIntent.create(params);
    }

    public void attachPaymentMethod(String customerId, String paymentMethodId) throws StripeException {
        PaymentMethod paymentMethod = PaymentMethod.retrieve(paymentMethodId);
        PaymentMethodAttachParams attachParams = PaymentMethodAttachParams.builder()
                .setCustomer(customerId)
                .build();
        paymentMethod.attach(attachParams);
        Users user = userRepository.findByStripeCustomerId(customerId).orElseThrow(() -> new RuntimeException("User not found with this customerId: " + customerId));
        com.example.FlightBooking.Models.PaymentMethod newPaymentMethod = new com.example.FlightBooking.Models.PaymentMethod();
        newPaymentMethod.setUser(user);
        newPaymentMethod.setStripePaymentMethodId(paymentMethod.getId());
        newPaymentMethod.setCardLast4(paymentMethod.getCard().getLast4());
        newPaymentMethod.setCardBrand(paymentMethod.getCard().getBrand());
        paymentMethodRepository.save(newPaymentMethod);
    }

    public PaymentIntent createPaymentIntent(String token, double amount, Long flightId) throws StripeException {
        String customerId = getStripeCustomerId(token);
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))  // amount in cents
                .setCurrency("usd")
                .setCustomer(customerId)
                .setPaymentMethod(getPaymentMethodId(token))
                .setConfirm(true)
                .setOffSession(true)
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        String username = jwtService.getUsername(token);
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));

        Statistics statistics = new Statistics();
        statistics.setUserId(user.getId());
        statistics.setAmount(amount);
        statistics.setFlightId(flightId);
        statisticsRepository.save(statistics);
        return paymentIntent;
    }

    public String getPaymentMethodId(String token) throws StripeException {
        String username = jwtService.getUsername(token);
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        SetupIntent setupIntent = SetupIntent.retrieve(user.getSetupIntentId());
        return setupIntent.getPaymentMethod();
    }
}