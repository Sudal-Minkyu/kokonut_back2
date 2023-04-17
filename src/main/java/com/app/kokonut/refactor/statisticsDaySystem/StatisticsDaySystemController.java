package com.app.kokonut.refactor.statisticsDaySystem;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "")
@Validated
@RestController
@RequestMapping("/statisticsDaySystem")
public class StatisticsDaySystemController {

    @Autowired
    private StatisticsDaySystemService statisticsDaySystemService;

}
