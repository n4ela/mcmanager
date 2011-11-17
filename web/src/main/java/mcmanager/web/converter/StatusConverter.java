package mcmanager.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import mcmanager.data.StatusEnum;

@FacesConverter("mcmanager.web.converter.StatusConverter")
public class StatusConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
            String value) throws ConverterException {
        return StatusEnum.matcherByDesc(value);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) throws ConverterException {
        if (value instanceof StatusEnum) {
            return ((StatusEnum)value).getDesc();
        }
        return null;
    }

}
