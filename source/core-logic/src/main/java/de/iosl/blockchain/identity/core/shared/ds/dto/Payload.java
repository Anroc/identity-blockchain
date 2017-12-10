package de.iosl.blockchain.identity.core.shared.ds.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Payload {

    @NotBlank
    private String ethID;

}
