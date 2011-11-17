package mcmanager.web;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Distribution;
import mcmanager.data.Group;
import mcmanager.data.StatusEnum;
import mcmanager.data.TypeDistributionEnum;
import mcmanager.exception.CoreException;
import mcmanager.log.LogEnum;
import mcmanager.utils.MessageUtils;
import mcmanager.utils.StringUtils;
import mcmanager.utils.TorrentInfo;
import mcmanager.web.WebBrowser.TorrentFile;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

@SessionScoped
@ManagedBean(name = "indexBean")
public class IndexBean {

    
    /**
     * Все группы в системе
     */
    private List<SelectItem> groupItem;
    
    private List<SelectItem> filmType;

    private List<SelectItem> filmStatus;

    private String findFilmParam = "";

    private StatusEnum actualStatus;
    
    /**
     * Выбраная группа
     * Это группа с который мы сейчас работаем(активная группа)
     */
    private Group selectGroupData;
    
    private Group groupEdit = new Group();
    
    private Distribution filmEdit = new Distribution();
    
    private List<Group> groupList;
    
    private DataModel<Group> groupModel;
    
    private List<Distribution> filmList;
    
    private List<Distribution> allFilmList;
    
    private DataModel<Distribution> filmModel;
    
    private boolean activeFilm = true;

    private boolean sortByDate = false;

    @PostConstruct
    public void init() {
        getAllGroup();
        getAllFilm();
        loadFilmType();
        loadFilmStatus();
        getFilmByFilter(null);
    }
    
    public void refreshTable(AjaxBehaviorEvent event) {
        getAllFilm();
        getAllGroup();
        getFilmByFilter(null);
        groupModel = new ListDataModel<Group>(groupList);
    }
    
    public void changeActivePanel(AjaxBehaviorEvent event) {
        activeFilm = !activeFilm;
    }
    
    private void fillGroupItem() {
        groupItem = new ArrayList<SelectItem>();
        for (Group group : groupList) {
            groupItem.add(new SelectItem(group, group.getName()));
        }
    }
    
    private void getAllGroup() {
        groupList = DaoFactory.getInstance().getGroupDao().getAllGroup();
        fillGroupItem();
    }
    
    private void getAllFilm() {
        filmList = DaoFactory.getInstance().getDistributionDao().getAllDistribution();
        allFilmList = new ArrayList<Distribution>(filmList);
    }

    public void getFilmByFilter(AjaxBehaviorEvent event) {
        filmList = prepareFilmList();
        filmModel = new ListDataModel<Distribution>(filmList);
    }

    public void sortById(AjaxBehaviorEvent event) {
        sortByDate = !sortByDate;
        getFilmByFilter(null);
    }

    private List<Distribution> prepareFilmList() {
        List<Distribution> filmList = null;
        filmList = new ArrayList<Distribution>();
        Collections.sort(allFilmList, new Comparator<Distribution>() {
            @Override
            public int compare(Distribution o1, Distribution o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });
        if (!sortByDate)
            Collections.reverse(allFilmList);

        for (Distribution film : allFilmList) {
            if ((selectGroupData == null || film.getGroup().equals(selectGroupData)) &&
                    (film.getStatus() == actualStatus || actualStatus == null) &&
                        film.getTitle().toLowerCase().contains(findFilmParam.toLowerCase())) {
                filmList.add(film);
            }
        }
        return filmList;
    }
    
    public void dialogChangeGroup(AjaxBehaviorEvent event) {
        if (filmEdit != null && filmEdit.getGroup() != null) {
            filmEdit.setMailMessage(filmEdit.getGroup().getEmailMessage());
            filmEdit.setMailRegexp(filmEdit.getGroup().getEmailRegexp());
        }
    }

    private void loadFilmType() {
        filmType = new ArrayList<SelectItem>();
        for (TypeDistributionEnum type : TypeDistributionEnum.values()) {
            filmType.add(new SelectItem(type.getType(), type.getDesc()));
        }
    }

    private void loadFilmStatus() {
        filmStatus = new ArrayList<SelectItem>();
        for (StatusEnum status : StatusEnum.values()) {
            filmStatus.add(new SelectItem(status, status.getDesc()));
        }
    }

    public void deleteGroup(AjaxBehaviorEvent event) {
        Group group = groupModel.getRowData();
        DaoFactory.getInstance().getGroupDao().deleteGroup(group.getId());
        groupList.remove(group);
        fillGroupItem();
        //        getAllGroup();
    }
    
    public void deleteFilm(AjaxBehaviorEvent event) {
        Distribution distribution = filmModel.getRowData();
        DaoFactory.getInstance().getDistributionDao().removeDistribution(distribution.getId());
        filmList.remove(distribution);
//        getAllFilm();
    }
    
    public void editGroup(AjaxBehaviorEvent event) {
        clearSubmittedValues(event.getComponent().getParent().getParent().getParent(), "groupDialog");
        groupEdit = groupModel.getRowData();
    }
    
    public void editFilm(AjaxBehaviorEvent event) {
        clearSubmittedValues(event.getComponent().getParent().getParent().getParent(), "filmDialog");
        filmEdit = filmModel.getRowData();
    }
    
    public void clear(AjaxBehaviorEvent event) {
        groupEdit = new Group();
        filmEdit = new Distribution();
        filmEdit.setStatus(StatusEnum.NEW);
        clearSubmittedValues(event.getComponent(), "groupDialog");
        clearSubmittedValues(event.getComponent(), "filmDialog");
        if (!groupItem.isEmpty()) {
            filmEdit.setGroup((Group) groupItem.get(0).getValue());
        }
        dialogChangeGroup(null);
    }

    public static void clearSubmittedValues(UIComponent component, String componentName) {
        UIComponent uiComponent = component.findComponent(componentName);
        clearSubmittedValues(uiComponent);
    }

    public static void clearSubmittedValues(UIComponent uiComponent) {
        if (uiComponent == null) {
            return;
        }

        Iterator<UIComponent> children = (uiComponent).getFacetsAndChildren();
        while (children.hasNext()) {
            clearSubmittedValues(children.next());
        }
        if (uiComponent instanceof UIInput) {
            ((UIInput) uiComponent).setSubmittedValue(null);
            ((UIInput) uiComponent).setValue(null);
            ((UIInput) uiComponent).setLocalValueSet(false);
            ((UIInput) uiComponent).resetValue();
        }
    }

    
    public void updateGroup(AjaxBehaviorEvent event) {
        DaoFactory.getInstance().getGroupDao().updateGroup(groupEdit);
    }
    
    public void saveGroup(AjaxBehaviorEvent event) {
        DaoFactory.getInstance().getGroupDao().addGroup(groupEdit);
        groupList.add(groupEdit);
        fillGroupItem();
    }
    
    public void updateFilm(AjaxBehaviorEvent event) {
        System.out.println("PRE UPDATE");
        System.out.println(filmEdit);
        DaoFactory.getInstance().getDistributionDao().updateDistribution(filmEdit);
        System.out.println("POST UPDATE");
    }
    
    public void saveFilm(AjaxBehaviorEvent event) {
        WebBrowser webBrowser = new WebBrowser(LogEnum.WEB.getLog());
        try {
            webBrowser.goToUrl(filmEdit.getLinkRutracker());
            filmEdit.setTitle(webBrowser.getTitle());
        } catch (CoreException e) {
            //TODO Избавится от такого большого исключения сделать один статический метод
            FacesMessage error = new FacesMessage();
            error.setSeverity(FacesMessage.SEVERITY_ERROR);
            error.setSummary(e.getMessage());
            throw new ValidatorException(error);
        }
        
        if (filmEdit.getType() != TypeDistributionEnum.SERIALS.getType()) {
            filmEdit.setRegexpSerialNumber(null);
            filmEdit.setSeasonNumber(null);
        }
        DaoFactory.getInstance().getDistributionDao().addDistribution(filmEdit);
        if (filmList.size() > 0)
            filmList.add(0, filmEdit);
        filmList.add(filmEdit);
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
    
    public void testMessage(AjaxBehaviorEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        String url = (String) ((UIInput)component.findComponent("filmLinkRutracker")).getValue();
        
        WebBrowser webBrowser = new WebBrowser(LogEnum.WEB.getLog());
        FacesMessage message = new FacesMessage();
        try {
            webBrowser.goToUrl(url);
            String title = webBrowser.getTitle();
            filmEdit.setTitle(title);
            String regexp = (String) ((UIInput)component.findComponent("filmMailRegexp")).getValue();
            String mailMessage = (String) ((UIInput)component.findComponent("filmMailMessage")).getValue();
            message.setSummary(MessageUtils.createMessage(title, regexp, mailMessage));
            message.setSeverity(FacesMessage.SEVERITY_INFO);
        } catch (CoreException e) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary(e.getMessage());
        }
        context.addMessage("otherMessageHidden", message);
        context.renderResponse();
    }
    
    public void testTorrent(AjaxBehaviorEvent event) {
        //TODO Это точно такая же часть как и в testMessage
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent component = UIComponent.getCurrentComponent(context);
        String url = (String) ((UIInput)component.findComponent("filmLinkRutracker")).getValue();
        
        WebBrowser webBrowser = new WebBrowser(LogEnum.WEB.getLog());
        //
        
        String regexp = (String) ((UIInput)component.findComponent("filmRegexpSerialNumber")).getValue();
        FacesMessage message = new FacesMessage();
        try {
            webBrowser.goToUrl(url);
            filmEdit.setTitle(webBrowser.getTitle());
            TorrentFile torrent = webBrowser.downloadTorrentFile(webBrowser.getTorrentUrl());
            ByteArrayInputStream bais = new ByteArrayInputStream(torrent.getContent());
            TorrentInfo info = new TorrentInfo(new BufferedInputStream(bais));
            StringBuilder builder = new StringBuilder();
            for (String fileName : info.getInfo()) {
                builder.append(fileName).append(" № серии: \"").append(MessageUtils.parseEpisode(fileName, regexp)).append("\"  ");
            }
            message.setSummary(builder.toString());
            message.setSeverity(FacesMessage.SEVERITY_INFO);
        } catch (CoreException e) {
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            message.setSummary(e.getMessage());
        }
        context.addMessage("otherMessageHidden", message);
        context.renderResponse();
        
    }
    
    public Group getSelectGroupData() {
        return selectGroupData;
    }

    public void setSelectGroupData(Group selectGroupData) {
        this.selectGroupData = selectGroupData;
    }

    public List<SelectItem> getGroupItem() {
        return groupItem;
    }

    public void setGroupItem(List<SelectItem> groupItem) {
        this.groupItem = groupItem;
    }
    
    public DataModel<Group> getGroupModel() {
        if (groupModel == null)
            groupModel = new ListDataModel<Group>(groupList);
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

    public Distribution getFilmEdit() {
        return filmEdit;
    }

    public void setFilmEdit(Distribution filmEdit) {
        this.filmEdit = filmEdit;
    }

    public DataModel<Distribution> getFilmModel() {
        if (filmModel == null)
            filmModel = new ListDataModel<Distribution>(filmList);
        return filmModel;
    }

    public void setFilmModel(DataModel<Distribution> filmModel) {
        this.filmModel = filmModel;
    }

    public List<SelectItem> getFilmType() {
        return filmType;
    }

    public void setFilmType(List<SelectItem> filmType) {
        this.filmType = filmType;
    }

    public List<SelectItem> getFilmStatus() {
        return filmStatus;
    }

    public void setFilmStatus(List<SelectItem> filmStatus) {
        this.filmStatus = filmStatus;
    }

    public boolean isActiveFilm() {
        return activeFilm;
    }

    public void setActiveFilm(boolean activeFilm) {
        this.activeFilm = activeFilm;
    }

    public String getFindFilmParam() {
        return findFilmParam;
    }

    public void setFindFilmParam(String findFilmParam) {
        this.findFilmParam = findFilmParam;
    }

    public StatusEnum getActualStatus() {
        return actualStatus;
    }

    public void setActualStatus(StatusEnum actualStatus) {
        this.actualStatus = actualStatus;
    }
}
