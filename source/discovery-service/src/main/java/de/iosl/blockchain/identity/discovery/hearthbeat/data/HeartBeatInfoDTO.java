package de.iosl.blockchain.identity.discovery.hearthbeat.data;

import de.iosl.blockchain.identity.discovery.data.ECSignature;
import de.iosl.blockchain.identity.discovery.data.RequestDTO;
import de.iosl.blockchain.identity.discovery.registry.data.RegistryEntryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeartBeatInfoDTO extends RequestDTO<RegistryEntryDTO> {

    private LifeState lifeState;

    public HeartBeatInfoDTO(
            RegistryEntryDTO payload,
            ECSignature ecSignature,
            LifeState lifeState) {
        super(payload, ecSignature);
        this.lifeState = lifeState;
    }
}
