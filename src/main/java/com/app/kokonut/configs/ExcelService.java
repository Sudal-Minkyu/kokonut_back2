package com.app.kokonut.configs;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND;

@Slf4j
@Service
public class ExcelService {
	
	public List<List<String>> read(InputStream is) {
		List<List<String>> data = new ArrayList<>();
		
		XSSFWorkbook workbook;
		try {
	        workbook = new XSSFWorkbook(is);

		    Sheet sheet = workbook.getSheetAt(0);
		    int rowCount = sheet.getPhysicalNumberOfRows();
		    
		    int maxCellCount = 0;
		    for(int r = 0; r < rowCount; r++){
			    Row row=sheet.getRow(r);
	        	int cellCount=row.getPhysicalNumberOfCells();
	        	if(cellCount > maxCellCount) maxCellCount = cellCount;
		    }
		    
		    for(int r = 0; r < rowCount; r++){
			    Row row=sheet.getRow(r);
			    
		        if(row !=null){
		        	List<String> rowData = new ArrayList<>();
		        					    
		            for(int c = 0; c < maxCellCount; c++){
		                Cell cell = row.getCell(c);
		                String value = "";
		                if(cell != null){
//			                cell.setCellType(Cell.CELL_TYPE_STRING);
			                value = cell.getStringCellValue();                
		                }

		                rowData.add(value);
		            }
		            data.add(rowData);
		        }
		    }
		} catch (EncryptedDocumentException | IOException e) {
			e.printStackTrace();
		}

		return data;
	}
	
	public void download(HttpServletRequest request, HttpServletResponse response, String fileName, List<String> headerList, List<List<String>> dataList) throws IOException {
		log.info("엑셀 다운로드 download 호출");

		Workbook workbook = new XSSFWorkbook();

		// 첫 번째 시트
		Sheet sheet = workbook.createSheet("data");

		// 폰트 설정
		Font headerFont = workbook.createFont();
		
		// 헤더 스타일 설정
		CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
		headerCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
		headerCellStyle.setFont(headerFont);

		// 헤더 보조 설명 스타일 설정
        CellStyle headerInfoCellStyle = sheet.getWorkbook().createCellStyle();
        headerInfoCellStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.index);
        headerInfoCellStyle.setFont(headerFont);
        
        // Data 스타일 설정
        CellStyle dataCellStyle = sheet.getWorkbook().createCellStyle();
        XSSFDataFormat dataFormat = (XSSFDataFormat) sheet.getWorkbook().createDataFormat();
        dataCellStyle.setDataFormat(dataFormat.getFormat("@"));

        // 회원 엑셀 양식 스타일 설정
        if(fileName.contains("회원 엑셀 양식")) {
            headerCellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SEA_GREEN.getIndex());
            headerCellStyle.setFillPattern(SOLID_FOREGROUND);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);

            headerCellStyle.setTopBorderColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
            headerCellStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
            headerCellStyle.setLeftBorderColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
            headerCellStyle.setRightBorderColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());

            headerInfoCellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_GREEN.getIndex());
            headerInfoCellStyle.setFillPattern(SOLID_FOREGROUND);
            headerInfoCellStyle.setAlignment(HorizontalAlignment.CENTER);

            headerInfoCellStyle.setBorderTop(BorderStyle.THIN);
            headerInfoCellStyle.setBorderBottom(BorderStyle.THIN);
            headerInfoCellStyle.setBorderLeft(BorderStyle.THIN);
            headerInfoCellStyle.setBorderRight(BorderStyle.THIN);

            headerInfoCellStyle.setTopBorderColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
            headerInfoCellStyle.setBottomBorderColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
            headerInfoCellStyle.setLeftBorderColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
            headerInfoCellStyle.setRightBorderColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        }
        
		// Header
        Row row0 = sheet.createRow(0);
        for (int i = 0; i < headerList.size(); i++) {
        	Cell cell = row0.createCell(i);
        	cell.setCellStyle(headerCellStyle);
        	cell.setCellValue(headerList.get(i));
			
        	// 해당 열에 text 포맷 스타일 지정
	        sheet.setDefaultColumnStyle(i, dataCellStyle);
		}

        // Data
        for (int i = 0; i < dataList.size(); i++) {
        	List<String> data = dataList.get(i);
        	
			Row row = sheet.createRow(i + 1);

			for (int j = 0; j < data.size(); j++) {
				Cell cell = row.createCell(j);
				cell.setCellValue(data.get(j));

				// 회원 엑셀 양식의 경우 보조 설명 관련
		        if(fileName.contains("회원 엑셀 양식")) {
					if(i==0) cell.setCellStyle(headerInfoCellStyle);
					else cell.setCellStyle(dataCellStyle);
				}
			}
		}

 		// CELL 사이즈 조정
 		autoSizeColumns(workbook);
 		
 		// 파일명 인코딩
 		String browser = "";
		String userAgent = request.getHeader("User-Agent");
		
		if (userAgent.contains("MSIE") || userAgent.contains("Trident"))   {
			browser = "MSIE";
		} else if (userAgent.contains("Opera") || userAgent.contains("OPR")) {
			browser = "Opera";
		} else if (userAgent.contains("Safari")) {
			if(userAgent.contains("Chrome")){
				browser = "Chrome";	
			}else{
				browser = "Safari";	
			}
		} else {
			browser = "Firefox";
		}
		
		if (browser.equals("MSIE")) {
			fileName = new String(fileName.getBytes("euc-kr"), StandardCharsets.ISO_8859_1);
        } else if (browser.equals("Firefox") || browser.equals("Safari") || browser.equals("Opera")) {
        	fileName =  '"' + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1) + '"';
        } else {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < fileName.length(); i++)
            {
                char c = fileName.charAt(i);
                if (c > '~') {
                    sb.append(URLEncoder.encode("" + c, StandardCharsets.UTF_8));
                } else {
                    sb.append(c);
                }
            }
            fileName = sb.toString();
        }
		
		if(fileName.length() == 0){
			fileName = "excel.xls";
		}
		
        // 컨텐츠 타입과 파일명 지정
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\";");
        response.setHeader("Content-Transfer-Encoding", "binary;");

        // Excel File Output
        workbook.write(response.getOutputStream());
        workbook.close();
	}
	
	public String generateFileName(List<String> toks) {
		log.info("generateFileName 호출");

		StringBuilder sb = new StringBuilder();
		
		final String SEP = "_";
		final String EXT = "xlsx";

		for (String tok : toks) {
			sb.append(tok);
			sb.append(SEP);
		}
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
	    String date = sdfDate.format(new Date());
	    sb.append(date);
	    
		sb.append(".");
		sb.append(EXT);
		
		return sb.toString();
	}
	
	private void autoSizeColumns(Workbook workbook) {
	    int numberOfSheets = workbook.getNumberOfSheets();
	    for (int i = 0; i < numberOfSheets; i++) {
	        Sheet sheet = workbook.getSheetAt(i);
	        if (sheet.getPhysicalNumberOfRows() > 0) {
	            Row row = sheet.getRow(sheet.getFirstRowNum());
	            Iterator<Cell> cellIterator = row.cellIterator();
	            while (cellIterator.hasNext()) {
	                Cell cell = cellIterator.next();
	                int columnIndex = cell.getColumnIndex();
	                sheet.autoSizeColumn(columnIndex);
	                sheet.setColumnWidth(columnIndex, (sheet.getColumnWidth(columnIndex))+2024 );
	            }
	        }
	    }
	}
}
