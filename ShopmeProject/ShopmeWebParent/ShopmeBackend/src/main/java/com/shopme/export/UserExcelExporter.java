package com.shopme.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.User;

@Service
public class UserExcelExporter extends AbstractExporter {

	private XSSFWorkbook workBook;
	private XSSFSheet sheet;

	public UserExcelExporter() {

		workBook = new XSSFWorkbook();

	}

	private void writeHeaderLine() {

		sheet = workBook.createSheet("Users");

		XSSFRow row = sheet.createRow(0);

		XSSFCellStyle cellStyle = workBook.createCellStyle();

		XSSFFont font = workBook.createFont();

		font.setBold(true);

		font.setFontHeight(16);

		cellStyle.setFont(font);

		createCell(row, 0, "#ID", cellStyle);
		createCell(row, 1, "EMAIL ID", cellStyle);
		createCell(row, 2, "FIRST NAME", cellStyle);
		createCell(row, 3, "LAST NAME", cellStyle);
		createCell(row, 4, "ROLES", cellStyle);
		createCell(row, 5, "ENABLED", cellStyle);
	}

	private void createCell(XSSFRow row, int colIndex, Object value, CellStyle style) {

		sheet.autoSizeColumn(colIndex);

		XSSFCell cell = row.createCell(colIndex);

		if (value instanceof Integer)
			cell.setCellValue((Integer) value);
		else if (value instanceof Boolean)
			cell.setCellValue((Boolean) value);
		else
			cell.setCellValue((String) value);

		cell.setCellStyle(style);
	}

	public void export(List<User> users, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "application/octet-stream", ".xlsx");

		writeHeaderLine();
		writeDataLine(users);
		ServletOutputStream outputStream = response.getOutputStream();
		workBook.write(outputStream);
		workBook.close();
		outputStream.close();
	}

	private void writeDataLine(List<User> users) {
		// TODO Auto-generated method stub
		int rowIndex = 1;

		XSSFCellStyle cellStyle = workBook.createCellStyle();

		XSSFFont font = workBook.createFont();

		font.setBold(true);

		font.setFontHeight(16);

		cellStyle.setFont(font);

		for (User user : users) {

			XSSFRow row = sheet.createRow(rowIndex++);

			int colIndex = 0;

			createCell(row, colIndex++, user.getId(), cellStyle);
			createCell(row, colIndex++, user.getEmail(), cellStyle);
			createCell(row, colIndex++, user.getFirstName(), cellStyle);
			createCell(row, colIndex++, user.getLastName(), cellStyle);
			createCell(row, colIndex++, user.getRoles().toString(), cellStyle);
			createCell(row, colIndex++, user.isEnabled(), cellStyle);
		}
	}
}
