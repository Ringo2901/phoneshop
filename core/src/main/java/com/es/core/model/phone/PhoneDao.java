package com.es.core.model.phone;

import com.es.core.enums.SortField;
import com.es.core.enums.SortOrder;

import java.util.List;
import java.util.Optional;

public interface PhoneDao {
    Optional<Phone> get(Long key);
    void save(Phone phone);

    List<Phone> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query);

    Long numberByQuery(String query);
    Optional<Phone> get(String model);
}
