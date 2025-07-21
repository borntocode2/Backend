package goodspace.backend.payment.dto;

import lombok.Data;

@Data
public class PaymentApproveResultDto {
    private String resultCode;
    private String resultMsg;
    private String tid;
    private String cancelledTid;
    private String orderId;
    private String ediDate;
    private String signature;
    private String status;
    private String paidAt;
    private String failedAt;
    private String cancelledAt;
    private String payMethod;
    private int amount;
    private int balanceAmt;
    private String goodsName;
    private String mallReserved;
    private boolean useEscrow;
    private String currency;
    private String channel;
    private String approveNo;
    private String buyerName;
    private String buyerTel;
    private String buyerEmail;
    private String receiptUrl;
    private String mallUserId;
    private boolean issuedCashReceipt;
    private String coupon;

    private CardInfo card; // nested class로 카드 정보 처리

    @Data
    public static class CardInfo {
        private String cardCode;
        private String cardName;
        private String cardNum;
        private int cardQuota;
        private boolean isInterestFree;
        private String cardType;
        private boolean canPartCancel;
        private String acquCardCode;
        private String acquCardName;
    }
}
