package de.iosl.blockchain.identity.core.user.account;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.ds.registry.DiscoveryClient;
import de.iosl.blockchain.identity.core.shared.eba.EBAInterface;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.core.shared.keychain.KeyChainService;
import de.iosl.blockchain.identity.core.shared.keychain.data.KeyInfo;
import de.iosl.blockchain.identity.crypt.CryptEngine;
import de.iosl.blockchain.identity.crypt.KeyConverter;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyPair;

@Service
public class AccountService {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private KeyChain keyChain;
    @Autowired
    private KeyChainService keyChainService;
    @Autowired
    private EBAInterface ebaInterface;


    public String register(String password) throws IOException {
        keyChainService.createDir(keyChainService.getDefaultWalletDir());

        Account account = ebaInterface.createWallet(password, Paths.get(KeyChain.WALLET_DIR));
        keyChain.setAccount(account);

        KeyPair keyPair = createKeyPair(password, account.getFile().getAbsolutePath());
        keyChain.setRsaKeyPair(keyPair);

        registerToDS();
        keyChain.setRegistered(true);

        return keyChain.getAccount().getAddress();
    }

    public String login(String password) throws IOException {
        KeyInfo keyInfo = readKeyPair(password);
        keyChain.setRsaKeyPair(keyInfo.getKeyPair());

        Account account = ebaInterface.accessWallet(password, Paths.get(keyInfo.getAccountPath()).toFile());
        keyChain.setAccount(account);

        registerToDS();
        keyChain.setRegistered(true);

        return keyChain.getAccount().getAddress();
    }

    public void logout() {
        keyChain.setRegistered(false);
        keyChain.setAccount(null);
        keyChain.setRsaKeyPair(null);
    }

    private KeyInfo readKeyPair(String password) throws IOException {
        return keyChainService.readKeyChange(keyChainService.getDefaultWalletFile(), password);
    }

    private KeyPair createKeyPair(String password, String absolutePath) throws IOException {
        KeyPair keyPair = CryptEngine.generate()
                .with(1024)
                .string()
                .rsa()
                .getAsymmetricCipherKeyPair();

        keyChainService.saveKeyChain(keyPair, keyChainService.getDefaultWalletFile(), password, absolutePath);
        return keyPair;
    }

    private void registerToDS() {
        discoveryClient.register(
                keyChain.getAccount().getAddress(),
                KeyConverter.from(keyChain.getRsaKeyPair().getPublic()).toBase64(),
                keyChain.getAccount().getPrivateKey());
    }

    public byte[] getQRCode(int width, int height) {
        if( keyChain.getAccount() == null) {
            throw new ServiceException(HttpStatus.FORBIDDEN);
        }

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            StringBuilder stringBuilder = new StringBuilder()
                    .append(keyChain.getAccount().getAddress())
                    .append(":")
                    .append(KeyConverter.from(keyChain.getRsaKeyPair().getPublic()).toBase64())
                    .append(":")
                    .append("0x1231231981238123"); // TODO: add Smart Contract address here

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
