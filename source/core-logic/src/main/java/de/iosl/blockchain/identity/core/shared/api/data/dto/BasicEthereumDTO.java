package de.iosl.blockchain.identity.core.shared.api.data.dto;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicEthereumDTO implements Serializable {

    private static final long serialVersionUID = 22749196824329013L;

    @NotBlank
    @Field
    private String ethID;

}
