package com.choikang.poor.the_poor_back.repository;

import com.choikang.poor.the_poor_back.model.AttendancePosts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendancePostsRepository extends JpaRepository<AttendancePosts, Integer> {

}
