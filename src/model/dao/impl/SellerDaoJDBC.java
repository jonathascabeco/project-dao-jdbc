package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	// implementa��o da conex�o com o banco de dados, vari�vel pela qual ser� feita
	// a conex�o;
	public Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub

	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName "
							+ "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id "
							+ "WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) { // vericando se o proximo campo � null, sendo null a verifica��o para, o retorno
								// de dados acaba;
				Department dep = instantiateDepartment(rs);
				Seller obj = instantiateSeller(rs, dep);
				return obj;
			}
			return null;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
			// n�o h� necessidade de fechar a conex�o, pois ser� reutilizada, ela � instanciada pelo DaoFactory;
		}
	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rs.getInt("Id"));
		obj.setName(rs.getString("Name"));
		obj.setEmail(rs.getString("Email"));
		obj.setBaseSalary(rs.getDouble("BaseSalary"));
		obj.setBirthDate(rs.getDate("BirthDate"));
		obj.setDepartment(dep);
		// no caso o objeto Seller recebe o objeto Dep em seu campo, pois o retorno � de
		// um objeto Seller;
		return obj;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();		
		dep.setId(rs.getInt("DepartmentId")); // a exce��o foi propagada por conta de estar sendo tratada no Seller findById;
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findyAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"SELECT seller.*,department.Name as DepName " //n�o esquecer o espa�o;
					+"FROM seller INNER JOIN department "
					+"ON seller.DepartmentId = department.Id "
					+"WHERE DepartmentId = ? "
					+"ORDER BY Name");
			
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			// utilizado para fazer uma rotina de verifica��o de igualdade de id, para n�o instanciar varios objetos Departament
			// o departmant possui varios vendedores, mas os vendedores nao possuem um department exclusivo;
			while (rs.next()) { 
			// por ter possibilidade de v�rios objs como retorno, o while foi implementado; 
				
				Department dep =  map.get(rs.getInt("DepartmentId"));
				// pegando o id do BD, para verificar repeti��o;
				
				if(dep == null) {
					dep =  instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
					// atribui o id instanciado ao map para compar�-lo com o pr�ximo, dentro do bloco while;
				}
				
				Seller obj = instantiateSeller(rs, dep);
				list.add(obj);
			}
			return list;
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}
}
