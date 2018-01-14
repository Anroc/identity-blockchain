package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.BasicEthereumDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NestedSignedRequestDTO<T extends BasicEthereumDTO> extends BasicEthereumDTO {

    private SignedRequest<T> signedRequest;

    public NestedSignedRequestDTO(String ethID,
            SignedRequest<T> signedRequest) {
        super(ethID);
        this.signedRequest = signedRequest;
    }
}
