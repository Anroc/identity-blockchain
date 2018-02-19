package de.iosl.blockchain.identity.core.user.permission.db;

import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
import de.iosl.blockchain.identity.lib.wrapper.CouchbaseWrapper;
import org.springframework.stereotype.Component;

@Component
public class PermissionRequestDB extends CouchbaseWrapper<PermissionRequest, String> {

    public PermissionRequestDB(PermissionRequestRepository repository) {
        super(repository);
    }
}
