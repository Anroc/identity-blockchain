package de.iosl.blockchain.identity.database.repository;

import de.iosl.blockchain.identity.database.model.Beer;
import org.springframework.data.repository.CrudRepository;

public interface BeerRepository extends CrudRepository<Beer,String>{
    Beer findById(String id);
}
