package com.shopme.common.entity;

import java.util.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "UserTable")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;
	@Column(length = 128, nullable = false, unique = true)
	private String email;

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", password=" + password + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", photots=" + photots + ", enabled=" + enabled + ", roles=" + roles + "]";
	}

	@Column(length = 64, nullable = false)
	private String password;

	@Column(name = "First_Name", length = 64, nullable = false)
	private String firstName;

	@Column(name = "Last_Name", length = 64, nullable = false)
	private String lastName;

	@Column(length = 64)
	private String photots;

	private boolean enabled;
		
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "roles_id"))
	private Set<Role> roles = new HashSet<>();

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public String getPhotots() {
		return photots;
	}

	public void setPhotots(String photots) {
		this.photots = photots;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		this.roles.add(role);
	}

	@Transient
	public String getPhotosImagePath() {
		if (id == null || photots == null) 
			return "/images/default.png";
//		System.out.println("/userphotos/" + this.id + "/" + this.photots);
		return "/userphotos/" + this.id + "/" + this.photots;
	}
	
	@Transient
	public String getFullName() {
		return this.firstName+" "+ this.lastName;
	}
	
	public boolean hasRole(String roleName) {
		Iterator<Role> iterator = this.roles.iterator();
		while(iterator.hasNext()) {
			Role role = iterator.next();
			if(role.getName() == roleName) {
				return false;
			}
		}
		return true;
	}
}
