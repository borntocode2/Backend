package goodspace.backend.shipping.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShippingDetailDto {

    @JacksonXmlProperty(localName = "sortNo")
    private Integer sortNo;

    @JacksonXmlProperty(localName = "date")
    private String date;

    @JacksonXmlProperty(localName = "time")
    private String time;

    @JacksonXmlProperty(localName = "statue")
    private String status;

    @JacksonXmlProperty(localName = "location")
    private String location;

    @JacksonXmlProperty(localName = "remark")
    private String remark;
}
