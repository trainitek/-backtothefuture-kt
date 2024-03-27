package com.trainitek.backtothefuture.domain

import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface CourseRepository : CrudRepository<Course, UUID>
