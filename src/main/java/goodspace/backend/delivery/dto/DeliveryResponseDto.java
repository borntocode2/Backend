package goodspace.backend.delivery.dto;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.util.List;

@Data
@JacksonXmlRootElement(localName = "LongitudinalDomesticListResponse")
public class DeliveryResponseDto {

    @JacksonXmlProperty(localName = "cmmMsgHeader")
    private CmmMsgHeaderDto cmmMsgHeader;

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

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "longitudinalDomesticList")
    private List<DeliveryHistoryDto> longitudinalDomesticList;
}