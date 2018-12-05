/*
 * Copyright 2018 The Code Department
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.tcdng.jacklyn.shared.xml.config.module;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Parameter configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class ParameterConfig {

	private String name;

	private String description;

	private String editor;

	private String type;

	private boolean mandatory;

	public ParameterConfig() {
		this.type = String.class.getName();
	}

	public String getName() {
		return name;
	}

	@XmlAttribute(required = true)
	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	@XmlAttribute(required = true)
	public void setDescription(String description) {
		this.description = description;
	}

	public String getEditor() {
		return editor;
	}

	@XmlAttribute(required = true)
	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getType() {
		return type;
	}

	@XmlAttribute(required = true)
	public void setType(String type) {
		this.type = type;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	@XmlAttribute
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
}
