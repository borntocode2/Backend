package goodspace.backend.domain.user;


import jakarta.persistence.Embeddable;

@Embeddable
public class Delivery {
    private String recipient;
    private String contact_number_1;
    private String contact_number_2;
    private int postalCode;
    private String address;
    private String detailed_address;

}
