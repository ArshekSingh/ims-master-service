package convertor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sts.ims.dto.ProductMasterDto;

@Component
public class ProductConvertor {

	@Autowired
	Gson gson;

	@Autowired
	ObjectMapper objectMapper;

	public List<ProductMasterDto> convertFileToDto(MultipartFile file) {
		// TODO Auto-generated method stub
		List<ProductMasterDto> dtoList = new ArrayList<>();
		try {
			XSSFWorkbook workBook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet workSheet = workBook.getSheetAt(0);
			List<JsonObject> dataList = new ArrayList<>();
			XSSFRow header = workSheet.getRow(0);
			for (int i = 1; i < workSheet.getPhysicalNumberOfRows(); i++) {
				XSSFRow row = workSheet.getRow(i);
				JsonObject rowJsonObject = new JsonObject();
				for (int j = 0; j < row.getPhysicalNumberOfCells(); j++) {
					String columnName = header.getCell(j).toString();
					String columnValue = row.getCell(j).toString();
					rowJsonObject.add(columnName, gson.toJsonTree(columnValue));
				}
				dataList.add(rowJsonObject);
			}
			for (JsonObject obj : dataList) {
				String jsonString = gson.toJson(obj);
				ProductMasterDto dto = objectMapper.readValue(jsonString, ProductMasterDto.class);
				dtoList.add(dto);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dtoList;
	}

}
