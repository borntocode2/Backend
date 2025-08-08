package goodspace.backend.delivery.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class DeliveryStatusDto {
    @JacksonXmlProperty(localName = "applcntNm")
    private String sender;

    @JacksonXmlProperty(localName = "addrseNm")
    private String receiver;

    @JacksonXmlProperty(localName = "dlvyDe")
    private String completeDate;

    @JacksonXmlProperty(localName = "dlvySttus")
    private String status;

    @JacksonXmlProperty(localName = "pstmtrKnd")
    private String postType;

    @JacksonXmlProperty(localName = "rgist")
    private String registrationNumber;

    @JacksonXmlProperty(localName = "trtmntSe")
    private String treatType;






}
