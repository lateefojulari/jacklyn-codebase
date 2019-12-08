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
package com.tcdng.jacklyn.system.entities;

import com.tcdng.jacklyn.common.entities.BaseInstallEntityQuery;

/**
 * Query class for modules.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class ModuleQuery extends BaseInstallEntityQuery<Module> {

    public ModuleQuery() {
        super(Module.class);
    }

    @Override
    public ModuleQuery addOrder(String field) {
        return (ModuleQuery) super.addOrder(field);
    }

    @Override
    public ModuleQuery addSelect(String field) {
        return (ModuleQuery) super.addSelect(field);
    }

    public ModuleQuery name(String name) {
        return (ModuleQuery) addEquals("name", name);
    }

    public ModuleQuery descriptionLike(String description) {
        return (ModuleQuery) addLike("description", description);
    }
}
