package com.shopme.export;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.common.entity.User;

@Service
public class UserCsvExporter extends AbstractExporter {

	public void export(List<User> users, HttpServletResponse response) throws IOException {

		super.setResponseHeader(response, "text/csv", ".csv");
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = { "ID", "EMAIL ID", "FIRST NAME", "LAST NAME", "ROLES", "ENABLED" };
		
		String[] userFieldMapping = {"id", "email", "firstName" ,"lastName", "roles" ,"enabled"};

		csvWriter.writeHeader(csvHeader);
		
		for(User user: users) {
				csvWriter.write(user, userFieldMapping);
		}

		csvWriter.close();
	}
}
