package com.app.kokonut.company.company;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.company.companycategory.CompanyCategoryService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v2/api/Company")
public class CompanyRestController {

    private final CompanyCategoryService companyCategoryService;

    @Autowired
    public CompanyRestController(CompanyCategoryService companyCategoryService) {
        this.companyCategoryService = companyCategoryService;
    }

    @ApiOperation(value="기본 카테고리 항목을 가져온다.", notes="" +
            "1. 기본으로 제공하는 카테고리를 조회한다." +
            "2. DB내에 추가된 카테고리와 해당 카테고리의 항목을 보내준다.")
    @GetMapping(value = "/categoryList") // -> 추가된 카테고리 리스트가져오기
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> categoryList() {
        return companyCategoryService.categoryList();
    }

    @ApiOperation(value="추가 카테고리 항목을 가져온다.", notes="" +
            "1. cpCode를 통해 추가된 카테고리를 조회한다." +
            "2. 결과값을 보낸다.")
    @GetMapping(value = "/addCategoryList") // -> 추가된 카테고리 리스트가져오기
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> addCategoryList() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyCategoryService.addCategoryList(jwtFilterDto);
    }

    @ApiOperation(value="유저테이블 항목을 가져온다.", notes="" +
            "1. cpCode를 통해 추가된 카테고리를 조회한다." +
            "2. 결과값을 보낸다.")
    @GetMapping(value = "/userTableList") // -> 추가된 카테고리 리스트가져오기
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> userTableList() {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyCategoryService.userTableList(jwtFilterDto);
    }


}
