package com.aobei.authserver.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.aobei.authserver.model.Wxapp;

@Repository
public class WxappRepositoryImpl implements WxappRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private RowMapper<Wxapp> rowMapper = new BeanPropertyRowMapper<Wxapp>(Wxapp.class);

	@Override
	public List<Wxapp> findAll() {
		return jdbcTemplate.query("select * from wx_app", rowMapper);
	}

}
