package m2m.test2;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;

@Entity
public class Parent implements Serializable {
	private static final long serialVersionUID = -668323835330374839L;
	@Id
    public ParentPk id;
    public int age;

    @OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    @JoinColumns ({
        @JoinColumn(name="parentLastName", referencedColumnName = "lastName"),
        @JoinColumn(name="parentFirstName", referencedColumnName = "firstName")
    })
    public Set<Child> children; //unidirectional
}