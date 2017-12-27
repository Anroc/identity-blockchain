package state.identity.blockchain.iosl.de.blockidclientqrscanner;

import android.content.res.AssetManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.List;

import state.identity.blockchain.iosl.de.blockidclientqrscanner.data.ApiRequest;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.data.ECSignature;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.data.Payload;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.sign.EthereumSigner;

public class Logic {

    private final EthereumSigner ethereumSigner = new EthereumSigner();
    private final Credentials credentials;
    private final GovClientAdapter govClientAdapter;

    public Logic() {
        this.govClientAdapter = FeignAdapterFactory.createAdapter(GovClientAdapter.class);
        this.credentials = Credentials.create(
                new ECKeyPair(
                        new BigInteger("41612168455812980506052176742578745810404045351404415736689711698647231665955"),
                        new BigInteger("4118619166090737025028487494221820666512560755856621214718530857044966040608557586289080215606045920805620818404142820501451756584736937580076882022108433")
                )
        );
    }

    public ApiRequest approveUser(String contents) {

        String[] content = contents.split(":");

        if(content.length != 3) {
            throw new RuntimeException("Not right format: " + contents);
        }

        Payload payload = new Payload(content[0], content[1], content[2]);

        ApiRequest apiRequest = new ApiRequest(
                payload,
                ECSignature.fromSignatureData(
                        ethereumSigner.sign(payload, credentials.getEcKeyPair())
                )
        );
        return apiRequest;
    }

    public void doPost(String userId, ApiRequest apiRequest) {
        try {
            Log.i("HELP", userId + " and " + new ObjectMapper().writeValueAsString(apiRequest));
        } catch (JsonProcessingException e) {
            Log.e("HELP", e.getMessage());
        }
        govClientAdapter.registerUser(userId, apiRequest);
    }

    public List<String> doGet(String givenName, String familyName) {
        return govClientAdapter.findUserIds(givenName, familyName);
    }
}
