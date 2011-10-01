package mcmanager.web;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

@SessionScoped
@ManagedBean(name = "indexBean")
public class IndexBean {

    
    /**
     * Все группы в системе
     */
    private List<SelectItem> groupItem;
    
    private List<SelectItem> filmType;
    
    /**
     * Выбраная группа
     * Это группа с который мы сейчас работаем(активная группа)
     */
    private String selectGroupData;
    
    private Group groupEdit = new Group();
    /**
     * Таблица групп
     */
    private DataModel<Group> groupModel;
    
    private Distribution filmEdit = new Distribution();
    
    private DataModel<Distribution> filmModel;
    
    @PostConstruct
    public void init() {
        getAllGroup();
        getAllFilm();
        loadFilmType();
    }
    
    private void getAllGroup() {
        groupItem = new ArrayList<SelectItem>();
        List<Group> groupList = DaoFactory.getInstance().getGroupDao().getAllGroup();
        for (Group group : groupList) {
            groupItem.add(new SelectItem(group, group.getName()));
        }
        groupModel = new ArrayDataModel<Group>(groupList.toArray(new Group[groupList.size()]));
    }
    
    private void getAllFilm() {
        List<Distribution> distributionList = DaoFactory.getInstance().getDistributionDao().getAllDistribution();
        filmModel = new ArrayDataModel<Distribution>(distributionList.toArray(new Distribution[distributionList.size()]));
    }
    
    private void loadFilmType() {
        filmType = new ArrayList<SelectItem>();
        for (TypeDistributionEnum type : TypeDistributionEnum.values()) {
            filmType.add(new SelectItem(type.getType(), type.getDesc()));
        }
    }

    public void deleteGroup(AjaxBehaviorEvent event) {
        Group group = groupModel.getRowData();
        DaoFactory.getInstance().getGroupDao().deleteGroup(group.getId());
        getAllGroup();
    }
    
    public void deleteFilm(AjaxBehaviorEvent event) {
        Distribution distribution = filmModel.getRowData();
        DaoFactory.getInstance().getDistributionDao().removeDistribution(distribution.getId());
        getAllFilm();
    }
    
    public void editGroup(AjaxBehaviorEvent event) {
        groupEdit = groupModel.getRowData();
    }
    
    public void editFilm(AjaxBehaviorEvent event) {
        filmEdit = filmModel.getRowData();
    }
    
    public void clear(AjaxBehaviorEvent event) {
        groupEdit = new Group();
        filmEdit = new Distribution();
        filmEdit.setStatus(StatusEnum.NEW.getStatus());
    }
    
    public void updateGroup(AjaxBehaviorEvent event) {
        DaoFactory.getInstance().getGroupDao().updateGroup(groupEdit);
        getAllGroup();
    }
    
    public void saveGroup(AjaxBehaviorEvent event) {
        DaoFactory.getInstance().getGroupDao().addGroup(groupEdit);
        getAllGroup();
    }
    
    public void updateFilm(AjaxBehaviorEvent event) {
        DaoFactory.getInstance().getDistributionDao().updateDistribution(filmEdit);
        getAllFilm();
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
        
        DaoFactory.getInstance().getDistributionDao().addDistribution(filmEdit);
        getAllFilm();
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

    public Distribution getFilmEdit() {
        return filmEdit;
    }

    public void setFilmEdit(Distribution filmEdit) {
        this.filmEdit = filmEdit;
    }

    public DataModel<Distribution> getFilmModel() {
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
    
}
