package org.example.eventmanager.events.repository;

import org.example.eventmanager.events.model.RegistrationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends CrudRepository<RegistrationEntity, Long> {
}
