package org.bmo.qrcode;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class QRService {

    private static Logger LOG = LoggerFactory.getLogger(QRService.class);

    private static final String TCKN = "tckn";
    private static final String SICIL = "sicil";
    private static final String DOT = ".";
    private static final int WIDTH = 150;
    private static final int HEIGHT = 150;

    private String qrCodeFileName;

    private final JdbcTemplate jdbcTemplate;

    public QRService(JdbcTemplate jdbcTemplate, @Value("${qr-code-path}") String qrCodeExportPath) {
        this.jdbcTemplate = jdbcTemplate;
        this.qrCodeFileName = qrCodeExportPath + "%s." + ImageType.JPG.name();
    }

    public ByteArrayOutputStream generateOne(String identityNumber) {
        MemberInformation member = findMember(identityNumber);
        return generateOne(member.getIdentityNumber(), member.getRegistrationNumber());
    }

    private ByteArrayOutputStream generateOne(String identityNumber, String registrationNumber) {

        ByteArrayOutputStream outputStream = null;
        JSONObject memberInformation = prepareMemberQrInfo(identityNumber, registrationNumber);

        try {
            // generating the QR code
            outputStream = QRCode.from(memberInformation.toString())
                    .withSize(WIDTH, HEIGHT)
                    .to(ImageType.JPG)
                    .stream();

            LOG.info("QR Code generated for {}.", registrationNumber);
        } catch (Exception e) {
            LOG.error("Something went wrong {}", e.getMessage());
        }

        return outputStream;
    }

    public void generate(String identityNumber) {
        MemberInformation member = findMember(identityNumber);
        generateQRCodeImage(member.getIdentityNumber(), member.getRegistrationNumber());
    }

    public void generate() {
        listMembers().stream().forEach(item -> generateQRCodeImage(item.getIdentityNumber(), item.getRegistrationNumber()));
    }

    private void generateQRCodeImage(String identityNumber, String registrationNumber) {

        JSONObject memberInformation = prepareMemberQrInfo(identityNumber, registrationNumber);

        try {
            // preparing the filename and output stream
            String qrFileNameWithPath = String.format(qrCodeFileName, registrationNumber);
            OutputStream outputStream = new FileOutputStream(qrFileNameWithPath);

            // generating the QR code
            QRCode.from(memberInformation.toString())
                    .withSize(WIDTH, HEIGHT)
                    .to(ImageType.JPG)
                    .stream()
                    .writeTo(outputStream);

            LOG.info("QR Code generated for {}.", registrationNumber);

            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            LOG.error("Something went wrong {}", e.getMessage());
        }
    }

    private JSONObject prepareMemberQrInfo(String identityNumber, String registrationNumber) {
        JSONObject member = new JSONObject();
        member.put(TCKN, identityNumber);
        member.put(SICIL, registrationNumber);
        return member;
    }

    private List<MemberInformation> listMembers() {

        String activeMemberQuery = "select k.kimlikno, u.sicilno "
                + "     from oltp.kisi k  "
                + "         left join oltp.uye u on k.rid = u.kisiref "
                + "     where k.aktif = 1 "
                + "         and u.uyedurum in (2, 3, 4, 5, 6, 8) "
                + "         and u.uyetip = 1 "
                + "     order by u.sicilno asc";

        return jdbcTemplate.queryForList(activeMemberQuery).stream()
                .map(record -> new MemberInformation(record.get("kimlikno").toString(), record.get("sicilno").toString()))
                .collect(toList());
    }

    private MemberInformation findMember(String identityNumber) {

        String queryByIdentityNumber = "select k.kimlikno, u.sicilno "
                + "     from oltp.kisi k  "
                + "         left join oltp.uye u on k.rid = u.kisiref "
                + "     where k.aktif = 1 "
                + "         and k.kimlikno = ? "
                + "         and u.uyedurum in (2, 3, 4, 5, 6, 8) "
                + "         and u.uyetip = 1 ";

        return jdbcTemplate.queryForObject(queryByIdentityNumber, new Object[]{Long.valueOf(identityNumber)},
                (rs, rowNum) ->
                        new MemberInformation(
                                String.valueOf(rs.getLong("kimlikno")),
                                rs.getString("sicilno")
                        ));
    }
}
