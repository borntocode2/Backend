package goodspace.backend.admin.dto.order;

import goodspace.backend.order.domain.PaymentApproveResult;
import lombok.Builder;

import java.util.List;

@Builder
public record PaymentApproveResultDto(
        String resultCode,
        String resultMsg,
        String tid,
        String cancelledTid,
        Long orderId,
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
        BankDto bank,
        List<CancelInfoDto> cancels,
        List<CashReceiptInfoDto> cashReceipts,
        VbankInfoDto vbank,
        CouponDto coupon,
        CardInfoDto card
) {
    public static PaymentApproveResultDto from(PaymentApproveResult approveResult) {
        if (approveResult == null) {
            return null;
        }

        return PaymentApproveResultDto.builder()
                .resultCode(approveResult.getResultCode())
                .resultMsg(approveResult.getResultMsg())
                .tid(approveResult.getTid())
                .cancelledTid(approveResult.getCancelledTid())
                .orderId(approveResult.getOrderId())
                .ediDate(approveResult.getEdiDate())
                .signature(approveResult.getSignature())
                .status(approveResult.getStatus())
                .paidAt(approveResult.getPaidAt())
                .failedAt(approveResult.getFailedAt())
                .cancelledAt(approveResult.getCancelledAt())
                .payMethod(approveResult.getPayMethod())
                .amount(approveResult.getAmount())
                .balanceAmt(approveResult.getBalanceAmt())
                .goodsName(approveResult.getGoodsName())
                .mallReserved(approveResult.getMallReserved())
                .useEscrow(approveResult.getUseEscrow())
                .currency(approveResult.getCurrency())
                .channel(approveResult.getChannel())
                .approveNo(approveResult.getApproveNo())
                .buyerName(approveResult.getBuyerName())
                .buyerTel(approveResult.getBuyerTel())
                .buyerEmail(approveResult.getBuyerEmail())
                .receiptUrl(approveResult.getReceiptUrl())
                .mallUserId(approveResult.getMallUserId())
                .issuedCashReceipt(approveResult.getIssuedCashReceipt())
                .cellphone(approveResult.getCellphone())
                .messageSource(approveResult.getMessageSource())
                .bank(approveResult.getBank() != null ? BankDto.from(approveResult.getBank()) : null)
                .cancels(approveResult.getCancels() != null
                        ? approveResult.getCancels().stream().map(CancelInfoDto::from).toList()
                        : null)
                .cashReceipts(approveResult.getCashReceipts() != null
                        ? approveResult.getCashReceipts().stream().map(CashReceiptInfoDto::from).toList()
                        : null)
                .vbank(approveResult.getVbank() != null ? VbankInfoDto.from(approveResult.getVbank()) : null)
                .coupon(approveResult.getCoupon() != null ? CouponDto.from(approveResult.getCoupon()) : null)
                .card(approveResult.getCard() != null ? CardInfoDto.from(approveResult.getCard()) : null)
                .build();
    }

    public PaymentApproveResult toEntity() {
        return PaymentApproveResult.builder()
                .resultCode(this.resultCode)
                .resultMsg(this.resultMsg)
                .tid(this.tid)
                .cancelledTid(this.cancelledTid)
                .orderId(this.orderId)
                .ediDate(this.ediDate)
                .signature(this.signature)
                .status(this.status)
                .paidAt(this.paidAt)
                .failedAt(this.failedAt)
                .cancelledAt(this.cancelledAt)
                .payMethod(this.payMethod)
                .amount(this.amount)
                .balanceAmt(this.balanceAmt)
                .goodsName(this.goodsName)
                .mallReserved(this.mallReserved)
                .useEscrow(this.useEscrow)
                .currency(this.currency)
                .channel(this.channel)
                .approveNo(this.approveNo)
                .buyerName(this.buyerName)
                .buyerTel(this.buyerTel)
                .buyerEmail(this.buyerEmail)
                .receiptUrl(this.receiptUrl)
                .mallUserId(this.mallUserId)
                .issuedCashReceipt(this.issuedCashReceipt)
                .cellphone(this.cellphone)
                .messageSource(this.messageSource)
                .bank(this.bank != null ? this.bank.toEntity() : null)
                .cancels(this.cancels != null
                        ? this.cancels.stream().map(CancelInfoDto::toEntity).toList()
                        : null)
                .cashReceipts(this.cashReceipts != null
                        ? this.cashReceipts.stream().map(CashReceiptInfoDto::toEntity).toList()
                        : null)
                .vbank(this.vbank != null ? this.vbank.toEntity() : null)
                .coupon(this.coupon != null ? this.coupon.toEntity() : null)
                .card(this.card != null ? this.card.toEntity() : null)
                .build();
    }


    @Builder
    public record CancelInfoDto(
            String cancelDate,
            String cancelAmount,
            String cancelReason,
            String cancelType
    ) {
        public static CancelInfoDto from(PaymentApproveResult.CancelInfo cancelInfo) {
            return CancelInfoDto.builder()
                    .cancelDate(cancelInfo.getCancelDate())
                    .cancelAmount(cancelInfo.getCancelAmount())
                    .cancelReason(cancelInfo.getCancelReason())
                    .cancelType(cancelInfo.getCancelType())
                    .build();
        }

        public PaymentApproveResult.CancelInfo toEntity() {
            return PaymentApproveResult.CancelInfo.builder()
                    .cancelDate(this.cancelDate)
                    .cancelAmount(this.cancelAmount)
                    .cancelReason(this.cancelReason)
                    .cancelType(this.cancelType)
                    .build();
        }
    }

    @Builder
    public record CashReceiptInfoDto(
            String receiptId,
            String orgTid,
            String status,
            Integer amount,
            Integer taxFreeAmt,
            String receiptType,
            String issueNo,
            String receiptUrl
    ) {
        public static CashReceiptInfoDto from(PaymentApproveResult.CashReceiptInfo info) {
            return CashReceiptInfoDto.builder()
                    .receiptId(info.getReceiptId())
                    .orgTid(info.getOrgTid())
                    .status(info.getStatus())
                    .amount(info.getAmount())
                    .taxFreeAmt(info.getTaxFreeAmt())
                    .receiptType(info.getReceiptType())
                    .issueNo(info.getIssueNo())
                    .receiptUrl(info.getReceiptUrl())
                    .build();
        }

        public PaymentApproveResult.CashReceiptInfo toEntity() {
            return PaymentApproveResult.CashReceiptInfo.builder()
                    .receiptId(this.receiptId)
                    .orgTid(this.orgTid)
                    .status(this.status)
                    .amount(this.amount)
                    .taxFreeAmt(this.taxFreeAmt)
                    .receiptType(this.receiptType)
                    .issueNo(this.issueNo)
                    .receiptUrl(this.receiptUrl)
                    .build();
        }
    }

    @Builder
    public record CouponDto(
            int couponAmt
    ) {
        public static CouponDto from(PaymentApproveResult.Coupon coupon) {
            return CouponDto.builder()
                    .couponAmt(coupon.getCouponAmt())
                    .build();
        }

        public PaymentApproveResult.Coupon toEntity() {
            return PaymentApproveResult.Coupon.builder()
                    .couponAmt(this.couponAmt)
                    .build();
        }
    }

    @Builder
    public record CardInfoDto(
            String cardCode,
            String cardName,
            String cardNum,
            int cardQuota,
            boolean interestFree,
            String cardType,
            boolean canPartCancel,
            String acquCardCode,
            String acquCardName
    ) {
        public static CardInfoDto from(PaymentApproveResult.CardInfo cardInfo) {
            return CardInfoDto.builder()
                    .cardCode(cardInfo.getCardCode())
                    .cardName(cardInfo.getCardName())
                    .cardNum(cardInfo.getCardNum())
                    .cardQuota(cardInfo.getCardQuota())
                    .interestFree(cardInfo.isInterestFree())
                    .cardType(cardInfo.getCardType())
                    .canPartCancel(cardInfo.isCanPartCancel())
                    .acquCardCode(cardInfo.getAcquCardCode())
                    .acquCardName(cardInfo.getAcquCardName())
                    .build();
        }

        public PaymentApproveResult.CardInfo toEntity() {
            return PaymentApproveResult.CardInfo.builder()
                    .cardCode(this.cardCode)
                    .cardName(this.cardName)
                    .cardNum(this.cardNum)
                    .cardQuota(this.cardQuota)
                    .interestFree(this.interestFree)
                    .cardType(this.cardType)
                    .canPartCancel(this.canPartCancel)
                    .acquCardCode(this.acquCardCode)
                    .acquCardName(this.acquCardName)
                    .build();
        }
    }

    @Builder
    public record VbankInfoDto(
            String vbankName,
            String vbankNumber,
            String vbankCode,
            String vbankExpDate,
            String vbankHolder
    ) {
        public static VbankInfoDto from(PaymentApproveResult.VbankInfo vbank) {
            return VbankInfoDto.builder()
                    .vbankName(vbank.getVbankName())
                    .vbankNumber(vbank.getVbankNumber())
                    .vbankCode(vbank.getVbankCode())
                    .vbankExpDate(vbank.getVbankExpDate())
                    .vbankHolder(vbank.getVbankHolder())
                    .build();
        }

        public PaymentApproveResult.VbankInfo toEntity() {
            return PaymentApproveResult.VbankInfo.builder()
                    .vbankName(this.vbankName)
                    .vbankNumber(this.vbankNumber)
                    .vbankCode(this.vbankCode)
                    .vbankExpDate(this.vbankExpDate)
                    .vbankHolder(this.vbankHolder)
                    .build();
        }
    }

    @Builder
    public record BankDto(
            String bankCode,
            String bankName
    ) {
        public static BankDto from(PaymentApproveResult.Bank bank) {
            return BankDto.builder()
                    .bankCode(bank.getBankCode())
                    .bankName(bank.getBankName())
                    .build();
        }

        public PaymentApproveResult.Bank toEntity() {
            return PaymentApproveResult.Bank.builder()
                    .bankCode(this.bankCode)
                    .bankName(this.bankName)
                    .build();
        }
    }
}
