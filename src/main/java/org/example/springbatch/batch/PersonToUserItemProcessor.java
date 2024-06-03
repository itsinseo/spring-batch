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
        final String firstName = person.getFirstName().toUpperCase();
        final String lastName = person.getLastName().toUpperCase();
        final Date createdDate = new Date();
        final String code = person.getInputSourceFileName().substring(0, 3);

        final User transformedPerson = User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .createdDate(createdDate)
                .code(code)
                .build();

        log.info(String.format("Converting (%s) into (%s) from file (%s)", person, transformedPerson, person.getInputSourceFileName()));

        return transformedPerson;
    }

}
