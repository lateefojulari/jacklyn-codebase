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
package com.tcdng.jacklyn.system.web.lists;

import com.tcdng.jacklyn.common.web.lists.BaseListCommand;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.unify.core.annotation.Configurable;

/**
 * Abstract base class for system module list commands.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public abstract class AbstractSystemListCommand<T> extends BaseListCommand<T> {

    @Configurable
    private SystemService systemService;

    public AbstractSystemListCommand(Class<T> paramType) {
        super(paramType);
    }

    protected SystemService getSystemService() {
        return systemService;
    }
}
