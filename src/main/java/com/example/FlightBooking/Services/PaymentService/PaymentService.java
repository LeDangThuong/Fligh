package com.example.FlightBooking.Services.PaymentService;
import com.example.FlightBooking.Models.Users;
import com.example.FlightBooking.Repositories.UserRepository;
import com.example.FlightBooking.Services.AuthJWT.JwtService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerCreateParams;
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
//    public void chargeCustomer(String customerId, String paymentMethodId, long amount, String paymentType, Order order) throws Exception {
//        PaymentContext paymentContext = new PaymentContext();
//        if (paymentType.equals("stripe")) {
//            paymentContext.setPaymentStrategy(new StripePaymentStrategy(stripeSecretKey, customerId, paymentMethodId));
//        } else if (paymentType.equals("vnpay")) {
//            paymentContext.setPaymentStrategy(new VNPayPaymentStrategy());
//        }
//        paymentContext.executePayment(amount, order);
//    }
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
}