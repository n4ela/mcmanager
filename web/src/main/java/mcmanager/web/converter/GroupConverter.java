package mcmanager.web.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import mcmanager.dao.DaoFactory;
import mcmanager.data.Group;
import mcmanager.utils.StringUtils;

@FacesConverter(forClass=Group.class)
public class GroupConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) throws ConverterException {
        if (!StringUtils.isEmpty(value)) {
            List<Group> groups = DaoFactory.getInstance().getGroupDao().getGroupByName(value);
            if (groups != null && groups.size() == 1)
                return groups.get(0);
            else 
                throw new ConverterException("Не удалось получить группу. " + groups + 
                        (groups != null ? "group.size: " + groups.size() : ""));
        } else 
            throw new ConverterException("Не указана группа для поиска");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) throws ConverterException {
        return value instanceof Group ? ((Group)value).getName() : "";
    }
}
