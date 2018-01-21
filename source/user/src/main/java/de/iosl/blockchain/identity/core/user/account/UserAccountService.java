package de.iosl.blockchain.identity.core.user.account;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import de.iosl.blockchain.identity.core.shared.account.AccountService;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class UserAccountService extends AccountService {

    public byte[] getQRCode(int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            StringBuilder stringBuilder = new StringBuilder()
                    .append(keyChain.getAccount().getAddress())
                    .append(":")
                    .append(KeyConverter.from(keyChain.getRsaKeyPair().getPublic()).toBase64())
                    .append(":")
                    .append(keyChain.getRegisterSmartContractAddress());

            if (stringBuilder.toString().getBytes().length > width * height) {
                throw new ServiceException("Content does not fit into QR code size.", HttpStatus.UNPROCESSABLE_ENTITY);
            }

            BitMatrix matrix = qrCodeWriter.encode(stringBuilder.toString(), BarcodeFormat.QR_CODE, width, height);
            BufferedImage image = bitMatrixToImage(matrix);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            return baos.toByteArray();
        } catch (WriterException | IOException e) {
            throw new ServiceException(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private BufferedImage bitMatrixToImage(@NonNull BitMatrix bitMatrix) {
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); // create an empty image

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                image.setRGB(i, j, bitMatrix.get(i, j) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
        return image;
    }
}
