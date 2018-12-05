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
package com.tcdng.jacklyn.shared.xml.config.workflow;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tcdng.jacklyn.shared.xml.config.BaseConfig;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.util.xml.adapter.DataTypeXmlAdapter;

/**
 * Document field configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class WfDocFieldConfig extends BaseConfig {

	private DataType dataType;

	private Boolean multiple;

	public WfDocFieldConfig() {
		this.multiple = Boolean.FALSE;
	}

	public DataType getDataType() {
		return dataType;
	}

	@XmlJavaTypeAdapter(DataTypeXmlAdapter.class)
	@XmlAttribute(name = "type", required = true)
	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public Boolean getMultiple() {
		return multiple;
	}

	@XmlAttribute
	public void setMultiple(Boolean multiple) {
		this.multiple = multiple;
	}

}
