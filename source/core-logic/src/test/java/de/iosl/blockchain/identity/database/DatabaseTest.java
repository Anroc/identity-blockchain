package de.iosl.blockchain.identity.database;

import de.iosl.blockchain.identity.database.repository.BeerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DatabaseTest {
    @Autowired
    private BeerRepository repository;

    @Test
    public void getBeer(){
        repository.findById("1");
    }
}
