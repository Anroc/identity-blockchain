package de.iosl.blockchain.identity.core.shared.ds.beats;

import de.iosl.blockchain.identity.core.shared.BasicMockSuite;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.core.shared.ds.beats.data.Beat;
import de.iosl.blockchain.identity.core.shared.ds.beats.data.HeartBeatRequest;
import de.iosl.blockchain.identity.core.shared.ds.dto.ECSignature;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.web3j.crypto.Sign;

import java.io.File;
import java.math.BigInteger;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class HeartBeatServiceTest extends BasicMockSuite {

    @Mock
    private HeartBeatAdapter heartBeatAdapter;
    @Mock
    private KeyChain keyChain;
    @Mock
    private EthereumSigner ethereumSigner;
    @Spy
    @InjectMocks
    private HeartBeatService heartBeatService;

    @Test
    public void testBeat() {
        ECSignature ecSignature = mock(ECSignature.class);
        Beat beat = new Beat("ID", 0L, new Date(), ecSignature, new HeartBeatRequest(
                "ethID",
                "/myEndpoint/ethID"
        ));
        Account account = new Account("0x123", BigInteger.TEN, BigInteger.TEN, mock(File.class),null);

        doReturn(ethereumSigner).when(heartBeatService).getSigner();
        doReturn(account).when(keyChain).getAccount();
        doReturn(true).when(keyChain).isRegistered();
        doReturn(mock(Sign.SignatureData.class)).when(ecSignature).toSignatureData();
        doReturn(Lists.newArrayList(beat)).when(heartBeatAdapter).beat(anyString(), eq(0L), eq(Long.MAX_VALUE));
        doReturn(true).when(ethereumSigner).verifySignature(any(), any(), anyString());

        heartBeatService.beat();

        assertThat(heartBeatService.getBeatCounter()).isEqualTo(1L);

    }

}