package com.we.timetrack.db;

import java.util.List;

import com.we.timetrack.model.Role;

public interface RoleRepository {

	/**
	 * Gets role's record with matching roleId.
	 */
	public Role getRole(int roleId);
	
	/**
	 * Returns list of all role's database records.
	 */
	public List<Role> getRoles();
	
	/**
	 * Saves a Role's object.
	 */
	public void saveRole(Role role);
	
	/**
	 * Deletes a Role's record.
	 */
	public void deleteRole(Role role);
	
	/**
	 * Get role's record with matching name 
	 */
	public Role getRole(String name);
}
