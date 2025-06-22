package com.epam.trainerworkloadservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trainers")
@CompoundIndex(name = "firstname-lastname", def = "{'firstname': 1, 'lastname': 1}")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trainer {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String firstname;

    private String lastname;

    private Boolean isActive;
}
