package com.app.kokonut.company.company;

import com.app.kokonut.auth.dtos.CompanyEncryptDto;

/**
 * @author Woody
 * Date : 2022-12-22
 * Time :
 * Remark : 기존의 코코넛 프로젝트의 Company Sql 쿼리호출
 */
public interface CompanyRepositoryCustom {

    CompanyEncryptDto findByDataKey(Long companyId);

//    public void InsertCompany(HashMap<String, Object> paramMap);
//
//    public void UpdateCompany(HashMap<String, Object> paramMap);
//
//    public void UpdateadminIdOfCompany(HashMap<String, Object> paramMap);
//
//    public int SelectCompanyCountByBusinessNumber(HashMap<String, Object> paramMap);
//
//    public HashMap<String, Object> SelectCompanyByIdx(int idx);
//
//    public List<HashMap<String, Object>> SelectCompanyList(HashMap<String, Object> paramMap);
//
//    public void UpdateEncryptOfCompany(HashMap<String, Object> paramMap);
//
//    public int SelectCompanyListCount(HashMap<String, Object> paramMap);
//
//    public void UpdatePaymentInfo(HashMap<String, Object> paramMap);
//
//    public String SelectCustomUid(HashMap<String, Object> paramMap);
//
//    public void UpdatePaymentCancel(HashMap<String, Object> paramMap);
//
//    public void UpdatePaymentAutoInfo(HashMap<String, Object> paramMap);
//
//    public HashMap<String, Object> SelectCompanyByName(String companyName);
//
//    public void UpdateDormantAccumulate(HashMap<String, Object> paramMap);
//
//    public void DeleteBycompanyId(Long companyId);
//
//    public void UpdateStopService(HashMap<String, Object> paramMap);
//
//    public void UpdateValidEndThreeDays(int idx);
//
//    public List<HashMap<String, Object>> SelectCompanySendMessageList(HashMap<String, Object> paramMap);
//
//    public int SelectCompanySendMessageListCount(HashMap<String, Object> paramMap);

}