package de.iosl.blockchain.identity.core.shared.api;

public class ProviderAPIConstances {

    public static final String API_PATH = "/api";
    public static final String ETH_ID_PARAM = "ethID";

    private static final String CLAIM_PATH = "/claims";
    private static final String INFO_PATH = "/info";
    private static final String CLAIM_ID_PATH = "/claimID";
    private static final String USER_PATH = "/user/{" + ETH_ID_PARAM + "}";
    private static final String PERMISSION_CONTRACT_PATH = "/permission";

    public static final String ABSOLUTE_CLAIM_ATH = API_PATH + CLAIM_PATH;
    public static final String ABSOLUTE_INFO_PATH = API_PATH + INFO_PATH;
    public static final String ABSOLUTE_PPR_PATH = API_PATH + USER_PATH + PERMISSION_CONTRACT_PATH;

    public static final String ABSOLUTE_CLAIM_ID_PATH = API_PATH + USER_PATH + CLAIM_ID_PATH;
}
