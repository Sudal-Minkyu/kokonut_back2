package com.app.kokonut.common.realcomponent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.costexplorer.CostExplorerClient;
import software.amazon.awssdk.services.costexplorer.model.*;

/**
 * @author Woody
 * Date : 2023-08-02
 * Time :
 * Remark : AWS 금액 호출 유틸리티
 */
@Slf4j
@Service
public class AwsPaymentUtil {

    public static void main(String[] args) {
        CostExplorerClient ce = CostExplorerClient.builder().region(Region.AP_NORTHEAST_2).build();

        GetCostAndUsageRequest request = GetCostAndUsageRequest.builder()
                .timePeriod(DateInterval.builder()
                        .start("2021-01-01")
                        .end("2022-12-31")
                        .build())
                .granularity(Granularity.MONTHLY)
                .metrics("UnblendedCost")
                .build();

        GetCostAndUsageResponse response = ce.getCostAndUsage(request);

        for (ResultByTime result : response.resultsByTime()) {
            System.out.println("The amount you spent on AWS for month, " + result.timePeriod().start() + " is: " + result.total().get("UnblendedCost").amount());
        }
    }


//	// 리스트에 값을 담을때 유니크한 값을 반환해주는 함수
//	public static String generateUniqueName(String name, List<String> list) {
//		int count = 1;
//		String originalName = name;
//		while (list.contains(name)) {
//			count++;
//			name = originalName + count;
//		}
//		return name;
//	}

}
