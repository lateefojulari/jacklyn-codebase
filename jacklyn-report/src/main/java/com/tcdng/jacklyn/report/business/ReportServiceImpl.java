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
package com.tcdng.jacklyn.report.business;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tcdng.jacklyn.common.business.AbstractJacklynBusinessService;
import com.tcdng.jacklyn.common.constants.JacklynApplicationAttributeConstants;
import com.tcdng.jacklyn.common.data.ReportColumnOptions;
import com.tcdng.jacklyn.common.data.ReportFilterOptions;
import com.tcdng.jacklyn.common.data.ReportJoinOptions;
import com.tcdng.jacklyn.common.data.ReportOptions;
import com.tcdng.jacklyn.common.utils.JacklynUtils;
import com.tcdng.jacklyn.report.constants.ReportModuleNameConstants;
import com.tcdng.jacklyn.report.constants.ReportModuleSysParamConstants;
import com.tcdng.jacklyn.report.entities.ReportConfiguration;
import com.tcdng.jacklyn.report.entities.ReportConfigurationQuery;
import com.tcdng.jacklyn.report.entities.ReportFilter;
import com.tcdng.jacklyn.report.entities.ReportGroup;
import com.tcdng.jacklyn.report.entities.ReportGroupQuery;
import com.tcdng.jacklyn.report.entities.ReportParameter;
import com.tcdng.jacklyn.report.entities.ReportableDefinition;
import com.tcdng.jacklyn.report.entities.ReportableDefinitionQuery;
import com.tcdng.jacklyn.report.entities.ReportableField;
import com.tcdng.jacklyn.report.entities.ReportableFieldQuery;
import com.tcdng.jacklyn.shared.report.ReportParameterConstants;
import com.tcdng.jacklyn.shared.xml.config.module.ColumnConfig;
import com.tcdng.jacklyn.shared.xml.config.module.FieldConfig;
import com.tcdng.jacklyn.shared.xml.config.module.FilterConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ManagedConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ModuleConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ParameterConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ReportConfig;
import com.tcdng.jacklyn.shared.xml.config.module.ReportGroupConfig;
import com.tcdng.jacklyn.system.business.SystemService;
import com.tcdng.jacklyn.system.constants.SystemModuleSysParamConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Transactional;
import com.tcdng.unify.core.constant.HAlignType;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.ReportColumn;
import com.tcdng.unify.core.report.ReportFormat;
import com.tcdng.unify.core.report.ReportLayout;
import com.tcdng.unify.core.report.ReportServer;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.util.WebUtils;

/**
 * Default implementation of report business service.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Transactional
@Component(ReportModuleNameConstants.REPORTSERVICE)
public class ReportServiceImpl extends AbstractJacklynBusinessService implements ReportService {

    @Configurable
    private SystemService systemService;

    @Override
    public List<ReportableDefinition> findReportables(ReportableDefinitionQuery query) throws UnifyException {
        return db().listAll(query);
    }

    @Override
    public ReportableDefinition findReportDefinition(Long id) throws UnifyException {
        return db().list(ReportableDefinition.class, id);
    }

    @Override
    public List<ReportableDefinition> findRoleReportables(Long moduleId) throws UnifyException {
        // TODO
        return null;
    }

    @Override
    public ReportColumn[] findReportableColumns(String reportableName) throws UnifyException {
        ReportableDefinition reportableDefinition = db().find(new ReportableDefinitionQuery().name(reportableName));

        List<ReportableField> reportFieldList =
                db().findAll(new ReportableFieldQuery().reportableId(reportableDefinition.getId())
                        .installed(Boolean.TRUE).orderById());

        ReportColumn[] reportColumns = new ReportColumn[reportFieldList.size()];
        for (int i = 0; i < reportColumns.length; i++) {
            ReportableField reportableField = reportFieldList.get(i);
            reportColumns[i] =
                    ReportColumn.newBuilder().title(reportableField.getDescription()).name(reportableField.getName())
                            .className(reportableField.getType()).widthRatio(reportableField.getWidth())
                            .formatter(reportableField.getFormatter())
                            .horizontalAlignment(HAlignType.fromName(reportableField.getHorizontalAlign())).build();
        }
        return reportColumns;
    }

    @Override
    public ReportOptions getDynamicReportOptions(String recordName, List<String> priorityPropertyList)
            throws UnifyException {
        ReportableDefinition reportableDefinition = db().find(new ReportableDefinitionQuery().recordName(recordName));
        ReportOptions reportOptions = new ReportOptions();
        reportOptions.setReportName(reportableDefinition.getName());
        reportOptions.setTitle(reportableDefinition.getTitle());
        reportOptions.setRecordName(recordName);

        Map<String, ReportableField> fieldMap =
                db().listAllMap(String.class, "name", new ReportableFieldQuery()
                        .reportableId(reportableDefinition.getId()).parameterOnly(false).installed(Boolean.TRUE));
        boolean isSelectAll = priorityPropertyList == null;
        if (!isSelectAll) {
            for (String property : priorityPropertyList) {
                if (property != null) {
                    ReportableField reportableField = fieldMap.remove(property);
                    if (reportableField != null) {
                        reportOptions.addColumnOptions(createReportColumnOptions(reportableField, true));
                    }
                }
            }
        }

        for (ReportableField reportableField : fieldMap.values()) {
            // Add what's left
            reportOptions.addColumnOptions(createReportColumnOptions(reportableField, isSelectAll));
        }

        return reportOptions;
    }

    @Override
    public void generateDynamicReport(ReportOptions reportOptions, OutputStream outputStream) throws UnifyException {
        Report.Builder rb = Report.newBuilder();
        rb.code(reportOptions.getReportName());
        rb.title(reportOptions.getTitle());
        rb.dataSource(reportOptions.getDataSource());
        rb.pageWidth(reportOptions.getPageWidth());
        rb.pageHeight(reportOptions.getPageHeight());
        rb.dynamicDataSource(reportOptions.isDynamicDataSource());
        rb.printColumnNames(reportOptions.isPrintColumnNames());
        rb.underlineRows(reportOptions.isUnderlineRows());
        rb.shadeOddRows(reportOptions.isShadeOddRows());
        rb.landscape(reportOptions.isLandscape());
        rb.format(ReportFormat.fromName(reportOptions.getReportFormat()));
        if (reportOptions.isColumnarLayout()) {
            rb.layout(ReportLayout.COLUMNAR);
        }

        List<ReportColumnOptions> reportColumnOptionsList =
                new ArrayList<ReportColumnOptions>(reportOptions.getColumnOptionsList());
        DataUtils.sort(reportColumnOptionsList, ReportColumnOptions.class, "group", false);

        List<ReportColumnOptions> sortReportColumnOptionsList = new ArrayList<ReportColumnOptions>();
        for (ReportColumnOptions reportColumnOptions : reportColumnOptionsList) {
            if (reportColumnOptions.isIncluded()) {
                if (reportColumnOptions.isGroup() || reportColumnOptions.getOrder() != null) {
                    sortReportColumnOptionsList.add(reportColumnOptions);
                }

                rb.addColumn(reportColumnOptions.getDescription(), reportColumnOptions.getTableName(),
                        reportColumnOptions.getColumnName(), reportColumnOptions.getType(),
                        reportColumnOptions.getFormatter(), OrderType.fromName(reportColumnOptions.getOrder()),
                        reportColumnOptions.getHorizontalAlignment(), reportColumnOptions.getWidth(),
                        reportColumnOptions.isGroup(), reportColumnOptions.isSum());
            }
        }

        if (reportOptions.isBeanCollection()) {
            Class<?> dataClass = ReflectUtils.getClassForName(reportOptions.getRecordName());
            List<?> content = reportOptions.getContent();
            for (int i = sortReportColumnOptionsList.size() - 1; i >= 0; i--) {
                ReportColumnOptions reportColumnOptions = sortReportColumnOptionsList.get(i);
                DataUtils.sort(content, dataClass, reportColumnOptions.getColumnName(),
                        OrderType.ASCENDING.code().equals(reportColumnOptions.getOrder()));
            }
            rb.beanCollection(content);
        } else {
            rb.query(reportOptions.getQuery());
            rb.table(reportOptions.getTableName());

            if (reportOptions.isJoinOptions()) {
                for (ReportJoinOptions rjo : reportOptions.getJoinOptionsList()) {
                    rb.addJoin(rjo.getTableA(), rjo.getColumnA(), rjo.getTableB(), rjo.getColumnB());
                }
            }

            if (reportOptions.isFilterOptions()) {
                for (ReportFilterOptions rfo : reportOptions.getFilterOptionsList()) {
                    rb.addFilter(rfo.getOp(), rfo.getTableName(), rfo.getColumnName(), rfo.getParam1(),
                            rfo.getParam2());
                }
            }
        }

        Report report = rb.build();
        setCommonReportParameters(report);
        getCommonReportServer().generateReport(report, outputStream);
    }

    @Override
    public boolean isReportable(String recordName) throws UnifyException {
        return db().find(new ReportableDefinitionQuery().name(recordName)) != null;
    }

    @Override
    public void installFeatures(List<ModuleConfig> moduleConfigList) throws UnifyException {
        // Uninstall old records
        db().updateAll(new ReportableDefinitionQuery().installed(Boolean.TRUE),
                new Update().add("installed", Boolean.FALSE));

        // Install new and update old
        ReportableDefinition reportableDefinition = new ReportableDefinition();
        for (ModuleConfig moduleConfig : moduleConfigList) {
            Long moduleId = systemService.getModuleId(moduleConfig.getName());

            // Handle reportables first
            Map<String, Long> reportableIds = new HashMap<String, Long>();
            if (!DataUtils.isBlank(moduleConfig.getReportableList())) {
                logDebug("Installing reportable definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));
                ReportableDefinitionQuery rdQuery = new ReportableDefinitionQuery();
                for (ReportConfig reportConfig : moduleConfig.getReportableList()) {
                    rdQuery.clear();
                    String reportName = reportConfig.getName();
                    String description = resolveApplicationMessage(reportConfig.getDescription());
                    String title = reportConfig.getTitle();
                    if (title == null) {
                        title = description;
                    }

                    ReportableDefinition oldReportableDefinition = db().findLean(rdQuery.name(reportName));
                    Long reportableId = null;
                    if (oldReportableDefinition == null) {
                        reportableDefinition = new ReportableDefinition();
                        reportableDefinition.setModuleId(moduleId);
                        reportableDefinition.setName(reportName);
                        reportableDefinition.setRecordName(reportConfig.getReportable());
                        reportableDefinition.setTitle(title);
                        reportableDefinition.setDescription(description);
                        reportableDefinition.setInstalled(Boolean.TRUE);
                        populateChildList(moduleConfig, reportableDefinition);
                        reportableId = (Long) db().create(reportableDefinition);
                    } else {
                        // Update old definition
                        oldReportableDefinition.setRecordName(reportConfig.getReportable());
                        oldReportableDefinition.setTitle(title);
                        oldReportableDefinition.setDescription(description);
                        oldReportableDefinition.setInstalled(Boolean.TRUE);
                        populateChildList(moduleConfig, oldReportableDefinition);
                        db().updateByIdVersion(oldReportableDefinition);
                        reportableId = oldReportableDefinition.getId();
                    }

                    reportableIds.put(reportConfig.getReportable(), reportableId);
                }
            }

            // Handle configured reports
            if (moduleConfig.getReports() != null
                    && !DataUtils.isBlank(moduleConfig.getReports().getReportGroupList())) {
                logDebug("Installing configured report definitions for module [{0}]...",
                        resolveApplicationMessage(moduleConfig.getDescription()));
                for (ReportGroupConfig reportGroupConfig : moduleConfig.getReports().getReportGroupList()) {
                    ReportGroupQuery rgQuery = new ReportGroupQuery();
                    ReportGroup oldReportGroup =
                            db().find(rgQuery.moduleId(moduleId).name(reportGroupConfig.getName()));
                    Long reportGroupId = null;
                    if (oldReportGroup == null) {
                        ReportGroup reportGroup = new ReportGroup();
                        reportGroup.setModuleId(moduleId);
                        reportGroup.setName(reportGroupConfig.getName());
                        reportGroup.setDescription(reportGroupConfig.getDescription());
                        reportGroupId = (Long) db().create(reportGroup);
                    } else {
                        oldReportGroup.setDescription(reportGroupConfig.getDescription());
                        db().updateByIdVersion(oldReportGroup);
                        reportGroupId = oldReportGroup.getId();
                    }

                    ReportConfigurationQuery rcQuery = new ReportConfigurationQuery();
                    if (!DataUtils.isBlank(reportGroupConfig.getReportList())) {
                        for (ReportConfig reportConfig : reportGroupConfig.getReportList()) {
                            rcQuery.clear();
                            String description = resolveApplicationMessage(reportConfig.getDescription());
                            String title = reportConfig.getTitle();
                            if (title == null) {
                                title = description;
                            }

                            ReportConfiguration oldReportConfiguration =
                                    db().findLean(rcQuery.reportGroupId(reportGroupId).name(reportConfig.getName()));
                            Long reportableId = null;
                            if (!StringUtils.isBlank(reportConfig.getReportable())) {
                                reportableId = reportableIds.get(reportConfig.getReportable());
                            }

                            if (oldReportConfiguration == null) {
                                ReportConfiguration reportConfiguration = new ReportConfiguration();
                                reportConfiguration.setReportGroupId(reportGroupId);
                                reportConfiguration.setReportableId(reportableId);
                                reportConfiguration.setName(reportConfig.getName());
                                reportConfiguration.setDescription(description);
                                reportConfiguration.setTitle(title);
                                reportConfiguration.setTemplate(reportConfig.getTemplate());
                                reportConfiguration.setProcessor(reportConfig.getProcessor());
                                reportConfiguration.setLandscape(reportConfig.isLandscape());
                                reportConfiguration.setShadeOddRows(reportConfig.isShadeOddRows());
                                reportConfiguration.setUnderlineRows(reportConfig.isUnderlineRows());
                                populateChildList(reportConfig, reportConfiguration);
                                db().create(reportConfiguration);
                            } else {
                                oldReportConfiguration.setReportableId(reportableId);
                                oldReportConfiguration.setDescription(description);
                                oldReportConfiguration.setTitle(title);
                                oldReportConfiguration.setTemplate(reportConfig.getTemplate());
                                oldReportConfiguration.setProcessor(reportConfig.getProcessor());
                                oldReportConfiguration.setLandscape(reportConfig.isLandscape());
                                oldReportConfiguration.setShadeOddRows(reportConfig.isShadeOddRows());
                                oldReportConfiguration.setUnderlineRows(reportConfig.isUnderlineRows());
                                populateChildList(reportConfig, oldReportConfiguration);
                                db().updateByIdVersion(oldReportConfiguration);
                            }
                        }
                    }

                }

            }
        }
    }

    private void populateChildList(ModuleConfig moduleConfig, ReportableDefinition reportableDefinition)
            throws UnifyException {
        List<ReportableField> fieldList = new ArrayList<ReportableField>();
        // Re-create/Create report fields
        ManagedConfig managedConfig = JacklynUtils.getManagedConfig(moduleConfig, reportableDefinition.getRecordName());
        for (FieldConfig rfd : managedConfig.getFieldList()) {
            if (rfd.isReportable()) {
                ReportableField reportableField = new ReportableField();
                reportableField.setDescription(rfd.getDescription());
                reportableField.setFormatter(rfd.getFormatter());
                reportableField.setHorizontalAlign(rfd.gethAlign());
                reportableField.setName(rfd.getName());
                reportableField.setParameterOnly(rfd.isParameterOnly());
                reportableField.setType(rfd.getType());
                reportableField.setWidth(rfd.getWidth());
                reportableField.setInstalled(Boolean.TRUE);
                fieldList.add(reportableField);
            }
        }

        reportableDefinition.setFieldList(fieldList);
    }

    private void populateChildList(ReportConfig reportConfig, ReportConfiguration reportConfiguration) {
        // Columns
        if (reportConfig.getColumns() != null && !DataUtils.isBlank(reportConfig.getColumns().getColumnList())) {
            List<com.tcdng.jacklyn.report.entities.ReportColumn> columnList =
                    new ArrayList<com.tcdng.jacklyn.report.entities.ReportColumn>();
            for (ColumnConfig columnConfig : reportConfig.getColumns().getColumnList()) {
                com.tcdng.jacklyn.report.entities.ReportColumn reportColumn =
                        new com.tcdng.jacklyn.report.entities.ReportColumn();
                reportColumn.setColumnOrder(columnConfig.getColumnOrder());
                reportColumn.setFieldName(columnConfig.getFieldName());
                reportColumn.setDescription(columnConfig.getDescription());
                reportColumn.setType(columnConfig.getType());
                reportColumn.setWidth(columnConfig.getWidth());
                reportColumn.setGroup(columnConfig.isGroup());
                reportColumn.setSum(columnConfig.isSum());
                columnList.add(reportColumn);
            }

            reportConfiguration.setColumnList(columnList);
        }

        // Parameters
        if (reportConfig.getParameters() != null
                && !DataUtils.isBlank(reportConfig.getParameters().getParameterList())) {
            List<ReportParameter> parameterList = new ArrayList<ReportParameter>();
            for (ParameterConfig parameterConfig : reportConfig.getParameters().getParameterList()) {
                ReportParameter reportParameter = new ReportParameter();
                reportParameter.setName(parameterConfig.getName());
                reportParameter.setDescription(parameterConfig.getDescription());
                reportParameter.setEditor(parameterConfig.getEditor());
                reportParameter.setLabel(parameterConfig.getLabel());
                reportParameter.setMandatory(parameterConfig.isMandatory());
                reportParameter.setType(parameterConfig.getType());
                reportParameter.setDefaultVal(parameterConfig.getDefaultVal());
                parameterList.add(reportParameter);
            }

            reportConfiguration.setParameterList(parameterList);
        }

        // Filters
        if (reportConfig.getFilter() != null) {
            List<ReportFilter> filterList = new ArrayList<ReportFilter>();
            populateFilterList(reportConfig.getFilter(), filterList, 0);
            reportConfiguration.setFilterList(filterList);
        }
    }

    private void populateFilterList(FilterConfig filterConfig, List<ReportFilter> filterList, int compoundIndex) {
        ReportFilter reportFilter = new ReportFilter();
        reportFilter.setFieldName(filterConfig.getFieldName());
        reportFilter.setOperation(filterConfig.getOp());
        reportFilter.setValue1(filterConfig.getValue1());
        reportFilter.setValue2(filterConfig.getValue2());
        reportFilter.setUseParameter(filterConfig.isUseParameter());
        reportFilter.setCompoundIndex(compoundIndex);
        filterList.add(reportFilter);

        if (filterConfig.getOp().isCompound() && !DataUtils.isBlank(filterConfig.getFilterList())) {
            int subCompoundIndex = compoundIndex + 1;
            for (FilterConfig subFilterConfig : filterConfig.getFilterList()) {
                populateFilterList(subFilterConfig, filterList, subCompoundIndex);
            }
        }
    }

    private ReportServer getCommonReportServer() throws UnifyException {
        return (ReportServer) getApplicationAttribute(JacklynApplicationAttributeConstants.COMMON_REPORT_SERVER);
    }

    private ReportColumnOptions createReportColumnOptions(ReportableField reportableField, boolean included) {
        return new ReportColumnOptions(reportableField.getName(), reportableField.getDescription(),
                reportableField.getType(), reportableField.getFormatter(),
                HAlignType.fromName(reportableField.getHorizontalAlign()), reportableField.getWidth(), included);
    }

    private void setCommonReportParameters(Report report) throws UnifyException {
        report.setParameter(ReportParameterConstants.APPLICATION_TITLE, getUnifyComponentContext().getInstanceName());
        report.setParameter(ReportParameterConstants.CLIENT_TITLE,
                systemService.getSysParameterValue(String.class, SystemModuleSysParamConstants.SYSPARAM_CLIENT_TITLE));
        report.setParameter(ReportParameterConstants.REPORT_TITLE, report.getTitle());

        String imagePath =
                WebUtils.expandThemeTag(systemService.getSysParameterValue(String.class,
                        ReportModuleSysParamConstants.REPORT_CLIENT_LOGO));
        byte[] clientLogo = IOUtils.readFileResourceInputStream(imagePath, getUnifyComponentContext().getWorkingPath());
        report.setParameter(ReportParameterConstants.CLIENT_LOGO, clientLogo);

        String templatePath =
                systemService.getSysParameterValue(String.class, ReportModuleSysParamConstants.REPORT_TEMPLATE_PATH);
        String template = report.getTemplate();
        if (template == null) {
            String templateParameter = ReportModuleSysParamConstants.DYNAMIC_REPORT_PORTRAIT_TEMPLATE;
            if (report.isLandscape()) {
                templateParameter = ReportModuleSysParamConstants.DYNAMIC_REPORT_LANDSCAPE_TEMPLATE;
            }
            template = systemService.getSysParameterValue(String.class, templateParameter);
        }
        report.setTemplate(IOUtils.buildFilename(templatePath, template));
    }
}
