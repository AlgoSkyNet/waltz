/*
 * Waltz - Enterprise Architecture
 * Copyright (C) 2016, 2017 Waltz open source project
 * See README.md for more information
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.khartec.waltz.data.physical_specification_definition;

import com.khartec.waltz.model.physical_specification_definition.ImmutablePhysicalSpecDefinitionField;
import com.khartec.waltz.model.physical_specification_definition.PhysicalSpecDefinitionField;
import com.khartec.waltz.model.physical_specification_definition.PhysicalSpecDefinitionFieldType;
import com.khartec.waltz.schema.tables.records.PhysicalSpecDefnFieldRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.function.Function;

import static com.khartec.waltz.common.Checks.checkNotNull;
import static com.khartec.waltz.schema.tables.PhysicalSpecDefnField.PHYSICAL_SPEC_DEFN_FIELD;

@Repository
public class PhysicalSpecDefinitionFieldDao {

    public static final RecordMapper<? super Record, PhysicalSpecDefinitionField> TO_DOMAIN_MAPPER = r -> {
        PhysicalSpecDefnFieldRecord record = r.into(PHYSICAL_SPEC_DEFN_FIELD);
        return ImmutablePhysicalSpecDefinitionField.builder()
                .id(record.getId())
                .specDefinitionId(record.getSpecDefnId())
                .name(record.getName())
                .position(record.getPosition())
                .type(PhysicalSpecDefinitionFieldType.valueOf(record.getType()))
                .description(record.getDescription())
                .lastUpdatedAt(record.getLastUpdatedAt().toLocalDateTime())
                .lastUpdatedBy(record.getLastUpdatedBy())
                .build();
    };


    private static final Function<PhysicalSpecDefinitionField, PhysicalSpecDefnFieldRecord> TO_RECORD_MAPPER = f -> {
        PhysicalSpecDefnFieldRecord record = new PhysicalSpecDefnFieldRecord();
        record.setSpecDefnId(f.specDefinitionId());
        record.setName(f.name());
        record.setPosition(f.position());
        record.setType(f.type().name());
        record.setDescription(f.description());
        record.setLastUpdatedAt(Timestamp.valueOf(f.lastUpdatedAt()));
        record.setLastUpdatedBy(f.lastUpdatedBy());

        return record;
    };


    private final DSLContext dsl;


    @Autowired
    public PhysicalSpecDefinitionFieldDao(DSLContext dsl) {
        checkNotNull(dsl, "dsl cannot be null");

        this.dsl = dsl;
    }


    public List<PhysicalSpecDefinitionField> findForSpecDefinition(long specDefinitionId) {
        return dsl.selectFrom(PHYSICAL_SPEC_DEFN_FIELD)
                .where(PHYSICAL_SPEC_DEFN_FIELD.SPEC_DEFN_ID.eq(specDefinitionId))
                .fetch(TO_DOMAIN_MAPPER);
    }


    public long create(PhysicalSpecDefinitionField definitionField) {
        PhysicalSpecDefnFieldRecord record = TO_RECORD_MAPPER.apply(definitionField);

        return dsl.insertInto(PHYSICAL_SPEC_DEFN_FIELD)
                .set(record)
                .returning(PHYSICAL_SPEC_DEFN_FIELD.ID)
                .fetchOne()
                .getId();
    }


    public int delete(long specDefinitionFieldId) {
        return dsl.deleteFrom(PHYSICAL_SPEC_DEFN_FIELD)
                .where(PHYSICAL_SPEC_DEFN_FIELD.ID.eq(specDefinitionFieldId))
                .execute();
    }


    public int deleteForSpecDefinition(long specDefinitionId) {
        return dsl.deleteFrom(PHYSICAL_SPEC_DEFN_FIELD)
                .where(PHYSICAL_SPEC_DEFN_FIELD.SPEC_DEFN_ID.eq(specDefinitionId))
                .execute();
    }
}
