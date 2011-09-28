package mcmanager.web.validate;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import mcmanager.exception.CoreException;
import mcmanager.log.LogEnum;
import mcmanager.utils.StringUtils;
import mcmanager.web.WebBrowser;

@SessionScoped
@ManagedBean(name = "filmValidate")
public class FilmValidate {
    public void validateRutrackerLink(FacesContext context, UIComponent component, Object value) {
        String strValue = (String) value;    
        if (!StringUtils.isEmpty(strValue)) {
            WebBrowser webBrowser = new WebBrowser(LogEnum.WEB.getLog());            
            try {
                webBrowser.validateUrl(strValue);
            } catch (CoreException e) {
                FacesMessage error = new FacesMessage();
                error.setSeverity(FacesMessage.SEVERITY_ERROR);
                error.setSummary(e.getMessage());
                throw new ValidatorException(error);
            }
        }
    }
}
