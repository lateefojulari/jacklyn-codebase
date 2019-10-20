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
package com.tcdng.jacklyn.shared.workflow;

import com.tcdng.unify.core.annotation.StaticList;
import com.tcdng.unify.core.constant.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Workflow step type constants.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@StaticList("wfsteptypelist")
public enum WorkflowStepType implements EnumConst {

    START("S"),
    MANUAL("M"),
    AUTOMATIC("A"),
    INTERACTIVE("I"),
    ERROR("X"),
    SPLIT("T"),
    MERGE("G"),
    END("E");

    private final String code;

    private WorkflowStepType(String code) {
        this.code = code;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return START.code;
    }

    public boolean isStart() {
        return START.equals(this);
    }

    public boolean isManual() {
        return MANUAL.equals(this);
    }

    public boolean isError() {
        return ERROR.equals(this);
    }

    public boolean isSplit() {
        return SPLIT.equals(this);
    }

    public boolean isMerge() {
        return MERGE.equals(this);
    }

    public boolean isEnd() {
        return END.equals(this);
    }

    public boolean isUserInteractive() {
        return MANUAL.equals(this) || INTERACTIVE.equals(this);
    }

    public static WorkflowStepType fromCode(String code) {
        return EnumUtils.fromCode(WorkflowStepType.class, code);
    }

    public static WorkflowStepType fromName(String name) {
        return EnumUtils.fromName(WorkflowStepType.class, name);
    }
}
