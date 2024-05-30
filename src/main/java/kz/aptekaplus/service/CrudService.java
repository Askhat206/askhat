package kz.aptekaplus.service;

import java.util.UUID;

public interface CrudService<ENTITY, REQUEST, RESPONSE> {

    RESPONSE getById(UUID id);
    RESPONSE create(REQUEST requestDto);
    RESPONSE update(UUID id, REQUEST requestDto);
    void delete(UUID id);

    ENTITY getEntityById(UUID id);

}
