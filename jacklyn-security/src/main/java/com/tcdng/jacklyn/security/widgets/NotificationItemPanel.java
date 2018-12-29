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

package com.tcdng.jacklyn.security.widgets;

import com.tcdng.jacklyn.common.data.SystemNotification;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.AbstractPanel;

/**
 * Panel for presenting a notification item.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("ui-notificationitempanel")
@UplBinding("web/security/upl/notificationitempanel.upl")
public class NotificationItemPanel extends AbstractPanel {

    @Override
    public void switchState() throws UnifyException {
        super.switchState();

        SystemNotification systemNotification = (SystemNotification) getValueStore().getValueObject();
        if (systemNotification != null) {
            setVisible("notifLink", !StringUtils.isBlank(systemNotification.getLink()));
        }
    }

}
