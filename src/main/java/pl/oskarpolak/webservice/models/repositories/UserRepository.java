package pl.oskarpolak.webservice.models.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.oskarpolak.webservice.models.UserModel;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Integer>{
    Optional<UserModel> findByNick(String nick);
    boolean existsByNick(String nick);
    void deleteByNick(String nick);
}

