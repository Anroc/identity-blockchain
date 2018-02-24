package de.iosl.blockchain.identity.core.provider.user.data.dto;

import de.iosl.blockchain.identity.core.shared.api.data.dto.PayloadDTO;
import de.iosl.blockchain.identity.core.shared.api.data.dto.ProviderDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnsignedClaimDTO {

    @NotBlank
    private String id;
    @Valid
    @NotNull
    private ProviderDTO provider;
    @Valid
    @NotNull
    private PayloadDTO claimValue;
}
