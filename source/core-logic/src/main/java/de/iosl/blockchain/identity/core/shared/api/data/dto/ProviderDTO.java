package de.iosl.blockchain.identity.core.shared.api.data.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import de.iosl.blockchain.identity.core.shared.claims.data.Provider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder(alphabetic = true, value = {"ethID", "name"})
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
