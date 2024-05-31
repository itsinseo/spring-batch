package org.example.springbatch.batch;

import lombok.extern.slf4j.Slf4j;
import org.example.springbatch.entity.Person;
import org.example.springbatch.entity.User;
import org.springframework.batch.item.ItemProcessor;

import java.util.Date;

@Slf4j
public class PersonToUserItemProcessor implements ItemProcessor<Person, User> {

    @Override
    public User process(final Person person) {
        final String firstName = person.firstName().toUpperCase();
        final String lastName = person.lastName().toUpperCase();
        final Date createdDate = new Date();

        final User transformedPerson = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .createdDate(createdDate)
                .build();

        log.info("Converting ({}) into ({})", person, transformedPerson);

        return transformedPerson;
    }

}
