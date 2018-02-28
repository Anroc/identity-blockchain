package state.identity.blockchain.iosl.de.blockidclientqrscanner.data;

public class Payload {

    private String ethID;
    private String publicKey;
    private String registerContractAddress;

    public Payload(String ethID, String publicKey, String registerContractAddress) {
        this.ethID = ethID;
        this.publicKey = publicKey;
        this.registerContractAddress = registerContractAddress;
    }

    public String getEthID() {
        return ethID;
    }

    public void setEthID(String ethID) {
        this.ethID = ethID;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getRegisterContractAddress() {
        return registerContractAddress;
    }

    public void setRegisterContractAddress(String registerContractAddress) {
        this.registerContractAddress = registerContractAddress;
    }
}
