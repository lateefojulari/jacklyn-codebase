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

package com.tcdng.jacklyn.workflow.data;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcdng.jacklyn.workflow.constants.WorkflowModuleErrorConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.PackableDocConfig;

/**
 * Workflow document definition.
 * 
 * @author Lateef
 * @since 1.0
 */
public class WfDocDef extends BaseWfDef {

	private static final long serialVersionUID = 5731603377730336455L;

	private Long wfDocId;

	private PackableDocConfig docConfig;

	private String globalName;

	private Date timestamp;

	private Map<Class<?>, WfDocBeanMappingDef> entryBeanMappings;

	private Map<String, WfDocBeanMappingDef> beanMappings;

	private Map<String, WfDocAttachmentDef> attachments;

	private Map<String, WfDocClassifierDef> classifiers;

	public WfDocDef(Long wfDocId, String globalName, String name, String description,
			PackableDocConfig docConfig, Date timestamp, List<WfDocBeanMappingDef> beanMappingList,
			List<WfDocAttachmentDef> attachmentList, List<WfDocClassifierDef> classifierList) {
		super(name, description);
		this.wfDocId = wfDocId;
		this.globalName = globalName;
		this.docConfig = docConfig;
		this.timestamp = timestamp;

		if (beanMappingList != null) {
			entryBeanMappings = new HashMap<Class<?>, WfDocBeanMappingDef>();
			beanMappings = new HashMap<String, WfDocBeanMappingDef>();
			for (WfDocBeanMappingDef wfDocBeanMappingDef : beanMappingList) {
				if (wfDocBeanMappingDef.isEntryMapping()) {
					entryBeanMappings.put(wfDocBeanMappingDef.getBeanType(), wfDocBeanMappingDef);
				}

				beanMappings.put(wfDocBeanMappingDef.getName(), wfDocBeanMappingDef);
			}
		} else {
			beanMappings = Collections.emptyMap();
			entryBeanMappings = Collections.emptyMap();
		}

		if (attachmentList != null) {
			attachments = new LinkedHashMap<String, WfDocAttachmentDef>();
			for (WfDocAttachmentDef wfDocAttachmentDef : attachmentList) {
				attachments.put(wfDocAttachmentDef.getName(), wfDocAttachmentDef);
			}

			attachments = Collections.unmodifiableMap(attachments);
		} else {
			attachments = Collections.emptyMap();
		}

		if (classifierList != null) {
			classifiers = new LinkedHashMap<String, WfDocClassifierDef>();
			for (WfDocClassifierDef wfDocClassifierDef : classifierList) {
				classifiers.put(wfDocClassifierDef.getName(), wfDocClassifierDef);
			}

			classifiers = Collections.unmodifiableMap(classifiers);
		} else {
			classifiers = Collections.emptyMap();
		}
	}

	public Long getWfDocId() {
		return wfDocId;
	}

	public PackableDocConfig getDocConfig() {
		return docConfig;
	}

	public String getGlobalName() {
		return globalName;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public Map<String, WfDocAttachmentDef> getAttachments() {
		return attachments;
	}

	public Map<String, WfDocClassifierDef> getClassifiers() {
		return classifiers;
	}

	public Set<String> getWfDocBeanMappingNames() {
		return beanMappings.keySet();
	}

	public WfDocBeanMappingDef getWfDocBeanMappingDef(String name) {
		return beanMappings.get(name);
	}

	public WfDocBeanMappingDef getEntryWfDocBeanMappingDef(Class<?> beanType)
			throws UnifyException {
		WfDocBeanMappingDef wfDocBeanMappingDef = entryBeanMappings.get(beanType);
		if (wfDocBeanMappingDef == null) {
			throw new UnifyException(
					WorkflowModuleErrorConstants.WORKFLOW_DOCUMENT_ENTRY_BEANMAPPING_FOR_TYPE_UNKNOWN,
					getDescription(), beanType);
		}

		return wfDocBeanMappingDef;
	}

	public Set<String> getWfDocAttachmentNames() {
		return attachments.keySet();
	}

	public WfDocAttachmentDef getWfDocAttachmentDef(String name) {
		return attachments.get(name);
	}

	public Set<String> getWfDocClassifierNames() {
		return classifiers.keySet();
	}

	public WfDocClassifierDef getWfDocClassifierDef(String name) {
		return classifiers.get(name);
	}

	public int getFieldCount() {
		return docConfig.getFieldCount();
	}
}
