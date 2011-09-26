package mcmanager.web;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.ArrayDataModel;
import javax.faces.model.DataModel;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Group;
import mcmanager.utils.StringUtils;

@SessionScoped
@ManagedBean(name = "indexBean")
public class IndexBean {

    
    /**
     * Все группы в системе
     */
    private List<SelectItem> groupItem;
    
    /**
     * Выбраная группа
     * Это группа с который мы сейчас работаем(активная группа)
     */
    private String selectGroupData;
    
    /**
     * Таблица групп
     */
    private DataModel<Group> groupModel;
    
    private Group groupEdit;
    
    @PostConstruct
    public void init() {
        getAllGroup();
    }
    
    private void getAllGroup() {
        groupItem = new ArrayList<SelectItem>();
        List<Group> groupList = DaoFactory.getInstance().getGroupDao().getAllGroup();
        for (Group group : groupList) {
            groupItem.add(new SelectItem(group.getName()));
        }
        groupModel = new ArrayDataModel<Group>(groupList.toArray(new Group[groupList.size()]));
    }

    public void deleteGroup(AjaxBehaviorEvent event) {
        Group group = groupModel.getRowData();
        DaoFactory.getInstance().getGroupDao().deleteGroup(group.getId());
        getAllGroup();
    }
    
    public void editGroup(AjaxBehaviorEvent event) {
        System.out.println("editGroup");
        groupEdit = groupModel.getRowData();
    }
    
    public void clearGroup(AjaxBehaviorEvent event) {
        System.out.println("clearGroup");
        groupEdit = new Group();
    }
    
    public void updateGroup(AjaxBehaviorEvent event) {
        DaoFactory.getInstance().getGroupDao().updateGroup(groupEdit);
        getAllGroup();
    }
    
    public void saveGroup(AjaxBehaviorEvent event) {
        System.out.println("SAVE");
        DaoFactory.getInstance().getGroupDao().addGroup(groupEdit);
        getAllGroup();
    }
    
    
    public void validateGroupMessage(FacesContext context, UIComponent component, Object value) {
        String strValue = (String) value;
        if (StringUtils.isEmpty(strValue)) {
            UIComponent ui = component.findComponent("groupEmailRegexp");
            UIInput input = (UIInput)ui;
            if (!StringUtils.isEmpty((String)input.getValue())) {
                FacesMessage error = new FacesMessage();
                error.setSeverity(FacesMessage.SEVERITY_ERROR);
                error.setSummary("Поле обязательно для заполнения");
                throw new ValidatorException(error);
            }
        }
        
    }
    
    public void validateGroupRegexp(FacesContext context, UIComponent component, Object value) {
        String strValue = (String) value;    
        try {
            if (!StringUtils.isEmpty(strValue))
                Pattern.compile(strValue);
        } catch (PatternSyntaxException e) {
            FacesMessage error = new FacesMessage();
            error.setSeverity(FacesMessage.SEVERITY_ERROR);
            error.setSummary("Не валидное regexp выражение");
            throw new ValidatorException(error);
        }
    }
    
    public String getSelectGroupData() {
        return selectGroupData;
    }

    public void setSelectGroupData(String selectGroupData) {
        this.selectGroupData = selectGroupData;
    }

    public List<SelectItem> getGroupItem() {
        return groupItem;
    }

    public void setGroupItem(List<SelectItem> groupItem) {
        this.groupItem = groupItem;
    }
    
    public DataModel<Group> getGroupModel() {
        return groupModel;
    }

    public void setGroupModel(DataModel<Group> groupModel) {
        this.groupModel = groupModel;
    }

    public Group getGroupEdit() {
        return groupEdit;
    }

    public void setGroupEdit(Group groupEdit) {
        this.groupEdit = groupEdit;
    }
    
}
