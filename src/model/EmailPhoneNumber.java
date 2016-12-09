package model;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Model from front-side POST request to get suggested contacts
 */

@XmlRootElement
public class EmailPhoneNumber {
    private List<String> email;
    private List<String> phoneNumber;

    public List<String> getEmail() {
        return email;
    }

    public void setEmail(List<String> email) {
        this.email = email;
    }

    public List<String> getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(List<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
