package de.iosl.blockchain.identity.core.shared.ds.registry;

import de.iosl.blockchain.identity.core.shared.BasicMockSuite;
import de.iosl.blockchain.identity.core.shared.config.BlockchainIdentityConfig;
import de.iosl.blockchain.identity.core.shared.config.ServiceConfig;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.lib.dto.Payload;
import de.iosl.blockchain.identity.lib.dto.RegistryEntryDTO;
import de.iosl.blockchain.identity.lib.dto.RequestDTO;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class DiscoveryClientTest extends BasicMockSuite {

    private static final String ETH_ID = "ETH_ID";
    @Mock
    private DiscoveryClientAdapter discoveryClientAdapter;
    @Mock
    private BlockchainIdentityConfig config;
    @Mock
    private EthereumSigner signer;
    @Spy
    @InjectMocks
    private DiscoveryClient discoveryClient;
    private RequestDTO<RegistryEntryDTO> registryEntry;
    private RegistryEntryDTO payload;
    private ECSignature ecSignature;

    @Before
    public void setup() {
        payload = new RegistryEntryDTO();
        payload.setEthID(ETH_ID);
        payload.setDomainName("localhost");
        payload.setPort(8080);
        payload.setPublicKey("SOME_PUBLIC_KEY");

        EthereumSigner signer = new EthereumSigner();
        BigInteger ecPrivateKey = BigInteger.TEN;

        ecSignature = ECSignature.fromSignatureData(
                signer.sign(payload, ECKeyPair.create(ecPrivateKey))
        );

        registryEntry = new RequestDTO<>(payload, ecSignature);

        doReturn(signer).when(discoveryClient).getSigner();
    }

    @Test
    public void getEntriesTest() {
        doReturn(Lists.newArrayList(registryEntry)).when(discoveryClientAdapter)
                .getEntries(any(Map.class));
        doReturn(true).when(discoveryClient).isSignatureValid(registryEntry);

        List<Payload> res = discoveryClient.getEntries(null);
        assertThat(res).contains(payload);
    }

    @Test
    public void getEntryTest() {
        doReturn(Optional.of(registryEntry)).when(discoveryClientAdapter)
                .getEntry(ETH_ID);
        doReturn(true).when(discoveryClient).isSignatureValid(registryEntry);

        Payload res = discoveryClient.getEntry(ETH_ID);

        assertThat(res).isEqualTo(payload);
    }

    @Test
    public void registerTest() {
        ServiceConfig serviceConfig = mock(ServiceConfig.class);

        doNothing().when(discoveryClientAdapter)
                .register(any(RequestDTO.class));
        doReturn(serviceConfig).when(config).getCore();
        doReturn("localhost").when(serviceConfig).getAddress();
        doReturn(8080).when(serviceConfig).getPort();
        doReturn(ecSignature.toSignatureData()).when(signer)
                .sign(any(Payload.class), any(ECKeyPair.class));

        String ethID = "ETH_ID";
        String rsaPublicKey = "asdasdasd";
        BigInteger ecPrivateKey = BigInteger.TEN;

        discoveryClient.register(ethID, rsaPublicKey, ecPrivateKey);

        verify(discoveryClientAdapter).register(any(RequestDTO.class));
    }
}
