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
package com.tcdng.jacklyn.notification.lists;

import com.tcdng.jacklyn.notification.business.NotificationModule;
import com.tcdng.jacklyn.notification.constants.NotificationModuleNameConstants;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.list.AbstractListCommand;

/**
 * Abstract base class for notification module list commands.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractNotificationListCommand<T> extends AbstractListCommand<T> {

	@Configurable(NotificationModuleNameConstants.NOTIFICATIONBUSINESSMODULE)
	private NotificationModule notificationModule;

	public AbstractNotificationListCommand(Class<T> type) {
		super(type);
	}

	protected NotificationModule getNotificationModule() {
		return notificationModule;
	}

}
