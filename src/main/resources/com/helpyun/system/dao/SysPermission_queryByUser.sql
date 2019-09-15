SELECT p.*
		   FROM  sys_permission p
		   WHERE exists(
		   		select a.id from sys_role_permission a
		   		join sys_role b on a.role_id = b.id
		   		join sys_user_role c on c.role_id = b.id
		   		join sys_user d on d.id = c.user_id
		   		where p.id = a.permission_id AND d.username = #{username,jdbcType=VARCHAR}
		   )
		   and p.del_flag = 0
		   order by p.sort_no ASC