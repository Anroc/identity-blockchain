package state.identity.blockchain.iosl.de.blockidclientqrscanner.data;

public class Payload {

    private String ethereumID;
    private String publicKey;
    private String registerContractAddress;

    public Payload(String ethereumID, String publicKey, String registerContractAddress) {
        this.ethereumID = ethereumID;
        this.publicKey = publicKey;
        this.registerContractAddress = registerContractAddress;
    }

    public String getEthereumID() {
        return ethereumID;
    }

    public void setEthereumID(String ethereumID) {
        this.ethereumID = ethereumID;
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
