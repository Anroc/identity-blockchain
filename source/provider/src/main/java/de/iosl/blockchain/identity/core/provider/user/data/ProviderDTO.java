package de.iosl.blockchain.identity.core.provider.user.data;

import de.iosl.blockchain.identity.core.shared.claims.provider.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDTO {

    @NotBlank
    private String ethID;
    @NotBlank
    private String name;

    public ProviderDTO(@NonNull Provider provider) {
        this.ethID = provider.getEthID();
        this.name = provider.getName();
    }

    public Provider toProvider() {
        return new Provider(ethID, name);
    }
}
