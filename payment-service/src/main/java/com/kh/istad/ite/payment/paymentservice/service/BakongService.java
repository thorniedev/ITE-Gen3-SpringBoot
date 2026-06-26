package com.kh.istad.ite.payment.paymentservice.service;

import com.kh.istad.ite.payment.paymentservice.dto.BakongRequest;
import com.kh.istad.ite.payment.paymentservice.dto.BakongResponse;
import com.kh.istad.ite.payment.paymentservice.dto.CheckTransactionRequest;
import kh.gov.nbc.bakong_khqr.model.KHQRData;
import kh.gov.nbc.bakong_khqr.model.KHQRResponse;


public interface BakongService {

    KHQRResponse<KHQRData> generateQR(BakongRequest request);
    byte[] getQRImage(KHQRData qr);
    BakongResponse checkTransactionByMD5(CheckTransactionRequest request);
}
