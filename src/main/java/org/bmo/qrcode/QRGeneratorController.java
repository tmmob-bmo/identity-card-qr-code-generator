package org.bmo.qrcode;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;

@RestController
@RequestMapping("/generate")
public class QRGeneratorController {

    private final QRService qrGeneratorService;

    public QRGeneratorController(QRService qrGeneratorService) {
        this.qrGeneratorService = qrGeneratorService;
    }

    @GetMapping(produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> printQrCode(@RequestParam(value = "tckn") String identityNumber) {

        ByteArrayOutputStream qrOutputStream = qrGeneratorService.generateOne(identityNumber);
        byte[] qrCodeBytes = qrOutputStream.toByteArray();
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(qrCodeBytes);
    }

    @GetMapping("/all")
    public ResponseEntity<String> generateAllQrCodes() {
        qrGeneratorService.generate();
        return ResponseEntity.ok().body("done");
    }
}
