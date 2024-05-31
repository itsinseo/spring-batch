package org.example.springbatch.util;

import lombok.RequiredArgsConstructor;
import org.example.springbatch.config.MongoConfig;
import org.example.springbatch.entity.User;
import org.example.springbatch.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoCrud {

    private final MongoConfig mongoConfig;
    private final UserRepository userRepository;

    // TODO: exception handling, bulk operations
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
