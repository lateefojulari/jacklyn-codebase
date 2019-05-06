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

package com.tcdng.jacklyn.workflow.business;

import com.tcdng.jacklyn.notification.data.Message;
import com.tcdng.jacklyn.notification.data.NotificationTemplateDef;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.jacklyn.workflow.constants.WorkflowModuleNameConstants;
import com.tcdng.jacklyn.workflow.data.WfAlertDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.util.StringUtils.StringToken;

/**
 * Default workflow item alert logic implementation.
 * 
 * @author Lateef
 * @since 1.0
 */
@Component(
        name = WorkflowModuleNameConstants.DEFAULTWORKFLOWITEMALERTLOGIC,
        description = "Default Workflow Item Alert Logic")
public class WfItemAlertLogicImpl extends AbstractWfItemAlertLogic {

    @Override
    public void sendAlert(WfItemReader wfItemReader, WfAlertDef wfAlertDef) throws UnifyException {
        logDebug("Sending alert...");
        String senderName = null;
        String senderContact = null;
        String channelName = null;
        switch (wfAlertDef.getType()) {
            case EMAIL:
                senderName =
                        getSystemService().getSysParameterValue(String.class,
                                SystemModuleSysParamConstants.SYSPARAM_SYSTEM_NAME);
                senderContact =
                        getSystemService().getSysParameterValue(String.class,
                                SystemModuleSysParamConstants.SYSPARAM_SYSTEM_EMAIL);
                channelName =
                        getSystemService().getSysParameterValue(String.class,
                                SystemModuleSysParamConstants.SYSPARAM_EMAIL_CHANNEL);
                break;
            case SMS:
                break;
            case SYSTEM:
            default:
                break;
        }

        if (channelName != null) {
            String globalTemplateName = wfAlertDef.getNotificationTemplateCode();
            NotificationTemplateDef notificationTemplateDef =
                    getNotificationService().getRuntimeNotificationTemplateDef(globalTemplateName);

            // Build message
            Message.Builder msgBuilder = Message.newBuilder(globalTemplateName).fromSender(senderName, senderContact);
            // TODO get recipients from role
            msgBuilder.toRecipient("Lateef Ojulari", "lateefojulari@gmail.com");

            // Populate message dictionary from workflow item
            for (StringToken token : notificationTemplateDef.getTokenList()) {
                if(token.isParam()) {
                    String tokenName = token.getToken();
                    msgBuilder.usingDictionaryEntry(tokenName, wfItemReader.readFieldValue(tokenName));
                }
            }

            // Set channel
            msgBuilder.sendVia(channelName);

            // Send notification
            getNotificationService().sendNotification(msgBuilder.build());
        }
    }

}
