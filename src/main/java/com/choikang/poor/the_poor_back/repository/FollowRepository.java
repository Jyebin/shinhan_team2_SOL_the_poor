package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FollowRepository extends JpaRepository<Follow, Long> {


}
