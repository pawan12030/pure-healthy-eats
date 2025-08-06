package com.example.purehealthyeats.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class QRCodeService {

    public String generateUPIQRCode(String merchantId, String merchantName, String amount, String transactionId) {
        try {
            // Create UPI payment string
            String upiString = String.format(
                "upi://pay?pa=%s&pn=%s&am=%s&tid=%s&cu=INR",
                merchantId, merchantName, amount, transactionId
            );

            // Generate QR code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(upiString, BarcodeFormat.QR_CODE, 300, 300);

            // Convert to image bytes
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();

            // Convert to Base64
            return Base64.getEncoder().encodeToString(qrCodeBytes);

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate QR code", e);
        }
    }

    public String generateOrderTrackingQR(String orderId, String trackingUrl) {
        try {
            String trackingString = trackingUrl + "?orderId=" + orderId;

            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(trackingString, BarcodeFormat.QR_CODE, 200, 200);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
            byte[] qrCodeBytes = outputStream.toByteArray();

            return Base64.getEncoder().encodeToString(qrCodeBytes);

        } catch (WriterException | IOException e) {
            throw new RuntimeException("Failed to generate tracking QR code", e);
        }
    }
}
