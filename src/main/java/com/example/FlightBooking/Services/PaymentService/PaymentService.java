package com.example.FlightBooking.Services.PaymentService;
import com.example.FlightBooking.Models.Statistics;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.StatisticsRepository;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.AuthJWT.JwtService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public PaymentService() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createStripeCustomer(String email) throws StripeException {
        Stripe.apiKey = stripeSecretKey;
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(email)
                .build();
        Customer customer = Customer.create(params);
        Users users = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        users.setStripeCustomerId(customer.getId());
        userRepository.save(users);
        return customer.getId();
    }


    public String getStripeCustomerId (String token)
    {
        String username = jwtService.getUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        return users.getStripeCustomerId();
    }
    public String getStripeSetupIntentId (String token)
    {
        String username = jwtService.getUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        return users.getSetupIntentId();
    }
    public String getPaymentMethodId (String token) {
        Stripe.apiKey = stripeSecretKey;
        String username = jwtService.getUsername(token);
        Users users = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found with this username: " + username));
        SetupIntent setupIntent = null;
        try {
            setupIntent = SetupIntent.retrieve(users.getSetupIntentId());
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        return setupIntent.getPaymentMethod();
    }
    public PaymentIntent createPayment(String token, double amount, Long flightId) throws StripeException {
        String paymentMethodId = getPaymentMethodId(token);
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (amount * 100))  // amount in cents
                .setCurrency("usd")
                .setPaymentMethod(paymentMethodId)
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
}