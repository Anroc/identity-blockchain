package de.iosl.blockchain.identity.core.user.permission.db;

import de.iosl.blockchain.identity.core.user.permission.data.PermissionRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRequestRepository extends CrudRepository<PermissionRequest, String> {
}
