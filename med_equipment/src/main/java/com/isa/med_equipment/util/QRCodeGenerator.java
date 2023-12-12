package com.isa.med_equipment.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.isa.med_equipment.model.Reservation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QRCodeGenerator {

    public static byte[] generateQRCodeImageAsByteArray(Reservation reservation) {
        try {
            String formattedStartDate = formatDate(reservation.getTimeSlot().getStart());
            Date endDate = calculateEndDate(reservation.getTimeSlot().getStart(), reservation.getTimeSlot().getDuration());

            String qrData = String.format("""
                             Email: %s
                             Appointment start date: %s
                             Appointment end date: %s
                             Equipment: %s""",
                    reservation.getUser().getUsername(),
                    formattedStartDate,
                    formatDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()),
                    reservation.getEquipment().getName());

            int size = 200;
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, size, size, hints);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    private static String formatDate(LocalDateTime dateTime) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dateFormat.format(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
    }

    private static Date calculateEndDate(LocalDateTime startDate, int duration) {
        long endTimeMillis = startDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() + (long) duration * 60 * 1000;
        return new Date(endTimeMillis);
    }
}