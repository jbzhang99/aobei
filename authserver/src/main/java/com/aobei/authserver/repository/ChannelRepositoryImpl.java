package com.aobei.authserver.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.aobei.authserver.model.Channel;

@Repository
public class ChannelRepositoryImpl implements ChannelRepository{

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RowMapper<Channel> rowMapper = new BeanPropertyRowMapper<Channel>(Channel.class);
	
	@Override
	public Channel findByCode(String code) {
		List<Channel> userList = jdbcTemplate.query("select * from channel where code=?", rowMapper, code);
		return DataAccessUtils.singleResult(userList);
	}
	
}
