package de.iosl.blockchain.identity.core.shared.claims.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.Dictionary;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Payload {

    private Dictionary content;

}
