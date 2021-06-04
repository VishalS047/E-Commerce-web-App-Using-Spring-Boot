package com.shopme.export;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;

public class UserPdfExporter extends AbstractExporter {

	public void export(List<User> listAllUsers, HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		super.setResponseHeader(response, "application/pdf", ".pdf");

		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, response.getOutputStream());

		document.open();

		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setSize(18);
		font.setColor(Color.BLACK);

		Paragraph paragraph = new Paragraph("List of Users", font);
		paragraph.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(paragraph);

		PdfPTable table = new PdfPTable(6);

		table.setWidthPercentage(100f);

		table.setSpacingBefore(10);

		table.setWidths(new float[] { 1.3f, 4.5f, 3.0f, 3.0f, 4.0f, 1.7f });

		writeTableHeader(table);
		writeTableData(table, listAllUsers);

		document.add(table);

		document.close();

	}

	private void writeTableData(PdfPTable table, List<User> listAllUsers) {
		// TODO Auto-generated method stub
		for (User user : listAllUsers) {
			table.addCell(String.valueOf(user.getId()));
			table.addCell(String.valueOf(user.getEmail()));
			table.addCell(String.valueOf(user.getFirstName()));
			table.addCell(String.valueOf(user.getLastName()));
			table.addCell(String.valueOf(user.getRoles().toString()));
			table.addCell(String.valueOf(user.isEnabled()));
		}
	}

	private void writeTableHeader(PdfPTable table) {
		// TODO Auto-generated method stub
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLACK);
		cell.setPadding(5);

		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);

		cell.setPhrase(new Phrase("#ID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("EMAIL ID", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("FIRST NAME", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("LAST NAME", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("ROLES", font));
		table.addCell(cell);

		cell.setPhrase(new Phrase("ENABLED", font));
		table.addCell(cell);
	}

}
