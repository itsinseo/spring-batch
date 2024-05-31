package org.example.springbatch.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@NoArgsConstructor
public class User {

    @Id
    public String id; // standard name for a MongoDB ID; _id or id

    private String firstName;
    private String lastName;
    private Date createdDate;

    @Builder
    public User(String firstName, String lastName, Date createdDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return String.format("User[%s %s %s]", firstName, lastName, createdDate);
    }
}
