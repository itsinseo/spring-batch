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
    private String code;

    @Builder
    public User(String firstName, String lastName, Date createdDate, String code) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdDate = createdDate;
        this.code = code;
    }

    @Override
    public String toString() {
        return String.format("User[%s %s %s %s]", firstName, lastName, createdDate, code);
    }
}
