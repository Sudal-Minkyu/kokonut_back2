package com.app.kokonut.company.company;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.company.companyitem.CompanyItemService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v2/api/Company")
public class CompanyRestController {

    private final CompanyItemService companyItemService;

    @Autowired
    public CompanyRestController(CompanyItemService companyItemService) {
        this.companyItemService = companyItemService;
    }

    @ApiOperation(value="기본 카테고리 항목을 가져온다.", notes="" +
            "1. 기본으로 제공하는 카테고리를 조회한다." +
            "2. DB내에 추가된 카테고리와 해당 카테고리의 항목을 보내준다.")
    @GetMapping(value = "/categoryList") // -> 추가된 카테고리 리스트가져오기
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> categoryList() {
        return companyItemService.categoryList();
    }

    @ApiOperation(value="추가 카테고리 항목을 가져온다.", notes="" +
            "1. cpCode를 통해 추가된 카테고리를 조회한다." +
            "2. 결과값을 보낸다.")
    @GetMapping(value = "/addItemList") // -> 추가된 카테고리 리스트가져오기
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> addItemList() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.addItemList(jwtFilterDto);
    }

    @ApiOperation(value="추가 카테고리의 항목을 추가한다.", notes="" +
            "1. 항목의 이름, 암호화여부의 데이터를 받는다." +
            "2. 해당 항목을 저정한다.")
    @PostMapping(value = "/saveItem") // -> 추가 카테고리의 항목을 추가한다.
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> addCategoryList(@RequestParam(name="ciName", defaultValue = "") String ciName,
                                                              @RequestParam(name="ciSecurity", defaultValue = "") Integer ciSecurity) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.saveItem(jwtFilterDto, ciName, ciSecurity);
    }

    @ApiOperation(value="테이블을 추가한다.", notes="" +
            "1. 추가할 테이블명을 받는다." +
            "2. 해당 테이블을 저정한다.")
    @PostMapping(value = "/userTableSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> userTableSave(@RequestParam(name="ctDesignation", defaultValue = "") String ctDesignation) {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.userTableSave(jwtFilterDto, ctDesignation);
    }

    @ApiOperation(value="모든 테이블을 가져온다.", notes="" +
            "1. cpCode를 통해 추가된 카테고리를 조회한다." +
            "2. 결과값을 보낸다.")
    @GetMapping(value = "/userTableList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> userTableList() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.userTableList(jwtFilterDto);
    }

}
