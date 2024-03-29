package de.iosl.blockchain.identity.discovery.hearthbeat.db;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonArray;
import de.iosl.blockchain.identity.discovery.hearthbeat.data.Beat;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Component
public class BeatDB extends CouchbaseWrapper<Beat, String> {

    private final Bucket bucket;
    private final BeatRepository repository;

    @Autowired
    public BeatDB(BeatRepository repository, Bucket bucket) {
        super(repository);
        this.bucket = bucket;
        this.repository = repository;
    }

    public long getCounter(String ethID) {
        return bucket.counter(createCounterID(ethID), 0, 0L).content();
    }

    public long incrementCounter(String ethID) {
        return bucket.counter(createCounterID(ethID), 1L).content();
    }

    public void deleteCounter(String ethID) {
        bucket.remove(createCounterID(ethID));
    }

    public String createCounterID(String ethID) {
        return ethID + "_counter";
    }

    public List<Beat> findBeatsByEthIDAndCounterRange(String ethId, long from, long to) {
        JsonArray jsonArray = JsonArray.from(
                LongStream.range(from, to)
                          .boxed()
                          .map(val -> Beat.buildID(ethId, val))
                          .collect(Collectors.toList())
        );

        return repository.findByIds(jsonArray);
    }
}
