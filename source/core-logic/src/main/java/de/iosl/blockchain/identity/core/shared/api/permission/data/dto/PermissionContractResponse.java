package de.iosl.blockchain.identity.core.shared.api.permission.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.ClaimDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.SignedRequest;
import de.iosl.blockchain.identity.core.shared.api.permission.data.Closure;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionContractResponse {

    @NotNull
    private List<ClaimDTO> claims;

    @NotNull
    private List<SignedRequest<Closure>> signedClosures;

}
