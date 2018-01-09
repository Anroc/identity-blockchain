package state.identity.blockchain.iosl.de.blockidclientqrscanner.data;

import android.util.Base64;

import org.web3j.crypto.Sign;

public class ECSignature {

    private String r;
    private String s;
    private byte v;

    public ECSignature(String r, String s, byte v) {
        this.r = r;
        this.s = s;
        this.v = v;
    }

    public ECSignature() {
    }

    public static ECSignature fromSignatureData(Sign.SignatureData signatureData) {
        return new ECSignature(
                new String(Base64.encode(signatureData.getR(), Base64.DEFAULT)),
                new String(Base64.encode(signatureData.getS(), Base64.DEFAULT)),
                signatureData.getV());
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public byte getV() {
        return v;
    }

    public void setV(byte v) {
        this.v = v;
    }
}
