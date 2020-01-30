package com.we.timetrack.db;

import java.util.List;

import com.we.timetrack.model.Privilege;

public interface PrivilegeRepository {

	/**
	 * Gets privilege record with matching privilegeId.
	 */
	public Privilege getPrivilege(int privilegeId);
	
	/**
	 * Returns list of all privilege's database records.
	 */
	public List<Privilege> getPrivileges();
	
	/**
	 * Saves a Privilege object.
	 */
	public void savePrivilege(Privilege privilege);
	
	/**
	 * Deletes Privilege's record.
	 */
	public void deletePrivilege(Privilege privilege);
}
