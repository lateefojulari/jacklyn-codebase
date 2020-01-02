/*
 * Copyright 2018-2020 The Code Department.
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

package com.tcdng.jacklyn.shared.workflow.data;

import javax.xml.bind.annotation.XmlRootElement;

import com.tcdng.jacklyn.shared.workflow.WorkflowRemoteCallNameConstants;
import com.tcdng.unify.web.remotecall.RemoteCallParams;

/**
 * Get tooling policy logic request parameters.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@XmlRootElement
public class GetToolingPolicyLogicParams extends RemoteCallParams {

    public GetToolingPolicyLogicParams() {
        super(WorkflowRemoteCallNameConstants.GET_TOOLING_POLICY_LOGIC_LIST);
    }

}
