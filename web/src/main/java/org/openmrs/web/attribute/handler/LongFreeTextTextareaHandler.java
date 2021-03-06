/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.web.attribute.handler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.openmrs.customdatatype.CustomDatatype;
import org.openmrs.customdatatype.CustomDatatypeUtil;
import org.openmrs.customdatatype.DownloadableDatatypeHandler;
import org.openmrs.customdatatype.InvalidCustomValueException;
import org.openmrs.customdatatype.datatype.LongFreeTextDatatype;
import org.openmrs.web.WebUtil;
import org.springframework.stereotype.Component;

/**
 * Handler for the {@link LongFreeTextDatatype} that displays as a textarea.
 * @since 1.9
 */
@Component
public class LongFreeTextTextareaHandler implements WebDatatypeHandler<LongFreeTextDatatype, String>, DownloadableDatatypeHandler<String> {
	
	int rows = 5;
	
	int cols = 72;
	
	/**
	 * @see org.openmrs.customdatatype.CustomDatatypeHandler#setHandlerConfiguration(java.lang.String)
	 */
	@Override
	public void setHandlerConfiguration(String handlerConfig) {
		if (handlerConfig != null) {
			Map<String, String> map = CustomDatatypeUtil.deserializeSimpleConfiguration(handlerConfig);
			if (map.containsKey("rows"))
				rows = Integer.valueOf(map.get("rows"));
			if (map.containsKey("cols"))
				cols = Integer.valueOf(map.get("cols"));
		}
	}
	
	/**
	 * @see org.openmrs.web.attribute.handler.HtmlDisplayableDatatypeHandler#toHtmlSummary(org.openmrs.customdatatype.CustomDatatype, java.lang.String)
	 */
	@Override
	public CustomDatatype.Summary toHtmlSummary(CustomDatatype<String> datatype, String valueReference) {
		CustomDatatype.Summary summary = datatype.getTextSummary(valueReference);
		summary.setSummary(WebUtil.escapeHTML(summary.getSummary()));
		return summary;
	}
	
	/**
	 * @see org.openmrs.web.attribute.handler.HtmlDisplayableDatatypeHandler#toHtml(org.openmrs.customdatatype.CustomDatatype, java.lang.String)
	 */
	@Override
	public String toHtml(CustomDatatype<String> datatype, String valueReference) {
		return WebUtil.escapeHTML(datatype.fromReferenceString(valueReference));
	}
	
	/**
	 * @see org.openmrs.web.attribute.handler.WebDatatypeHandler#getWidgetHtml(org.openmrs.customdatatype.CustomDatatype, java.lang.String, java.lang.String, java.lang.Object)
	 */
	@Override
	public String getWidgetHtml(LongFreeTextDatatype datatype, String formFieldName, String widgetId, String startingValue) {
		StringBuilder sb = new StringBuilder();
		sb.append("<textarea id=\"" + widgetId + "\" rows=\"" + rows + "\" cols=\"" + cols + "\" name=\"" + formFieldName
		        + "\">");
		if (startingValue != null)
			sb.append(WebUtil.escapeHTML(startingValue));
		sb.append("</textarea>");
		return sb.toString();
	}
	
	/**
	 * @see org.openmrs.web.attribute.handler.WebDatatypeHandler#getValue(org.openmrs.customdatatype.CustomDatatype, javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	@Override
	public String getValue(LongFreeTextDatatype datatype, HttpServletRequest request, String formFieldName)
	        throws InvalidCustomValueException {
		String val = request.getParameter(formFieldName);
		return "".equals(val) ? null : val;
	}
	
	/**
	 * @see org.openmrs.customdatatype.DownloadableDatatypeHandler#getContentType(org.openmrs.customdatatype.CustomDatatype, java.lang.String)
	 */
	@Override
	public String getContentType(CustomDatatype<String> dt, String valueReference) {
		return "text/plain; charset=utf-8";
	}
	
	/**
	 * @see org.openmrs.customdatatype.DownloadableDatatypeHandler#getFilename(org.openmrs.customdatatype.CustomDatatype, java.lang.String)
	 */
	@Override
	public String getFilename(CustomDatatype<String> dt, String valueReference) {
		return "OpenMRS-long-free-text.txt";
	}
	
	/**
	 * @see org.openmrs.customdatatype.DownloadableDatatypeHandler#writeToStream(org.openmrs.customdatatype.CustomDatatype, java.lang.String, java.io.OutputStream)
	 */
	@Override
	public void writeToStream(CustomDatatype<String> dt, String valueReference, OutputStream os) throws IOException {
		String val = dt.fromReferenceString(valueReference);
		os.write(val.getBytes("UTF-8"));
	}
	
}
