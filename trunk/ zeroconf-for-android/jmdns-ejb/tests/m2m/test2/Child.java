package m2m.test2;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;

@Entity
public class Child implements Serializable {
	private static final long serialVersionUID = -2754408258302487849L;

	@Id
    public Integer id;

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumns ({
        @JoinColumn(name="parentLastName", referencedColumnName = "lastName"),
        @JoinColumn(name="parentFirstName", referencedColumnName = "firstName")
    })
    public Parent parent; //unidirectional
} 