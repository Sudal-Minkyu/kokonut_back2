package com.app.kokonut.refactor.policy;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "")
@Validated
@RestController
@RequestMapping("/policy")
public class PolicyController {

    @Autowired
    private PolicyService policyService;
}
