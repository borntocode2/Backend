package goodspace.backend.delivery.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class CmmMsgHeaderDto {
    @JacksonXmlProperty(localName = "requestMsgId")
    private String requestMsgId;

    @JacksonXmlProperty(localName = "responseMsgId")
    private String responseMsgId;

    @JacksonXmlProperty(localName = "responseTime")
    private String responseTime;

    @JacksonXmlProperty(localName = "successYN")
    private String successYN;

    @JacksonXmlProperty(localName = "returnCode")
    private String returnCode;

    @JacksonXmlProperty(localName = "errMsg")
    private String errMsg;
}