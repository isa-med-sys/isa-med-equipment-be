package com.isa.med_equipment.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.isa.med_equipment.model.Equipment;
import com.isa.med_equipment.model.Reservation;
import com.isa.med_equipment.model.TimeSlot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QRCodeGenerator {

    public static byte[] generateReservationQRCode(Reservation reservation) {
        try {
            String formattedStartDate = formatDate(reservation.getTimeSlot().getStart());
            Date endDate = calculateEndDate(reservation.getTimeSlot().getStart(), (int) TimeSlot.DURATION.toMinutes());
            String equipmentNames =  reservation.getEquipment().stream()
                    .map(Equipment::getName)
                    .collect(Collectors.joining(", "));

            String qrData = String.format("""
                    Reservation Number: %s
                    Client: %s
                    
                    Pickup Date: %s - %s
                    Equipment: %s
                    
                    Admin Contact: %s
                    
                    %s""",
                    reservation.getId(),
                    reservation.getUser(),
                    formattedStartDate,
                    formatDate(endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()),
                    equipmentNames,
                    reservation.getTimeSlot().getAdmin(),
                    reservation.getTimeSlot().getAdmin().getCompany()
            );


            ByteArrayOutputStream byteArrayOutputStream = getByteArrayOutputStream(qrData);

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            return null;
        }
    }

    private static ByteArrayOutputStream getByteArrayOutputStream(String qrData) throws WriterException, IOException {
        int size = 200;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrData, BarcodeFormat.QR_CODE, size, size, hints);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        return byteArrayOutputStream;
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