package com.app.kokonut.refactor.statisticsDay;

import com.app.kokonut.refactor.statisticsDay.dtos.StatisticsDayDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Api(tags = "")
@Validated
@RestController
@RequestMapping("/statisticsDay")
public class StatisticsDayController {

    @Autowired
    private StatisticsDayService statisticsDayService;

}
