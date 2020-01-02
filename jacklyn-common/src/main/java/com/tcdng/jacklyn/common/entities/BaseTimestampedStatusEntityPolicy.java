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
package com.tcdng.jacklyn.common.entities;

import java.util.Date;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity policy class for time-stamped status entity.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Component("timestampedstatusentity-policy")
public class BaseTimestampedStatusEntityPolicy extends BaseStatusEntityPolicy {

    public BaseTimestampedStatusEntityPolicy() {
        super(true); // Set now
    }

    @Override
    public Object preCreate(Entity record, Date now) throws UnifyException {
        BaseTimestampedStatusEntity baseTimestampedRecord = (BaseTimestampedStatusEntity) record;
        if (baseTimestampedRecord.getCreateDt() == null) {
            baseTimestampedRecord.setCreateDt(now);
        }

        if (baseTimestampedRecord.getCreateBy() == null) {
            baseTimestampedRecord.setCreateBy(getUserLoginId());
        }

        baseTimestampedRecord.setUpdateDt(now);
        baseTimestampedRecord.setUpdateBy(getUserLoginId());
        return super.preCreate(record, now);
    }

    @Override
    public void preUpdate(Entity record, Date now) throws UnifyException {
        ((BaseTimestampedStatusEntity) record).setUpdateDt(now);
        ((BaseTimestampedStatusEntity) record).setUpdateBy(getUserLoginId());
        super.preUpdate(record, now);
    }
}
