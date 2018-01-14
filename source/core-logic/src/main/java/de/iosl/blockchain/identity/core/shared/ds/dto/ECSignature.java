package de.iosl.blockchain.identity.core.shared.ds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.hibernate.validator.constraints.NotBlank;
import org.web3j.crypto.Sign;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ECSignature {

    @NotBlank
    private String r;
    @NotBlank
    private String s;
    private byte v;

    public static ECSignature fromSignatureData(
            Sign.SignatureData signatureData) {
        return new ECSignature(
                new String(Base64.encode(signatureData.getR())),
                new String(Base64.encode(signatureData.getS())),
                signatureData.getV());
    }

    public Sign.SignatureData toSignatureData() {
        return new Sign.SignatureData(v, Base64.decode(getR().getBytes()),
                Base64.decode(getS().getBytes()));
    }

}
