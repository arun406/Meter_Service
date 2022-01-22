package com.aktimetrics.referencedata.service;

import com.aktimetrics.referencedata.model.StepDefinition;
import com.aktimetrics.referencedata.repository.StepDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StepDefinitionService {

    private final StepDefinitionRepository repository;

    /**
     * Saves a new Step Definition
     *
     * @param stepDefinition
     * @return
     */
    public StepDefinition add(StepDefinition stepDefinition) {
        repository.save(stepDefinition);
        return stepDefinition;
    }

    /**
     * Returns all Step Definitions
     *
     * @return
     */
    public List<StepDefinition> list() {
        return this.repository.findAll();
    }

    /**
     * @return
     */
    public StepDefinition findByStepCode(String tenant, String stepCode) {
        return this.repository.findByStepCode(tenant, stepCode);
    }
}
