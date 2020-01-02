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

package com.tcdng.jacklyn.organization.web.controllers;

import com.tcdng.jacklyn.common.web.beans.BasePageBean;
import com.tcdng.jacklyn.common.web.controllers.BasePageController;
import com.tcdng.jacklyn.organization.business.OrganizationService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Abstract base page controller for organization module.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractOrganizationPageController<T extends BasePageBean> extends BasePageController<T> {

    @Configurable
    private OrganizationService organizationService;

    public AbstractOrganizationPageController(Class<T> pageBeanClass, boolean secured, boolean readOnly,
            boolean resetOnWrite) {
        super(pageBeanClass, secured, readOnly, resetOnWrite);
    }

    protected OrganizationService getOrganizationService() throws UnifyException {
        return organizationService;
    }

}
