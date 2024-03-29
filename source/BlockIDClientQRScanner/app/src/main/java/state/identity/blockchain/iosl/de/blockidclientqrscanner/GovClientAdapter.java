package state.identity.blockchain.iosl.de.blockidclientqrscanner;

import java.util.List;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import state.identity.blockchain.iosl.de.blockidclientqrscanner.data.ApiRequest;

public interface GovClientAdapter {

    @RequestLine("POST /users/{userId}/register")
    @Headers("Content-Type: application/json")
    void registerUser(@Param("userId") String userId, ApiRequest apiRequest);

    @RequestLine("GET /users/search?givenName={givenName}&familyName={familyName}")
    List<String> findUserIds(@Param("givenName") String givenName, @Param("familyName") String familyName);
}
