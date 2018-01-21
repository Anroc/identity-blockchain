package de.iosl.blockchain.identity.core.shared.ds.beats;

import de.iosl.blockchain.identity.core.shared.BasicMockSuite;
import de.iosl.blockchain.identity.core.shared.KeyChain;
import de.iosl.blockchain.identity.lib.dto.beats.Beat;
import de.iosl.blockchain.identity.lib.dto.beats.EventType;
import de.iosl.blockchain.identity.lib.dto.beats.HeartBeatRequest;
import de.iosl.blockchain.identity.lib.dto.ECSignature;
import de.iosl.blockchain.identity.core.shared.eba.main.Account;
import de.iosl.blockchain.identity.crypt.sign.EthereumSigner;
import de.iosl.blockchain.identity.lib.dto.beats.SubjectType;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;

import java.io.File;
import java.math.BigInteger;
import java.util.Date;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
                "/myEndpoint/ethID",
                EventType.NEW_CLAIMS,
                SubjectType.URL
        ));
        Account account = new Account("0x123", BigInteger.TEN, BigInteger.TEN, mock(File.class), mock(Credentials.class));

        doReturn(ethereumSigner).when(heartBeatService).getSigner();
        doReturn(account).when(keyChain).getAccount();
        doReturn(true).when(keyChain).isRegistered();
        doReturn(mock(Sign.SignatureData.class)).when(ecSignature).toSignatureData();
        doReturn(Lists.newArrayList(beat)).when(heartBeatAdapter).beat(anyString(), eq(0L), eq(Long.MAX_VALUE));
        doReturn(true).when(ethereumSigner).verifySignature(any(), any(), anyString());
        doReturn(Optional.empty()).when(heartBeatService).findBeatCounter();
        doNothing().when(heartBeatService).persistBeatCounter(1L);

        heartBeatService.beat();

        verify(heartBeatService).findBeatCounter();
        verify(heartBeatService).persistBeatCounter(1L);

    }

    @Test
    public void testBeatWithSubscriber() {
        ECSignature ecSignature = mock(ECSignature.class);
        EventListener eventListener = mock(EventListener.class);

        Beat beat = new Beat("ID", 0L, new Date(), ecSignature, new HeartBeatRequest(
                "ethID",
                "/myEndpoint/ethID",
                EventType.NEW_CLAIMS,
                SubjectType.URL
        ));
        Account account = new Account("0x123", BigInteger.TEN, BigInteger.TEN, mock(File.class), mock(Credentials.class));
        Queue<EventListener> listenerQueue = new ConcurrentLinkedQueue<>();
        listenerQueue.add(eventListener);

        doReturn(ethereumSigner).when(heartBeatService).getSigner();
        doReturn(account).when(keyChain).getAccount();
        doReturn(true).when(keyChain).isRegistered();
        doReturn(mock(Sign.SignatureData.class)).when(ecSignature).toSignatureData();
        doReturn(Lists.newArrayList(beat)).when(heartBeatAdapter).beat(anyString(), eq(0L), eq(Long.MAX_VALUE));
        doReturn(true).when(ethereumSigner).verifySignature(any(), any(), anyString());
        doReturn(listenerQueue).when(heartBeatService).getEventListeners();
        doNothing().when(eventListener).trigger(any(Event.class), eq(EventType.NEW_CLAIMS));
        doReturn(Optional.empty()).when(heartBeatService).findBeatCounter();
        doNothing().when(heartBeatService).persistBeatCounter(1L);

        heartBeatService.beat();

        verify(heartBeatService).persistBeatCounter(1L);
        verify(eventListener, times(1)).trigger(any(Event.class), eq(EventType.NEW_CLAIMS));
    }

    @Test
    public void subscribe() {
        EventListener eventListener = mock(EventListener.class);

        heartBeatService.subscribe(eventListener);

        assertThat(heartBeatService.getEventListeners()).containsExactly(eventListener);
    }
}