package com.app.kokonut.company.company;

import com.app.kokonut.auth.jwt.SecurityUtil;
import com.app.kokonut.auth.jwt.dto.JwtFilterDto;
import com.app.kokonut.company.companyitem.CompanyItemService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/v2/api/Company")
public class CompanyRestController {

    private final CompanyItemService companyItemService;

    @Autowired
    public CompanyRestController(CompanyItemService companyItemService) {
        this.companyItemService = companyItemService;
    }

//  @@@@@@@@@@@@@@@@@@@@@@@@@ 개인정보 항목관리 페이지 카테고리 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

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
    public ResponseEntity<Map<String,Object>> saveItem(@RequestParam(name="ciName", defaultValue = "") String ciName,
                                                              @RequestParam(name="ciSecurity", defaultValue = "") Integer ciSecurity) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.saveItem(jwtFilterDto, ciName, ciSecurity);
    }

    @ApiOperation(value="추가 카테고리의 항목을 수정한다.", notes="" +
            "1. 해당 항목의 고유ID, 항목의 이름 데이터를 받는다." +
            "2. 해당 항목을 조회하고 존해하면 받은 이름으로 수정한다.")
    @PostMapping(value = "/updateItem") // -> 추가 카테고리의 수정을 추가한다.
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> updateItem(@RequestParam(name="ciId", defaultValue = "") Long ciId,
                                                         @RequestParam(name="ciName", defaultValue = "") String ciName) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.updateItem(ciId, ciName, jwtFilterDto);
    }

    @ApiOperation(value="추가 카테고리의 항목을 삭제한다.", notes="" +
            "1. 삭제할 항목의 고유ID 데이터를 받는다." +
            "2. 해당 항목을 조회하여 삭제한다.")
    @PostMapping(value = "/deleteItem") // -> 추가 카테고리의 항목을 삭제한다.
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> deleteItem(@RequestParam(name="ciId", defaultValue = "") Long ciId) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.deleteItem(ciId, jwtFilterDto);
    }

//  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



//  @@@@@@@@@@@@@@@@@@@@@@@@@ 개인정보 테이블 정보 호출 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    @ApiOperation(value="테이블을 추가한다.", notes="" +
            "1. 추가할 테이블명을 받는다." +
            "2. 해당 테이블을 저정한다.")
    @PostMapping(value = "/userTableSave")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> userTableSave(@RequestParam(name="ctDesignation", defaultValue = "") String ctDesignation) throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.userTableSave(jwtFilterDto, ctDesignation);
    }

    @ApiOperation(value="모든 테이블을 가져온다.", notes="" +
            "1. cpCode를 통해 추가된 카테고리를 조회한다." +
            "2. 결과값을 보낸다.")
    @GetMapping(value = "/userTableList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> userTableList() throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.userTableList(jwtFilterDto);
    }

    @ApiOperation(value="개인정보검색용 테이블리스트를 가져온다.", notes="")
    @GetMapping(value = "/privacyTableList")
    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
    public ResponseEntity<Map<String,Object>> privacyTableList() throws IOException {
        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
        return companyItemService.privacyTableList(jwtFilterDto);
    }

//  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


//  @@@@@@@@@@@@@@@@@@@@@@@@@ 서비스설정 호출 관련 API @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

//    @ApiOperation(value="해외로그인 차단 서비스 설정", notes="")
//    @GetMapping(value = "/setting")
//    @ApiImplicitParam(name ="Authorization",  value="JWT Token",required = true, dataTypeClass = String.class, paramType = "header", example = "jwtKey")
//    public ResponseEntity<Map<String,Object>> setting() throws IOException {
//        JwtFilterDto jwtFilterDto = SecurityUtil.getCurrentJwt();
//        return companyItemService.setting(jwtFilterDto);
//    }




//  @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@



}
