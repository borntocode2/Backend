package goodspace.backend.delivery.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class DeliveryHistoryDto {
    @JacksonXmlProperty(localName = "dlvyDate")
    private String date;

    @JacksonXmlProperty(localName = "dlvyTime")
    private String time;

    @JacksonXmlProperty(localName = "nowLc")
    private String location;

    @JacksonXmlProperty(localName = "processSttus")
    private String processStatus;

    @JacksonXmlProperty(localName = "detailDc")
    private String detailDescription;
}
