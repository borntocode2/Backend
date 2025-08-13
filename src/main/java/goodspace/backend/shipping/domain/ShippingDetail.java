package goodspace.backend.shipping.domain;

import jakarta.persistence.Embeddable;

@Embeddable
public class ShippingDetail {
    private Long sortNo;
    private String date;
    private String time;
    private String status;
    private String location;
    private String remark;

}
