package de.iosl.blockchain.identity.discovery.registry.repository;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.Delete;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.Statement;
import de.iosl.blockchain.identity.lib.exception.ServiceException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Optional;

import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;

@Slf4j
@RequiredArgsConstructor
public class CouchbaseWrapper<T, ID extends Serializable> {

	private final CrudRepository<T, ID> repository;

	@Autowired
	private Bucket bucket;

	public Optional<T> findEntity(@NonNull ID id) {
		return Optional.ofNullable(repository.findOne(id));
	}

	public boolean exist(@NonNull ID id) {
		return repository.exists(id);
	}

	public T upsert(T entity) {
		return repository.save(entity);
	}

	public void deleteAll(Class<T> clazz) {
		Statement statement =
				Delete.deleteFrom(i(bucket.name()))
				.where(i("_class").eq(s(clazz.getCanonicalName())));

		query(statement);
	}

	private N1qlQueryResult query(Statement statement) {
		N1qlQueryResult result = bucket.query(statement);
		if(! result.finalSuccess()) {
			throw new ServiceException("Could not delete bucket. Reasons: %s",
					HttpStatus.INTERNAL_SERVER_ERROR,
					Arrays.toString(result.errors().toArray()));
		}
		return result;
	}
}
