package de.iosl.blockchain.identity.discovery.registry.data;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.data.couchbase.core.mapping.Document;
import org.web3j.crypto.Sign;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class ECSignature {
	@Field
	private String r;
	@Field
	private String s;
	@Field
	private byte v;

	public static ECSignature fromSignatureData(Sign.SignatureData signatureData) {
		return new ECSignature(
				new String(Base64.encode(signatureData.getR())),
				new String(Base64.encode(signatureData.getS())),
				signatureData.getV());
	}

	public Sign.SignatureData toSignatureData() {
		return new Sign.SignatureData(v, Base64.decode(getR().getBytes()), Base64.decode(getS().getBytes()));
	}

}
