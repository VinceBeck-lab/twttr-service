package de.openknowledge.twttrService.api.rest.domain.account;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("MODERATOR")
public class Moderator extends Account{

    public Moderator () {
        // for JPA
    }

}
