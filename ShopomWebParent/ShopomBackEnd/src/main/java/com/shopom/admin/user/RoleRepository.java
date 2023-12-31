package com.shopom.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;



import com.shopom.common.entity.Role;


public interface RoleRepository extends JpaRepository<Role, Integer> {

}
