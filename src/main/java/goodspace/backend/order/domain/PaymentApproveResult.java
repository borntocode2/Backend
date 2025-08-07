package goodspace.backend.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Embeddable
public class PaymentApproveResult {
    private String resultCode;
    private String resultMsg;
    private String tid;
    private String cancelledTid;
    private Long orderId;
    private String ediDate;
    private String signature;
    private String status;
    private String paidAt;
    private String failedAt;
    private String cancelledAt;
    private String payMethod;
    private Integer amount;
    private Integer balanceAmt;
    private String goodsName;
    private String mallReserved;
    private Boolean useEscrow;
    private String currency;
    private String channel;
    private String approveNo;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;
    private String receiptUrl;
    private String mallUserId;
    private Boolean issuedCashReceipt;
    private String cellphone;
    private String messageSource;

    @Embedded
    private Bank bank;

    @ElementCollection
    private List<CancelInfo> cancels;

    @ElementCollection
    private List<CashReceiptInfo> cashReceipts;

    @Embedded
    private VbankInfo vbank;

    @Embedded
    private Coupon coupon;

    @Embedded
    private CardInfo card; // nested class로 카드 정보 처리

    @Data
    @Embeddable
    public static class CancelInfo {
        private String cancelDate;
        private String cancelAmount;
        private String cancelReason;
        private String cancelType;
        // 실제 API 문서 참고해서 필드 조정 필요
    }

    @Data
    @Embeddable
    public static class CashReceiptInfo {
        private String receiptId;
        private String orgTid;
        private String status;
        private Integer amount;
        private Integer taxFreeAmt;
        private String receiptType;
        private String issueNo;
        private String receiptUrl;
        // API 문서에 따라 필드 변경 가능
    }

    @Data
    @Embeddable
    public static class Coupon {
        private int couponAmt;
    }

    @Data
    @Embeddable
    public static class CardInfo {
        private String cardCode;
        private String cardName;
        private String cardNum;
        private int cardQuota;
        @JsonProperty("isInterestFree")
        private boolean interestFree;
        private String cardType;
        private boolean canPartCancel;
        private String acquCardCode;
        private String acquCardName;
    }

    @Data
    @Embeddable
    public static class VbankInfo {
        private String vbankName;
        private String vbankNumber;
        private String vbankCode;
        private String vbankExpDate;
        private String vbankHolder;
    }

    @Data
    @Embeddable
    public static class Bank {
        private String bankCode;
        private String bankName;
    }
}
