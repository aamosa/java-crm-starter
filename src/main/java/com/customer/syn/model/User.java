package com.customer.syn.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

import static com.customer.syn.model.Enums.Status.NEW;
import static com.customer.syn.model.FormInputType.CHECKBOX;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

@Entity
public class User extends BaseEntity<Long>
	implements Serializable {

	private static final transient long serialVersionUID = 3184776518741L;
	private static final transient Logger LOG = LoggerFactory.getLogger(User.class);

	@Transient private boolean editable;

	@Email @NotNull
	@Column(unique=true)
	@ViewMeta(order=4)
	private String email;

	@ViewMeta(order=8, formField=false)
	private Instant lastLogin;

	@ViewMeta(order=3)
	private String lastName;

	@ViewMeta(order=2)
	private String firstName;

	@Enumerated(STRING)
	@ViewMeta(order=1, formField=false)
	private Enums.Status status = NEW;

	@ViewMeta(order=5)
	@Size(min=3, max=30)
	@Column(nullable=false, unique=true)
	@NotNull private String userName;

	@ViewMeta(order=6)
	@Size(min=4, max=30)
	@NotNull private String password;


	@ViewMeta(order=9, type=CHECKBOX)
	@ManyToMany(fetch=LAZY, cascade={ PERSIST, MERGE })
	@JoinTable(name="users_roles",
		joinColumns=@JoinColumn(name="user_id", nullable=false),
		inverseJoinColumns=@JoinColumn(name="role_id"))
	private Set<Role> roles = new HashSet<>();


	// ------------------------------------------------------------- constructors
	public User() { /* no-args constructor */ }


	public User(String firstName, String lastName, String userName, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.password = password;
	}


	// ------------------------------------------------------------- utility methods
	public void addRole(Role role) {
		getRoles().add(role);
		role.getUsers().add(this);
	}

	public void addRoles(Set<Role> roles) {
		setRoles(roles);
		for (Role role : roles) {
			role.getUsers().add(this);
		}
	}

	public void removeRole(Role role) {
		getRoles().remove(role);
		// role.getUsers().remove(this);
	}


	// ------------------------------------------------------------- setters and getters
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Instant getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Instant lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Enums.Status getStatus() {
		return status;
	}

	public void setStatus(Enums.Status status) {
		this.status = status;
	}

	public Set<Role> getRoles() {
		LOG.debug("getroles invoked");  // TODO
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		LOG.debug("setroles invoked"); // TODO
		this.roles = roles;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public String toString() {
		return userName;
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof User)
			&& (id != null) ? id.equals(((User) object).id)
			: (object == this);
	}

	@Override
	public int hashCode() {
		return (id != null)
			? (User.class.hashCode() + id.hashCode())
			: super.hashCode();
	}

}
