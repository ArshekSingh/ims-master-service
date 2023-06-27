package com.sas.ims.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Shahzad Hussain
 */
@Slf4j
@Component
public class ExcelGeneratorUtil {

    public void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook) {
        String sheetName = (String) model.get("Excel Name");
        List<String> headers = (List<String>) model.get("HEADERS");
        List<List<String>> results = (List<List<String>>) model.get("RESULTS");
        List<String> numericColumns = new ArrayList<>();
        if (model.containsKey("numericcolumns")) numericColumns = (List<String>) model.get("numericcolumns");
        HSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(15);
        int currentRow = 0;
        short currentColumn = 0;
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        HSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        HSSFRow headerRow = sheet.createRow(currentRow);
        for (String header : headers) {
            HSSFRichTextString text = new HSSFRichTextString(header);
            HSSFCell cell = headerRow.createCell(currentColumn);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(text);
            currentColumn++;
        }
        currentRow++;
        for (List<String> result : results) {
            currentColumn = 0;
            HSSFRow row = sheet.createRow(currentRow);
            for (String value : result) {
                HSSFCell cell = row.createCell(currentColumn);
                if (numericColumns.contains(headers.get(currentColumn))) {
                    cell.setCellValue(value);
                } else {
                    HSSFRichTextString text = new HSSFRichTextString(value);
                    cell.setCellValue(text);
                }
                currentColumn++;
            }
            currentRow++;
        }
    }

    public Map<String, Object> populateHeaderAndName(String headerParam, String fileName) {
        Map<String, Object> map = new HashMap<>();
        map.put("Excel Name", fileName);
        map.put("HEADERS", Arrays.asList(headerParam.split(",")));
        return map;
    }

    public void downloadDocument(HttpServletResponse httpServletResponse, Map<String, Object> map, HSSFWorkbook workbook) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + getFileName(map));
            httpServletResponse.setContentType("application/xls");
            httpServletResponse.getOutputStream().write(bytes);
        } catch (Exception exception) {
            log.error("Exception occurs while downloading Request Order Excel {}", exception.getMessage());
        }
    }

    public void downloadColendingCollectionDocument(HttpServletResponse httpServletResponse, Map<String, Object> map, XSSFWorkbook workbook) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + getFileName(map) + ".xlsx");
            httpServletResponse.setContentType("application/xlsx");
            httpServletResponse.getOutputStream().write(bytes);
        } catch (Exception exception) {
            log.error("Exception occurs while downloading NEFT Excel {}", exception.getMessage());
        }
    }

    public String getFileName(Map<String, Object> map) {
        log.info(String.valueOf(map.get("Excel Name")));
        return String.valueOf(map.get("Excel Name"));
    }

    public void downloadErrorDocument(HttpServletResponse httpServletResponse, Map<String, Object> map, HSSFWorkbook workbook) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + getFileName(map));
            httpServletResponse.setContentType("application/xls");
            httpServletResponse.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
            httpServletResponse.getOutputStream().write(bytes);
        } catch (Exception exception) {
            log.error("Exception occurs while downloading Excel {}", exception.getMessage());
        }
    }

    public void downloadMultipleZipDocument(HttpServletResponse httpServletResponse, List<Map<String, Object>> map) {
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Content-Disposition", "attachment; filename=loanDetail_repayment.zip");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);

        try (ZipOutputStream zippedOut = new ZipOutputStream(httpServletResponse.getOutputStream())) {
            for (Map<String, Object> file : map) {
                XSSFWorkbook workbook = new XSSFWorkbook();
                buildExcelDocument(file, workbook);
                zippedOut.putNextEntry(new ZipEntry(getFileName(file)));
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                workbook.write(bos);
                bos.writeTo(zippedOut);
                zippedOut.closeEntry();
            }
            zippedOut.finish();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    public void buildExcelDocument(Map<String, Object> model, XSSFWorkbook workbook) {
        String sheetName = (String) model.get("Excel Name");
        List<String> headers = (List<String>) model.get("HEADERS");
        List<List<String>> results = (List<List<String>>) model.get("RESULTS");
        List<String> numericColumns = new ArrayList<>();
        if (model.containsKey("numericcolumns")) numericColumns = (List<String>) model.get("numericcolumns");
        XSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(15);
        int currentRow = 0;
        short currentColumn = 0;
        XSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        XSSFFont headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        XSSFRow headerRow = sheet.createRow(currentRow);
        for (String header : headers) {
            XSSFRichTextString text = new XSSFRichTextString(header);
            XSSFCell cell = headerRow.createCell(currentColumn);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(text);
            currentColumn++;
        }
        currentRow++;
        for (List<String> result : results) {
            currentColumn = 0;
            XSSFRow row = sheet.createRow(currentRow);
            for (String value : result) {
                XSSFCell cell = row.createCell(currentColumn);
                if (numericColumns.contains(headers.get(currentColumn))) {
                    cell.setCellValue(value);
                } else {
                    XSSFRichTextString text = new XSSFRichTextString(value);
                    cell.setCellValue(text);
                }
                currentColumn++;
            }
            currentRow++;
        }
    }

    public byte[] downloadDocument(XSSFWorkbook workbook) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        } catch (Exception exception) {
            log.error("Exception occurs while downloading Collection Detail Excel {}", exception.getMessage());
            return new byte[0];
        }
    }

    public void downloadDocuments(HttpServletResponse httpServletResponse, Map<String, Object> map, XSSFWorkbook workbook) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            byte[] bytes = byteArrayOutputStream.toByteArray();
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=" + getFileName(map) + ".xlsx");
            httpServletResponse.setContentType("application/xlsx");
            httpServletResponse.getOutputStream().write(bytes);
        } catch (Exception exception) {
            log.error("Exception occurs while downloading IBL Excel {}", exception.getMessage());
        }
    }

}
