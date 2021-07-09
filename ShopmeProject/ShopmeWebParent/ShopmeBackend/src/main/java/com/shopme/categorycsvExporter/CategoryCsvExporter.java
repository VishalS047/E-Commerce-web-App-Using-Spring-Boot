package com.shopme.categorycsvExporter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.Category;

@Service
public class CategoryCsvExporter extends AbstractExporter{

	public void export(List<Category> listCategories, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv", ".csv", "categories_");

		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "ID", "CATEGORY NAME" };

		String[] userFieldMapping = { "cid", "name" };

		csvWriter.writeHeader(csvHeader);

		for (Category category : listCategories) {
			category.setName(category.getName().replace("**", ""));
			csvWriter.write(category, userFieldMapping);
		}

		csvWriter.close();
	}
}
