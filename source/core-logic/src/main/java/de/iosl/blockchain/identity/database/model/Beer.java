package de.iosl.blockchain.identity.database.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.couchbase.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Beer {
    @Id
    private String brewery_id;
    @Field
    private String name;
}


/**
 * {
 "abv": 5.2,
 "brewery_id": "21st_amendment_brewery_cafe",
 "category": "British Ale",
 "description": "Traditional English E.S.B. made with English malt and hops. Fruity aroma with an imparted tart flavor brought about by replicating the water profile in England at Burton on Trent.",
 "ibu": 0,
 "name": "Potrero ESB",
 "srm": 0,
 "style": "Special Bitter or Best Bitter",
 "type": "beer",
 "upc": 0,
 "updated": "2010-07-22 20:00:20"
 }
 */
