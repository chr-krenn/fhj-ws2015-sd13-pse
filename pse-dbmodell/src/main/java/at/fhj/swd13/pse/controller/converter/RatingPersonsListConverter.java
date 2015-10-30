package at.fhj.swd13.pse.controller.converter;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import at.fhj.swd13.pse.dto.UserDTO;

@FacesConverter("at.fhj.swd13.pse.controller.converter.RatingPersonsListConverter")
public class RatingPersonsListConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent c, String ratingPersonsString) {
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent c, Object list) {
		if(list == null) {
			return null;
		}
		
		@SuppressWarnings("unchecked")
		List<UserDTO> ratingPersonsList = (List<UserDTO>) list;
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < ratingPersonsList.size(); i ++) {
			if(i<3) {
				sb.append(ratingPersonsList.get(i).getFullName());
				if(i != ratingPersonsList.size() -1) {
					sb.append(", ");
				}
			}
			else if(i==3){
				sb.append(" ...");
				break;
			}
		}
		sb.append(" gefällt das.");
		return sb.toString();
	}

}
