package de.iosl.blockchain.identity.core.shared.api.data.dto;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicEthereumDTO {

    @NotBlank
    @Field
    private String ethID;

}
