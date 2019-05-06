package com.hy.service.Scavenger;

public class WebClass {

    public class Web_Bill_MasterControl {
        public String RowNo;

        public String PDAColumnName;

        public String SelectName;

        public String ShowName;

        public String CtrlType;

        public String SelectCtrl_DisplayField;

        public String SelectCtrl_Filer;

        public String DefaultValue;

        public String IsShow;

        public String IsReadOnly;

        public String IsNotNull;

        public String IsSelectBillFiler;

        public String IsNotEmpty;

        public String getRowNo() {
            return RowNo;
        }

        public void setRowNo(String rowNo) {
            RowNo = rowNo;
        }

        public String getPDAColumnName() {
            return PDAColumnName;
        }

        public void setPDAColumnName(String PDAColumnName) {
            this.PDAColumnName = PDAColumnName;
        }

        public String getSelectName() {
            return SelectName;
        }

        public void setSelectName(String selectName) {
            SelectName = selectName;
        }

        public String getShowName() {
            return ShowName;
        }

        public void setShowName(String showName) {
            ShowName = showName;
        }

        public String getCtrlType() {
            return CtrlType;
        }

        public void setCtrlType(String ctrlType) {
            CtrlType = ctrlType;
        }

        public String getSelectCtrl_DisplayField() {
            return SelectCtrl_DisplayField;
        }

        public void setSelectCtrl_DisplayField(String selectCtrl_DisplayField) {
            SelectCtrl_DisplayField = selectCtrl_DisplayField;
        }

        public String getSelectCtrl_Filer() {
            return SelectCtrl_Filer;
        }

        public void setSelectCtrl_Filer(String selectCtrl_Filer) {
            SelectCtrl_Filer = selectCtrl_Filer;
        }

        public String getDefaultValue() {
            return DefaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            DefaultValue = defaultValue;
        }

        public String getIsShow() {
            return IsShow;
        }

        public void setIsShow(String isShow) {
            IsShow = isShow;
        }

        public String getIsReadOnly() {
            return IsReadOnly;
        }

        public void setIsReadOnly(String isReadOnly) {
            IsReadOnly = isReadOnly;
        }

        public String getIsNotNull() {
            return IsNotNull;
        }

        public void setIsNotNull(String isNotNull) {
            IsNotNull = isNotNull;
        }

        public String getIsSelectBillFiler() {
            return IsSelectBillFiler;
        }

        public void setIsSelectBillFiler(String isSelectBillFiler) {
            IsSelectBillFiler = isSelectBillFiler;
        }

        public String getIsNotEmpty() {
            return IsNotEmpty;
        }

        public void setIsNotEmpty(String isNotEmpty) {
            IsNotEmpty = isNotEmpty;
        }
    }

    /// <summary>
    /// 设置主表控件的属性
    /// </summary>
    /// <param name="RowNo">行号</param>
    /// <param name="PDAColumnName">PDA本地列,不能列,有一套固定列名</param>
    /// <param name="SelectName">查询的赋值显示名,　只有Combox和SelectText用到</param>
    /// <param name="ShowName">中文Lable显示内容</param>
    /// <param name="CtrlType">控件类型分为Text,DateTime,SelectText,ComBox</param>
    /// <param name="SelectCtrl_DisplayField">SelectText控件用－查询时显示的格式</param>
    /// <param name="SelectCtrl_Filer">SelectText控件用－查询时的条件</param>
    /// <param name="DefaultValue">默认值-Text,DateTime控件用</param>
    /// <param name="IsShow">控件是否显示 值为 1 和 0</param>
    /// <param name="IsReadOnly">是否只读 值为 1 和 0</param>
    /// <param name="IsNotNull">是否必填 值为 1 和 0</param>
    /// <param name="IsSelectBillFiler">是否为查询时的条件  值为 1 和 0 </param>
    /// <param name="IsNotEmpty">点击新单后是否保留内容 值为 1 和 0</param>
    /// <returns></returns>
    public static String SetMasterCtrl(String RowNo, String PDAColumnName, String SelectName, String ShowName, String CtrlType, String SelectCtrl_DisplayField, String SelectCtrl_Filer, String DefaultValue, String IsShow, String IsReadOnly, String IsNotNull, String IsSelectBillFiler, String IsNotEmpty) {
        return "{ \"RowNo\": \"" + RowNo + "\",\"PDAColumnName\": \"" + PDAColumnName + "\", \"SelectName\":\"" + SelectName + "\", \"ShowName\":\"" + ShowName + "\", \"CtrlType\": \"" + CtrlType + "\", \"SelectCtrl_DisplayField\": \"" + SelectCtrl_DisplayField + "\", \"SelectCtrl_Filer\": \"" + SelectCtrl_Filer + "\", \"DefaultValue\": \"" + DefaultValue + "\",\"IsShow\": \"" + IsShow + "\", \"IsReadOnly\": \"" + IsReadOnly + "\", \"IsNotNull\": \"" + IsNotNull + "\", \"IsSelectBillFiler\": \"" + IsSelectBillFiler + "\", \"IsNotEmpty\": \"" + IsNotEmpty + "\" }";
    }

    public class Web_Bill_DetailedControl {

        public String RowNo;

        public String ColumnNo;

        public String PDAColumnName;

        public String SelectName;

        public String ShowName;

        public String CtrlType;

        public String SelectCtrl_DisplayField;


        public String SelectCtrl_Filer;

        public String DefaultValue;

        public String IsShow;

        public String IsReadOnly;

        public String IsNotNull;

        public String IsNotEmpty;

        public String Formula;

        public String IsOnly;

        public String getRowNo() {
            return RowNo;
        }

        public void setRowNo(String rowNo) {
            RowNo = rowNo;
        }

        public String getColumnNo() {
            return ColumnNo;
        }

        public void setColumnNo(String columnNo) {
            ColumnNo = columnNo;
        }

        public String getPDAColumnName() {
            return PDAColumnName;
        }

        public void setPDAColumnName(String PDAColumnName) {
            this.PDAColumnName = PDAColumnName;
        }

        public String getSelectName() {
            return SelectName;
        }

        public void setSelectName(String selectName) {
            SelectName = selectName;
        }

        public String getShowName() {
            return ShowName;
        }

        public void setShowName(String showName) {
            ShowName = showName;
        }

        public String getCtrlType() {
            return CtrlType;
        }

        public void setCtrlType(String ctrlType) {
            CtrlType = ctrlType;
        }

        public String getSelectCtrl_DisplayField() {
            return SelectCtrl_DisplayField;
        }

        public void setSelectCtrl_DisplayField(String selectCtrl_DisplayField) {
            SelectCtrl_DisplayField = selectCtrl_DisplayField;
        }

        public String getSelectCtrl_Filer() {
            return SelectCtrl_Filer;
        }

        public void setSelectCtrl_Filer(String selectCtrl_Filer) {
            SelectCtrl_Filer = selectCtrl_Filer;
        }

        public String getDefaultValue() {
            return DefaultValue;
        }

        public void setDefaultValue(String defaultValue) {
            DefaultValue = defaultValue;
        }

        public String getIsShow() {
            return IsShow;
        }

        public void setIsShow(String isShow) {
            IsShow = isShow;
        }

        public String getIsReadOnly() {
            return IsReadOnly;
        }

        public void setIsReadOnly(String isReadOnly) {
            IsReadOnly = isReadOnly;
        }

        public String getIsNotNull() {
            return IsNotNull;
        }

        public void setIsNotNull(String isNotNull) {
            IsNotNull = isNotNull;
        }

        public String getIsNotEmpty() {
            return IsNotEmpty;
        }

        public void setIsNotEmpty(String isNotEmpty) {
            IsNotEmpty = isNotEmpty;
        }

        public String getFormula() {
            return Formula;
        }

        public void setFormula(String formula) {
            Formula = formula;
        }

        public String getIsOnly() {
            return IsOnly;
        }

        public void setIsOnly(String isOnly) {
            IsOnly = isOnly;
        }
    }

    /// <summary>
    /// 设置从表控件的属性
    /// </summary>
    /// <param name="RowNo">行号</param>
    /// <param name="PDAColumnName">PDA本地列,不能列,有一套固定列名</param>
    /// <param name="SelectName">查询的赋值显示名,　只有Combox和SelectText用到</param>
    /// <param name="ShowName">中文Lable显示内容</param>
    /// <param name="CtrlType">控件类型分为Text,DateTime,SelectText,ComBox</param>
    /// <param name="SelectCtrl_DisplayField">SelectText控件用－查询时显示的格式</param>
    /// <param name="SelectCtrl_Filer">SelectText控件用－查询时的条件</param>
    /// <param name="DefaultValue">默认值-Text,DateTime控件用</param>
    /// <param name="IsShow">控件是否显示 值为 1 和 0</param>
    /// <param name="IsReadOnly">是否只读 值为 1 和 0</param>
    /// <param name="IsNotNull">是否必填 值为 1 和 0</param>
    /// <param name="IsNotEmpty">点击新单后是否保留内容 值为 1 和 0</param>
    ///  <param name="Formula">计算公式　列名用max(列名)</param>
    ///  <param name="IsOnly">是否是唯一列　,用于在明细中判断是否合并列的条件</param>
    /// <returns></returns>
    public static String SetDetailedCtrl(String RowNo, String PDAColumnName, String SelectName, String ShowName, String CtrlType, String SelectCtrl_DisplayField, String SelectCtrl_Filer, String DefaultValue, String IsShow, String IsReadOnly, String IsNotNull, String IsNotEmpty, String Formula, String IsOnly) {
        return "{ \"RowNo\": \"" + RowNo + "\",\"PDAColumnName\": \"" + PDAColumnName + "\", \"SelectName\":\"" + SelectName + "\", \"ShowName\":\"" + ShowName + "\", \"CtrlType\": \"" + CtrlType + "\", \"SelectCtrl_DisplayField\": \"" + SelectCtrl_DisplayField + "\", \"SelectCtrl_Filer\": \"" + SelectCtrl_Filer + "\", \"DefaultValue\": \"" + DefaultValue + "\",\"IsShow\": \"" + IsShow + "\", \"IsReadOnly\": \"" + IsReadOnly + "\", \"IsNotNull\": \"" + IsNotNull + "\", \"IsNotEmpty\": \"" + IsNotEmpty + "\", \"Formula\": \"" + Formula + "\", \"IsOnly\": \"" + IsOnly + "\" }";
    }



}
