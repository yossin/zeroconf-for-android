package m2m.test2;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class ParentPk implements Serializable {
	private static final long serialVersionUID = -8050844396171328278L;
	public String firstName;
    public String lastName;
} 