package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import db.DbIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao {

	
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
		
	}
	
	/*Ordem (implementação rotina CRUD):
	 * findById; ok!
	 * instanciar elementos reutilizáveis em uma função; ok!
	 * findAll;ok!
	 * insert;ok!
	 * update;ok!
	 * delete;
	 * */
		
	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
				try {
					st = conn.prepareStatement("INSERT INTO department (name) VALUES (?)",
							// não há necessidade do id;
							Statement.RETURN_GENERATED_KEYS);
					
					st.setString(1, obj.getName());
					
					int rowsAffected = st.executeUpdate();
					
					if(rowsAffected > 0) {
						ResultSet rs = st.getGeneratedKeys();						
						if(rs.next()) {
							int id = rs.getInt(1);
							obj.setId(id);
							}
						DB.closeResultSet(rs);
					}
					else {
						throw new DbException("Unexpected Error! No rows affected!");						
					}					
				}
				catch (SQLException e) {
					throw new DbException(e.getMessage());
				}
				finally {
					DB.closeStatement(st);
				}		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("UPDATE department SET name= ? WHERE(id = ?)");			
			
			st.setString(1,  obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();			
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}		
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("DELETE FROM department WHERE id = ?");
			
			st.setInt(1, id);
			
			int rows = 	st.executeUpdate();
			
			if(rows == 0) {
				throw new DbException("This id do not exists!");
			}			
		}
		catch (SQLException e) {
			throw new DbIntegrityException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}		
	}

	@Override
	public Department findById(Integer id) {
		PreparedStatement st = null; // inserção de comandos no BD;
		ResultSet rs = null; // resultado da consulta acima, vinda do BD;
		try {
			st = conn.prepareStatement(
					"SELECT * FROM coursejdbc.department WHERE department.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Department dep = instantiateDepartment(rs);
				return dep;
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("Id"));
		dep.setName(rs.getString("Name"));
		return dep;		
	}

	@Override
	public List<Department> findyAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT * FROM department ORDER by Name");
			
			rs = st.executeQuery();
			
			List<Department> list = new ArrayList<>();
			//Map<Integer, Department> map = new HashMap<>(); 
			//sem necessidade, porque não está sendo instanciado um outro objeto proveniente de department;
			
			while(rs.next()) {
				Department 	dep = instantiateDepartment(rs);
				list.add(dep);
			}
			return list;			
		}
		catch(SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		
	}

}
