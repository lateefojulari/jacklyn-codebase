/*
 * Copyright 2018-2019 The Code Department.
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

import com.tcdng.unify.core.constant.RequirementType;
import com.tcdng.unify.core.util.xml.adapter.RequirementTypeXmlAdapter;

/**
 * Workflow attachment check configuration.
 * 
 * @author Lateef Ojulari
 * @version 1.0
 */
public class WfAttachmentCheckConfig {

    private String document;
    
    private String attachmentName;

    private RequirementType requirementType;

    public String getDocument() {
        return document;
    }

    @XmlAttribute(required = true)
    public void setDocument(String document) {
        this.document = document;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    @XmlAttribute(name = "attachment", required = true)
    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public RequirementType getRequirementType() {
        return requirementType;
    }

    @XmlJavaTypeAdapter(RequirementTypeXmlAdapter.class)
    @XmlAttribute(name = "requirement-type", required = true)
    public void setRequirementType(RequirementType requirementType) {
        this.requirementType = requirementType;
    }

}
