package state.identity.blockchain.iosl.de.blockidclientqrscanner.data;

public class ApiRequest {

    private Payload payload;
    private ECSignature signature;

    public ApiRequest(Payload payload, ECSignature signature) {
        this.payload = payload;
        this.signature = signature;
    }

    public ApiRequest() {
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public ECSignature getSignature() {
        return signature;
    }

    public void setSignature(ECSignature signature) {
        this.signature = signature;
    }
}
