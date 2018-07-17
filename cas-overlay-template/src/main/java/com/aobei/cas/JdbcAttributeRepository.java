package com.aobei.cas;

import java.util.*;

import javax.sql.DataSource;

import org.jasig.services.persondir.IPersonAttributes;
import org.jasig.services.persondir.support.AbstractFlatteningPersonAttributeDao;
import org.jasig.services.persondir.support.NamedPersonImpl;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcAttributeRepository extends AbstractFlatteningPersonAttributeDao {

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    private ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();

    private String sqluser;

    private String sqlrolekey;

    @Override
    public IPersonAttributes getPerson(String uid) {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(dataSource);
        }
        Map<String, Object> map = DataAccessUtils.singleResult(jdbcTemplate.query(sqluser, rowMapper, uid));

        if (map != null && map.size() > 0) {
            Map<String, List<Object>> attributes = new LinkedHashMap<>();
            for (String key : map.keySet()) {
                attributes.put(key, Arrays.asList(map.get(key)));
            }
            String userId = map.get("userid") == null ? "0" : map.get("userid").toString();
            List<Map<String, Object>> roleKeyList = jdbcTemplate.query(sqlrolekey, rowMapper, userId);
            String roles = "";
            for (int i = 0; i < roleKeyList.size(); i++) {
                if (roleKeyList.get(i).get("roles") == null)
                    continue;

                String tmp = roleKeyList.get(i).get("roles").toString().trim();
                if (i == roleKeyList.size() - 1) {
                    roles += tmp;
                } else {
                    roles += tmp + ",";
                }
                roles.trim();
            }
            roles += map.get("roles") == null ? "" : "," + map.get("roles");
            roles.trim();
            if (roles.endsWith(",")) {
                roles = roles.substring(0, roles.length() - 1);
            }
            if (roles.startsWith(",")) {
                roles = roles.substring(1, roles.length());
            }
            attributes.put("roles", Arrays.asList((Object) roles));
            return new NamedPersonImpl(uid, attributes);

        } else {
            return null;
        }


//		if (list != null && list.size() > 0) {
//			Map<String, List<Object>> attributes = new LinkedHashMap<>();
//			for (String key : list.get(0).keySet()) {
//				attributes.put(key, Arrays.asList(list.get(0).get(key)));
//			}
//            System.out.println(attributes);
//			return new NamedPersonImpl(uid, attributes);
//		} else {
//			return null;
//		}

    }

    @Override
    public Set<IPersonAttributes> getPeopleWithMultivaluedAttributes(Map<String, List<Object>> query) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getPossibleUserAttributeNames() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<String> getAvailableQueryAttributes() {
        // TODO Auto-generated method stub
        return null;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getSqluser() {
        return sqluser;
    }

    public void setSqluser(String sqluser) {
        this.sqluser = sqluser;
    }

    public String getSqlrolekey() {
        return sqlrolekey;
    }

    public void setSqlrolekey(String sqlrolekey) {
        this.sqlrolekey = sqlrolekey;
    }
}
