package com.app.kokonut.refactor.policy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PolicyService {

    @Autowired
    private PolicyRepository policyRepository;

}
