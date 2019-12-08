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
package com.tcdng.jacklyn.system.web.lists;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.tcdng.jacklyn.common.constants.RecordStatus;
import com.tcdng.jacklyn.system.entities.SupportedLocale;
import com.tcdng.jacklyn.system.entities.SupportedLocaleQuery;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.ZeroParams;
import com.tcdng.unify.core.util.DataUtils;

/**
 * List command of active supported locale language.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("supportedlocalelanguagelist")
public class SupportedLocaleLanguageListCommand extends AbstractZeroParamsSystemListCommand {

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams params) throws UnifyException {
        List<ListData> result = Collections.emptyList();
        SupportedLocaleQuery query = new SupportedLocaleQuery();
        query.status(RecordStatus.ACTIVE).addOrder("description");
        List<SupportedLocale> supportedLocaleList = getSystemService().findSupportedLocales(query);
        if (DataUtils.isNotBlank(supportedLocaleList)) {
            result = new ArrayList<ListData>();
            for (SupportedLocale supportedLocale : supportedLocaleList) {
                Locale supLocale = Locale.forLanguageTag(supportedLocale.getLanguageTag());
                result.add(new ListData(supportedLocale.getLanguageTag(), supLocale.getDisplayName(supLocale)));
            }
        }

        return result;
    }
}
