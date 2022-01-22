package com.aktimetrics.referencedata.repository;

import com.aktimetrics.referencedata.model.ProcessDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProcessDefinitionRepository extends MongoRepository<ProcessDefinition, String> {
}
