package goodspace.backend.fixture;

import goodspace.backend.order.domain.PaymentApproveResult;
import goodspace.backend.order.domain.PaymentApproveResult.CardInfo;

public enum PaymentApproveResultFixture {
    A(
            "0000",
            "정상 처리되었습니다.",
            "UT0000113m01012111051714341073",
            null,
            "2021-11-05T17:14:35.150+0900",
            "63b251b31c990eebef1a9f4fcc19e77bdbc8f64cf1066a29670f8627186865cd",
            "paid",
            "2021-11-05T17:14:35.000+0900",
            "0",
            "0",
            "CARD",
            1004,
            1004,
            "니이스페이-상품",
            null,
            false,
            "KRW",
            "pc",
            "000000",
            null, null, null,
            "https://npg.nicepay.co.kr/issue/IssueLoader.do?tid=UT0000113m01012111051714341073",
            null,
            false,
            null,
            null,
            CardInfo.builder()
                    .cardCode("04")
                    .cardName("삼성")
                    .cardNum("12341234****1234")
                    .cardQuota(0)
                    .interestFree(false)
                    .cardType("credit")
                    .canPartCancel(true)
                    .acquCardCode("04")
                    .acquCardName("삼성")
                    .build()
    ),
    B(
            "1001",
            "부분 승인되었습니다.",
            "UT0000113m01012121051714341073",
            null,
            "2021-12-01T12:00:00.000+0900",
            "abc123xyz456sig789signatureb",
            "partially_paid",
            "2021-12-01T12:01:00.000+0900",
            "0",
            "2021-12-01T12:05:00.000+0900",
            "VBANK",
            8000,
            2000,
            "부분결제 상품",
            "B_RESERVED",
            true,
            "KRW",
            "mobile",
            "111111",
            "이영희", "01023456789", "lee@example.com",
            "https://npg.nicepay.co.kr/issue/IssueLoader.do?tid=UT0000113m01012121051714341073",
            "mallUserB",
            true,
            "01087654321",
            "KCP",
            CardInfo.builder()
                    .cardCode("05")
                    .cardName("현대")
                    .cardNum("43214321****4321")
                    .cardQuota(2)
                    .interestFree(true)
                    .cardType("credit")
                    .canPartCancel(false)
                    .acquCardCode("05")
                    .acquCardName("현대")
                    .build()
    ),
    C(
            "9999",
            "결제 실패",
            "UT0000113m01012131051714341073",
            "UT0000113m01012131051714341073-CANCEL",
            "2021-12-15T18:30:00.000+0900",
            "sig-fail-20211215",
            "failed",
            null,
            "2021-12-15T18:31:00.000+0900",
            "2021-12-15T18:32:00.000+0900",
            "CASH",
            15000,
            0,
            "실패 상품",
            "C_RESERVED",
            false,
            "USD",
            "app",
            "999999",
            "박지민", "01034567890", "park@example.com",
            "https://npg.nicepay.co.kr/issue/IssueLoader.do?tid=UT0000113m01012131051714341073",
            "mallUserC",
            false,
            "01099887766",
            "PG",
            CardInfo.builder()
                    .cardCode("06")
                    .cardName("롯데")
                    .cardNum("98769876****9876")
                    .cardQuota(12)
                    .interestFree(false)
                    .cardType("할부")
                    .canPartCancel(true)
                    .acquCardCode("06")
                    .acquCardName("롯데")
                    .build()
    );

    private final String resultCode;
    private final String resultMsg;
    private final String tid;
    private final String cancelledTid;
    private final String ediDate;
    private final String signature;
    private final String status;
    private final String paidAt;
    private final String failedAt;
    private final String cancelledAt;
    private final String payMethod;
    private final Integer amount;
    private final Integer balanceAmt;
    private final String goodsName;
    private final String mallReserved;
    private final Boolean useEscrow;
    private final String currency;
    private final String channel;
    private final String approveNo;
    private final String buyerName;
    private final String buyerTel;
    private final String buyerEmail;
    private final String receiptUrl;
    private final String mallUserId;
    private final Boolean issuedCashReceipt;
    private final String cellphone;
    private final String messageSource;
    private final CardInfo card;

    PaymentApproveResultFixture(
            String resultCode,
            String resultMsg,
            String tid,
            String cancelledTid,
            String ediDate,
            String signature,
            String status,
            String paidAt,
            String failedAt,
            String cancelledAt,
            String payMethod,
            Integer amount,
            Integer balanceAmt,
            String goodsName,
            String mallReserved,
            Boolean useEscrow,
            String currency,
            String channel,
            String approveNo,
            String buyerName,
            String buyerTel,
            String buyerEmail,
            String receiptUrl,
            String mallUserId,
            Boolean issuedCashReceipt,
            String cellphone,
            String messageSource,
            CardInfo card
    ) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
        this.tid = tid;
        this.cancelledTid = cancelledTid;
        this.ediDate = ediDate;
        this.signature = signature;
        this.status = status;
        this.paidAt = paidAt;
        this.failedAt = failedAt;
        this.cancelledAt = cancelledAt;
        this.payMethod = payMethod;
        this.amount = amount;
        this.balanceAmt = balanceAmt;
        this.goodsName = goodsName;
        this.mallReserved = mallReserved;
        this.useEscrow = useEscrow;
        this.currency = currency;
        this.channel = channel;
        this.approveNo = approveNo;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerEmail = buyerEmail;
        this.receiptUrl = receiptUrl;
        this.mallUserId = mallUserId;
        this.issuedCashReceipt = issuedCashReceipt;
        this.cellphone = cellphone;
        this.messageSource = messageSource;
        this.card = card;
    }

    public PaymentApproveResult getInstanceWith(long orderId) {
        return PaymentApproveResult.builder()
                .resultCode(resultCode)
                .resultMsg(resultMsg)
                .tid(tid)
                .cancelledTid(cancelledTid)
                .orderId(orderId)
                .ediDate(ediDate)
                .signature(signature)
                .status(status)
                .paidAt(paidAt)
                .failedAt(failedAt)
                .cancelledAt(cancelledAt)
                .payMethod(payMethod)
                .amount(amount)
                .balanceAmt(balanceAmt)
                .goodsName(goodsName)
                .mallReserved(mallReserved)
                .useEscrow(useEscrow)
                .currency(currency)
                .channel(channel)
                .approveNo(approveNo)
                .buyerName(buyerName)
                .buyerTel(buyerTel)
                .buyerEmail(buyerEmail)
                .receiptUrl(receiptUrl)
                .mallUserId(mallUserId)
                .issuedCashReceipt(issuedCashReceipt)
                .cellphone(cellphone)
                .messageSource(messageSource)
                .card(card)
                .build();
    }
}
