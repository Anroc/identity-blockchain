package de.iosl.blockchain.identity.core.register;

import de.iosl.blockchain.identity.core.BasicMockSuite;
import de.iosl.blockchain.identity.discovery.registry.data.Payload;
import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;

public class DiscoveryClientTest extends BasicMockSuite {

	@Mock
	private DiscoveryClientAdapter discoveryClientAdapter;

	@InjectMocks
	private DiscoveryClient discoveryClient;

	@Test
	public void getEntriesTest() {
		doReturn(Lists.emptyList()).when(discoveryClientAdapter).getEntries(any(Map.class));

		List<Payload> res = discoveryClient.getEntries(null);
		assertThat(res).isEmpty();
	}
}
