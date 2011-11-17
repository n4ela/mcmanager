package mcmanager.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import mcmanager.data.TypeDistributionEnum;

@FacesConverter("mcmanager.web.converter.TypeConverter")
public class TypeConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, 
            String value) throws ConverterException {
        return TypeDistributionEnum.matcherByDesc(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) throws ConverterException {
        if (value != null) {
            return TypeDistributionEnum.matcherByType((Integer)value).getDesc();
        }
        return null;
    }

}
