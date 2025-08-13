package goodspace.backend.shipping.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

public class ShippingResponseDto {
    @JacksonXmlProperty(localName = "header")
    private Header header;

    @JacksonXmlProperty(localName = "trackInfo")
    private TrackInfo trackInfo;

    @Getter
    @Setter
    public static class Header {
        @JacksonXmlProperty(localName = "requestRegiNo")
        private String requestRegiNo;

        @JacksonXmlProperty(localName = "responseTime")
        private String responseTime;

        @JacksonXmlProperty(localName = "successYN")
        private String successYN;

        @JacksonXmlProperty(localName = "errorMessage")
        private String errorMessage;
    }

    @Getter @Setter
    public static class TrackInfo {
        @JacksonXmlProperty(localName = "regiNo")
        private String trackingNumber;

        @JacksonXmlProperty(localName = "senderName")
        private String senderName;

        @JacksonXmlProperty(localName = "senderData")
        private String senderDate;

        @JacksonXmlProperty(localName = "receiveName")
        private String receiverName;

        @JacksonXmlProperty(localName = "receiveDate")
        private String receiverDate;

        @JacksonXmlProperty(localName = "trackState")
        private String trackState;

        @JacksonXmlProperty(localName = "expressType")
        private String expressType;

        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "detaileTrackList")
        private List<ShippingDetailDto> detailTrackList;
    }
}
